package model;

public class PetriNet {

	private int inicial;
	private String nome;

	public PetriNet() {
		// TODO Auto-generated constructor stub
	}

	public int getInicial() {
		return inicial;
	}

	public void setInicial(int inicial) {
		this.inicial = inicial;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "PetriNet [inicial=" + inicial + ", nome=" + nome + "]";
	}

}
