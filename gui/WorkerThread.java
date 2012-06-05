package gui;

import a3.CVRP;
import a3.IGraph;

/**
 * Arbeiterthread, der den ACO Algorithmus benutzt und den errechneten Pfad,
 * dem DrawPanel �bergibt.
 */
public class WorkerThread extends Thread {
	private final DrawPanel panel;
	private final CVRP algo;
	private final IGraph graph;
	private final int capacity;
	
	/**
	 * Konstruktor der Arbeiterthreads.
	 * @param panel 	Drawpanel, dem der errechnete Pfads �bergeben werden soll
	 * @param algo		ACO Algorithmus
	 * @param graph		Graph auf dem der Pfad gesucht werden soll
	 * @param capacity 
	 */
	public WorkerThread(DrawPanel panel, CVRP algo, IGraph graph, int capacity) {
		this.panel = panel;
		this.algo = algo;
		this.graph = graph;
		this.capacity = capacity;
	}
	
	/**
	 * Run-Methode, die den Pfad errechnet und �bergibt.
	 */
	@Override
	public void run() {
		panel.setPath(algo.shortestPath(1, capacity, 500));
	}
}
