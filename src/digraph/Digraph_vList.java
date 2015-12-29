package digraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Approach 1: List of Edges and List of Vertex: Nr of Verts |V| = n Nr of Edges
 * |E|
 * 
 * Space useage of this design would be O(|V| + |E|)
 * 
 * Frequently used Operations: - Find all adjacent Nodes of a given Node: O(|E|)
 * -> would lead to O(n^2) because max nr of edges in a simple graph is n(n-1) -
 * Find out if 2 given Nodes are Connected: O(|E|)
 * 
 * Approach 2: Adjaency Matrix:
 * 
 * Space useage of this design would be O(|V|^2)
 * 
 * - Find all adjacent Nodes of a given Node: O(|V|) - Find out if 2 given Nodes
 * are Connected: O(1) (With a Hash Table)
 * 
 * Apprach 3: Adjacency lists:
 * 
 * Degree of Vertex = maximum of 2*|E| if no multiedge allowed
 * 
 * Space useage of this design would be O(|E|)
 * 
 * - Find all adjacent Nodes of a given Node: O(2*|V|) - Find out if 2 given
 * Nodes are Connected: O(|V|) - Using a linear search in an unsorted list.
 * using binary search in a sorted list.
 * 
 * @author Roman
 *
 * @param <V>
 *            Type of Vertices
 * @param <E>
 *            Type of Edges
 */
public class Digraph_vList<V, E> {

	// TODO: I'm iterating lot through this shit. Maybe use more efficient one?
	private HashMap<V, Vertex> m_vList;

	/**
	 * m_size defines how many Verts are stored in vList. Used to define the
	 * Index of a Vertex.
	 * 
	 */
	private int m_size;
	/**
	 * defines how many Edges there are in the Graph. If no multiedges or
	 * selfloopes allowed maxnumber of Edged is n(n-1) if so it is a Full Graph.
	 */
	private int m_size_E;

	/**
	 * Construct a Digraph with initialCapacity = 16 and loadFactor = 0.75
	 */
	public Digraph_vList() {
		m_vList = new HashMap<>();
		m_size = 0;
		m_size_E = 0;
	}

	/**
	 * Construct a Digraph with initialCapacity = size and loadFactor = 0.75
	 * 
	 * @param size
	 *            sets the Size of initialCapacity
	 * @throws IllegalArgumentException
	 *             - if the initial capacity is negative
	 */
	// TODO: without param.
	public Digraph_vList(int size) {
		m_vList = new HashMap<>(size);
		m_size = 0;
		m_size_E = 0;
	}

	/**
	 * Construct a Digraph with initialCapacity = size and loadFactor =
	 * loadFactor
	 * 
	 * @param size
	 *            sets the Size of initialCapacity
	 * @param loadFactor
	 *            sets loadFactor of HashMap
	 * @throws IllegalArgumentException
	 *             - if the initial capacity is negative or the load factor is
	 *             nonpositive.
	 */
	public Digraph_vList(int size, float loadFactor) {
		m_vList = new HashMap<>(size, loadFactor);
		m_size = 0;
		m_size_E = 0;
	}

	/**
	 * Adds a Vertex into the Graph. Time complexity: O(1) Duplicated keys are
	 * discarded
	 * 
	 * @param key
	 *            of the vertex.
	 */
	public boolean addVertex(V key) {
		if (!m_vList.containsKey(key)) {
			m_vList.put(key, new Vertex(key));
			m_size++;
			return true;
		}
		return false;
	}

	/**
	 * Adds a Vertex v into the Graph. Time complexity: O(1)
	 * 
	 * @param v
	 *            to insert
	 * @return true if added, false otherwise
	 */
	// TODO: Check Parent of Vertex
	public boolean addVertex(Vertex v) {
		if (!m_vList.containsValue(v)) {
			m_vList.put(v.m_key, v);
			m_size++;
			return true;
		}
		return false;
	}

	/**
	 * Get the Vertex from the Digraph. Time complexity: O(1)
	 * 
	 * @param key
	 * @return specific Vertex from Digraph or null
	 */
	public Vertex getVertex(V key) {
		return m_vList.get(key);
	}

	/**
	 * Get a Edge from specific vertices In time complexity of DoubleLinkedList
	 * Implementation
	 * 
	 * @param u
	 *            is original vertex
	 * @param v
	 *            is destination vertex
	 * @return edge or null
	 */

	// TODO: Do javadoc. Adjust to hashmap.
	public Edge getEdge(Vertex u, Vertex v) {
		for (Edge e : u.adjacencyList) {
			if (e.origin.equals(u) && e.destination.equals(v))
				return e;
		}
		return null;

	}

	/**
	 * remove vertex from a Digraph O(1)
	 * 
	 * @param key
	 *            of vertex to remove
	 */
	public boolean removeVertex(V key) {
		if (!m_vList.containsKey(key)) {
			m_vList.remove(key);
			m_size--;
			return true;
		}
		return false;
	}

	public void removeEdge(Edge e) {
		Vertex u = e.origin;
		Vertex v = e.destination;
		u.adjacencyList.remove(v);
	}

	/**
	 * Adds a Edge into the Graph
	 * 
	 * @param weight
	 *            give an appropriate weight or 0 for default.
	 * @param origin
	 *            from which Vertex the Edge should come
	 * @param destination
	 *            to which Vertex the Edge should go
	 */
	public void addEdge(int weight, Vertex origin, Vertex destination) {

		// TODO: Check if Vertex exists
		Edge e = new Edge(weight, origin, destination);
		if (!origin.adjacencyList.contains(e)) {
			origin.adjacencyList.add(e);
			m_size_E++;
		}
	}

	/**
	 * Checks if a Vertex with the given Key already exists in the Graph.
	 * Timecomplexity: O(1)
	 * 
	 * @param key
	 */
	public boolean containsVertex(V key) {
		return m_vList.containsKey(key);
	}

	/**
	 * Checks if a given Vertex already exists in the Graph. Time complexity:
	 * O(1)
	 * 
	 * @param v
	 */
	public boolean containsVertex(Vertex v) {
		return m_vList.containsValue(v);
	}

	/**
	 * Checks if two Vertex are connected by an Edge
	 * 
	 * @param v1
	 * @param v2
	 * @return true if they are. False otherwise.
	 */
	public boolean areNeighbors(Vertex v1, Vertex v2) {
		// TODO: return statement
		if (v1.adjacencyList.contains(v2)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if two Vertex are connected by an Edge
	 * 
	 * @param index1
	 * @param index2
	 * @return true if they are. False otherwise.
	 */
	public boolean areNeighbors(int index1, int index2) {
		if (vList.get(index1).adjacencyList.contains(vList.get(index2))) {
			return true;
		}
		return false;
	}

	/**
	 * Returns a List of Vertex that are adjacent to a given Vertex v.
	 * 
	 * @param v
	 *            given Vert.
	 * @return List<Vertex<V>> of adjacent Verts or NULL if v does not exist.
	 *         Returned list may be empty if the given Vertex has no neightbors
	 */
	public List<Vertex> getNeighbors(Vertex v) {
		// TODO: Discuss in team: Pro - Gives a list that the user can destroy
		// without tempering with the Graph
		// Cons - Is a frequently used Method and not very efficient i'd say.
		// Maybe we can do this with stream framework?
		List<Vertex> l = new LinkedList<>();
		for (Edge e : v.adjacencyList) {
			l.add(e.destination);
		}
		return l;
	}

	/**
	 * Returns a List of Vertex that are adjacent to a Vertex that is stored at
	 * the given Index.
	 * 
	 * @param index
	 *            index of the Vertex in the Graph
	 * @return List<Vertex<V>> of adjacent Verts or NULL if index > size of the
	 *         array. Returned list may be empty if the given Vertex has no
	 *         neightbors
	 */
	public List<Vertex> getNeighbors(int index) {
		// TODO: Same as the method above
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
	public void clear() {
		m_vList.clear();
		m_size = 0;
	}

	/**
	 * Good Luck!
	 */
	public String toString() {
		// TODO: Implement this shit
		// for (Vertex vertex : m_vList) {
		// vertex.toString();
		// }
		return null;
	}

	public void printGraph() {
		int i = 0;
		for (Entry<V, Vertex> entry : m_vList.entrySet()) {
			System.out.println("[" + i++ + "] --->" + entry.getValue().toString());
		}

	}

	/**
	 * 
	 * @author Matthias
	 *
	 * @param <E>
	 *            Wrapper Type of weight
	 *
	 */
	private class Edge {
		private E m_weight;
		//TODO: Delete if not needed!
		private Vertex origin;
		private Vertex destination;

		public E getWeight() {
			return m_weight;
		}

		public void setWeight(E weight) {
			m_weight = weight;
		}

		/**
		 * Constructor for Edges. Destination and Origin have to be in the same
		 * graph
		 * 
		 * @param weight
		 *            defines the "cost" of an Edge. Use 0 for unweighed Edges.
		 * @param origin
		 *            * Vertex from which the Edge comes. Can be same as
		 *            destination.
		 * @param destination
		 *            Vertex to which the Edge goes. Can be same as origin.
		 */
		public Edge(E weight, Vertex origin, Vertex destination) {
			this.origin = origin;
			this.destination = destination;
			m_weight = weight;
		}

		/**
		 * Checks if origin and destination are equal
		 * 
		 * @param o
		 * @return true if they are. false otherwise.
		 */
		public boolean equals(Object o) {
			try {
				return (origin.equals(((Edge) o).origin) && destination.equals(((Edge) o).destination)
						&& m_weight.equals(((Edge) o).m_weight));
			} catch (Exception e) {
				return false;
			}
		}

	}

	/**
	 * 
	 * @author Matthias
	 *
	 * @param <V>
	 */
	private class Vertex {
		/**
		 * Unique Identifier
		 */
		private V m_key;
		/**
		 * Index of vList in with the adjacency list is stored
		 */
		private List<Edge> adjacencyList;

		private Vertex(V key) {
			m_key = key;
			adjacencyList = new DLinkedList<>();
		}

		/**
		 * Only checks if keys are equal.
		 * 
		 * @param o
		 * @return true if key of given Vertex is equal. False otherwise.
		 */
		@SuppressWarnings("unchecked")
		public boolean equals(Object o) {
				return m_key.equals(((Vertex)o).m_key);			
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("[" + m_key.toString() + "]");
			for (Edge edge : adjacencyList) {
				sb.append(" --> " + "[" + edge.destination.m_key.toString() + "]");
			}
			return sb.toString();
		}
	}
}
