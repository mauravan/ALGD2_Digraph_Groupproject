package digraph;

import java.util.HashMap;

public class DijkstraMain {
	
	
	public static void main(String[] args) {
		Digraph<Character, String> test = new Digraph<>();
		
		test.addVertex('A');
		test.addVertex('B');
		test.addVertex('C');
		test.addVertex('D');
		test.addVertex('E');
		test.addVertex('F');
		
		test.addEdge("ab", 4, 'A', 'B');
		test.addEdge("ac", 2, 'A', 'C');
		test.addEdge("ae", 4, 'A', 'E');
		test.addEdge("bc", 1, 'B', 'C');
		test.addEdge("cd", 3, 'C', 'D');
		test.addEdge("ce", 5, 'C', 'E');
		test.addEdge("df", 2, 'D', 'F');
		test.addEdge("ef", 1, 'E', 'F');
		
		HashMap<Character, Double> dij = test.dijkstra('A');
		for (Character c : dij.keySet()) {
			System.out.println(c +"  => " +dij.get(c).toString());
		}
		System.out.println(test.toString());
	}
}
