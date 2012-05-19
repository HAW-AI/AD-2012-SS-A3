package graph;

import java.util.Set;

public interface IGraph {
	/**
	 * Gibt die Distanz zwischen zwei Knoten im Graphen zur�ck <br>
	 * gibt eine 0 zur�ck, falls eine ung�ltige Anfrage gestellt wurde
	 * 
	 * @preConditions vert1, vert2 >= 0 & vert1, vert2 < Anzahl Knoten
	 */
	int edgeWeight(int vert1, int vert2);

	int getNumberOfVertices();

	Set<Integer> getneighbours(int Vertex);
}
