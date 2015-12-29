package digraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public interface IDigraph<V, E> {
	

	/**
	 * Creates a new Edge into the Graph.
	 * @param weigth ONLY POSITIVE ALLOWED
	 * @param origin
	 * @param destination
	 * @return
	 */
	E addEdge(double weigth, V origin, V destination);
	
	/**
	 * Adds an Edge into the Graph.
	 * @param edge
	 * @param origin
	 * @param destination
	 * @return true if Edge was added.
	 */
	boolean addEdge(E edge, V origin, V destination);
	
	/**
	 * Adds a Vertex into the Graph
	 * @param vertex
	 * @return true if Vert was added.
	 */
	boolean addVertex(V vertex);
	
	/**
	 * 
	 * @return Shallow Copy of the Graph
	 */
	Object clone();
	
	/**
	 * 
	 * @param edge
	 * @return true if the graph contains the given Edge
	 */
	boolean containsEdge(E edge);
	
	/**
	 * 
	 * @param origin
	 * @param destination
	 * @return true if the graph contains an Edge from origin to destination
	 */
	boolean containsEdge(V origin, V destination);
	
	/**
	 * 
	 * @param vertex
	 * @return true if vertex is contained
	 */
	boolean containsVertex(V vertex);
	
	/**
	 * 
	 * @param vertex
	 * @return indegree of given vertex.
	 */
	int indegOf(V vertex);
	
	/**
	 * 
	 * @param vertex
	 * @return outdegree of given vertex
	 */
	int outdegOf(V vertex);
	
	/**
	 * 
	 * @return a set of the verts in the Graph
	 */
	Set<V> getVertsOfGraph();
	
	/**
	 * 
	 * @param vertex
	 * @return a set of the edges contained in the Graph
	 */
	Set<E> getEdgesOfGraph();
	
	/**
	 * 
	 * @param vertex
	 * @return a set of all edges leaving and coming into the given vertex
	 */
	Set<E> getAllEdgesOfVertex(V vertex);
	
	/**
	 * @param vertex
	 * @return returns a set of all edges leaving this Vertex
	 */
	Set<E> getLeavingEdgesOfVertex(V vertex);
	
	/**
	 * 
	 * @param vertex
	 * @return a Set of edges ending on the given Vertex
	 */
	Set<E> getIncomingEdgesOfVertex(V vertex);
	
	/**
	 * 
	 * @param source
	 * @param destination
	 * @return a set of all edges connecting source and destination
	 */
	Set<E> getEdgesBetween(V source, V destination);
	
	/**
	 * 
	 * @param edge
	 * @return returns the source vertex of the edge given.
	 */
	V getEdgeSource(E edge);
	
	/**
	 * 
	 * @param edge
	 * @return the destination vertex of the edge given.
	 */
	V getEdgeDestination(E edge);
	
	/**
	 * 
	 * @param edge
	 * @return the weight of a given edge 
	 */
	double getEdgeWeight(E edge);
	
	/**
	 * removes the given Edge from the Graph
	 * @param edge
	 * @return true if it was removed, false if not present or unable to remove
	 */
	boolean removeEdge(E edge);
	
	/**
	 * 
	 * @param weight ONLY POSITIVE ALLOWED
	 * @param origin
	 * @param destination
	 * @return the Edge that was removed if it was present.
	 */
	E removeEdge(double weight, V origin, V destination);
	
	/**
	 * Removes the Vertex and ALL touching Edges
	 * @param v
	 * @return true if the Vertex was removed.
	 */
	boolean removeVertex(V v);
	
	/**
	 * 
	 * @param edge
	 * @param weight ONLY POSITIVE ALLOWED
	 */
	void setEdgeWeight(E edge, double weight);
	
	/**
	 * 
	 * @param vertex
	 * @return True if vertex is in the Graph
	 * @throws AssertionError
	 */
	boolean assertVertexExist(V vertex);
	
	//TODO: Do we need this?
	//boolean equals(Object obj);
	
	/**
	 * Removes all edges that are in the given Colletion
	 * @param edges
	 * @return true if all edges were deleted.
	 */
	boolean removeAllEdges(Collection<? extends E> edges);

	/**
	 * Removes all edges that are in the given Array
	 * @param edges
	 * @return
	 */
	boolean removeAllEdges(E[] edges);
	
	/**
	 * Removes ALL the edges in the Graph
	 * @return true if all were removed
	 */
	boolean removeAllEdges();
	
	/**
	 * Removes all Elements in the Graph (The same as removeAllVertices)
	 * @return
	 */
	boolean clear();
	
	/**
	 * Removes all the edges going from the specified source vertex to the specified target vertex, and returns a set of all removed edges.
	 * @param origin
	 * @param destination
	 * @return
	 */
	Set<E> removeAllEdgesBetweenVertex(V origin, V destination);
	
	/**
	 * Removes all vertices that are in the given Colletion
	 * @param vertices
	 * @return true if all vertices were deleted.
	 */
	boolean removeAllVertices(Collection<? extends V> vertices);
	
	String toString();
	/**
	 * 
	 * @return number of Vertices in the Graph
	 */
	int getNumberOfVerts();
	
	/**
	 * 
	 * @return number of Vertices in the Graph
	 */
	int getNumberOfEdges();
	

	//TODO: Discuss if Euler and Hamilton Path are wanted..
	
	/**
	 * 
	 * @param vertex
	 * @param destination
	 * @return a set of Vertices representing the shortest Path from origin to destination
	 */
	Set<V> dijkstra(V origin, V destination); //TODO: Discuss in team
	
//	/**
//	 * 
//	 * @param origin
//	 * @return an ArrayList of Sets of Vertices representing the shortest Path from origin to all other Vertices
//	 */
//	ArrayList<Set<V>> dijkstra(V origin); //TODO: Discuss in team
}
