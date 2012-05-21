package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Brute Force Algorithmus zur Bestimmung des TSP Problems. 
 * Berechnungzeit: explosionsartig [(n-1)!/2]
 */
public class BruteForce {

	/**
	 * Liefert den kürzesten Weg von der Start/Ziel-Ecke über den Graphen zurück.
	 * @param g			Graph
	 * @param start		Start/Ziel-Ecke
	 * @return Liste der Indizes der Ecken des kürzesten Weges
	 */
	public static List<Integer> shortestPath(IGraph g, int start) {
		List<Integer> vertices = new ArrayList<Integer>();
		List<Integer> way = new ArrayList<Integer>();
		for (int i = 0; i < g.getNumberOfVertices(); i++) {
			if (i != start) {
				vertices.add(i);
			}
		}
		way.add(start);
		List<Integer> shortPath = rekAlgo(g, vertices, way);

		return shortPath;
	}

	/**
	 * Rekursiver Algorithmus, der alle möglichen Wege austestet.
	 * @param g			Graph
	 * @param vertices	(noch vorhandene) Liste von Ecken
	 * @param way		bisheriger Weg
	 * @return Liste aus Indizes von Ecken, neuer Weg
	 */
	private static List<Integer> rekAlgo(IGraph g, List<Integer> vertices,
			List<Integer> way) {
		if (vertices.size() == 1) {
			way.add(vertices.get(0));
			way.add(way.get(0));
			return way;
		}

		List<Integer> verts = new ArrayList<Integer>();
		List<Integer> pathToTry = new ArrayList<Integer>();
		int minPathLength = Integer.MAX_VALUE;
		List<Integer> minPath = new ArrayList<Integer>();
		for (int i : vertices) {
			// Überspringen, falls es gar keinen Weg gibt
			if (g.edgeWeight(way.get(way.size()-1), i) == IGraph.NON_EXISTING_EDGE) continue;
			
			verts.clear();
			verts.addAll(vertices);
			((Collection<Integer>) verts).remove(i);
			pathToTry.clear();
			pathToTry.addAll(way);
			pathToTry.add(i);
			List<Integer> Path = rekAlgo(g, verts, pathToTry);
			int pathLength = getPathLength(Path, g);
			if (pathLength <= minPathLength) {
				minPathLength = pathLength;
				minPath.clear();
				minPath.addAll(Path);
			}
		}
		return minPath;
	}

	/**
	 * Berechnet die Summe der Distanzen des gegebenen Weges anhand des gegegeben Graphen.
	 * @param path		Liste aus Indizes von Ecken, die den Weg bilden
	 * @param g			Graph
	 * @return Summe der Kantendistanzen des Weges
	 */
	public static int getPathLength(List<Integer> path, IGraph g) {
		int wayLength = 0;
		for (int i = 0; i < path.size() - 1; ++i) {
			wayLength += g.edgeWeight(path.get(i), path.get(i + 1));
		}
		return wayLength;

	}

}
