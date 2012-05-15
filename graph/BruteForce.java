package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BruteForce {

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
			verts.clear();
			verts.addAll(vertices);
			((Collection<Integer>)verts).remove(i);
			pathToTry.clear();
			pathToTry.addAll(way);
			pathToTry.add(i);
			List<Integer> Path = rekAlgo(g, verts, pathToTry);
			int pathLength = pathLength(g, Path);
			if (pathLength <= minPathLength) {
				minPathLength = pathLength;
				minPath.clear();
				minPath.addAll(Path);
			}
		}
		return minPath;
	}

	public static int pathLength(IGraph g, List<Integer> path) {
		int wayLength = 0;
		for (int i = 0; i < path.size() - 1; ++i) {
			wayLength += g.edgeWeight(path.get(i), path.get(i + 1));
		}
		return wayLength;

	}

}
