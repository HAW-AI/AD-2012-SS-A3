package gui;


import gui.ISOMGraphLayout;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

import a3.CVRP;
import a3.IGraph;

/**
 * Panel zur Anzeige des Graphen und dem errechneten k�rzesten Pfades.
 */
public class DrawPanel extends JPanel implements Runnable, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
    private static final int ANTS = 1500;
    private static final int STEPS = 500;
    private static final int HEIGHT = 811;
    private static final int WIDTH = 1360;
    
	private final CVRP algo;
	private final IGraph graph;
	private final ISOMGraphLayout layout;
	private final Thread thread;
	
	private final Color borderColor = Color.black;
	private final Color normalColor = new Color(250, 220, 100);
	private final Color pathColor = Color.red;
	private final Color pickColor = Color.orange;
	
	private List<Integer> path = null;
	private List<Node> nodes;
	private boolean showAllEdges = true;
	private Node pick = null;
	
	/**
	 * Konstruktor mit �bergabe des anzuzeigenden Graphen.
	 * @param graph	anzuzeigender Graph
	 */
	public DrawPanel(IGraph graph) {
		this.graph = graph;
		this.algo = new CVRP(graph, STEPS);
		this.layout = new ISOMGraphLayout(graph, 50 * graph.getNumberOfVertices(), WIDTH, HEIGHT, 50);
		
		// Algorithmenthread
		new Thread(new WorkerThread(this, algo, graph)).start();
		// Zeichenthread
		thread = new Thread(this);
		thread.start();
		// Mouse Listener
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	/**
	 * Paint-Methode von JComponent, die das Panel und damit den Graphen + Pfad zeichnet.
	 * @param g 	Graphics-Object auf dem gezeichnet werden soll
	 */
	@Override
	public void paint(Graphics g) {
		if (g == null) return;
		FontMetrics fm = getFontMetrics(getFont());
		
		// Hintergrund
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		nodes = layout.getNextLayout();
		
		// Kanten malen
		if (showAllEdges) {
			List<Node> done = new ArrayList<Node>();
			for (Node n : nodes) {
				for (Node m : nodes) {
					if (n == m) continue;
					if (done.contains(m)) continue;
					if (graph.edgeWeight(n.nr, m.nr) == IGraph.NON_EXISTING_EDGE) continue;
					paintEdge(g, n, m, borderColor);
				}
				done.add(n);
			}
		}
		
		// Weg malen
		if (path != null) {
			for (int i = 0; i < path.size() - 1; i++) {
				Node n = findNode(nodes, path.get(i));
				Node m = findNode(nodes, path.get(i+1));
				
				if (n != null && m != null) paintEdge(g, n, m, pathColor);
			}
			
		}
		
		// String in die Ecke schreiben
		g.setColor(borderColor);
		if (path == null) {
			g.drawString("Path: Not found yet!", 0, HEIGHT - fm.getHeight() * 2);
			g.drawString("Len : -1", 0, HEIGHT - fm.getHeight());
		}
		else {
			g.drawString("Path: "+path, 0, HEIGHT - fm.getHeight() * 2);
			g.drawString("Len : "+graph.getPathLength(path), 0, HEIGHT - fm.getHeight());
		}
			
		// Ecken malen
		for (Node n : nodes) {
			paintNode(g, n, path, fm);
		}
	}
	
	/**
	 * Aktiviert/deaktiviert die Anzeige der Kanten, die nicht zum k�rzesten Pfad geh�ren.
	 */
	public void toggleEdgeDisplay() {
		showAllEdges = !showAllEdges;
		repaint();
	}
	
	/**
	 * Findet das Node in der Liste mit der gegebenen Nummer.
	 * @param nodes	Liste der Nodes
	 * @param nr	gesuchte Nummer
	 * @return	Node mit der Nummer, sonst null
	 */
	private Node findNode(List<Node> nodes, int nr) {
		for (Node n : nodes) {
			if (n.nr == nr) return n;
		}
		return null;
	}
	
	/**
	 * Zeichnet ein Node an seiner Position.
	 * @param g		Graphics-Object auf dem gezeichnet werden soll
	 * @param n		das zu zeichnende Node
	 * @param path	Liste des Pfades
	 * @param fm	Metriken des benutzen Fonts
	 */
	private void paintNode(Graphics g, Node n, List<Integer> path, FontMetrics fm) {
		int x = (int) n.x;
		int y = (int) n.y;
		g.setColor(pick == n ? pickColor : path != null && path.size() > 0 && n.nr == path.get(0) ? pathColor : normalColor);
		int w = fm.stringWidth(n.lbl) + 10;
		int h = fm.getHeight() + 4;
		
		g.fillRect(x - w / 2, y - h / 2, w, h);
		g.setColor(borderColor);
		g.drawRect(x - w / 2, y - h / 2, w - 1, h - 1);
		g.drawString(n.lbl, x - (w - 10) / 2, (y - (h - 4) / 2) + fm.getAscent());
	}
	
	/**
	 * Zeichnet eine Kante von den gegebenen Nodes mit der Farbe.
	 * @param g		Graphics-Object auf dem gezeichnet werden soll
	 * @param start	Startnode
	 * @param end	Endnode
	 * @param color	Farbe der Kante
	 */
    private void paintEdge(Graphics g, Node start, Node end, Color color) {        
        g.setColor(color);
    	g.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
    }

    /**
     * Setzt den k�rzsten Pfad (durch den Arbeiterthread)
     * @param path	k�rzester Pfad
     */
    synchronized void setPath(List<Integer> path) {
    	this.path = path;
    	System.out.println("Errechneter Pfad: "+path+" Len: "+graph.getPathLength(path));
    }
    
    /**
     * Run-Methode, die die Anzeige aktualisiert, w�hrend der Graph sich ordnet.
     */
	@Override
	public void run() {		
		while (!layout.isDone()) {
			try {
				Thread.sleep(10);
				if (getGraphics() != null) paint(getGraphics());
			}
			catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {	
		if (e.getButton() != MouseEvent.BUTTON1) return;
		
		FontMetrics fm = getFontMetrics(getFont());
		int x = e.getX();
		int y = e.getY();
		
		for (Node n : nodes) {
			int w = fm.stringWidth(n.lbl) + 10;
			int h = fm.getHeight() + 4;
			
			if (x >= n.x - w/2 && x <= n.x + w/2 && y >= n.y - h/2 && y <= n.y + h/2) {
				pick = n;
				pick.x = x;
				pick.y = y;
				break;
			}
		}
		
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (pick == null) return;
		
		pick.x = e.getX();
		pick.y = e.getY();
		repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		pick = null;
		repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
