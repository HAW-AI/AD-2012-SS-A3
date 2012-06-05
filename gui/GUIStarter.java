package gui;


import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import a3.CVRP;
import a3.GraphFactory;
import a3.GraphImpl;
import a3.IGraph;
import a3.IOManager;

/**
 * Klasse zum Starten der GUI. Derzeitig noch keine interaktive Auswahl von Graphen.
 */
public class GUIStarter {
	private static final int ANTS = 2000;
	private static final int STEPS = 5000;

	public static void main_old(String[] args) {
					// GraphFactory.createGraph(new IOManager("src/simGraph(10).txt").readGraphMatrixFromSimFile());
		IGraph graph = GraphFactory.createDiGraph(IOManager.RandomDistanceMatrix(1338, 200, 50, 0.5));
		JFrame frame = new MainFrame(graph, "Graph Viewer", 5555);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
        IOManager iOManager = new IOManager("src/a3/file7.txt");
        try {
			IGraph graphT = new GraphImpl(iOManager.getEdgeWeightSection(), iOManager.getDemandSection());
			CVRP test1 = new CVRP(graphT, 0);
	        System.out.println("Graph T, Kapazität " + iOManager.getAntCapacity());
//			List<List<Integer>> path = test1.shortestPath(ANTS, iOManager.getAntCapacity(), STEPS);
//	    	int gesLaenge = 0;
//	    	for(List<Integer> list : path){
//				gesLaenge += graphT.getPathLength(list);
//	    	}
//	    	System.out.println(gesLaenge + " " + path);

			JFrame frame = new MainFrame(graphT, "Graph Viewer", iOManager.getAntCapacity());
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
        } catch (Exception e) {
			e.printStackTrace();
		}
	}
}
