/**
 * @overview Loads a JSON and draws a Petri Net from it.
 * @author Vitor Cortez <vitor.a.cortez@gmail.com>
 */
function onLoad() {
  // java.log('onLoad was called');

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

  var places = createPlaces(json.lugares, json.marcas);
  var transitions = createTransitions(json.transicoes);

  var cells = places.concat(transitions);

  var links = createLinks(json.arcos, json.pesos);

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

    var placesBefore = _.map(inbound, function (link) {
      return [graph.getCell(link.get('source').id), link];
    });

    var placesAfter = _.map(outbound, function (link) {
      return graph.getCell(link.get('target').id);
    });

    var isFirable = true;
    _.each(placesBefore, function (p) {
      var t = p[0].get('tokens');
      var w = p[1].get('weight');

      if (t <= 0 || w > t)
        isFirable = false;
    });

    if (isFirable) {

      _.each(placesBefore, function (p) {
        // Let the execution finish before adjusting the value of tokens. So that we can loop over all transitions
        // and call fireTransition() on the original number of tokens.
        var t = p[0].get('tokens');
        var w = p[1].get('weight');

        _.defer(function () {
          p[0].set('tokens', t - w);
        });

        /*
        var link = _.find(inbound, function (l) {
          return l.get('source').id === p[0].id;
        });
        */
        var link = p[1];

        paper.findViewByModel(link).sendToken(V('circle', {
          r: 5,
          fill: '#feb662'
        }).node, sec * 1000);

      });

      _.each(placesAfter, function (p) {
        var link = _.find(outbound, function (l) {
          return l.get('target').id === p.id;
        });

        var w = link.get('weight');

        paper.findViewByModel(link).sendToken(V('circle', {
          r: 5,
          fill: '#feb662'
        }).node, sec * 1000, function () {
          p.set('tokens', p.get('tokens') + w);
        });

      });
    }
  }

  function simulate(transitions) {
    //var transitions = [pProduce, pSend, cAccept, cConsume];
    _.each(transitions, function (t) {
      if (Math.random() < 0.5) fireTransition(t, 1);
    });

    return setInterval(function () {
      _.each(transitions, function (t) {
        if (Math.random() < 0.5) fireTransition(t, 1);
      });
    }, 2000);
  }

  function stopSimulation(simulationId) {
    clearInterval(simulationId);
  }

  var simulationId = simulate(transitions);
}

function createPlaces(places, tokens) {
  var pn = joint.shapes.pn;
  var plcs = places.map(function (pl, i) {
    return new pn.Place({
      id: 'p' + pl.id,
      attrs: {
        '.label': { text: pl.legenda, fill: '#7c68fc' },
        '.root': { stroke: '#9586fd', 'stroke-width': 3 },
        '.tokens > circle': { fill: '#7a7e9b' }
      },
      tokens: tokens[i]
    });
  });

  return plcs;
}

function createTransitions(transitions) {
  var pn = joint.shapes.pn;
  var trs = transitions.map(function (tr) {
    return new pn.Transition({
      id: 't' + tr.id,
      attrs: {
        '.label': { text: tr.legenda, fill: '#fe854f' },
        '.root': { fill: '#9586fd', stroke: '#9586fd' }
      }
    });
  });

  return trs;
}

function createLinks(arcos, pesos) {
  var pn = joint.shapes.pn;
  var offset = arcos.entrada.length;

  var entrada = arcos.entrada.map(function (arco, i) {
    return new pn.Link({
      source: { id: 'p' + arco.lugar, selector: '.root' },
      target: { id: 't' + arco.transicao, selector: '.root' },
      attrs: {
        '.connection': {
          fill: 'none',
          'stroke-linejoin': 'round',
          'stroke-width': '2',
          stroke: '#4b4a67'
        }
      },
      weight: pesos[i],
      labels: [{ position: 25, attrs: { text: { text: pesos[i] } } }]
    });
  });

  var saida = arcos.saida.map(function (arco, i) {
      return new pn.Link({
        source: { id: 't' + arco.transicao, selector: '.root' },
        target: { id: 'p' + arco.lugar, selector: '.root' },
        attrs: {
          '.connection': {
            fill: 'none',
            'stroke-linejoin': 'round',
            'stroke-width': '2',
            stroke: '#4b4a67'
          }
        },
        weight: pesos[i + offset],
        labels: [{ position: 25, attrs: { text: { text: pesos[i + offset] } } }]
      });
    });

  return entrada.concat(saida);
}

window.addEventListener('load', onLoad, false);
