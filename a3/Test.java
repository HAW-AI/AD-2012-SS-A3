package a3;

import graph.BruteForce;
import graph.GraphFactory;
import graph.IGraph;
import java.util.List;

public class Test {
    private final static int RUNS = 10;
    private final static int STEPS = 500;
    private final static int ANTS = 2000;
    private final static int GRAPH = 24;
    
    public static void main(String ... args) {
       
		//hier nun Testgraphen erstellen:
		
        System.out.println("Teste kleine vollstaendige Graphen");
    	int[][] distancematrix1 = {{0,1,1},{1,0,1},{1,1,0}};
    	int[] eckenkapazitaet1 = {0,1,1};
    	//Kombination distancematrix1, eckenkapazitaet1:
    	//Loesung bei einer Ameisenkapazitaet von >= 2: {{0 1 2 0}} || {{0 2 1 0}}  L: 3
    	//Loesung bei einer Ameisenkapazitaet von 1: {{0 1 0},{0 2 0}}  L: 4
    	
    	int[][] distancematrix2 = {{0,1,3},{5,0,3},{3,2,0}};
    	int[] eckenkapazitaet2 = {0,2,3};
    	//Kombination distancematrix2, eckenkapazitaet2:
    	//Loesung bei einer Ameisenkapazitaet von >= 5: {{0 1 2 0}}  L: 7
    	//Loesung bei einer Ameisenkapazitaet von 3: {{0 1 0},{0 2 0}}  L: 12
    	
    	  	
        
        System.out.println("Teste kleine unvollstaendige Graphen");
    	int[][] distancematrix3 = {{0,-1,2},{5,0,7},{5,5,0}};
    	int[] eckenkapazitaet3 = {0,2,3};
    	//Kombination distancematrix3, eckenkapazitaet3:
    	//Loesung bei einer Ameisenkapazitaet von >= 5: {{0 2 1 0}} L: 12
    	//Loesung bei einer Ameisenkapazitaet von 3 oder 4: {{0 2 0},{0 2 1 0}}  L: 19
        
    	int[][] distancematrix4 = {{0,1,-1},{2,0,3},{-1,4,0}};
    	int[] eckenkapazitaet4 = {0,3,5};
    	//Kombination distancematrix4, eckenkapazitaet4:
    	//Loesung bei einer Ameisenkapazitaet von >= 8: {{0 1 2 0}}  L: 10
    	//Loesung bei einer Ameisenkapazitaet von 5,6 oder 7 : {{0 1 0},{0 1 2 1 0}}  L: 13
    	
       	int[][] distancematrix5 = {{0,1,5},{5,0,2},{1,-1,0}};
    	int[] eckenkapazitaet5 = {0,1,3};
    	//Kombination distancematrix5, eckenkapazitaet5:
    	//Loesung bei einer Ameisenkapazitaet von >= 4: {{0 1 2 0}}  L: 4 
    	//Loesung bei einer Ameisenkapazitaet von 3: {{0 1 2 0},{0 2 0}}  L: 10
    	
    	
       	int[][] distancematrix6 = {{0,3,-1,1},{1,0,-1,-1},{7,1,0,7},{-1,-1,1,0}};
    	int[] eckenkapazitaet6 = {0,3,1,1};
    	//Kombination distancematrix6, eckenkapazitaet6:
    	//Loesung bei einer Ameisenkapazitaet von >= 5: {{0 3 2 1 0}}  L: 4
    	//Loesung bei einer Ameisenkapazitaet von 3 : {{0 1 0},{0 3 2 1 0}}  L: 8 
    	
    	
    	
    	
    	
    	
    	
//        ACO algo = new ACO(ANTS,STEPS);
//
//        System.out.println("### Teste kleine unvollstaendige Graphen ###");
//    	int[][] distancematrix1 = {{0,-1,2},{5,0,7},{5,5,0}}; //Lsg: 0 2 1 0  L: 12
//    	int[][] distancematrix2 = {{0,-1,-1},{5,0,7},{5,5,0}}; //Kein Pfad
//   		int[][] distancematrix3 = {{0,-1,15},{5,0,1},{1,15,0}}; //Lsg: 0 2 1 0  L: 35
//   		IOManager io;
//    			
//        IGraph graph = GraphFactory.createDiGraph(distancematrix1);
//        List<Integer> path = algo.shortestPath(graph, 0);
//        System.out.println(graph.getPathLength(path) + " " + path + " | Lsg: 0 2 1 0  L: 12");
//        
//        graph = GraphFactory.createDiGraph(distancematrix2);
//        path = algo.shortestPath(graph, 0);
//        System.out.println(graph.getPathLength(path) + " " + path + " | Lsg: Kein Pfad");
//        
//        graph = GraphFactory.createDiGraph(distancematrix3);
//        path = algo.shortestPath(graph, 0);
//        System.out.println(graph.getPathLength(path) + " " + path + " | Lsg: 0 2 1 0  L: 35");
//       
//        System.out.println("### Teste grossen unvollstaendigen Graphen ohne bekannte Loesung ###");
//        graph = GraphFactory.createDiGraph(IOManager.RandomDistanceMatrix(1338, 200, 200, 0.1));
//        path = algo.shortestPath(graph, 0);
//        System.out.println(graph.getPathLength(path) + " " + path + " | Lsg: Unbekannt");
//        
//        System.out.println("### Teste kleinen unvollstaendigen Graphen mit Loesung ###");
//        io = new IOManager("src/digraph/digraph.txt"); //LSG 0 1 2 0 L:9
//        graph = GraphFactory.createDiGraph(io.readDigraphMatrixFromUniFile());
//        path = algo.shortestPath(graph, 0);
//        System.out.println(graph.getPathLength(path) + " " + path + " | Lsg: 0 1 2 0  L: 9");
//        
//        System.out.println("### Teste kleinen unvollstaendigen Graphen anhand BruteForce ###");
//        path = BruteForce.shortestPath(graph, 0);
//        System.out.println(graph.getPathLength(path) + " " + path + " | Lsg: 0 1 2 0  L: 9");
//        
//        System.out.println("### Teste grossen unvollstaendigen Graphen aus Uni File ###");
//        io = new IOManager("src/atsp/ftv170.atsp"); 
//        graph = GraphFactory.createDiGraph(io.readDigraphMatrixFromUniFile());
//        path = algo.shortestPath(graph, 0);
//        System.out.println(graph.getPathLength(path) + " " + path + " | L: 2755");   
//        
//        System.out.println("### Teste kleinen ungerichteten vollstaendigen Graphen aus Uni File ###");
//        io = new IOManager("src/tsp/gr24.tsp"); 
//        graph = GraphFactory.createGraph(io.readGraphMatrixFromUniFile());
//        
//        path = algo.shortestPath(graph, 0);
//        System.out.println(graph.getPathLength(path) + " " + path + " | ");          
//        
//        System.out.println("### Teste symmetrische ###");
//        io = new IOManager("src/simGraph("+ GRAPH +").txt");
//        graph = GraphFactory.createGraph(io.readGraphMatrixFromSimFile());
//        int bestSolution = io.readBestSolution();
//        int totaldiff = 0;
//        int fails = 0;
//        int maxdiff = 0;
//        for(int i = 0; i < RUNS; ++i) {
//            System.out.print(i+") ");
//            path = algo.shortestPath(graph, 0);
//            int w = graph.getPathLength(path);
//            int diff = w-bestSolution;
//            System.out.println("Wegl�nge: "+ w + "("+ (diff)+")");
//            maxdiff = Math.max(maxdiff, diff);
//            totaldiff += diff;
//            if (diff > 0)
//                ++fails;
//        }
//        System.out.println("-------------------------------------");
//        System.out.println("Erfolgsquote:     " + String.format("%.2f",(RUNS-fails)*100.0/RUNS) + "%   (" + fails + " Fehler in " + RUNS + " Durchl�ufen)");
//        System.out.println("Gesamtdifferenz:  " + totaldiff);
//        System.out.println("Gr��te Differenz: " + maxdiff);
//        System.out.println("Durchschntl.Diff: " + String.format("%.2f", (((double)totaldiff)/RUNS)));
//        System.out.println("-------------------------------------");
    }
}