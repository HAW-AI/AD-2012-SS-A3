package gui;

import graph.IGraph;
import a3.ACO;

public class WorkerThread extends Thread {
	private final DrawPanel panel;
	private final ACO algo;
	private final IGraph graph;
	
	public WorkerThread(DrawPanel panel, ACO algo, IGraph graph) {
		this.panel = panel;
		this.algo = algo;
		this.graph = graph;
	}
	
	@Override
	public void run() {
		panel.setPath(algo.shortestPath(graph, 0));
	}
}
