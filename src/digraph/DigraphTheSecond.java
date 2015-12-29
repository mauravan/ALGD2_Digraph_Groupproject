package digraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



/**
 * A directed, weighted (only positive) Graph which allows self-loops and multiedges using a HashSet to store all the Vertices.
 * To allow weighted Edges and nested Adjacency Lists we used the inner classes Vertex<V> and Edge<E>. 
 * 
 * @author Roman Meier, Alex Melliger, Matthias Keller, Stefan Mettler
 *
 * @param <V> The Type of the Vertices
 * @param <E> The Type of the Edges
 * 
 */
public class DigraphTheSecond<V, E> implements IDigraph<V, E> {

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
    public DigraphTheSecond() {
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
    public DigraphTheSecond(int size) {
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
    public DigraphTheSecond(int size, float loadFactor) {
        m_vList = new HashMap<>(size, loadFactor);
        m_size = 0;
        m_size_E = 0;
    }
	
	
	@Override
	public E addEdge(double weigth, V origin, V destination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addEdge(E edge, V origin, V destination) {
		// TODO Auto-generated method stub
		return false;
	}
	

	public Object clone() {
		return null;
	}

	@Override
	public boolean addVertex(V vertex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsEdge(E edge) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsEdge(V origin, V destination) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsVertex(V vertex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int indegOf(V vertex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int outdegOf(V vertex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Set<V> getVertsOfGraph() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<E> getEdgesOfGraph() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<E> getAllEdgesOfVertex(V vertex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<E> getLeavingEdgesOfVertex(V vertex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<E> getIncomingEdgesOfVertex(V vertex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<E> getEdgesBetween(V source, V destination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V getEdgeSource(E edge) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V getEdgeDestination(E edge) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getEdgeWeight(E edge) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean removeEdge(E edge) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E removeEdge(double weight, V origin, V destination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeVertex(V v) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setEdgeWeight(E e, double weight) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean assertVertexExist(V vertex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAllEdges(Collection<? extends E> edges) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAllEdges(E[] edges) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAllEdges() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean clear() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<E> removeAllEdgesBetweenVertex(V origin, V destination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAllVertices(Collection<? extends V> vertices) {
		// TODO Auto-generated method stub
		return false;
	}

	public Set<V> dijkstra(V origin, V destination) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public ArrayList<Set<V>> dijkstra(V origin) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	@Override
	public int getNumberOfVerts() {
		// TODO Auto-generated method stub
		return m_size;
	}

	@Override
	public int getNumberOfEdges() {
		// TODO Auto-generated method stub
		return m_size_E;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////INNER CLASSES////////////////////////////////////////////////////////////////////
	/**
     * Representing the weighted Edges in a Graph.
     * 
     * @author Roman Meier, Alex Melliger, Matthias Keller, Stefan Mettler
     * @param <E> Type of the Edge
     * 
     */
    private class Edge<E> {
        private double m_weight;
        // TODO: Delete if not needed!
        private Vertex origin;
        private Vertex destination;

        public double getWeight() {
            return m_weight;
        }

        /**
         * Only works for positive weights.
         * 
         * @param weight
         */
        public void setWeight(double weight) {
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
        //TODO: Provide a static field DEFAULT_WEIGHT and a Constructor using only Vertex Origin or Vertex Destination
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
     * Representing the Vertices in the Graph. Stores all his Neighbors in a adjacencyList using a HashSet.
     * 
     * @author Roman Meier, Alex Melliger, Matthias Keller, Stefan Mettler
     *  
     * @param <V> Type of the Vertex
     */
    private class Vertex<V> {
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
