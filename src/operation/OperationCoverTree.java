package operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Petrinet;
import model.Transition;

public class OperationCoverTree {

	class Node {
		String father;
		String child;
		boolean raiz;
		String transition;

		public Node(String father, String child, String transition) {
			super();
			this.father = father;
			this.child = child;
			raiz = false;
			this.transition = transition;
		}

		@Override
		public String toString() {
			return father + "---" + transition + "---> " + child + ", raiz=" + raiz + "\n";
		}

	}

	List<Node> tree = new ArrayList<>();
	String raiz;
	Set<String> blockeds = new HashSet<>();

	private void addNode(String father, String child, String t) {
		Node node = new Node(father, child, t);
		if (father.equals(raiz))
			node.raiz = true;
		tree.add(node);
	}

	private Set<String> getFathers(Set<String> fathers, String child) {
		for (Node node : tree) {
			if (node.child.equals(child)) {
				fathers.add(node.father);
				if (node.raiz)
					fathers.add(child);
				else {
					child = node.father;
					getFathers(fathers, child);
				}
				break;
			}
		}

		return fathers;
	}

	public String executa(Petrinet pn) {
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
								} else if (mLine[i].contains("w"))
									hasW[i] = fathers.size();
								else if (Integer.valueOf(mLine[i]) >= Integer.valueOf(m2Line[i]))
									hasW[i] += 1;
								else
									hasW[i] -= 1;
							}
						}
					}

					String[] markSplit = mark.replaceAll("\\[|\\]| ", "").split(",");
					for (int i = 0; i < hasW.length; i++) {
						if (markSplit[i].contains("w"))
							mLine[i] = markSplit[i];
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
		StringBuilder r = new StringBuilder("\n");
		tree.forEach(t -> r.append(t));
		r.append("\n");
		return r.toString().replaceAll("\\d+(w)", "$1");
	}

}
