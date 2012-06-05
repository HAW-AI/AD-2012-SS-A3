package a3;

import java.util.List;
import java.util.Set;

/**
 * Interface fuer Graphen.
 */
public interface IGraph
{

    /**
     * Wert fuer eine nicht existente Kante zwischen zwei Ecken im Graphen.
     */
    public static final int NON_EXISTING_EDGE = -1;

    /**
     * Gibt den Paketbedarf eines Kundens zurück.
     *
     * @param vertex Index des Kunden
     * @return Bedarf des Knotens
     */
    int getDemandOfCustomer(int vertex);

    /**
     * Gibt die Distanz zwischen zwei Ecken im Graphen zurueck gibt eine 0
     * zurueck, falls eine ungueltige Anfrage gestellt wurde. @preCondition
     * vert1, vert2 > 0 @preCondition vert1, vert2 < Anzahl Knoten
     *
     * @param vert1	Index der Start-Ecke
     * @param vert2	Index der Ziel-Ecke
     * @return Distanz zwischen den zwei Ecken
     */
    int edgeWeight(int vert1, int vert2);

    /**
     * Gibt die Anzahl an Ecken im Graphen zurueck.
     *
     * @return Anzahl der Ecken im Graphen
     */
    int getNumberOfVertices();

    /**
     * Gibt die Liste der Knoten mit Bedarf zurück
     *
     * @return liste der Knoten
     */
    Set<Integer> getCustomers();

    /**
     * Gibt die Liste aller Knoten zurück
     *
     * @return liste der Knoten
     */
    Set<Integer> getVertices();

    /**
     * Berechnet die Summe der Distanzen des gegebenen Weges.
     *
     * @param path	Liste aus Indizes von Ecken, die den Weg bilden
     * @return Summe der Kantendistanzen des Weges
     */
    public int getPathLength(List<Integer> path);

    /**
     * Gibt die Menge aus Indezes von Ecken zurueck, die von der gegebenen Ecke
     * erreichbar sind.
     *
     * @return Menge aus Indezes der erreichbaren Ecken
     */
    Set<Integer> getNeighboursOf(int vertice);
}
