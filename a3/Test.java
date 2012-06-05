package a3;

import java.util.List;

public class Test {
	
    private final static int STEPS = 500;
    private final static int ANTS = 5000;
    
    public static void main(String ... args) {

        List<List<Integer>> path;
        
    	int[] distanceMatrix1 = {0,1,1,1,0,1,1,1,0};
    	int[] eckenkapazitaet1 = {0,1,1};
    	//Kombination distancematrix1, eckenkapazitaet1:
    	//Loesung bei einer Ameisenkapazitaet von >= 2: {{0 1 2 0}} || {{0 2 1 0}}  L: 3
    	//Loesung bei einer Ameisenkapazitaet von 1: {{0 1 0},{0 2 0}}  L: 4
    	
        IGraph graph1 = new GraphImpl(distanceMatrix1, eckenkapazitaet1);
        CVRP test1 = new CVRP(graph1, 0);

        System.out.println("Graph 1, Kapazitaet 1");        
    	path = test1.shortestPath(ANTS, 1, STEPS);
    	int gesLaenge = 0;
    	for(List<Integer> list : path){
    		gesLaenge += graph1.getPathLength(list);
    	}
    	System.out.println(gesLaenge + " " + path + " | Loesung: {{0 1 0},{0 2 0}}  L: 4");


        System.out.println("Graph 1, Kapazitaet 3");
    	path = test1.shortestPath(ANTS, 3, STEPS);
    	gesLaenge = 0;
    	for(List<Integer> list : path){
    		gesLaenge += graph1.getPathLength(list);
    	}
    	System.out.println(gesLaenge + " " + path + " | Loesung: {{0 1 2 0}}  L: 3");

        
        
        
        
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
    	
        System.out.println("Graph 3, Kapazitaet 5");
    	path = test3.shortestPath(ANTS, 5, STEPS);
    	gesLaenge = 0;
    	for(List<Integer> list : path){
    		gesLaenge += graph3.getPathLength(list);
    	}
    	System.out.println(gesLaenge + " " + path + " | Loesung: {{0 2 1 0}} L: 12");
    	
    	

    	int[] distanceMatrix4 = {0,1,-1,2,0,3,-1,4,0};
    	int[] eckenkapazitaet4 = {0,3,5};
    	//Kombination distancematrix4, eckenkapazitaet4:
    	//Loesung bei einer Ameisenkapazitaet von >= 8: {{0 1 2 0}}  L: 10
    	//Loesung bei einer Ameisenkapazitaet von 5,6 oder 7 : {{0 1 0},{0 1 2 1 0}}  L: 13
    	IGraph graph4 = new GraphImpl(distanceMatrix4, eckenkapazitaet4);
    	CVRP test4 = new CVRP(graph4, 0);
    	
    	System.out.println("Graph4, Kapazitaet 6");
    	path = test4.shortestPath(ANTS, 6, STEPS);
    	gesLaenge = 0;
    	for(List<Integer> list : path){
    		gesLaenge += graph4.getPathLength(list);
    	}
    	System.out.println(gesLaenge + " " + path + " | {{0 1 0},{0 1 2 1 0}}  L: 13");
    	
    	
    	
    	
       	int[] distanceMatrix5 = {0,1,5,5,0,2,1,-1,0};
    	int[] eckenkapazitaet5 = {0,1,3};
    	//Kombination distancematrix5, eckenkapazitaet5:
    	//Loesung bei einer Ameisenkapazitaet von >= 4: {{0 1 2 0}}  L: 4 
    	//Loesung bei einer Ameisenkapazitaet von 3: {{0 1 2 0},{0 2 0}}  L: 10
    	IGraph graph5 = new GraphImpl(distanceMatrix5, eckenkapazitaet5);
    	CVRP test5 = new CVRP(graph5,0);

    	System.out.println("Graph 5, Kapazitaet 3");
    	path = test5.shortestPath(ANTS, 3, STEPS);
    	gesLaenge = 0;
    	for(List<Integer> list : path){
    		gesLaenge += graph5.getPathLength(list);
    	}
    	System.out.println(gesLaenge + " " + path + " | {{0 1 2 0},{0 2 0}}  L: 10");
    	
    	
    	
       	int[] distanceMatrix6 = {0,2,-1,1,1,0,-1,-1,7,1,0,7,-1,-1,1,0};
    	int[] eckenkapazitaet6 = {0,3,1,1};
    	//Kombination distancematrix6, eckenkapazitaet6:
    	//Loesung bei einer Ameisenkapazitaet von >= 5: {{0 3 2 1 0}}  L: 4
    	//Loesung bei einer Ameisenkapazitaet von 3 : {{0 1 0},{0 3 2 1 0}}  L: 7 
    	IGraph graph6 = new GraphImpl(distanceMatrix6, eckenkapazitaet6);
    	CVRP test6 = new CVRP(graph6, 0);
    	
    	System.out.println("Graph 6, Kapazitaet 3");
    	path = test6.shortestPath(ANTS, 3, STEPS);
    	gesLaenge = 0;
    	for(List<Integer> list : path){
    		gesLaenge += graph6.getPathLength(list);
    	}
    	System.out.println(gesLaenge + " " + path + " |  {{0 1 0},{0 3 2 1 0}}  L: 7");

    	
    	
       	int[] distanceMatrix7 = {0,2,-1,1,1,0,-1,-1,7,1,0,7,-1,-1,1,0};
    	int[] eckenkapazitaet7 = {0,3,1,0};
    	//Kombination distancematrix7, eckenkapazitaet7:
    	//Loesung bei einer Ameisenkapazitaet von 3 : {{0 1 0},{0 3 2 1 0}}  L: 7 
    	IGraph graph7 = new GraphImpl(distanceMatrix7, eckenkapazitaet7);
    	CVRP test7 = new CVRP(graph7, 0);
    	
    	System.out.println("Graph 7, Kapazitaet 3");
    	path = test7.shortestPath(ANTS, 3, STEPS);
    	gesLaenge = 0;
    	for(List<Integer> list : path){
    		gesLaenge += graph6.getPathLength(list);
    	}
    	System.out.println(gesLaenge + " " + path + " |  {{0 1 0},{0 3 2 1 0}}  L: 7");
    	
    	
//-------------------------------------------------------------------------------------------------------------------    	
    	 
    	
		System.out.println("Tests ueber I/O-Manager: /n");

		/**
		 * so vorgefunden:
		 * */
		// IOManager iOManager = new IOManager("src/a3/file21.txt");
		// try {
		// IGraph graphT = new GraphImpl(iOManager.getEdgeWeightSection(),
		// iOManager.getDemandSection());
		// test1 = new CVRP(graphT, 0);
		// System.out.println("Graph T, Kapazitaet " +
		// iOManager.getAntCapacity());
		// path = test1.shortestPath(ANTS, iOManager.getAntCapacity(), STEPS);
		// gesLaenge = 0;
		// for(List<Integer> list : path){
		// gesLaenge += graphT.getPathLength(list);
		// }
		// System.out.println(gesLaenge + " " + path);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		IOManager ioFile3 = new IOManager("src/a3/file3.txt");

		IGraph graphFile3 = new GraphImpl(ioFile3.getEdgeWeightSection(),
				ioFile3.getDemandSection());
		test1 = new CVRP(graphFile3, 0);
		System.out.println("Graph3 Kapazitaet " + ioFile3.getAntCapacity());
		path = test1.shortestPath(ANTS, ioFile3.getAntCapacity(), STEPS);
		gesLaenge = 0;
		for (List<Integer> list : path) {
			gesLaenge += graphFile3.getPathLength(list);
		}
		System.out.println(gesLaenge + " " + path
				+ " | {{0 2 0},{0 2 1 0}}  L: 19");

		
		
		IOManager ioFile4 = new IOManager("src/a3/file4.txt");

		IGraph graphFile4 = new GraphImpl(ioFile4.getEdgeWeightSection(),
				ioFile4.getDemandSection());
		test1 = new CVRP(graphFile4, 0);
		System.out.println("Graph4  Kapazitaet " + ioFile4.getAntCapacity());
		path = test1.shortestPath(ANTS, ioFile4.getAntCapacity(), STEPS);
		gesLaenge = 0;
		for (List<Integer> list : path) {
			gesLaenge += graphFile4.getPathLength(list);
		}
		System.out.println(gesLaenge + " " + path + " | {{0 1 0},{0 3 2 1 0}}  L: 7");

		
		
		IOManager ioFile21 = new IOManager("src/a3/file21.txt");

		IGraph graphFile21 = new GraphImpl(ioFile21.getEdgeWeightSection(),
				ioFile21.getDemandSection());
		test1 = new CVRP(graphFile21, 0);
		System.out.println("Graph21  Kapazitaet " + ioFile21.getAntCapacity());
		path = test1.shortestPath(ANTS, ioFile21.getAntCapacity(), STEPS);
		gesLaenge = 0;
		for (List<Integer> list : path) {
			gesLaenge += graphFile21.getPathLength(list);
		}
		System.out.println(gesLaenge + " " + path + " | Unbekanntes Ergebnis");

		System.out.println();
		
		System.out.println("IO-Manager Test: es sollte die failMatrix = {0, 1, 2, 1, 0, 1, 1, 2, 0} zurueck kommen:");
		System.out.println("Fail 1: Eckenkapazitaet > Ameisenkapatitaet: ");
		IOManager ioFileFail1 = new IOManager("src/a3/failgraph1.txt");
		IGraph graphFailgraph1 = new GraphImpl(ioFileFail1.getEdgeWeightSection(), ioFileFail1.getDemandSection());
		System.out.println(graphFailgraph1);
		
		System.out.println("Fail 2: Sackgasse: ");
		IOManager ioFileFail2 = new IOManager("src/a3/failgraph2.txt");
		IGraph graphFailgraph2 = new GraphImpl(ioFileFail2.getEdgeWeightSection(), ioFileFail2.getDemandSection());
		System.out.println(graphFailgraph2);
		
		System.out.println("Fail 3: Unzusammenhaengender Graph: ");
		IOManager ioFileFail3 = new IOManager("src/a3/failgraph3.txt");
		IGraph graphFailgraph3 = new GraphImpl(ioFileFail3.getEdgeWeightSection(), ioFileFail3.getDemandSection());
		System.out.println(graphFailgraph3);
		
	}       
}