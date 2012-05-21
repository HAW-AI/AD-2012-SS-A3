package graph;

import java.util.Set;

/**
 * Interface f�r Graphen.
 */
public interface IGraph {
	/**
	 * Wert f�r eine nicht existente Kante zwischen zwei Ecken im Graphen.
	 */
	public static final int NON_EXISTING_EDGE = -1;
	
	/**
	 * Gibt die Distanz zwischen zwei Ecken im Graphen zur�ck gibt eine 0 zur�ck, falls eine ung�ltige Anfrage gestellt wurde.
	 * @preCondition vert1, vert2 > 0
	 * @preCondition vert1, vert2 < Anzahl Knoten
	 * @param vert1		Index der Start-Ecke
	 * @param vert2		Index der Ziel-Ecke
	 * @return Distanz zwischen den zwei Ecken
	 */
	int edgeWeight(int vert1, int vert2);

	/**
	 * Gibt die Anzahl an Ecken im Graphen zur�ck.
	 * @return Anzahl der Ecken im Graphen
	 */
	int getNumberOfVertices();

//	Set<Integer> getneighbours(int Vertex);
	/**
	 * Gibt die Menge aus Indezes von Ecken zur�ck, die von der gegebenen Ecke erreichbar sind.
	 * @return Menge aus Indezes der erreichbaren Ecken
	 */
	Set<Integer> reachableAdjacencyVerticesOf(int vertice);
	
	// F�r weiter Optimierung des Alg, ev n�tzlich
//	int inDegreeOf(int vertice);
//	int outDegreeOf(int vertice);
}
