package model;

public class Node {
	private String father;
	private String child;
	private boolean raiz;
	private String transition;

	public Node(String father, String child, String transition) {
		super();
		this.father = father;
		this.child = child;
		raiz = false;
		this.transition = transition;
	}

	@Override
	public String toString() {
		return father + "---" + transition + "---> " + child + (raiz ? "***RAIZ***" : "") + "\n";
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public String getChild() {
		return child;
	}

	public void setChild(String child) {
		this.child = child;
	}

	public boolean isRaiz() {
		return raiz;
	}

	public void setRaiz(boolean raiz) {
		this.raiz = raiz;
	}

	public String getTransition() {
		return transition;
	}

	public void setTransition(String transition) {
		this.transition = transition;
	}

}
