package a3;

import java.util.*;

/**
 * Ant Colony Optimization Algorithmus fuer das Traveling Salesman Problem.
 */
public class CVRP
{

    private final double VAPORIZE_RATE = 0.4;
    private final Random rand = new Random(1337);
    private final IGraph graph;
    private final IPathfinder pathfinder;
    private final int start;
    private final boolean outputInfo;
    private final int outputInterval;
    private int antCount;
    private int antCapacity;
    private int steps;
    double[][] pheroMatrix = null;

    /**
     * Konstruktor des CVRP Algorithmus.
     *
     * @param antCount Anzahl der maximal verwendeten Ameisen
     * @param steps	Anzahl der maximalen Schritte
     */
    public CVRP(IGraph graph, int start)
    {
        this(graph, start, true, 100);
    }

    public CVRP(IGraph graph, int start, boolean outputInfo, int outputInterval)
    {
        this.graph = graph;
        this.start = start;
        this.outputInfo = outputInfo;
        this.outputInterval = outputInterval;
        this.pathfinder = new FloydWarshall(graph);
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
    public List<List<Integer>> shortestPath(int antCount, int antCapacity, int steps)
    {
        return shortestPath(antCount, antCapacity, steps, 10000);
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
    public List<List<Integer>> shortestPath(int antCount, int antCapacity, int steps, int maxTryCount)
    {
        this.antCount = antCount;
        this.antCapacity = antCapacity;
        this.steps = steps;

        List<Integer> shortestPath = new ArrayList<Integer>();

        int stepCount = 0;
        int tryCount = 0; // Falls nach maxTryCount keine neuer kuerzester Weg gefunden wurde terminiere
        int shortestPathLength = Integer.MAX_VALUE;

        List<IAnt> ants = spawnAnts(this.antCount, start, graph.getCustomers());

        this.pheroMatrix = newPheroMatrix(graph.getNumberOfVertices(), 1);
        double[][] tempPheroMatrix = newPheroMatrix(pheroMatrix.length, 0);

        while (tryCount < maxTryCount && stepCount < this.steps)
        {
            for (IAnt ant : ants)
            {

                // der aktuelle Weg ist länger als der kürzeste, daher uninteressant.
                // man könnte auch längere graphen für die pheromatrix ausprobieren, muss man mal sehen.
                if (graph.getPathLength(ant.getPath()) > shortestPathLength)
                {
                    ant.reset();
                }

                if (ant.isAtCustomer())
                {
                    ant.decreaseLoad(graph.getDemandOfCustomer(ant.currentPosition()));
                    ant.addVisitedCustomer(ant.currentPosition());
                }

                // alle Kunden sind beliefert
                if (ant.getRemainingCustomers().isEmpty())
                {
                    List<Integer> pathHome = this.pathfinder.getShortestPath(ant.currentPosition(), this.start);

                    pathHome.remove(0);
                    ant.addPath(pathHome);

                    int newPathLength = graph.getPathLength(ant.getPath());
                    if (newPathLength <= shortestPathLength)
                    {
                        if (newPathLength < shortestPathLength)
                        {
                            tryCount = 0;
                        }

                        shortestPath = new ArrayList(ant.getPath());
                        shortestPathLength = graph.getPathLength(shortestPath);

                        markPath(tempPheroMatrix, ant.getPath(), 1.0 / shortestPathLength);
                    }

                    ant.reset();
                    continue;
                }

                int nextVertex = this.chooseNextVertex(ant);

                // der nächste Knoten (Kunde) hat mehr Bedarf als die Ameise bei sich trägt.
                if (ant.getRemainingCustomers().contains(nextVertex) && graph.getDemandOfCustomer(nextVertex) > ant.getLoad())
                {
                    if (ant.currentPosition() != this.start)
                    {
                        List<Integer> pathHome = this.pathfinder.getShortestPath(ant.currentPosition(), this.start);
                        pathHome.remove(0);
                        ant.addPath(pathHome);
                    }
                    ant.refill();
                    continue;
                }

                ant.moveTo(nextVertex);
            }

            vaporize(this.pheroMatrix, tempPheroMatrix);
            resetPheroMatrix(tempPheroMatrix, 0);
            ++stepCount;
            ++tryCount;

            //Debug Information
            if (outputInfo && stepCount % outputInterval == 0)
            {
                printInfo(stepCount, shortestPathLength, this.splitPath(start, shortestPath));
            }
        }

        return this.splitPath(start, shortestPath);
    }

    private int chooseNextVertex(IAnt ant)
    {
        List<Integer> reachableVertices = new ArrayList(this.graph.getNeighboursOf(ant.currentPosition()));

        // individuelle und summierte Attraktivität berechnen
        Map<Integer, Double> indivigualAttractiveness = new HashMap<Integer, Double>();
        double totalAttractiveness = 0.0;

        for (int vertex : reachableVertices)
        {
            indivigualAttractiveness.put(vertex, calculateAttractiveness(ant.currentPosition(), vertex));
            totalAttractiveness += indivigualAttractiveness.get(vertex);
        }

        // relative Attraktivität berechnen
        Map<Integer, Double> relativeAttractiveness = new HashMap<Integer, Double>();
        for (int vertex : reachableVertices)
        {
            relativeAttractiveness.put(vertex, indivigualAttractiveness.get(vertex) / totalAttractiveness);
        }

        double random = this.rand.nextFloat();
        double probability = 0.0;
        for (Map.Entry<Integer, Double> entry : relativeAttractiveness.entrySet())
        {
            probability += entry.getValue();
            if (probability >= random)
            {
                return entry.getKey();
            }
        }

        return 0;
    }

    /**
     * Berechnet die Attraktivität einer Bewegung von einem zum anderen Knoten
     *
     * @param source Startknoten
     * @param target Zielknoten
     * @return Attraktivität
     */
    private double calculateAttractiveness(int source, int target)
    {
        return (1.0 / this.graph.edgeWeight(source, target)) + this.pheroMatrix[source][target];
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

    private void printInfo(int stepCount, int shortestPathLength, List<List<Integer>> shortestPath)
    {
        System.out.println(String.format("Step %d, Length %d => Path %s", stepCount, shortestPathLength, shortestPath));
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
                    templist.add(start);
                }
            }
        }

        return pathlist;
    }
}
