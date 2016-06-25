package model;

public class Place {
	private int id;
	private String label;
	private int tokens;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getTokens() {
		return tokens;
	}

	public void setTokens(int tokens) {
		this.tokens = tokens;
	}

	@Override
	public String toString() {
		return "Place [id=" + id + ", label=" + label + ", tokens=" + tokens + "]";
	}

}
