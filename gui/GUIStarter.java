package gui;


import javax.swing.JFrame;
import javax.swing.WindowConstants;

import a3.GraphFactory;
import a3.IGraph;
import a3.IOManager;

/**
 * Klasse zum Starten der GUI. Derzeitig noch keine interaktive Auswahl von Graphen.
 */
public class GUIStarter {
	public static void main(String[] args) {
					// GraphFactory.createGraph(new IOManager("src/simGraph(10).txt").readGraphMatrixFromSimFile());
		IGraph graph = GraphFactory.createDiGraph(IOManager.RandomDistanceMatrix(1338, 200, 50, 0.5));
		JFrame frame = new MainFrame(graph, "Graph Viewer", 5555);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
