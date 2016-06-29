package model;

import java.io.ByteArrayOutputStream;

public class Petrinet {

	private Place[] places;
	private Transition[] transitions;
	private FromTo[] arcin;
	private FromTo[] arcout;
	private String jsonString;

	public Petrinet() {
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String loadJSON() {
		// String jsonString = new Gson().toJson(this);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		try {
			byte[] utf8Json = jsonString.getBytes("UTF8");
			buf.write(utf8Json);
			return buf.toString("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public FromTo[] getArcout() {
		return arcout;
	}

	public void setArcout(FromTo[] arcout) {
		this.arcout = arcout;
	}

	public FromTo[] getArcin() {
		return arcin;
	}

	public void setArcin(FromTo[] arcin) {
		this.arcin = arcin;
	}

	public Transition[] getTransitions() {
		return transitions;
	}

	public void setTransitions(Transition[] transitions) {
		this.transitions = transitions;
	}

	public Place[] getPlaces() {
		return places;
	}

	public void setPlaces(Place[] places) {
		this.places = places;
	}

}
