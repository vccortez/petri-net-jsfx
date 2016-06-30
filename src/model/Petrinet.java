package model;

import java.util.ArrayList;
import java.util.List;

public class Petrinet extends PetrinetObject {

	List<Place> places = new ArrayList<Place>();
	List<Transition> transitions = new ArrayList<Transition>();
	List<Arc> arcs = new ArrayList<Arc>();

	public Petrinet(String name) {
		super(name);
	}

	public String getMark() {
		List<Integer> mark = new ArrayList<>(places.size());
		for (Place place : places) {
			mark.add(place.getTokens());
		}
		return mark.toString();
	}

	public void setMark(String mark) {
		String[] marks = mark.replaceAll("\\[|\\]|w| ", "").split(",");
		for (int i = 0; i < marks.length; i++) {
			places.get(i).setTokens(Integer.valueOf(marks[i]));
		}
	}

	public List<Transition> getTransitionsAbleToFire(String mark) {
		String mark0 = getMark();
		setMark(mark);
		List<Transition> list = getTransitionsAbleToFire();
		setMark(mark0);
		return list;
	}

	public void add(PetrinetObject o) {
		if (o instanceof Arc) {
			arcs.add((Arc) o);
		} else if (o instanceof Place) {
			places.add((Place) o);
		} else if (o instanceof Transition) {
			transitions.add((Transition) o);
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

	public Transition transition(String name) {
		Transition t = new Transition(name);
		transitions.add(t);
		return t;
	}

	public Place place(String name) {
		Place p = new Place(name);
		places.add(p);
		return p;
	}

	public Place place(String name, int initial) {
		Place p = new Place(name, initial);
		places.add(p);
		return p;
	}

	public Arc arc(String name, Place p, Transition t) {
		Arc arc = new Arc(name, p, t);
		arcs.add(arc);
		return arc;
	}

	public Arc arc(String name, Transition t, Place p) {
		Arc arc = new Arc(name, t, p);
		arcs.add(arc);
		return arc;
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