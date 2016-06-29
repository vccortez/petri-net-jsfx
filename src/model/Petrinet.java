package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Petrinet extends PetrinetObject {

	private List<Place> places = new ArrayList<Place>();
	private List<Transition> transitions = new ArrayList<Transition>();
	private List<Arc> arcs = new ArrayList<Arc>();
	private Vector<Integer> initialMarks = new Vector<>();
	private Vector<Integer> weights = new Vector<>();

	public Petrinet(String name) {
		super(name);
	}

	public void add(PetrinetObject o) {
		if (o instanceof Arc) {
			addArc((Arc) o);
		} else if (o instanceof Place) {
			addPlace((Place) o);
		} else if (o instanceof Transition) {
			addTransition((Transition) o);
		}
	}

	public List<Transition> getTransitionsAbleToFire() {
		ArrayList<Transition> list = new ArrayList<Transition>();
		for (Transition t : transitions) {
			if (t.canFire()) {
				list.add(t);
			}
		}
		return list;
	}

	public Transition addTransition(String name) {
		Transition t = new Transition(name);
		addTransition(t);
		return t;
	}

	private void addTransition(Transition transition) {
		transitions.add(transition);
	}

	public Place addPlace(String name) {
		Place p = new Place(name);
		addPlace(p);
		return p;
	}

	public Place addPlace(String name, int initial) {
		Place p = new Place(name, initial);
		addPlace(p);
		return p;
	}

	private void addPlace(Place place) {
		places.add(place);
		initialMarks.add(places.indexOf(place), place.getTokens());
	}

	public Arc addArc(String name, Place p, Transition t) {
		Arc arc = new Arc(name, p, t);
		addArc(arc);
		return arc;
	}

	public Arc addArc(String name, Transition t, Place p) {
		Arc arc = new Arc(name, t, p);
		addArc(arc);
		return arc;
	}

	private void addArc(Arc arc) {
		arcs.add(arc);
		weights.add(arcs.indexOf(arc), arc.getWeight());
	}

	public List<Place> getPlaces() {
		return places;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public List<Arc> getArcs() {
		return arcs;
	}

	public ArrayList<Arc> getArcs(Transition t, Direction d) {
		ArrayList<Arc> list = new ArrayList<>();
		for (Arc arc : arcs) {
			if (arc.getDirection() == d) {
				if (arc.getTransition().equals(t)) {
					list.add(arc);
				}
			}
		}
		return list;
	}

	public int[][] getIncidenceMatrix() {
		int t = transitions.size();
		int p = places.size();
		int[][] matrix = new int[t][p];

		for (int i = 0; i < t; ++i) {
			ArrayList<Arc> incoming = getArcs(transitions.get(i), Direction.PLACE_TO_TRANSITION);
			ArrayList<Arc> outgoing = getArcs(transitions.get(i), Direction.TRANSITION_TO_PLACE);
			
			for (int j = 0; j < p; ++j) {
				int inc = 0;
				
				for (int k = 0; k < incoming.size(); ++k) {
					if (incoming.get(k).getPlace().equals(places.get(j))) {
						inc -= incoming.get(k).getWeight();
					}
				}
				
				for (int k = 0; k < outgoing.size(); ++k) {
					if (outgoing.get(k).getPlace().equals(places.get(j))) {
						inc += outgoing.get(k).getWeight();
					}
				}
				
				matrix[i][j] = inc;
			}
		}

		return matrix;
	}

	@Override
	public String toString() {
		String nl = "\n";
		StringBuilder sb = new StringBuilder("Petrinet ");

		sb.append(super.toString()).append(nl);
		sb.append("---Transitions---").append(nl);
		for (Transition t : transitions) {
			sb.append(t).append(nl);
		}
		sb.append("---Places---").append(nl);
		for (Place p : places) {
			sb.append(p).append(nl);
		}
		return sb.toString();
	}
}