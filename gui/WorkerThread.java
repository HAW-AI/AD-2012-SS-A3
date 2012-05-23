package gui;

import graph.IGraph;
import a3.ACO;

/**
 * Arbeiterthread, der den ACO Algorithmus benutzt und den errechneten Pfad,
 * dem DrawPanel übergibt.
 */
public class WorkerThread extends Thread {
	private final DrawPanel panel;
	private final ACO algo;
	private final IGraph graph;
	
	/**
	 * Konstruktor der Arbeiterthreads.
	 * @param panel 	Drawpanel, dem der errechnete Pfads übergeben werden soll
	 * @param algo		ACO Algorithmus
	 * @param graph		Graph auf dem der Pfad gesucht werden soll
	 */
	public WorkerThread(DrawPanel panel, ACO algo, IGraph graph) {
		this.panel = panel;
		this.algo = algo;
		this.graph = graph;
	}
	
	/**
	 * Run-Methode, die den Pfad errechnet und übergibt.
	 */
	@Override
	public void run() {
		panel.setPath(algo.shortestPath(graph, 0));
	}
}
