package a3;

import java.util.*;

/**
 * Ant Colony Optimization Algorithmus fuer das Traveling Salesman Problem.
 */
public class CVRP
{

    private static final boolean DEBUG = true;
    private final int antCount;
    private final int antCapacity;
    private final int steps;
    private final double VAPORIZE_RATE = 0.5;
    private final int outputInterval;
    private Random rand = new Random(1337);
    double[][] pheroMatrix = null;

    /**
     * Konstruktor des ACO Algorithmus.
     *
     * @param antCount Anzahl der maximal verwendeten Ameisen
     * @param steps	Anzahl der maximalen Schritte
     */
    CVRP(int antCount, int antCapacity, int steps)
    {
        this(antCount, antCapacity, steps, 100);
    }

    CVRP(int antCount, int antCapacity, int steps, int outputInterval)
    {
        this.antCount = antCount;
        this.antCapacity = antCapacity;
        this.steps = steps;
        this.outputInterval = outputInterval;
    }

    /**
     * Liefert den kuerzesten Weg von der gegebenen Ecke ueber alle Anderen bis
     * zu dieser zurueck.
     *
     * @param graph Verwendeter Graph
     * @param start	Index der Start/Ziel-Ecke
     * @return Liste mit dem Weg von der Ecke ueber alle Anderen bis zu dieser
     * zurueck.
     */
    public List<List<Integer>> shortestPath(IGraph graph, int start)
    {
        return shortestPath(graph, start, Integer.MAX_VALUE);
    }

    /**
     * Liefert den kuerzesten Weg von der gegebenen Ecke ueber alle Anderen bis
     * zu dieser zurueck.
     *
     * @param graph Verwendeter Graph
     * @param start	Index der Start/Ziel-Ecke
     * @param maxTryCount	maximale Anzahl an Versuche einen neuen Weg zu finden
     * @return Liste mit dem Weg von der Ecke ueber alle Anderen bis zu dieser
     * zurueck
     */
    public List<List<Integer>> shortestPath(IGraph graph, int start, int maxTryCount)
    {
        int stepCount = 0;
        int tryCount = 0; // Falls nach maxTryCount keine neuer kuerzester Weg gefunden wurde terminiere
        int shortestPathLength = Integer.MAX_VALUE;

        List<IAnt> ants = new ArrayList<IAnt>();
        ants.addAll(spawnAnts(this.antCount, start, graph.getCustomers()));

        List<Integer> shortestPath = new ArrayList<Integer>();

        this.pheroMatrix = newPheroMatrix(graph.getNumberOfVertices(), 1);
        double[][] tempPheroMatrix = newPheroMatrix(pheroMatrix.length, 0);

        while (tryCount < maxTryCount && stepCount < this.steps)
        {
            for (IAnt ant : ants)
            {
                // der aktuelle Weg ist länger als der kürzeste, daher uninteressant.
                // man könnte auch längere graphen für die pheromatrix ausprobieren, muss man mal sehen.
                if (shortestPathLength <= graph.getPathLength(ant.getPath()))
                {
                    ant.reset();
                }

                Set<Integer> reachable = graph.reachableAdjacencyVerticesOf(ant.currentPosition());
                int nextVertex = this.chooseNextVertex(graph, ant);

                if (ant.isGoingHome())
                {
                    if (ant.currentPosition() == start)
                    {
                        // alle Kunden wurden bedient
                        if (ant.getRemainingCustomers().isEmpty())
                        {
                            shortestPath = new ArrayList(ant.getPath());
                            shortestPathLength = graph.getPathLength(shortestPath);

                            markPath(tempPheroMatrix, ant.getPath(), 1.0 / shortestPathLength);
                            tryCount = 0;
                            ant.reset();
                        } else
                        {
                            ant.refill();
                            ant.setGoingOut();
                        }
                    }

                    // minioptimierung
                    if (reachable.contains(start))
                    {
                        ant.moveTo(start);
                        continue;
                    }
                } else
                {
                    if (ant.isAtCustomer())
                    {
                        ant.decreaseLoad(graph.getDemandOfCustomer(ant.currentPosition()));
                        ant.addVisitedCustomer(ant.currentPosition());
                    }

                    // alle Kunden abgearbeitet oder der nächste gewählte Knoten ist zu groß
                    if (ant.getRemainingCustomers().isEmpty()
                            || (ant.getRemainingCustomers().contains(nextVertex) && graph.getDemandOfCustomer(nextVertex) > ant.getLoad()))
                    {
                        ant.setGoingHome();
                    }
                }

                ant.moveTo(nextVertex);
            }

            vaporize(this.pheroMatrix, tempPheroMatrix);
            resetPheroMatrix(tempPheroMatrix, 0);
            ++stepCount;
            ++tryCount;

            //Debug Information
            if (DEBUG && stepCount % outputInterval == 0)
            {
                System.out.println(getPrintString(stepCount, shortestPathLength, this.splitPath(start, shortestPath)));
            }
        }

        return this.splitPath(start, shortestPath);
    }

    private int chooseNextVertex(IGraph graph, IAnt ant)
    {
        Set<Integer> reachableVertices = graph.reachableAdjacencyVerticesOf(ant.currentPosition());

        List<Integer> bestVertices = new ArrayList(reachableVertices);
        bestVertices.retainAll(ant.getRemainingCustomers());

        List<Integer> potentialVertices = new ArrayList(reachableVertices);

        if (ant.isGoingOut() && !bestVertices.isEmpty())
        {
            potentialVertices = bestVertices;
        }


        Map<Integer, Double> attractiveness = new HashMap<Integer, Double>();
        double totalAttractiveness = 0.0;

        for (int vertex : potentialVertices)
        {
            attractiveness.put(vertex, calculateAttractiveness(graph, ant.currentPosition(), vertex));
            totalAttractiveness += attractiveness.get(vertex);
        }

        // Knoten mit maximaler Attraktivität finden
        Map.Entry<Integer, Double> maxEntry = null;
        for (Map.Entry<Integer, Double> entry : attractiveness.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }

        return maxEntry.getKey();
    }

    private double calculateAttractiveness(IGraph graph, int source, int target)
    {
        double influence = this.rand.nextFloat();

        return Math.pow(graph.edgeWeight(source, target), influence)
                * Math.pow(this.pheroMatrix[source][target], 1 - influence);
    }

    /**
     * Erzeugt eine Anzahl an Ameisen bis zum Maximum und setzt die Startecke.
     *
     * @param currentAntCount	derzeitige Anzahl an Ameisen
     * @param start	Index der Startecke
     * @return eine Liste mit neuen Ameisen
     */
    private List<IAnt> spawnAnts(int currentAntCount, int start, Set<Integer> customers)
    {
        List<IAnt> ants = new ArrayList<IAnt>();
        for (int i = 0; i < this.antCount; ++i)
        {
            ants.add(new Ant(start, this.antCapacity, customers));
        }
        return ants;
    }

    /**
     * Initialisiert die Pheromonenmatrix mit einem gegebenen Wert.
     *
     * @param size	Anzahl der Ecken
     * @param val	Initialisierungswert
     * @return initialisierte Pheromonenmatrix
     */
    private double[][] newPheroMatrix(int size, double val)
    {
        return resetPheroMatrix(new double[size][size], val);
    }

    /**
     * Setzt alle Zellen einer Matrix auf den gegebenen Wert.
     *
     * @param mat	Matrix (Pheromonenmatrix)
     * @param val	Wert
     * @return veraenderte Matrix
     */
    private double[][] resetPheroMatrix(double[][] mat, double val)
    {
        for (int i = 0; i < mat.length; ++i)
        {
            for (int j = 0; j < mat.length; ++j)
            {
                mat[i][j] = val;
            }
        }
        return mat;
    }

    /**
     * Berechnung der neuen Pheromonenmatrix anhand der derzeitigen
     * Pheromonenmatrix verringert um einen gewissen Faktor und der temporaeren
     * Phermonenmatrix.
     *
     * @param pheroMatrix	Phermonenmatrix
     * @param tempPheroMat	temporaere Phermonenmatrix
     * @return	neu berechnete Phermonenmatrix
     */
    private double[][] vaporize(double[][] pheroMatrix, double[][] tempPheroMat)
    {
        for (int x = 0; x < pheroMatrix.length; ++x)
        {
            for (int y = 0; y < pheroMatrix.length; ++y)
            {
                pheroMatrix[x][y] = pheroMatrix[x][y] * (1 - VAPORIZE_RATE) + tempPheroMat[x][y];
            }
        }
        return pheroMatrix;
    }

    /**
     * Addiert den gegebenen Pheromonenwert des gegebenen Weges auf die
     * Pheromonenmatrix.
     *
     * @param pheroMatrix	Pheromonenmatrix
     * @param path	Liste aus Indizes von Ecken, die den Weg bilden
     * @param phero	Pheromonenwert
     * @return veraenderte Pheromonenmatrix
     */
    private double[][] markPath(double[][] pheroMatrix, List<Integer> path, double phero)
    {
        for (int i = 0; i < path.size() - 1; ++i)
        {
            pheroMatrix[path.get(i)][path.get(i + 1)] += phero;
        }
        return pheroMatrix;
    }

    private String getPrintString(int stepCount, int shortestPathLength, List<List<Integer>> shortestPath)
    {
        return String.format("Step %d, Length %d => Path %s", stepCount, shortestPathLength, shortestPath);
    }

    private List<List<Integer>> splitPath(int start, List<Integer> path)
    {
        List<List<Integer>> pathlist = new ArrayList<List<Integer>>();
        path = new ArrayList(path);

        if (!path.isEmpty())
        {
            path.remove(0);

            List<Integer> templist = new ArrayList<Integer>();
            templist.add(start);

            for (int i : path)
            {
                templist.add(i);

                if (i == start)
                {
                    pathlist.add(templist);
                    templist = new ArrayList<Integer>();
                    templist.add(0);
                }
            }

        }

        return pathlist;

    }

    // DEBUG
    private void debug(Object o)
    {
        System.out.println(o.toString());
    }
}
