package operation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
			return father + "---" + transition + "---> " + child + ", raiz=" + raiz;
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

	private List<String> getFathers(List<String> fathers, String child) {
		for (Node node : tree) {
			if (node.child.equals(child)) {
				fathers.add(node.father);
				if (!node.raiz) {
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
		for (Iterator<String> iterator = markNews.iterator(); iterator.hasNext();) {
			String mark = iterator.next();

			if (!mark.equals(raiz)) {
				if (markNews.contains(mark))
					continue;
				if (pn.getTransitionsAbleToFire().isEmpty()) {
					blockeds.add(mark);
					continue;
				}
			}

			List<Transition> toFire = pn.getTransitionsAbleToFire();
			for (Transition transition : toFire) {
				pn.setMark(mark);
				transition.fire();

				String markLine = pn.getMark();

				String[] mLine = markLine.replaceAll("\\]|\\]| ", "").split(",");
				List<String> fathers = getFathers(new ArrayList<>(), markLine);
				for (String mark2Line : fathers) {
					String[] m2Line = mark2Line.replaceAll("\\]|\\]| ", "").split(",");
					for (int i = 0; i < m2Line.length; i++) {
						if (m2Line[i].matches("w") || Integer.valueOf(mLine[i]) > Integer.valueOf(m2Line[i]))
							mLine[i] = "w";
					}
				}

				addNode(mark, markLine, transition.getName());
				markNews.add(markLine);
			}
		}
		StringBuilder r = new StringBuilder("\n");
		tree.forEach(t -> r.append(tree.toString() + "\n"));
		r.append("\n");
		return r.toString();
	}

}
