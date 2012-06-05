package a3;

import java.util.*;

import a3.IGraph;

/**
 * Ant Colony Optimization Algorithmus fuer das Traveling Salesman Problem.
 */
public class ACO {
	private static final boolean DEBUG = true;
	private final int maxAnts;
	private final int steps;
	private final int SPAWN_RATE = 100;
	private final double VAPORIZE_RATE = 0.5;
	private final int debugType;
	private final int outputInterval;
	private Random rand = new Random(1337);

	/**
	 * Konstruktor des ACO Algorithmus.
	 * @param antCount 		Anzahl der maximal verwendeten Ameisen
	 * @param steps			Anzahl der maximalen Schritte
	 */
	public ACO(int antCount, int steps) {
		maxAnts = antCount;
		this.steps = steps;
		this.debugType = 2;
		this.outputInterval = 100;
	}

	ACO(int antCount, int steps, int debugType, int outputInterval) {
		maxAnts = antCount;
		this.steps = steps;
		this.debugType = debugType;	
		this.outputInterval = outputInterval;
	}

	/**
	 * Liefert den kuerzesten Weg von der gegebenen Ecke ueber alle Anderen bis zu dieser zurueck.
	 * @param graph 		Verwendeter Graph
	 * @param start			Index der Start/Ziel-Ecke
	 * @return Liste mit dem Weg von der Ecke ueber alle Anderen bis zu dieser zurueck.
	 */
	public List<Integer> shortestPath(IGraph graph, int start){
		return shortestPath(graph, start, Integer.MAX_VALUE);
	}

	/**
	 * Liefert den kuerzesten Weg von der gegebenen Ecke ueber alle Anderen bis zu dieser zurueck.
	 * @param graph 		Verwendeter Graph
	 * @param start			Index der Start/Ziel-Ecke
	 * @param maxTryCount	maximale Anzahl an Versuche einen neuen Weg zu finden
	 * @return Liste mit dem Weg von der Ecke ueber alle Anderen bis zu dieser zurueck
	 */
	public List<Integer> shortestPath(IGraph graph, int start, int maxTryCount) {
		int stepCount = 0;
		int tryCount = 0; // Falls nach maxTryCount keine neuer kuerzester Weg gefunden wurde terminiere
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
						int pathLength = graph.getPathLength(ant.getPath());
						markPath(tempPheroMatrix, ant.getPath(), 1.0 / pathLength);

						if (pathLength < shortestPathLength) {
							shortestPathLength = pathLength;
							shortestPath = ant.getPath();
							tryCount = 0;
							if (DEBUG) {
								//Debug Informations
								if (debugType == 3 || debugType == 1) {
									System.out.println(getPrintString(stepCount, shortestPathLength, shortestPath));
								}
							}
						}
					}

					// Fang von vorne an
					ant.reset();						

				} else {
					int target = choosePath(getRoutingMap(graph, ant,pheroMatrix));
					// Falls die Ant in eine Sackgasse gelaufen ist, setzte sie wieder auf Start
					if (target < 0) {
						ant.reset();
					} else {
						ant.moveTo(target);
						if (DEBUG) {
							//Debug Informations
							if (debugType == 3) {
								System.out.println(getPrintString(ant));
							}
						}
					}
				}
			}
			vaporize(pheroMatrix, tempPheroMatrix);
			resetPheroMatrix(tempPheroMatrix, 0);
			++stepCount;
			++tryCount;
			if (DEBUG) {
				//Debug Informations
				if (debugType == 2 && stepCount % outputInterval == 0) {
					System.out.println(getPrintString(stepCount, shortestPathLength, shortestPath));
				}
			}
		}
		return shortestPath;
	}

	/**
	 * Erzeugt eine Anzahl an Ameisen bis zum Maximum und setzt die Startecke.
	 * @param currentAntCount	derzeitige Anzahl an Ameisen
	 * @param start				Index der Startecke
	 * @return eine Liste mit neuen Ameisen
	 */
	private List<IAnt> spawnAnts(int currentAntCount, int start) {
		int spawnCount = Math.min(maxAnts - currentAntCount, SPAWN_RATE);
		List<IAnt> ants = new ArrayList<IAnt>();
		for (int i = 0; i < spawnCount; ++i)
			ants.add(new Ant(start));
		return ants;
	}


	/**
	 * Berechnet die naechsten Zielecken einer Ameise und deren Gewichtungen.
	 * @param graph		verwendeter Graph
	 * @param ant		derzeitge Ameise
	 * @param pheroMat	Pheromonenmatrix
	 * @return Map mit Indizes von erreichbaren und noch nicht besuchten Ecken und deren Gewichtung
	 */
	public Map<Integer, Double> getRoutingMap(IGraph graph, IAnt ant, double[][] pheroMat) {
		Map<Integer, Double> routingMap = new HashMap<Integer, Double>();
		double totalValue = 0;
		int currentPosition = ant.currentPosition();
		Set<Integer> reachable = graph.reachableAdjacencyVerticesOf(currentPosition);
		// Bewerte nur erreichbare Nachbarn
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
	
	/**
	 * Berechnung der speziellen Distanz einer Kante anhand des Pheromonenwertes und der Distanz.
	 * @param weight	Distanz
	 * @param phero		Pheromonenwert
	 * @return spezielle Distanz
	 */
	public double determineRoutingValue(int weight, double phero) {
		return phero / weight;
	}

	/**
	 * Berechnet den Index der Ecke, die eine Ameise besuchen soll.
	 * Diese Funktion ist nicht deterministisch, weil durch "Zufall" eine Schranke ermittelt wird,
	 * ob eine Ecke genutzt wird oder nicht.
	 * @param routingMap	Map mit Indizes von Ecken und deren Gewichtung
	 * @return ein Index von der gewaehlten Ecke oder -1 falls die Map leer ist
	 */
	public int choosePath(Map<Integer, Double> routingMap) {
		double currentSum = 0;
		double rand = this.rand.nextDouble();
		for (int vertex : routingMap.keySet()) {
			if ((currentSum += routingMap.get(vertex)) >= rand) {
				return vertex;
			}
		}
		return -1; // Sackgasse, keine Eintraege in der routingMap
	}
	
	/**
	 * Initialisiert die Pheromonenmatrix mit einem gegebenen Wert.
	 * @param size	Anzahl der Ecken
	 * @param val	Initialisierungswert
	 * @return initialisierte Pheromonenmatrix
	 */
	private double[][] initPheroMatrix(int size, double val) {
		return resetPheroMatrix(new double[size][size], val);
	}

	/**
	 * Setzt alle Zellen einer Matrix auf den gegebenen Wert.
	 * @param mat	Matrix (Pheromonenmatrix)
	 * @param val	Wert
	 * @return veraenderte Matrix
	 */
	private double[][] resetPheroMatrix(double[][] mat, double val) {
		for (int i = 0; i < mat.length; ++i)
			for (int j = 0; j < mat.length; ++j)
				mat[i][j] = val;
		return mat;
	}

	/**
	 * Berechnung der neuen Pheromonenmatrix anhand der derzeitigen Pheromonenmatrix verringert
	 * um einen gewissen Faktor und der temporaeren Phermonenmatrix.
	 * @param pheroMatrix	Phermonenmatrix
	 * @param tempPheroMat	temporaere Phermonenmatrix
	 * @return	neu berechnete Phermonenmatrix
	 */
	private double[][] vaporize(double[][] pheroMatrix, double[][] tempPheroMat) {
		for (int x = 0; x < pheroMatrix.length; ++x)
			for (int y = 0; y < pheroMatrix.length; ++y)
				pheroMatrix[x][y] = pheroMatrix[x][y] * (1 - VAPORIZE_RATE) + tempPheroMat[x][y];
		return pheroMatrix;
	}

	/**
	 * Addiert den gegebenen Pheromonenwert des gegebenen Weges auf die Pheromonenmatrix.
	 * @param pheroMatrix	Pheromonenmatrix
	 * @param path			Liste aus Indizes von Ecken, die den Weg bilden
	 * @param phero			Pheromonenwert
	 * @return veraenderte Pheromonenmatrix
	 */
	private double[][] markPath(double[][] pheroMatrix, List<Integer> path, double phero) {
		for (int i = 0; i < path.size() - 1; ++i) {
			pheroMatrix[path.get(i)][path.get(i + 1)] += phero;
		}
		return pheroMatrix;
	}
	
    private String getPrintString(int stepCount, int shortestPathLength, List<Integer> shortestPath) {

        return "Step " + stepCount + ", Length: " + shortestPathLength + " => shortest Path: " + shortestPath ;
    }

    private String getPrintString(IAnt ant) {
        return "Ant " + ant + "=> " + ant.getPath();

    }
}
