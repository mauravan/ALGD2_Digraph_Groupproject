package digraph;

/**
 * We do
 * 
 * @author Matthias
 *
 * @param <E>
 *            Wrapper Type of weight
 *
 */
public class Edge<E> {
	private E m_weight;
	private Vertex origin;
	private Vertex destination;
	
	public E getM_weight() {
		return m_weight;
	}

	public void setM_weight(E m_weight) {
		this.m_weight = m_weight;
	}

	public Vertex getOrigin() {
		return origin;
	}

	public Vertex getDestination() {
		return destination;
	}



	/**
	 * Constructor for Edges.
	 * Destination and Origin have to be in the same graph
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
		assert(origin.getParent() == destination.getParent());
		this.origin = origin;
		this.destination = destination;
		m_weight = weight;
	}

	/**
	 * Copy-Constructor for Edges.
	 * 
	 * @param weight
	 *            defines the "cost" of an Edge. Use 0 for unweighed Edges.
	 * @param origin
	 *            Vertex from which the Edge comes. Can be same as destination.
	 * @param destination
	 *            Vertex to which the Edge goes. Can be same as origin.
	 */
	public Edge(Edge<E> e) {
		m_weight = e.m_weight;
		this.origin = e.origin;
		this.destination = e.destination;
	}

	/**
	 * Checks if origin and destination are equal
	 * 
	 * @param o
	 * @return true if they are. false otherwise.
	 */
	public boolean equals(Object o) {
		try {
			return (origin.equals(((Edge<E>) o).origin) && destination.equals(((Edge<E>) o).destination)
					&& m_weight.equals(((Edge<E>) o).m_weight));
		} catch (Exception e) {
			return false;
		}
	}

}
