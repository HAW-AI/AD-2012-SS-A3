package a3;

import java.util.List;
import java.util.Set;

/**
 * Interface einer Ameise für den Ant Colony Optimization Algorithmus.
 */
public interface IAnt
{

    /**
     * Gibt den derzeitigen Weg der Ameise zurück.
     *
     * @return Liste aus Indizes der Ecken als Weg
     */
    List<Integer> getPath();

    /**
     * Gibt die Anzahl der Pakete zurück, die die Ameise gerade trägt.
     *
     * @return Anzahl der Pakete, die die Ameise gerade trägt
     */
    int getLoad();

    /**
     * Zieht Pakete von der Ladung der Ameise ab.
     *
     * @param decreaseValue	Kapazität einer Ecke, die beliefert werden soll
     */
    void decreaseLoad(int decreaseValue);

    /**
     * Befüllt die Ameise auf ihre maximale Kapazität
     */
    void refill();

    public boolean isGoingHome();
    
    public void setGoingHome();

    public boolean isGoingOut();

    public void setGoingOut();

    /**
     * Fügt der Ameise einen neuen besuchten Knunden hinzu.
     *
     * @param customer   Kunde, der besucht wurde
     * @return Liste der zu besuchenden Kunden
     */
    public void addVisitedCustomer(int customer);

    /**
     * Gibt die noch zu besuchenden Kunden zurück
     *
     * @param totalCustomers
     * @return Liste der zu besuchenden Kunden
     */
    public Set<Integer> getRemainingCustomers();

    /**
     * gibt and, ob eine Ameise sich gerade bei einem Kunden befindet.
     * 
     * @return true wenn ja, sonst false
     */
    boolean isAtCustomer();
    
    /**
     * Bewegt die Ameise zu der gegebenen Ecke.
     *
     * @param vertex Index einer Ecke
     */
    void moveTo(int vertex);

    /**
     * Liefert den Index der aktuellen Ecke zurück.
     *
     * @return Index der Ecke, auf der sich die Ameise derzeitig befindet.
     */
    int currentPosition();

    /**
     * Setzt die Ameise auf die Startecke zurück.
     */
    void reset();
}