package gui;

import graph.IGraph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import a3.ACO;

public class DrawPanel extends JPanel implements Runnable  {
	private static final long serialVersionUID = 1L;
    private final static int STEPS = 500;
    private final static int ANTS = 1500;
	
	private final ACO algo;
	private final IGraph graph;
	private final ISOMGraphLayout layout;
	
	final Color borderColor = Color.black;
	final Color normalColor = new Color(250, 220, 100);
	final Color pathColor = Color.red;
	//final Color selectColor = Color.pink;
	
	List<Integer> path = new ArrayList<Integer>();
	
	public DrawPanel(IGraph graph) {
		this.graph = graph;
		this.algo = new ACO(ANTS, STEPS);
		this.layout = new ISOMGraphLayout(graph, STEPS, 800, 600, 50);
		
		setPreferredSize(new Dimension(800, 600));

		new Thread(new WorkerThread(this, algo, graph)).run();
	}
	
	public void paintNodes() {		
		Graphics g = getGraphics();
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		for (Node n : layout.getNextLayout()) {
			paintNode(g, n, path);
			paintEdges(g, n, path);
		}
	}
	
	private void paintNode(Graphics g, Node n, List<Integer> path) {
		FontMetrics fm = getFontMetrics(getFont());
		int x = (int) n.x;
		int y = (int) n.y;
		g.setColor(n.nr == 0 ? pathColor : normalColor);
		int w = fm.stringWidth(n.lbl) + 10;
		int h = fm.getHeight() + 4;
		
		g.fillRect(x - w / 2, y - h / 2, w, h);
		g.setColor(borderColor);
		g.drawRect(x - w / 2, y - h / 2, w - 1, h - 1);
		g.drawString(n.lbl, x - (w - 10) / 2, (y - (h - 4) / 2) + fm.getAscent());
	}
	
    private void paintEdges(Graphics g, Node n, List<Integer> path) {
        int x = (int) n.x;
        int y = (int) n.y;
        
        for (Node child : n.succ) {
        	int i = path.get(n.nr);
        	g.setColor((path.get(child.nr) == i - 1 || path.get(child.nr) == i + 1 || 
        			(child.nr == 0 && path.get(n.nr) == path.size() - 2)) ? pathColor : borderColor);
        	g.drawLine(x, y, (int)child.x, (int)child.y);
        }
    }

    synchronized void setPath(List<Integer> path) {
    	this.path = path;
    	System.out.println("Errechneter Pfad: "+path+" Len: "+algo.getPathLength(path, graph));
    }
    
	@Override
	public void run() {
		while (true) {
			try {
				if (layout.isDone()) {
					Thread.sleep(200);
				}
				else {
					Thread.sleep(10);
				}
			}
			catch (InterruptedException e) {
			}
			
			paintNodes();
		}
	}
}
