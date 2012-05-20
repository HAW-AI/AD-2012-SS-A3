package a3;

import graph.GraphFactory;
import graph.IGraph;
import java.util.List;

public class Test {
    private final static int RUNS = 3;
    private final static int STEPS = 500;
    private final static int ANTS = 1500;
    private final static int GRAPH = 24;
    
    public static void main(String ... args) {
       
        ACO algo = new ACO(ANTS,STEPS);

        //Teste kleine unvollstaendige Graphen
//    	int[][] distancematrix1 = {{0,-1,2},{5,0,7},{5,5,0}}; //Lsg: 0 2 1 0  L: 12
//    	int[][] distancematrix2 = {{0,-1,-1},{5,0,7},{5,5,0}}; //Kein Pfad
   		int[][] distancematrix3 = {{0,-1,15},{5,0,1},{1,15,0}}; //Lsg: 0 2 1 0  L: 35
    			
        IGraph incom = GraphFactory.createDiGraph(distancematrix3);
        List<Integer> path1 = algo.shortestPath(incom,0);
        System.out.println(algo.getPathLength(path1, incom) + " " + path1 );
       
        //Teste grosse unvollstaendige Graphen ohne bekannte Loesung
        IGraph digr = GraphFactory.createDiGraph(IOManager.RandomDistanceMatrix(1338, 200, 200, 1));
//        System.out.println(digr);
        List<Integer> path3 = algo.shortestPath(digr,0);
        System.out.println( algo.getPathLength(path3, digr) + " " + path3);
        
        IOManager io2 = new IOManager("digraph/digraph.txt"); //LSG 0 1 2 0 L:9
        IGraph dif = GraphFactory.createDiGraph(io2.readDigraphMatrix());
        List<Integer> path4 = algo.shortestPath(dif,0);
        System.out.println( algo.getPathLength(path4, dif) + " " + path4);
        
        //Teste symmetrische 
        IOManager io = new IOManager("simGraph("+ GRAPH +").txt");
//      IGraph g = GraphFactory.createRandomGraph(200,1337);
//      IGraph g = GraphFactory.createNormalizedGraph(50);
        IGraph g = GraphFactory.createGraph(io.readMatrix());
        int bestSolution = io.readBestSolution();
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