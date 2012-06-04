package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import a3.IGraph;

/**
 * ISOM Graph Layouter zur Darstellung von Graphen.
 * Ein Algorithmus von Bernd Meyer.
 * Quellen:
 * - Self-Organizing Graphs [URL: http://www.csse.monash.edu.au/~berndm/Papers/GD98.pdf.gz]
 * - ISOM Applet [URL: http://www.csse.monash.edu.au/~berndm/ISOM/source/]
 * - Adobe Flex Implementierung [URL: http://birdeye.googlecode.com/svn/trunk/ravis/libRaVis/org/un/cava/birdeye/ravis/graphLayout/layout/ISOMLayouter.as]
 */
public class ISOMGraphLayout {
	/**
	 * Enum f�r die Art der Verteilung der Nodes.
	 */
	public enum Distris {RECTANGULAR, TRIANGULAR, CIRCULAR;}
	Distris distribution = Distris.RECTANGULAR;
	
	private final int maxSteps;
	private final int minX;
	private final int minY;
	private final int maxX;
	private final int maxY;
	
	/**
	 * Referenzwerte aus dem Paper oder der Implementierung.
	 */
	private final int minRadius = 0;
	private final int radiusConstantTime = 100;
	private final double initialAdaption = 0.8;
	private final double minAdaption = 0.01;
	private final double coolingFactor = 0.4;
	
	private double adaption = initialAdaption;
	private double lastX;
	private double lastY;
	private double factor;
	private int currentStep = 0;
	private int radius = 3;
	private List<Node> nodes;
	
	public ISOMGraphLayout(IGraph graph, int maxSteps, int width, int height, int margin) {
		this.maxSteps = maxSteps;
		this.minX = margin;
		this.minY = margin;
		this.maxX = width - margin;
		this.maxY = height - margin;
		this.nodes = new ArrayList<Node>();
		
		// Nodes erstellen
		for (int i = 0; i < graph.getNumberOfVertices(); i++) {
			Node n = new Node(i);
			nodes.add(n);
			
			double x, y;
			do {
				x = minX + (maxX - minX) * Math.random();
				y = minY + (maxY - minY) * Math.random();
			} while (!insideDistribution(x, y));
			
			n.x = x;
			n.y = y;
		}
		
		// Edges erstellen
		for (Node n : nodes) {
			for (Node m : nodes) {
				if (n == m) continue;
				// Kantengewichtung = -1 => keine Kante
				if (graph.edgeWeight(n.nr, m.nr) == IGraph.NON_EXISTING_EDGE) continue;
				n.succ.add(m);
			}
		}
	}
	
	/**
	 * Berechnet einen neuen Schritt oder wenn die maximale Anzahl erreicht ist den letzten Zustand.
	 * @return Liste mit den Ecken
	 */
	public List<Node> getNextLayout() {
		if (currentStep < maxSteps) {
			// Alle Nodes zur�cksetzen
			for (Node n : nodes) {
				n.visited = false;
				n.distance = 0;
			}
			
			// Durch Zufall einen neuen Punkt berechnen
			do {
				lastX = minX + (maxX - minX) * Math.random();
				lastY = minY + (maxY - minY) * Math.random();
			} while (!insideDistribution(lastX, lastY));
			
	  		Node winner = closestNode(lastX, lastY);
	  		winner.visited = true;
	  		
	  		Queue<Node> queue = new ArrayBlockingQueue<Node>(nodes.size()); 
	  		queue.add(winner);
	  		
			while (!queue.isEmpty()) {
				Node current = queue.poll();

				double dx = lastX - current.x;
				double dy = lastY - current.y;
				double factor = adaption / Math.pow(2, current.distance);
				current.update(factor * dx, factor * dy);

				if (current.distance < radius) {
					for (Node child : current.succ) {
						if (!child.visited && insideDistribution(child.x, child.y)) {
							child.visited = true;
							child.distance = current.distance + 1;
							queue.add(child);
						}
					}
				}
			}
			
			// Update der Parameter
			currentStep++;
			// negative exponential cooling:
			factor = Math.exp(-1 * coolingFactor * (double) currentStep / maxSteps);
			adaption = Math.max(minAdaption, factor * initialAdaption);

			if ((radius > minRadius) && (0 == currentStep % radiusConstantTime)) {
				radius -= 1;
			}
		}
		
		return nodes;
	}
	
	/**
	 * Ist die maximale Anzahl an Schitte erreicht?
	 * @return true falls dem so ist, false sonst
	 */
	public boolean isDone() {
		return currentStep >= maxSteps;
	}
	
	/**
	 * Berechnet die Ecke, die am dichtesten zum gegebenen Punkt ist.
	 * @param x		X-Koordinate des Punktes
	 * @param y 	Y-Koordinate des Punktes
	 * @return dichteste Ecke
	 */
	private Node closestNode(double x, double y) {
		double dx, dy, x0, y0, dist;
		double d = Double.MAX_VALUE;
		Node current = null;
		for (Node n : nodes) {
			x0 = n.x;
			y0 = n.y;
			if (insideDistribution(x0, y0)) {
				dx = x0 - x;
				dy = y0 - y;
				dist = Math.sqrt(dx * dx + dy * dy);
				if (d > dist) {
					d = dist;
					current = n;
				}
			}
		}
		return current;
	}
	
	/**
	 * Pr�ft, ob ein Punkt innerhalb der Verteilung liegt.
	 * @param x		X-Koordinate des Punktes
	 * @param y		Y-Koordinate des Punktes
	 * @return true, falls der Punkt innerhalb der Verteilung liegt, false sonst
	 */
    private boolean insideDistribution(double x, double y) {
        switch (distribution) {
           case RECTANGULAR:
             return (x >= minX && x <= maxX && y >= minY && y <= maxY);
           case TRIANGULAR:
               double range = (y - minY) / (maxY - minY) * (maxX - minX) / 2;
               return (x >= (minX + (maxX - minX) / 2 - range) &&
                       x <= (minX + (maxX - minX) / 2 + range));
           case CIRCULAR:
               double centerX = minX + (maxX - minX) / 2;
               double centerY = minY + (maxY - minY) / 2;
               double radius = Math.min(maxX - minX, maxY - minY) / 2;
               double dist = Math.sqrt(Math.pow((centerX - x), 2.0) + Math.pow((centerY - y), 2.0));
               return (dist <= radius);
           default: 
             lastX = 0;
             lastY = 0;
             return true;
        }
    }
}
