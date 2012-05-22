package gui;

import graph.GraphFactory;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import a3.IOManager;

public class GUIStarter {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		JPanel panel = new DrawPanel(GraphFactory.createGraph(new IOManager("src/simGraph(10).txt").readGraphMatrixFromSimFile()));
		frame.getContentPane().add(panel);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
		new Thread((Runnable) panel).run();
	}
}
