package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Arc;
import model.Petrinet;
import model.PetrinetDeserializer;
import model.PetrinetSerializer;
import model.Place;
import model.Transition;
import operation.OperationPetrinet;

public class TestOperationPetrinet {

	private static Gson petrinetGson = new GsonBuilder().registerTypeAdapter(Petrinet.class, new PetrinetSerializer())
			.registerTypeAdapter(Petrinet.class, new PetrinetDeserializer()).setPrettyPrinting().create();

	public static void main(String[] args) {
		String json = "{\"lugares\": [{ \"id\": 1, \"legenda\": \"p1\" },{ \"id\": 2, \"legenda\": \"p2\" }],\"marcas\": [0, 1],\"transicoes\": [{ \"id\": 1, \"legenda\": \"t1\" },{ \"id\": 2, \"legenda\": \"t2\" }],\"arcos\": {\"entrada\": [{ \"transicao\": 2, \"lugar\": 1 },{ \"transicao\": 2, \"lugar\": 2 }],\"saida\": [{ \"transicao\": 1, \"lugar\": 1 },{ \"transicao\": 2, \"lugar\": 2 }]},\"pesos\": [1, 1, 1, 1]}";
		Petrinet pn = petrinetGson.fromJson(json, Petrinet.class);
		OperationPetrinet op = new OperationPetrinet(pn);
		// System.out.println(op.printTree());
		// System.out.println(op.getStatusUnlimited());
		System.out.println(op.isReachable("99,99"));
		// System.out.println("*********REDE 0");
		// Petrinet petrinet = getPetrinet();
		// testPetrinet(petrinet);
		//
		// System.out.println("*********REDE 2");
		// Petrinet petrinet2 = getPetrinet2();
		// testPetrinet(petrinet2);
		//
		// System.out.println("*********REDE 1");
		// Petrinet petrinet1 = getPetrinet1();
		// testPetrinet(petrinet1);

	}

	private static void testPetrinet(Petrinet petrinet) {
		OperationPetrinet op = new OperationPetrinet(petrinet);
		System.out.println(op.printTree());
		System.out.println(op.getStatusBlocked());
		System.out.println(op.getStatusUnlimited());
	}

	private static Petrinet getPetrinet() {
		Petrinet petrinet = new Petrinet("test");

		Place p0 = new Place("p0", 1);
		petrinet.add(p0);
		Place p1 = new Place("p1", 0);
		petrinet.add(p1);
		Place p2 = new Place("p2", 0);
		petrinet.add(p2);

		Transition t0 = new Transition("t0");
		petrinet.add(t0);
		Transition t1 = new Transition("t1");
		petrinet.add(t1);
		Transition t2 = new Transition("t2");
		petrinet.add(t2);

		petrinet.add(new Arc("1", p1, t1));
		petrinet.add(new Arc("2", p1, t2));
		petrinet.add(new Arc("3", t1, p0));
		petrinet.add(new Arc("4", t2, p0));
		petrinet.add(new Arc("5", p0, t0));
		petrinet.add(new Arc("6", t0, p1));
		petrinet.add(new Arc("7", t0, p2));
		petrinet.add(new Arc("8", p2, t1));
		return petrinet;
	}

	private static Petrinet getPetrinet2() {
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

	private static Petrinet getPetrinet1() {
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
