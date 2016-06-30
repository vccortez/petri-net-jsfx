package test;

import model.Petrinet;
import model.Place;

import java.util.Arrays;

import model.Arc;
import model.Transition;

public class TestMatrix {

	public static void main(String[] args) {

		Petrinet pn = new Petrinet("Petrinet Test");

		Place pa = pn.addPlace("pA", 2);
		Place pb = pn.addPlace("pB");

		Transition ta = pn.addTransition("tA");
		Transition tb = pn.addTransition("tB");

		Arc pata = pn.addArc("pa ta", pa, ta);
		Arc tapa = pn.addArc("ta pa", ta, pa);
		Arc tapb = pn.addArc("ta pa", ta, pb);
		
		Arc pbtb = pn.addArc("pb tb", pb, tb);
		Arc tbpa = pn.addArc("tb pc", tb, pa);
		
		pata.setWeight(1);
		tapa.setWeight(1);
		
		tapb.setWeight(3);
		pbtb.setWeight(1);
		tbpa.setWeight(2);

		System.out.println(Arrays.deepToString(pn.getIncidenceMatrix()));
	}
}
