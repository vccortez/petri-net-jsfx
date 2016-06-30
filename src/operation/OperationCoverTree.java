package operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Node;
import model.Petrinet;
import model.Transition;

public class OperationCoverTree {

	private List<Node> tree = new ArrayList<>();
	private String raiz;
	private Set<String> blockeds = new HashSet<>();
	private Petrinet pn;

	public OperationCoverTree(Petrinet pn) {
		this.pn = pn;
		executa();
	}

	private void addNode(String father, String child, String t) {
		Node node = new Node(father, child, t);
		if (father.equals(raiz))
			node.setRaiz(true);
		tree.add(node);
	}

	private Set<String> getFathers(Set<String> fathers, String child) {
		for (Node node : tree) {
			if (node.getChild().equals(child)) {
				fathers.add(node.getFather());
				if (node.isRaiz())
					fathers.add(child);
				else {
					child = node.getFather();
					getFathers(fathers, child);
				}
				break;
			}
		}

		return fathers;
	}

	public void executa() {
		List<String> markNews = new ArrayList<>();
		raiz = pn.getMark();
		markNews.add(raiz);
		int size = markNews.size();

		for (int z = 0; z < size; z++) {
			String mark = markNews.get(z);

			List<Transition> toFire = pn.getTransitionsAbleToFire(mark);
			if (toFire.isEmpty()) {
				blockeds.add(mark);
				continue;
			}

			for (Transition transition : toFire) {
				pn.setMark(mark);
				transition.fire();
				String markLine = pn.getMark();

				String[] mLine = markLine.replaceAll("\\[|\\]| ", "").split(",");
				Set<String> fathers = getFathers(new HashSet<>(), mark);
				// fathers.add(mark);
				if (!fathers.isEmpty()) {
					int[] hasW = new int[mLine.length];
					for (String mark2Line : fathers) {
						if (!mLine.equals(mark2Line)) {
							String[] m2Line = mark2Line.replaceAll("\\[|\\]| ", "").split(",");
							for (int i = 0; i < m2Line.length; i++) {
								if (m2Line[i].contains("w")) {
									hasW[i] = fathers.size();
									mLine[i] = m2Line[i];
								} else if (mLine[i].contains("w")) {
									hasW[i] = fathers.size();
								} else if (Integer.valueOf(mLine[i]) >= Integer.valueOf(m2Line[i]))
									hasW[i] += 1;
								else
									hasW[i] -= 1;
							}
						}
					}

					String[] markSplit = mark.replaceAll("\\[|\\]| ", "").split(",");
					for (int i = 0; i < hasW.length; i++) {
						if (markSplit[i].contains("w")) {
							// Integer mInt =
							// Integer.valueOf(mLine[i].replaceAll("(\\d+)w",
							// "$1")) + 1;
							// mLine[i] = mInt + "w";
							mLine[i] = markSplit[i];
						}
						if (!mLine[i].contains("w") && hasW[i] == fathers.size() && Integer.valueOf(markSplit[i]) > 0
								&& Integer.valueOf(markSplit[i]) >= Integer.valueOf(mLine[i]))
							mLine[i] = mLine[i] + "w";
					}
				}
				markLine = Arrays.toString(mLine);
				addNode(mark, markLine, transition.getName());
				if (!markNews.contains(markLine)) {
					markNews.add(markLine);
					++size;
				}
			}
		}

		if (!blockeds.isEmpty()) {
			for (Node node : tree)
				if (!node.getFather().contains("w") && blockeds.contains(node.getChild()))
					node.setChild(node.getChild().replaceAll("(\\d+)w", "$1"));

			Set<String> newBlockeds = new HashSet<>();
			for (String blo : blockeds)
				newBlockeds.add(blo.replaceAll("(\\d+)w", "$1"));
			blockeds = newBlockeds;
		}

	}

	public String printTree() {
		StringBuilder r = new StringBuilder("\n");
		tree.forEach(t -> r.append(t));
		r.append("\n");
		return r.toString().replaceAll("\\d+(w)", "$1");
	}

	public String getStatusBlocked() {
		if (blockeds.isEmpty())
			return "Nao existem estados bloqueantes na rede de petri. ";
		else {
			StringBuilder r = new StringBuilder("Os estados bloqueantes da rede sao: \n");
			blockeds.forEach(t -> r.append(t + "\n"));
			return r.toString();
		}
	}

}
