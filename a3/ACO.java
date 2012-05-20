package a3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graph.IGraph;
import java.util.*;

public class ACO {
	final int maxAnts;
	final int steps;
	private final int SPAWN_RATE = 100;
	private final double VAPORIZE_RATE = 0.5;
	private Random rand = new Random(1337);

	public ACO(int antCount, int steps) {
		maxAnts = antCount;
		this.steps = steps;
	}
	
	public List<Integer> shortestPath(IGraph graph, int start){
		return shortestPath(graph, start, Integer.MAX_VALUE);
	}

	public List<Integer> shortestPath(IGraph graph, int start, int maxTryCount) {
		int stepCount = 0;
		int tryCount = 0; // Falls nach maxTryCount keine neuer kürzester Weg gefunden wurde terminiere
		int shortestPathLength = Integer.MAX_VALUE;

		List<IAnt> ants = new ArrayList<IAnt>();
		List<Integer> shortestPath = new ArrayList<Integer>();

		double[][] pheroMatrix = initPheroMatrix(graph.getNumberOfVertices(), 1);
		double[][] tempPheroMatrix = initPheroMatrix(pheroMatrix.length, 0);

		while (tryCount < maxTryCount && stepCount < steps) {
			
			if (ants.size() < maxAnts)
				ants.addAll(spawnAnts(ants.size(), start));

			for (IAnt ant : ants) {
				if (ant.numberOfVisitedVertices() == graph.getNumberOfVertices()) {
					Set<Integer> reachable = graph.reachableAdjacencyVerticesOf(ant.currentPosition());
					// Falls die Ant von ihrer Position den Start wieder direkt erreichen kann, hat sie eine Pfad gefunden
					if (reachable.contains(start)) {
						ant.moveTo(start);
						int pathLength = getPathLength(ant.getPath(), graph);
						markPath(tempPheroMatrix, ant.getPath(), 1.0 / pathLength);

						if (pathLength < shortestPathLength) {
							shortestPathLength = pathLength;
							shortestPath = ant.getPath();
							tryCount = 0;
						}
						ant.reset();
					}else{
						//Fang von vorne an
						ant.reset();
						
					}
				} else {
					int target = choosePath(getRoutingMap(graph, ant,pheroMatrix));
					// Falls die Ant in eine Sackgasse gelaufen ist, setzte sie wieder auf Start
					if (target < 0) {
						ant.reset();
					} else {
						ant.moveTo(target);
					}
				}
			}
			vaporize(pheroMatrix, tempPheroMatrix);
			resetPheroMatrix(tempPheroMatrix, 0);
			++stepCount;
			++tryCount;
		}
		return shortestPath;
	}

	private List<IAnt> spawnAnts(int currentAntCount, int start) {
		int spawnCount = Math.min(maxAnts - currentAntCount, SPAWN_RATE);
		List<IAnt> ants = new ArrayList<IAnt>();
		for (int i = 0; i < spawnCount; ++i)
			ants.add(new Ant(start));
		return ants;
	}

	private double[][] initPheroMatrix(int size, double val) {
		return resetPheroMatrix(new double[size][size], val);
	}

	private double[][] resetPheroMatrix(double[][] mat, double val) {
		for (int i = 0; i < mat.length; ++i)
			for (int j = 0; j < mat.length; ++j)
				mat[i][j] = val;
		return mat;
	}

	private Map<Integer, Double> getRoutingMap(IGraph graph, IAnt ant, double[][] pheroMat) {
		Map<Integer, Double> routingMap = new HashMap<Integer, Double>();
		double totalValue = 0;
		int currentPosition = ant.currentPosition();
		Set<Integer> reachable = graph.reachableAdjacencyVerticesOf(currentPosition);
		//Bewerte nur erreichbare Nachbarn
		for (int vertex: reachable) {
			if (!ant.hasVisited(vertex)) {
				double routingValue = determineRoutingValue(graph.edgeWeight(currentPosition, vertex),pheroMat[currentPosition][vertex]);
				totalValue += routingValue;
				routingMap.put(vertex, routingValue);
			}
		}
		for (int vertex : routingMap.keySet()) {
			routingMap.put(vertex, routingMap.get(vertex) / totalValue);
		}
		return routingMap;
	}

	private int choosePath(Map<Integer, Double> routingMap) {
		double currentSum = 0;
		double rand = this.rand.nextDouble();
		for (int vertex : routingMap.keySet()) {
			if ((currentSum += routingMap.get(vertex)) >= rand) {
				return vertex;
			}
		}
		return -1; // Sackgasse, keine Einträge in der routingMap
	}

	private double[][] vaporize(double[][] pheroMatrix, double[][] tempPheroMat) {
		for (int x = 0; x < pheroMatrix.length; ++x)
			for (int y = 0; y < pheroMatrix.length; ++y)
				pheroMatrix[x][y] = pheroMatrix[x][y] * (1 - VAPORIZE_RATE) + tempPheroMat[x][y];
		return pheroMatrix;
	}

	private double[][] markPath(double[][] pheroMatrix, List<Integer> path, double phero) {
		for (int i = 0; i < path.size() - 1; ++i) {
			pheroMatrix[path.get(i)][path.get(i + 1)] += phero;
		}
		return pheroMatrix;
	}

	int getPathLength(List<Integer> path, IGraph graph) {
		int wayLength = 0;
		for (int i = 0; i < path.size() - 1; ++i) {
			wayLength += graph.edgeWeight(path.get(i), path.get(i + 1));
		}
		return wayLength;
	}

	public double determineRoutingValue(int weight, double phero) {
		return phero / weight;
	}
}
