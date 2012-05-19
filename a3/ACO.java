package a3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graph.IGraph;
import java.util.*;
import a3.IAnt;

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

	public List<Integer> shortestPath(IGraph graph, int start) {
		return shortestPath(graph, start, 0, Integer.MAX_VALUE);
	}

	public List<Integer> shortestPath(IGraph graph, int start,
			int notifyInterval, int maxwaitingtoterminate) {
		int stepCount = 0;
		int shortestPathLength = Integer.MAX_VALUE;

		List<IAnt> ants = new ArrayList<IAnt>();
		List<Integer> shortestPath = new ArrayList<Integer>();

		double[][] pheroMatrix = initPheroMatrix(graph.getNumberOfVertices(), 1);
		double[][] tempPheroMatrix = initPheroMatrix(pheroMatrix.length, 0);

		while (stepCount < steps) {
			if (notifyInterval > 0 && stepCount % notifyInterval == 0)
				System.out.println("(" + stepCount + ") bisher kürzester Weg: "
						+ shortestPath + " Länge: " + shortestPathLength);

			if (ants.size() < maxAnts)
				ants.addAll(spawnAnts(ants.size(), start));

			int TerminateCounter = 0;
			for (IAnt ant : ants) {
				if (TerminateCounter == maxwaitingtoterminate)
					return shortestPath;

				if (ant.numberOfVisitedVertices() == graph
						.getNumberOfVertices()) {
					ant.moveTo(start);
					int pathLength = getPathLength(ant.getPath(), graph);
					markPath(tempPheroMatrix, ant.getPath(), 1.0 / pathLength);

					if (pathLength < shortestPathLength) {
						shortestPathLength = pathLength;
						shortestPath = ant.getPath();
						TerminateCounter = 0;
					} else {
						TerminateCounter++;
					}
					ant.reset();
				} else {

					int target = choosePath(getRoutingMap(graph, ant,
							pheroMatrix));
					if (target != -1) {
						ant.moveTo(target);
					} else {
						ant.reset();
					}

				}
			}
			vaporize(pheroMatrix, tempPheroMatrix);
			resetPheroMatrix(tempPheroMatrix, 0);
			++stepCount;
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

	private Map<Integer, Double> getRoutingMap(IGraph graph, IAnt ant,
			double[][] pheroMat) {
		Map<Integer, Double> routingMap = new HashMap<Integer, Double>();
		double totalValue = 0;
		int currentPosition = ant.currentPosition();
		Set<Integer> neighbours = graph.getneighbours(currentPosition);
		if (neighbours.size() == 1) {

			for (int neighbour : neighbours) {
				if (neighbour == ant.currentPosition()) {
					routingMap.put(ant.currentPosition(), 1.0);
					return routingMap;
				}
			}

		}

		// Set benutzen

		for (int vertex = 0; vertex < graph.getNumberOfVertices(); ++vertex) {
			if (!ant.hasVisited(vertex)) {
				double routingValue = determineRoutingValue(
						graph.edgeWeight(currentPosition, vertex),
						pheroMat[currentPosition][vertex]);
				totalValue += routingValue;
				routingMap.put(vertex, routingValue);
			}
		}
		for (int vertex : routingMap.keySet()) {
			routingMap.put(vertex, routingMap.get(vertex) / totalValue);
		}
		return routingMap;
	}

	public int choosePath(Map<Integer, Double> routingMap) {
		double currentSum = 0;
		double rand = this.rand.nextDouble();
		Set<Integer> routing = routingMap.keySet();

		if (routing.size() == 1) {
			return -1;
		}

		for (int vertex : routingMap.keySet()) {
			if ((currentSum += routingMap.get(vertex)) >= rand) {
				return vertex;
			}
		}
		return 0; // never reached
	}

	private double[][] vaporize(double[][] pheroMatrix, double[][] tempPheroMat) {
		for (int x = 0; x < pheroMatrix.length; ++x)
			for (int y = 0; y < pheroMatrix.length; ++y)
				pheroMatrix[x][y] = pheroMatrix[x][y] * (1 - VAPORIZE_RATE)
						+ tempPheroMat[x][y];
		return pheroMatrix;
	}

	private double[][] markPath(double[][] pheroMatrix, List<Integer> path,
			double phero) {
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
