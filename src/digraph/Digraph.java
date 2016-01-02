package digraph;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A directed, weighted (only positive) Graph which allows self-loops and
 * multiedges using a HashSet to store all the Vertices. To allow weighted Edges
 * and nested Adjacency Lists we used the inner classes Vertex and Edge.
 * 
 * The Verticies and Edges are reffered by key. Those Key-Values are unique.
 * If one adds a key that is already present in the Graph, the old value will be discarded.
 * 
 * @author Roman Meier, Alex Melliger, Matthias Keller, Stefan Mettler
 *
 * @param <V>
 *            The Type of the Vertices
 * @param <E>
 *            The Type of the Edges
 * 
 */
public class Digraph<V, E> implements IDigraph<V, E>, Serializable {

	/**
	 * internal Datastructure used to store all Vertices
	 */
	private HashMap<V, Vertex> m_vList;

	/**
	 * internal Datastructure used to store all Edges
	 */
	private HashMap<E, Edge> m_eList;

	public Digraph() {
		m_vList = new HashMap<>();
		m_eList = new HashMap<>();
	}

	/**
	 * Construct a Digraph with initialCapacity = size and loadFactor = 0.75
	 * 
	 * @param sizeVertex
	 *            sets the size of the internal vertex hashmap
	 * @param sizeEdge
	 * 			  sets the size of the internal edge hashmap
	 * @throws IllegalArgumentException
	 *             - if the initial capacity is negative
	 */
	public Digraph(int sizeVertex, int sizeEdge) {
		m_vList = new HashMap<>(sizeVertex);
		m_eList = new HashMap<>(sizeEdge);
	}

	/**
	 * Construct a Digraph with initialCapacity = size and loadFactor =
	 * loadFactor
	 * 
	 * @param sizeVertex
	 *            sets the size of the internal vertex hashmap
	 * @param sizeEdge
	 * 			  sets the size of the internal edge hashmap
	 * @param loadFactorVertex
	 *            sets loadFactor of the vertex hashmap
	 * @param loadFactorEdge
	 *            sets loadFactor of the edge hashmap
	 * @throws IllegalArgumentException
	 *             - if the initial capacity is negative or the load factor is
	 *             nonpositive.
	 */
	public Digraph(int sizeVertex, int sizeEdge, float loadFactorVertex, float loadFactorEdge) {
		m_vList = new HashMap<>(sizeVertex, loadFactorVertex);
		m_eList = new HashMap<>(sizeEdge, loadFactorEdge);
	}

	/**
	 * clone the graph object using serialization to break selfloops
	 * 
	 * @return the new clone
	 */
	public Object clone() {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(out);
			oout.writeObject(this);
			ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
			return oin.readObject();
		} catch (Exception e) {
			throw new RuntimeException("cannot clone class");
		}
	}

	@Override
	public boolean addEdge(E key, double weight, V origin, V destination) {
		assert (weight>=0) : "weight must be >= 0";
		if (containsVertex(origin) && containsVertex(destination)) {
			Edge edge = new Edge(key, weight, origin, destination);
			//add the new edge to the graph hashmap and the specific outgoint/incominglists
			m_eList.put(key, edge);
			m_vList.get(origin).outgoingList.add(key);
			m_vList.get(destination).incomingList.add(key);
			return true;
		}
		return false;
	}

	@Override
	public boolean addVertex(V vertex) {
		if (!containsVertex(vertex)) {
			Vertex tmp = new Vertex(vertex);
			m_vList.put(vertex, tmp);
			return true;
		}
		return false;
	}

	@Override
	public boolean containsEdge(E edge) {
		return m_eList.containsKey(edge);
	}

	@Override
	public boolean containsEdge(V origin, V destination) {
		if (!(containsVertex(origin) && containsVertex(destination)))
			return false;
		for (E e : m_vList.get(origin).outgoingList) {
			if (m_eList.get(e).destination.equals(destination)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsVertex(V vertex) {
		return m_vList.containsKey(vertex);
	}

	@Override
	public int indegOf(V vertex) {
		assert (containsVertex(vertex)) : "Vertex must be in this Graph";
		return m_vList.get(vertex).incomingList.size();
	}

	@Override
	public int outdegOf(V vertex) {
		assert (containsVertex(vertex)) : "Vertex must be in this Graph";
		return m_vList.get(vertex).outgoingList.size();
	}

	@Override
	public Set<V> getVertsOfGraph() {
		return m_vList.keySet();
	}

	@Override
	public Set<E> getEdgesOfGraph() {
		return m_eList.keySet();
	}

	@Override
	public Set<E> getAllEdgesOfVertex(V vertex) {
		assert (containsVertex(vertex)) : "Vertex must be in this Graph";
		DLinkedList<E> inc = (DLinkedList<E>) m_vList.get(vertex).incomingList;
		DLinkedList<E> out = (DLinkedList<E>) m_vList.get(vertex).outgoingList;
		inc.conc(out, true);
		return new HashSet<>(inc);
	}

	@Override
	public Set<E> getLeavingEdgesOfVertex(V vertex) {
		assert (containsVertex(vertex)) : "Vertex must be in list";
		return new HashSet<E>(m_vList.get(vertex).outgoingList);
	}

	@Override
	public Set<E> getIncomingEdgesOfVertex(V vertex) {
		assert (containsVertex(vertex)) : "Vertex must be in list";
		return new HashSet<E>(m_vList.get(vertex).incomingList);
	}

	@Override
	public Set<E> getEdgesBetween(V source, V destination) {
		return (m_vList.get(source).outgoingList.stream().filter(e -> (m_eList.get(e).destination.equals(destination)))
				.collect(Collectors.toSet()));
	}

	@Override
	public V getEdgeSource(E edge) {
		assert (containsEdge(edge)) : "Edge must be in list";
		return m_eList.get(edge).origin;
	}

	@Override
	public V getEdgeDestination(E edge) {
		assert (containsEdge(edge)) : "Edge must be in list";
		return m_eList.get(edge).destination;	
	}

	@Override
	public double getEdgeWeight(E edge) {
		assert (containsEdge(edge)) : "Edge must be in list";
		return m_eList.get(edge).m_weight;
	}

	// delete from outgoing list of origin, incoming list of destination and
	// m_eList
	@Override
	public boolean removeEdge(E edge) {
		assert (containsEdge(edge)) : "Edge must be in list";
		Edge e = m_eList.get(edge);
		return (m_vList.get(e.origin).outgoingList.remove(edge) && m_vList.get(e.destination).incomingList.remove(edge)
				&& m_eList.remove(edge, e));

	}

	@Override
	public E removeEdge(double weight, V origin, V destination) {
		assert (weight>=0) : "not a valid weight";
		try{
		// find the edge in the HashMap
		Optional<Edge> edge = m_eList.values().stream()
				.filter(e -> (e.destination.equals(destination)
							  && e.origin.equals(origin)
							  && e.m_weight==weight))
				.findFirst();
		
		//remove the edge and return the key of it if it was successful
		return removeEdge(edge.get().m_key_E) ? edge.get().m_key_E : null;		
		}
		catch(NoSuchElementException e){
			return null;
		}

	}

	@Override
	public boolean removeVertex(V vertex) {
		//check if vertex is in graph
		if(!containsVertex(vertex))
			return false;
		
		//get all edges touching v (all incoming and outgoing)
		DLinkedList<E> touchingEdges = (DLinkedList<E>) m_vList.get(vertex).outgoingList;
		touchingEdges.conc(m_vList.get(vertex).incomingList,true);		
		
		//remove all those edges
		if(!removeAllEdges(touchingEdges))
			return false;		
		
		//remove v from vertex-hashmap
		m_vList.remove(vertex);
		
		return true;
	}

	@Override
	public void setEdgeWeight(E edge, double weight) {
		assert(weight >= 0) : "weight must be >=0";
		assert(containsEdge(edge)) : "Edge must be in Graph";
		if (weight >= 0)
			m_eList.get(edge).m_weight = weight;
	}

	@Override
	public boolean removeAllEdges(Collection<? extends E> edges) {
		int edgeSize = getNumberOfEdges();
		for (E e : edges) {
			if (containsEdge(e))
				removeEdge(e);
		}
		return (edgeSize - edges.size() == getNumberOfEdges());
	}

	@Override
	public boolean removeAllEdges(E[] edges) {
		int edgeSize = getNumberOfEdges();
		for (E e : edges) {
			if (containsEdge(e))
				removeEdge(e);
		}
		return (edgeSize - edges.length == getNumberOfEdges());
	}

	@Override
	public boolean removeAllEdges() {
		for (Vertex v : m_vList.values()) {
			v.outgoingList.clear();
			v.incomingList.clear();
		}
		m_eList.clear();
		return m_eList.isEmpty();
	}

	@Override
	public boolean clear() {
		m_eList.clear();
		m_vList.clear();
		return m_eList.isEmpty() && m_vList.isEmpty();
	}

	@Override
	public Set<E> removeAllEdgesBetweenVertex(V origin, V destination) {
		assert(containsVertex(origin) && containsVertex(destination)) : "Vertices must be in graph";
		HashSet<E> edgeList = new HashSet<>();
		for (E e : m_vList.get(origin).outgoingList) {
			if (m_eList.get(e).destination.equals(destination)) {
				// stop the deletion of further objects if one fails and return
				// the
				// ones deleted up to this point
				if (removeEdge(e))
					edgeList.add(e);
				else
					return edgeList;
			}
		}
		return edgeList;
	}

	@Override
	public boolean removeAllVertices(Collection<? extends V> vertices) {
		int listSize = m_vList.size();
		for (V v : vertices) {
			if (containsVertex(v))
				removeVertex(v);
		}
		return (listSize - vertices.size() == m_vList.size());
	}

	@Override
	public int getNumberOfVerts() {
		return m_vList.size();
	}

	@Override
	public int getNumberOfEdges() {
		return m_eList.size();
	}

	@Override
	public double getShortestPath(V origin, V desination) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public HashMap<V, Double> dijkstra(V origin) {
		//BEGIN INIT
		//D ist Hashmap mit Key = Vertex und Value = Wert des momentan kürzesten Pfades von origin nach v.
		//A enthält den Nachbarknoten über den der kürzeste Pfad geht
		HashMap<V, Double> d = new HashMap<>();
		LinkedList<V> a = new LinkedList<>();
		//Mit maximaler Anzahl initialisieren, damit immer kürzere Pfade gefunden werden können
		for (V v : m_vList.keySet()) {
			d.put(v, Double.MAX_VALUE);
			a.add(null);
		}
		d.put(origin, 0.0);
		LinkedList<V> perm = new LinkedList<>();
		PQueue<V> pq = new PQueue<>(new Comparator<V>() {
			@Override
			public int compare(V o1, V o2) {
				return (int) (d.get(o1)-d.get(o2));
			}
		});
		
		pq.add(origin);
		//END INIT
		while (!pq.isEmpty()) {
			V u = pq.removeMin();
			for (E e : m_vList.get(u).outgoingList) {
				V v = getEdgeDestination(e);
				if (d.get(v) == Double.MAX_VALUE) {
					pq.add(v);
				}
				E edge = getEdgesBetween(u, v).iterator().next();
				if (test(edge, d, a)) {
					PQItem tmp = pq.getFirstItem();
					pq.updateData(tmp, v);
				}
			}
			perm.add(u);
		}
		return d;
		
	}
	
	/**
	 * 
	 * @param e Edge which should be tested for shortest path
	 * @param d actual Values of shortest path from origin to all Vertices with key V
	 * @param a actual Vertices sequence of shortest path
	 * @return
	 */
	private boolean test(E e, HashMap<V, Double> d, LinkedList<V> a){
		V u = m_eList.get(e).origin;
		V v = m_eList.get(e).destination;
		Edge edge = m_eList.get(e);
		//prüft ob aktuelle strecke grösser ist als start-vertex und kantengewicht und ersetzt
		if (d.get(v) > d.get(u)+edge.m_weight) {
			d.put(v, (d.get(u)+edge.m_weight));
			a.add(v); //enthält den Nachbarknoten über den der kürzeste Pfad geht
			return true;
		}
		return false;
		
	}
	
		@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		for (Vertex v : m_vList.values()) {
			sb.append(v.toString());
			for (E e : v.outgoingList) {
				sb.append(m_eList.get(e).toString());
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////
	// INNER CLASSES
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Representing the Vertices in the Graph. Stores all his Neighbors in a
	 * adjacencyList using a HashSet.
	 * 
	 * @author Roman Meier, Alex Melliger, Matthias Keller, Stefan Mettler
	 * 
	 * @param <V>
	 *            Type of the Vertex
	 */
	private class Vertex {
		/**
		 * Unique Identifier
		 */
		private V m_key_V;
		/**
		 * Index of vList in with the adjacency list is stored
		 */
		private List<E> outgoingList;
		/**
		 * Used to efficiently delete Vertices from the Graph.
		 */
		private List<E> incomingList;

		private Vertex(V key) {
			m_key_V = key;
			outgoingList = new DLinkedList<>();
			incomingList = new DLinkedList<>();
		}

		/**
		 * Only checks if keys are equal.
		 * 
		 * @param o
		 * @return true if key of given Vertex is equal. False otherwise.
		 */
		@SuppressWarnings("unchecked")
		public boolean equals(Object o) {
			return m_key_V.equals(((Vertex) o).m_key_V);
		}

		@Override
		 public String toString() {
			 return ("(" + m_key_V.toString() + ")");
		 }
	}

	/**
	 * Representing the weighted Edges in a Graph.
	 * 
	 * @author Roman Meier, Alex Melliger, Matthias Keller, Stefan Mettler
	 * @param <E>
	 *            Type of the Edge
	 * 
	 */
	private class Edge {
		private double m_weight;
		// TODO: Delete if not needed!
		private V origin;
		private V destination;

		private E m_key_E;

		/**
		 * Constructor for Edges. Destination and Origin have to be in the same
		 * graph
		 * 
		 * @param weight
		 *            defines the "cost" of an Edge. Use 0 for unweighed Edges.
		 *            Only works for positive weights
		 * @param origin
		 *            * Vertex from which the Edge comes. Can be same as
		 *            destination.
		 * @param destination
		 *            Vertex to which the Edge goes. Can be same as origin.
		 */
		// TODO: Provide a static field DEFAULT_WEIGHT and a Constructor using
		// only Vertex Origin or Vertex Destination
		private Edge(E key, double weight, V origin, V destination) {
			m_key_E = key;
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
						&& m_weight == ((Edge) o).m_weight);
			} catch (Exception e) {
				return false;
			}
		}
		
		@Override
		public String toString(){
			return "---"+m_weight+"--->"+m_vList.get(destination).toString();
		}
		

	}

}
