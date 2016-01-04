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
		
		test.addEdge("ab", 2, 'A', 'B');
		test.addEdge("ac", 1, 'A', 'C');
		test.addEdge("ad", 4, 'A', 'D');
		test.addEdge("bd", 1, 'B', 'D');
		test.addEdge("bc", 2, 'B', 'C');
		test.addEdge("cd", 3, 'C', 'D');
		test.addEdge("ce", 1, 'C', 'E');
		test.addEdge("de", 2, 'D', 'E');
		
		HashMap<Character, Double> dij = test.dijkstra('A');
		for (Character c : dij.keySet()) {
			System.out.println(c +"  => " +dij.get(c).toString());
		}
		System.out.println(test.toString());
	}
}
