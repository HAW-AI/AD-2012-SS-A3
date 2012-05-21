package gui;

import java.util.HashSet;
import java.util.Set;

/**
 * Ecke eines Graphen zur Berechnung der Darstellung mittels des ISOM Algorithmus.
 */
class Node {
    double x;
    double y;   
    double oldX;
    double oldY;

    boolean fixed;
    int nr;
    String lbl;
    Set<Node> succ;
    boolean visited;
    int distance;
    
    /**
     * Konstruktor mit Übergabe der Eckennummer.
     * @param nr 	Eckennummer
     */
    public Node(int nr) {
    	this.nr = nr;
    	this.lbl = "N"+nr;
    	this.fixed = false;
    	this.succ = new HashSet<Node>();
    	this.visited = false;
    	this.distance = 0;
    }
    
    /**
     * Verschiebt die Ecke um die angegeben X und Y-Werte.
     * @param dx	X-Verschiebung
     * @param dy	Y-Verschiebung
     */
    void update(double dx, double dy) {
       oldX = x;
       oldY = y;
       x += dx;
       y += dy;
    }    
}
