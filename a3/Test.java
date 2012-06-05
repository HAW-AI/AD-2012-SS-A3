package a3;

import java.util.List;

public class Test {
	
    private final static int STEPS = 500;
    private final static int ANTS = 1;
    
    public static void main(String ... args) {

        List<List<Integer>> path;
        
    	int[] distanceMatrix1 = {0,1,1,1,0,1,1,1,0};
    	int[] eckenkapazitaet1 = {0,1,1};
    	//Kombination distancematrix1, eckenkapazitaet1:
    	//Loesung bei einer Ameisenkapazitaet von >= 2: {{0 1 2 0}} || {{0 2 1 0}}  L: 3
    	//Loesung bei einer Ameisenkapazitaet von 1: {{0 1 0},{0 2 0}}  L: 4
    	
        IGraph graph1 = new GraphImpl(distanceMatrix1, eckenkapazitaet1);
        CVRP test1 = new CVRP(graph1, 0);

        System.out.println("Graph 1, Kapazität 1");        
    	path = test1.shortestPath(ANTS, 1, STEPS);
    	int gesLaenge = 0;
    	for(List<Integer> list : path){
    		gesLaenge += graph1.getPathLength(list);
    	}
    	System.out.println(gesLaenge + " " + path + " | Loesung: {{0 1 0},{0 2 0}}  L: 4");


        System.out.println("Graph 1, Kapazität 3");
    	path = test1.shortestPath(ANTS, 3, STEPS);
    	gesLaenge = 0;
    	for(List<Integer> list : path){
    		gesLaenge += graph1.getPathLength(list);
    	}
    	System.out.println(gesLaenge + " " + path + " | Loesung: {{0 1 2 0}}  L: 3");

        
        IOManager iOManager = new IOManager("src/a3/file21.txt");
        try {
			IGraph graphT = new GraphImpl(iOManager.getEdgeWeightSection(), iOManager.getDemandSection());
			test1 = new CVRP(graphT, 0);
	        System.out.println("Graph T, Kapazität " + iOManager.getAntCapacity());
			path = test1.shortestPath(ANTS, iOManager.getAntCapacity(), STEPS);
	    	gesLaenge = 0;
	    	for(List<Integer> list : path){
	    		gesLaenge += graphT.getPathLength(list);
	    	}
	    	System.out.println(gesLaenge + " " + path);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        
        
    	int[] distanceMatrix2 = {0,1,3,5,0,3,3,2,0};
    	int[] eckenkapazitaet2 = {0,2,3};
    	//Kombination distancematrix2, eckenkapazitaet2:
    	//Loesung bei einer Ameisenkapazitaet von >= 5: {{0 1 2 0}}  L: 7
    	//Loesung bei einer Ameisenkapazitaet von 3: {{0 1 0},{0 2 0}}  L: 12
        
        IGraph graph2 = new GraphImpl(distanceMatrix2, eckenkapazitaet2);
        CVRP test2 = new CVRP(graph2, 0);
        
        System.out.println("Graph 2, Kapazität 5");
        path = test2.shortestPath(ANTS, 5, STEPS);
    	gesLaenge = 0;
    	for(List<Integer> list : path){
    		gesLaenge += graph2.getPathLength(list);
    	}
    	System.out.println(gesLaenge + " " + path + " | Loesung: {{0 1 2 0}}  L: 7");
    	
        
        System.out.println("Graph 2, Kapazität 3");
        path = test2.shortestPath(ANTS, 3, STEPS);
    	gesLaenge = 0;
    	for(List<Integer> list : path){
    		gesLaenge += graph2.getPathLength(list);
    	}
    	System.out.println(gesLaenge + " " + path + " | Loesung: {{0 1 0},{0 2 0}}  L: 12");        
    	
        
        
        
        
        
    	int[] distanceMatrix3 = {0,-1,2,5,0,7,5,5,0};
    	int[] eckenkapazitaet3 = {0,2,3};
    	//Kombination distancematrix3, eckenkapazitaet3:
    	//Loesung bei einer Ameisenkapazitaet von >= 5: {{0 2 1 0}} L: 12
    	//Loesung bei einer Ameisenkapazitaet von 3 oder 4: {{0 2 0},{0 2 1 0}}  L: 19
        
        
        IGraph graph3 = new GraphImpl(distanceMatrix3, eckenkapazitaet3);
        CVRP test3 = new CVRP(graph3, 0);
        
        System.out.println("Graph 3, Kapazität 3");
    	path = test3.shortestPath(ANTS, 3, STEPS);
    	gesLaenge = 0;
    	for(List<Integer> list : path){
    		gesLaenge += graph3.getPathLength(list);
    	}
    	System.out.println(gesLaenge + " " + path + " | Loesung: {{0 2 0},{0 2 1 0}}  L: 19");
    	
        System.out.println("Graph 3, Kapazität 3");
    	path = test3.shortestPath(ANTS, 5, STEPS);
    	gesLaenge = 0;
    	for(List<Integer> list : path){
    		gesLaenge += graph3.getPathLength(list);
    	}
    	System.out.println(gesLaenge + " " + path + " | Loesung: {{0 2 1 0}} L: 12");
    	
    	
//    	int[] distanceMatrix4 = {0,1,-1,2,0,3,-1,4,0};
//    	int[] eckenkapazitaet4 = {0,3,5};
//    	//Kombination distancematrix4, eckenkapazitaet4:
//    	//Loesung bei einer Ameisenkapazitaet von >= 8: {{0 1 2 0}}  L: 10
//    	//Loesung bei einer Ameisenkapazitaet von 5,6 oder 7 : {{0 1 0},{0 1 2 1 0}}  L: 13
//    	graph = new GraphImpl(distanceMatrix4, eckenkapazitaet4);
//    	path = cvrp6.shortestPath(graph, 0);
//    	gesLaenge = 0;
//    	for(List<Integer> list : path){
//    		gesLaenge += graph.getPathLength(list);
//    	}
//    	System.out.println(gesLaenge + " " + path + " | {{0 1 0},{0 1 2 1 0}}  L: 13");
//    	
//    	
//       	int[] distanceMatrix5 = {0,1,5,5,0,2,1,-1,0};
//    	int[] eckenkapazitaet5 = {0,1,3};
//    	//Kombination distancematrix5, eckenkapazitaet5:
//    	//Loesung bei einer Ameisenkapazitaet von >= 4: {{0 1 2 0}}  L: 4 
//    	//Loesung bei einer Ameisenkapazitaet von 3: {{0 1 2 0},{0 2 0}}  L: 10
//    	graph = new GraphImpl(distanceMatrix5, eckenkapazitaet5);
//    	path = cvrp3.shortestPath(graph, 0);
//    	gesLaenge = 0;
//    	for(List<Integer> list : path){
//    		gesLaenge += graph.getPathLength(list);
//    	}
//    	System.out.println(gesLaenge + " " + path + " | {{0 1 2 0},{0 2 0}}  L: 10");
//    	
//    	
//    	
//       	int[] distanceMatrix6 = {0,2,-1,1,1,0,-1,-1,7,1,0,7,-1,-1,1,0};
//    	int[] eckenkapazitaet6 = {0,3,1,1};
//    	//Kombination distancematrix6, eckenkapazitaet6:
//    	//Loesung bei einer Ameisenkapazitaet von >= 5: {{0 3 2 1 0}}  L: 4
//    	//Loesung bei einer Ameisenkapazitaet von 3 : {{0 1 0},{0 3 2 1 0}}  L: 7 
//    	graph = new GraphImpl(distanceMatrix6, eckenkapazitaet6);
//    	path = cvrp3.shortestPath(graph, 0);
//    	gesLaenge = 0;
//    	for(List<Integer> list : path){
//    		gesLaenge += graph.getPathLength(list);
//    	}
//    	System.out.println(gesLaenge + " " + path + " |  {{0 1 0},{0 3 2 1 0}}  L: 7");
 
    }        
}