package model;

public class Arc extends PetrinetObject {

	private Place place;
	private Transition transition;
	private Direction direction;
	private int weight = 1;

	private Arc(String name, Direction d, Place p, Transition t) {
		super(name);
		this.direction = d;
		this.place = p;
		this.setTransition(t);
	}

	public Arc(String name, Place p, Transition t) {
		this(name, Direction.PLACE_TO_TRANSITION, p, t);
		t.addIncoming(this);
	}

	public Arc(String name, Transition t, Place p) {
		this(name, Direction.TRANSITION_TO_PLACE, p, t);
		t.addOutgoing(this);
	}

	public Arc() {
		this("", null, null, null);
	}

	public boolean canFire() {
		return direction.canFire(place, weight);
	}

	public void fire() {
		this.direction.fire(place, this.weight);
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}

	public Transition getTransition() {
		return transition;
	}

	public void setTransition(Transition transition) {
		this.transition = transition;
	}

	protected Place getPlace() {
		return place;
	}

	protected void setPlace(Place place) {
		this.place = place;
	}

	protected Direction getDirection() {
		return direction;
	}

	protected void setDirection(Direction direction) {
		this.direction = direction;
	}
}