package digraph;


public class FirstMain {

	public static void main(String[] args) {
		Digraph<Integer, String> test = new Digraph<>(5);
		test.addVertex(5);
		test.addVertex(6);
//		test.addEdge(0, test.getVertex(5), test.getVertex(6));
//		
//		test.addVertex(200);
//		test.addVertex(344);
//		test.addVertex(53);
//		test.addEdge(0, test.getVertex(5), test.getVertex(53));
//		test.addEdge(0, test.getVertex(200), test.getVertex(344));
//		test.addEdge(0, test.getVertex(344), test.getVertex(200));
//		test.addEdge(0, test.getVertex(200), test.getVertex(344));
		
		test.addVertex(60);
		test.addVertex(70);
		test.addVertex(80);

		test.printGraph();
	}

}
