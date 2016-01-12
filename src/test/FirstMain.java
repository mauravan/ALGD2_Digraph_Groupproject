package test;

import digraph.Digraph;

public class FirstMain {

	public static void main(String[] args) {
		Digraph<Integer, String> test = new Digraph<>();
		
		test.addVertex(1);
		test.addVertex(2);
		test.addVertex(3);
		test.addVertex(4);
		test.addEdge("erster", 10, 1, 2);
		test.addEdge("zweiter", 20, 2, 3);
		test.addEdge("dritter", 30, 3, 4);
		test.addEdge("vierter", 10, 1, 3);
		
		System.out.println(test.toString());
	}

}
