package model;

public class FromTo {
	private int from;
	private int to;

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return "FromTo [from=" + from + ", to=" + to + "]";
	}

}
