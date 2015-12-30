package digraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Approach 1: List of Edges and List of Vertex: Nr of Verts |V| = n Nr of Edges |E|
 * 
 * Space useage of this design would be O(|V| + |E|)
 * 
 * Frequently used Operations: - Find all adjacent Nodes of a given Node: O(|E|) -> would lead to
 * O(n^2) because max nr of edges in a simple graph is n(n-1) - Find out if 2 given Nodes are
 * Connected: O(|E|)
 * 
 * Approach 2: Adjaency Matrix:
 * 
 * Space useage of this design would be O(|V|^2)
 * 
 * - Find all adjacent Nodes of a given Node: O(|V|) - Find out if 2 given Nodes are Connected: O(1)
 * (With a Hash Table)
 * 
 * Apprach 3: Adjacency lists:
 * 
 * Degree of Vertex = maximum of 2*|E| if no multiedge allowed
 * 
 * Space useage of this design would be O(|E|)
 * 
 * - Find all adjacent Nodes of a given Node: O(2*|V|) - Find out if 2 given Nodes are Connected:
 * O(|V|) - Using a linear search in an unsorted list. using binary search in a sorted list.
 * 
 * @author Roman
 *
 * @param <V> Type of Vertices
 * @param <E> Type of Edges
 */
public class DigraphOld<V, E> {

	/**
	 * internal Datastructure used to Store Vertices
	 */
	private HashMap<V, Vertex> m_vList;

    /**
     * m_size defines how many Verts are stored in vList. Used to define the Index of a Vertex.
     * 
     */
    private int m_size;
    /**
     * defines how many Edges there are in the Graph. If no multiedges or selfloopes allowed
     * maxnumber of Edged is n(n-1) if so it is a Full Graph.
     */
    private int m_size_E;

    /**
     * Construct a Digraph with initialCapacity = 16 and loadFactor = 0.75
     */
    public DigraphOld() {
        m_vList = new HashMap<>();
        m_size = 0;
        m_size_E = 0;
    }

    /**
     * Construct a Digraph with initialCapacity = size and loadFactor = 0.75
     * 
     * @param size sets the Size of initialCapacity
     * @throws IllegalArgumentException - if the initial capacity is negative
     */
    public DigraphOld(int size) {
        m_vList = new HashMap<>(size);
        m_size = 0;
        m_size_E = 0;
    }

    /**
     * Construct a Digraph with initialCapacity = size and loadFactor = loadFactor
     * 
     * @param size sets the Size of initialCapacity
     * @param loadFactor sets loadFactor of HashMap
     * @throws IllegalArgumentException - if the initial capacity is negative or the load factor is
     *             nonpositive.
     */
    public DigraphOld(int size, float loadFactor) {
        m_vList = new HashMap<>(size, loadFactor);
        m_size = 0;
        m_size_E = 0;
    }

    /**
     * Adds a Vertex into the Graph. Time complexity: O(1) Duplicated keys are discarded
     * 
     * @param key of the vertex.
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
     * Get the Vertex from the Digraph. Time complexity: O(1)
     * 
     * @param key
     * @return specific Vertex from Digraph or null
     */
    private Vertex getVertex(V key) {
        return m_vList.get(key);
        //Test
    }

    /**
     * Get a Edge from specific vertices In time complexity of DoubleLinkedList Implementation
     * 
     * @param u is original vertex
     * @param v is destination vertex
     * @return edge or null
     */
    private Edge getEdge(V u, V v) {
        Vertex ori = m_vList.get(u);
        Vertex dest = m_vList.get(v);
        for (Edge e : ori.adjacencyList) {
            if (e.destination.equals(dest)) {
                return e;
            }
        }
        return null;
    }

    /**
     * remove vertex from a Digraph TimeCoplexity: O(?)
     * 
     * @param key of vertex to remove
     */
    // TODO: Implement correctly: check adjacency list u.s.w
    public boolean removeVertex(V key) {
        if (!m_vList.containsKey(key)) {
            m_vList.remove(key);
            m_size--;
            return true;
        }
        return false;
    }

    public boolean removeEdge(int weight, V origin, V destination) {
        // TODO: adjust outdeg
        Edge e = new Edge(weight, m_vList.get(origin), m_vList.get(destination));
        return m_vList.get(origin).adjacencyList.remove(e);
    }

    /**
     * Adds a Edge into the Graph. Timecomplexity: O(outdeg(origin))
     * 
     * @param weight give an appropriate weight or 0 for default.
     * @param origin from which Vertex the Edge should come
     * @param destination to which Vertex the Edge should go
     * @return true if added, false if vertices are not in the Graph or edge is already in the list
     */
    public boolean addEdge(int weight, V origin, V destination) {
        if (m_vList.containsKey(origin) && m_vList.containsKey(destination)) {
            Edge e = new Edge(weight, m_vList.get(origin), m_vList.get(destination));
            if (!m_vList.get(origin).adjacencyList.contains(e)) {
                m_vList.get(origin).adjacencyList.add(e);
                m_size_E++;
                m_vList.get(destination).indeg++;
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a Vertex with the given Key already exists in the Graph. Timecomplexity: O(1)
     * 
     * @param key
     */
    public boolean containsVertex(V key) {
        return m_vList.containsKey(key);
    }

    /**
     * Checks if two Vertex are connected by an Edge (directed) Timecomplexity: O(outdeg(origin))
     * 
     * @param V v1
     * @param V v2
     * @return true if origin has an edge to destination. False otherwise.
     */
    public boolean areNeighbors(V origin, V destination) {
        return m_vList.get(origin).adjacencyList.contains(m_vList.get(destination));
    }

    /**
     * Returns a List of Vertex that are adjacent to a Vertex that is stored at the given key.
     * 
     * O(outdeg(key))
     * 
     * @param index index of the Vertex in the Graph
     * @return List<V> of adjacent Verts or NULL if index > size of the array. Returned list may be
     *         empty if the given Vertex has no neighbors
     */
    public List<V> getNeighbors(V key) {

        Vertex v = m_vList.get(key);
        ArrayList<V> verts = new ArrayList<>(v.adjacencyList.size());
        for (Edge edge : v.adjacencyList) {
			verts.add(edge.destination.m_key);
		}
        return verts;
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
     */
    private class Edge {
        private int m_weight;
        // TODO: Delete if not needed!
        private Vertex origin;
        private Vertex destination;

        public int getWeight() {
            return m_weight;
        }

        /**
         * Only works for positive weights.
         * 
         * @param weight
         */
        public void setWeight(int weight) {
            m_weight = weight;
        }

        /**
         * Constructor for Edges. Destination and Origin have to be in the same graph
         * 
         * @param weight defines the "cost" of an Edge. Use 0 for unweighed Edges. Only works for
         *            positive weights
         * @param origin * Vertex from which the Edge comes. Can be same as destination.
         * @param destination Vertex to which the Edge goes. Can be same as origin.
         */
        public Edge(int weight, Vertex origin, Vertex destination) {
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
                return (origin.equals(((Edge) o).origin) && destination.equals(((Edge) o).destination) && m_weight == ((Edge) o).m_weight);
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

        /**
         * Number of edges landing on this Vertex. For outdeg use adjacencylist.lenght()
         */
        private int indeg;

        private Vertex(V key) {
            m_key = key;
            adjacencyList = new DLinkedList<>();
            indeg = 0;
        }

        /**
         * Only checks if keys are equal.
         * 
         * @param o
         * @return true if key of given Vertex is equal. False otherwise.
         */
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            return m_key.equals(((Vertex) o).m_key);
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
