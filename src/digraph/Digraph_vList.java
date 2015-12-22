package digraph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Approach 1:
 * List of Edges and List of Vertex:
 * Nr of Verts |V| = n
 * Nr of Edges |E|
 * 
 * Space useage of this design would be O(|V| + |E|)
 * 
 * Frequently used Operations:
 *  - Find all adjacent Nodes of a given Node: O(|E|) -> would lead to O(n^2) because max nr of edges in a simple graph is n(n-1)
 *  - Find out if 2 given Nodes are Connected: O(|E|)
 * 
 * Approach 2:
 * Adjaency Matrix:
 * 
 * Space useage of this design would be O(|V|^2)
 * 
 * - Find all adjacent Nodes of a given Node: O(|V|)
 * - Find out if 2 given Nodes are Connected: O(1) (With a Hash Table)
 * 
 * Apprach 3:
 * Adjacency lists:
 * 
 * Degree of Vertex = maximum of 2*|E| if no multiedge allowed
 * 
 *  Space useage of this design would be O(|E|)
 *  
 *  - Find all adjacent Nodes of a given Node: O(2*|V|)
 *  - Find out if 2 given Nodes are Connected: O(|V|) - Using a linear search in an unsorted list. using binary search in a sorted list.
 * 
 * @author Roman
 *
 * @param <V> Type of Vertices
 * @param <E> Type of Edges
 */
public class Digraph_vList<V, E> {
	
	//TODO: I'm iterating lot through this shit. Maybe use more efficient one?
	private List<Vertex> vList;
	
	/**
	 * m_size defines how many Verts are stored in vList. Used to define the Index of a Vertex.
	 * 
	 */
	private int m_size;
	/**
	 * defines how many Edges there are in the Graph.
	 * If no multiedges or selfloopes allowed maxnumber of Edged is n(n-1) if so it is a Full Graph.
	 */
	private int m_size_E;
	
	/**
	 * Constructor ...
	 * 
	 * @param size sets the Size of the Datastructure
	 */
	public Digraph_vList(int size){
		vList = new ArrayList<>(size);
		m_size = 0;
		m_size_E = 0;
	}
	
	/**
	 * Adds a Vertex into the Graph.
	 * @param key Type of the vertex.
	 */
	public void addVertex(V key){
		if (!containsVertex(key)) {
			vList.add(new Vertex(key));
			m_size++;
		}
	}
	
	public Vertex getVertex(V key){
		//TODO: can someone fuck up the Graph? Maybe return a clone
		for (Vertex vertex : vList) {
			if (vertex.equals(new Vertex(key))) {
				return vertex;
			}
		}
		return null;
	}
	
	public Edge getEdge(){
		//TODO: can someone fuck up the Graph? Maybe return a clone. Implement
		return null;
		
	}
	
	public void removeVertex(){
		//TODO: can someone fuck up the Graph? Maybe return a clone. implement
	}
	
	public void removeEdge(){
		//TODO: can someone fuck up the Graph? Maybe return a clone. implement
	}
	
	/**
	 * Adds a Edge into the Graph
	 * @param weight give an appropriate weight or 0 for default.
	 * @param origin from which Vertex the Edge should come
	 * @param destination to which Vertex the Edge should go
	 */
	public void addEdge(int weight, Vertex origin, Vertex destination){
		//TODO: It seems somewhat bad to always create a new Edge or Vertex
		if (!origin.adjacencyList.contains(new Edge(weight, origin, destination))) {
			origin.adjacencyList.add(new Edge(weight, origin, destination));
			m_size_E++;
		}
	}
	
	/**
	 * Checks if a Vertex with the given Key already exists in the Graph.
	 * @param key
	 * @return true if key is already present in the Graph. false otherwise.
	 */
	public boolean containsVertex(V key){
		//TODO: Discuss in team if this is efficient enough.
		Vertex tmp = new Vertex(key);
		for (Vertex vertex : vList) {
			if (vertex.equals(tmp)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if two Vertex are connected by an Edge
	 * @param v1
	 * @param v2
	 * @return true if they are. False otherwise.
	 */
	public boolean areNeightbors(Vertex v1, Vertex v2){
		//TODO: How fast is this really?
		if (v1.adjacencyList.contains(v2)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if two Vertex are connected by an Edge
	 * @param index1
	 * @param index2
	 * @return true if they are. False otherwise.
	 */
	public boolean areNeightbors(int index1, int index2){
		//TODO: How fast is this really?
		if (vList.get(index1).adjacencyList.contains(vList.get(index2))) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns a List of Vertex that are adjacent to a given Vertex v.
	 * @param v given Vert.
	 * @return List<Vertex<V>> of adjacent Verts or NULL if v does not exist. Returned list may be empty if the given Vertex has no neightbors
	 */
	public List<Vertex> getNeightbors(Vertex v){
		//TODO: Discuss in team: Pro - Gives a list that the user can destroy without tempering with the Graph
		//						Cons - Is a frequently used Method and not very efficient i'd say.
		//Maybe we can do this with stream framework?
		List<Vertex> l = new LinkedList<>();
		for (Vertex vertex : vList) {
			if (vertex.equals(v)) {
				for (Edge e : vertex.adjacencyList) {
					l.add(e.destination);
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns a List of Vertex that are adjacent to a Vertex that is stored at the given Index.
	 * @param index index of the Vertex in the Graph
	 * @return List<Vertex<V>> of adjacent Verts or NULL if index > size of the array. Returned list may be empty if the given Vertex has no neightbors
	 */
	public List<Vertex> getNeightbors(int index){
		//TODO: Same as the method above
		List<Vertex> l = new LinkedList<>();
		if (index < m_size) {
			for (Edge e : vList.get(index).adjacencyList) {
				l.add(e.destination);
			}
			return l;
		}
		return null;
	}
	
	/**
	 * Clears the Graph
	 */
	public void clear(){
		vList.clear();
		m_size = 0;
	}
	
	/**
	 * Good Luck!
	 */
	public String toString(){
		//TODO: Implement this shit
		for (Vertex vertex : vList) {
			vertex.toString();
		}
		return null;
	}
	
	public void printGraph(){
		int i = 0;
		for (Vertex vertex : vList) {
			System.out.println("["+i++ +"] --->" + vertex.toString());
		}
	}

	
	/**
	 * Having an explicit Edge Class allowes for Storage of extra Information about them.
	 * 
	 * @author Roman
	 *
	 * @param <E> Type of Edge
	 */
	public class Edge {
		
		private int m_weight;
		private Vertex origin; //TODO: Make Array of some sort if Multiedge is allowed.
		private Vertex destination;
		
		/**
		 * Constructor for Edges.
		 * 
		 * @param weight defines the "cost" of an Edge. Use 0 for unweighed Edges.
		 * @param origin Vertex from which the Edge comes. Can be same as destination.
		 * @param destination Vertex to which the Edge goes. Can be same as origin.
		 */
		private Edge(int weight, Vertex origin, Vertex destination){
			//TODO: The Exercise states, that the Type of the Edge should be chooseable? Why would it need a type?
			m_weight = weight;
			this.origin = origin;
			this.destination = destination;
		}
		
		/**
		 * Checks if origin and destination are equal
		 * @param o
		 * @return true if they are. false otherwise.
		 */
		public boolean equals(Object o){
			//TODO: In Case multiedges are allowed. Change this. Also add check if weight is equal.
			//TODO: Fix unchecked type safety
			return (origin.equals(((Edge) o).origin) && destination.equals(((Edge)o).destination));
		}
		
	}
	
	/**
	 * 
	 * @author Roman
	 *
	 */
	public class Vertex{
		
		/**
		 * Unique Identifier
		 */
		private V m_key;
		/**
		 * Index of vList in with the adjacency list is stored
		 */
		private List<Edge> adjacencyList;
		
		private Vertex(V key){
			m_key = key;
			adjacencyList = new LinkedList<>();
		}
		
		

		/**
		 * Only checks if keys are equal.
		 * @param o
		 * @return true if key of given Vertex is equal. False otherwise.
		 */
		private boolean equals(Vertex o){ // Could be changed to Object with unchecked type safety
			return m_key.equals(o.m_key); // V must implement equals or we are fucked
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("["+m_key.toString()+"]");
			for (Edge edge : adjacencyList) {
				sb.append(" --> " + "[" + edge.destination.m_key.toString() +"]");
			}
			return sb.toString();
		}
	}
}
