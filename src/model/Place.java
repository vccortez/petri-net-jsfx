package model;

public class Place {
	private int id;
	private String label;
	private String tokens;

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

	public String getTokens() {
		return tokens;
	}

	public void setTokens(String tokens) {
		this.tokens = tokens;
	}

	@Override
	public String toString() {
		return "Place [id=" + id + ", label=" + label + ", tokens=" + tokens + "]";
	}

}
