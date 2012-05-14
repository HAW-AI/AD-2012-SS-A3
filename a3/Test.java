package a3;

import graph.GraphFactory;
import graph.IGraph;

import java.util.ArrayList;
import java.util.List;

public class Test {
    private final static int RUNS = 20;
    private final static int STEPS = 300;
    private final static int ANTS = 1500;
    private final static int GRAPH = 5;
    
    public static void main(String ... args) {
        IOManager io = new IOManager("src\\simGraph("+ GRAPH +").txt");
        //IGraph g = GraphFactory.createRandomGraph(12);
        //IGraph g = GraphFactory.createNormalizedGraph(30);
        IGraph g = GraphFactory.createGraph(io.readMatrix());
        int bestSolution = io.readBestSolution();
        ACO algo = new ACO(ANTS,STEPS);
        int totaldiff = 0;
        int fails = 0;
        int maxdiff = 0;
        for(int i = 0; i < RUNS; ++i) {
            System.out.print(i+") ");
            List<Integer> path = algo.shortestPath(g,0);
            int w =algo.getPathLength(path,g);
            int diff = w-bestSolution;
            System.out.println("Weglänge: "+ w + "("+ (diff)+")");
            maxdiff = Math.max(maxdiff, diff);
            totaldiff += diff;
            if (diff > 0)
                ++fails;
        }
        System.out.println("-------------------------------------");
        System.out.println("Erfolgsquote:     " + String.format("%.2f",(RUNS-fails)*100.0/RUNS) + "%   (" + fails + " Fehler in " + RUNS + " Durchläufen)");
        System.out.println("Gesamtdifferenz:  " + totaldiff);
        System.out.println("Größte Differenz: " + maxdiff);
        System.out.println("Durchschntl.Diff: " + String.format("%.2f", (((double)totaldiff)/RUNS)));
        System.out.println("-------------------------------------");
    }
}
