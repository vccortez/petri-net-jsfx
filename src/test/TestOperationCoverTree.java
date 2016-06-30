package test;

import model.Arc;
import model.Petrinet;
import model.Place;
import model.Transition;
import operation.OperationCoverTree;

public class TestOperationCoverTree {

	public static void main(String[] args) {
		Petrinet petrinet = getPetrinet();
		OperationCoverTree op = new OperationCoverTree();
		System.out.println(op.executa(petrinet));
	}

	private static Petrinet getPetrinet() {
		Petrinet petrinet = new Petrinet("test");

		Place p1 = new Place("p1", 1);
		petrinet.add(p1);
		Place p2 = new Place("p2", 0);
		petrinet.add(p2);
		Place p3 = new Place("p3", 0);
		petrinet.add(p3);
		Place p4 = new Place("p4", 0);
		petrinet.add(p4);

		Transition t1 = new Transition("t1");
		petrinet.add(t1);
		Transition t2 = new Transition("t2");
		petrinet.add(t2);
		Transition t3 = new Transition("t3");
		petrinet.add(t3);

		petrinet.add(new Arc("11", p1, t1));
		petrinet.add(new Arc("12", t1, p2));
		petrinet.add(new Arc("13", t1, p3));
		petrinet.add(new Arc("14", p2, t2));
		petrinet.add(new Arc("15", p2, t3));
		petrinet.add(new Arc("16", p3, t3));
		petrinet.add(new Arc("17", t3, p3));
		petrinet.add(new Arc("18", t3, p4));
		petrinet.add(new Arc("19", t2, p1));
		return petrinet;
	}

	private static Petrinet getPetrinet2() {
		Petrinet petrinet = new Petrinet("test");

		Place p1 = new Place("p1", 1);
		petrinet.add(p1);
		Place p2 = new Place("p2", 1);
		petrinet.add(p2);
		Place p3 = new Place("p3", 0);
		petrinet.add(p3);

		Transition t1 = new Transition("t1");
		petrinet.add(t1);
		Transition t2 = new Transition("t2");
		petrinet.add(t2);

		petrinet.add(new Arc("1", p1, t1));
		petrinet.add(new Arc("2", p2, t1));
		petrinet.add(new Arc("3", t1, p3));
		petrinet.add(new Arc("4", p3, t2));
		petrinet.add(new Arc("5", t2, p1));
		petrinet.add(new Arc("6", t2, p2));
		return petrinet;
	}

}
