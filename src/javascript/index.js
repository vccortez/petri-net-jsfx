/**
 * @overview Loads a JSON and draws a Petri Net from it.
 * @author Vitor Cortez <vitor.a.cortez@gmail.com>
 */
function onLoad() {
  java.log('onLoad was called');

  var json = JSON.parse(java.loadJSON());

  var graph = new joint.dia.Graph();

  var paper = new joint.dia.Paper({
    el: $('#petrinet'),
    width: 800,
    height: 350,
    gridSize: 10,
    perpendicularLinks: true,
    model: graph
  });
  
  var places = createPlaces(json.places);
  var transitions = createTransitions(json.transitions);

  var cells = places.concat(transitions);

  var links = createLinks(json.arcin, json.arcout);
  
  graph.resetCells(cells.concat(links));

  joint.layout.DirectedGraph.layout(graph, {
    rankDir: 'LR',
    edgeSep: 90
  });
  
  function fireTransition(t, sec) {
	  var inbound = graph.getConnectedLinks(t, {
	    inbound: true
	  });
	  var outbound = graph.getConnectedLinks(t, {
	    outbound: true
	  });

	  var placesBefore = _.map(inbound, function(link) {
	    return graph.getCell(link.get('source').id);
	  });
	  var placesAfter = _.map(outbound, function(link) {
	    return graph.getCell(link.get('target').id);
	  });

	  var isFirable = true;
	  _.each(placesBefore, function(p) {
	    if (p.get('tokens') === 0) isFirable = false;
	  });

	  if (isFirable) {

	    _.each(placesBefore, function(p) {
	      // Let the execution finish before adjusting the value of tokens. So that we can loop over all transitions
	      // and call fireTransition() on the original number of tokens.
	      _.defer(function() {
	        p.set('tokens', p.get('tokens') - 1);
	      });

	      var link = _.find(inbound, function(l) {
	        return l.get('source').id === p.id;
	      });
	      paper.findViewByModel(link).sendToken(V('circle', {
	        r: 5,
	        fill: '#feb662'
	      }).node, sec * 1000);

	    });

	    _.each(placesAfter, function(p) {
	      var link = _.find(outbound, function(l) {
	        return l.get('target').id === p.id;
	      });
	      paper.findViewByModel(link).sendToken(V('circle', {
	        r: 5,
	        fill: '#feb662'
	      }).node, sec * 1000, function() {
	        p.set('tokens', p.get('tokens') + 1);
	      });

	    });
	  }
	}

	function simulate(transitions) {
	  //var transitions = [pProduce, pSend, cAccept, cConsume];
	  _.each(transitions, function(t) {
	    if (Math.random() < 0.7) fireTransition(t, 1);
	  });

	  return setInterval(function() {
	    _.each(transitions, function(t) {
	      if (Math.random() < 0.7) fireTransition(t, 1);
	    });
	  }, 2000);
	}

	function stopSimulation(simulationId) {
	  clearInterval(simulationId);
	}

	var simulationId = simulate(transitions);
}

function createPlaces(places) {
  var pn = joint.shapes.pn;
  var plcs = places.map(function(pl) {
    return new pn.Place({
      id: ""+pl.id,
      attrs: {
        '.label': { text: pl.label, fill: '#7c68fc' },
        '.root': { stroke: '#9586fd', 'stroke-width': 3 },
        '.tokens > circle': { fill: '#7a7e9b' }
      },
      tokens: pl.tokens
    });
  });
  return plcs;
}

function createTransitions(transitions) {
  var pn = joint.shapes.pn;
  var trs = transitions.map(function(tr) {
    return new pn.Transition({
      id: ""+tr.id,
      attrs: {
        '.label': { text: tr.label, fill: '#fe854f' },
        '.root': { fill: '#9586fd', stroke: '#9586fd' }
      }
    });
  });
  return trs;
}

function createLinks(ins, out) {
  var pn = joint.shapes.pn;
  var links = ins.concat(out);
  var res = links.map(function(ln) {
    console.log('creating link ['+ ln.from + ','+ ln.to +']');
    return new pn.Link({
      source: { id: ""+ln.from, selector: '.root' },
      target: { id: ""+ln.to, selector: '.root' },
      attrs: {
        '.connection': {
          fill: 'none',
          'stroke-linejoin': 'round',
          'stroke-width': '2',
          'stroke': '#4b4a67'
        }
      }
    });
  });
  return res;
}

window.addEventListener('load', onLoad, false);
