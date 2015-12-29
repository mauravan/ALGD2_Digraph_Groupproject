package digraph;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Matthias
 *
 * @param <V>
 */
public class Vertex<V> {
	/**
	 * Unique Identifier
	 */
	private V m_key;
	private Digraph_vList parent;
	/**
	 * Index of vList in with the adjacency list is stored
	 */
	private List<Edge> adjacencyList;
	
	
	public V getM_key() {
		return m_key;
	}

	public void setM_key(V m_key) {
		this.m_key = m_key;
	}

	public Digraph_vList getParent() {
		return parent;
	}

	public void setParent(Digraph_vList parent) {
		this.parent = parent;
	}

	public List<Edge> getAdjacencyList() {
		return adjacencyList;
	}

	public void setAdjacencyList(List<Edge> adjacencyList) {
		this.adjacencyList = adjacencyList;
	}

	

	private Vertex(V key, Digraph_vList parent) {
		m_key = key;
		adjacencyList = new LinkedList<>();
		this.parent = parent;
	}

	/**
	 * Only checks if keys are equal.
	 * 
	 * @param o
	 * @return true if key of given Vertex is equal. False otherwise.
	 */
	public boolean equals(Object o) {
		try {
			return m_key.equals(((Vertex<V>) o).m_key) && parent.equals(((Vertex<V>) o).parent);
		} catch (Exception e) {
			return false;
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[" + m_key.toString() + "]");
		for (Edge edge : adjacencyList) {
			sb.append(" --> " + "[" + edge.getDestination().m_key.toString() + "]");
		}
		return sb.toString();
	}
}
