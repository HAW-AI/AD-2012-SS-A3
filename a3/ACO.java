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
	
	public List<Integer> shortestPath(IGraph graph, int start) {
		int stepcount = 0;
                int shortestStep = 0;
		List<IAnt> ants = new ArrayList<>();
		double[][] dufts = new double[graph.getNumberOfVertices()][graph.getNumberOfVertices()];
                resetPheroMatrix(dufts, 1);
		List<Integer> shortestPath = null;
                int shortestPathLength = Integer.MAX_VALUE;
                double[][] tempPheroMat = new double[dufts.length][dufts.length];
		while (stepcount < steps) {
                    if(ants.size() < maxAnts) {
                        int times = Math.min(maxAnts-ants.size(),SPAWN_RATE);
                        for(int i = 0; i < times;++i)
                            ants.add(new Ant(start));
                    }
                    for(IAnt ant : ants) {
                        if(ant.visitedVertices() == graph.getNumberOfVertices()) {
                            ant.moveTo(start);
                            int pathlength = getPathLength(ant.getPath(), graph);
                            markPath(tempPheroMat, ant.getPath(), 1.0/pathlength);
                            
                            
                            if (pathlength < shortestPathLength) {
                                shortestStep = stepcount;
                                shortestPathLength = pathlength;
                                shortestPath = ant.getPath();
                            }
                            ant.reset();
                        } else {
                            int target = choosePath(ant, getPossibleWays(graph, ant, dufts));
                            ant.moveTo(target);
                        }
                    }
                    vaporize(dufts, tempPheroMat);
                    resetPheroMatrix(tempPheroMat, 0);
                    ++stepcount;
		}
                System.out.print("Step: " + shortestStep+ " ");
		return shortestPath;
	}
        
        private void resetPheroMatrix(double[][] mat, double val) {
            for(int i = 0; i < mat.length; ++i) 
			for(int j = 0; j < mat.length; ++j) 
				mat[i][j] = val;            
        }
	
	private Map<Integer, Double> getPossibleWays(IGraph graph, IAnt ant, double[][] pheroMat) {
		Map<Integer, Double> possibleWays = new HashMap<Integer, Double>();
		double sumKram = 0;
		for(int vertex=0;vertex < graph.getNumberOfVertices();++vertex) {
			if(!ant.hasVisited(vertex)) {
				double ret = f(graph.getEdgeWeighting(ant.currentPosition(),vertex),pheroMat[ant.currentPosition()][vertex]);
				sumKram += ret;
				possibleWays.put(vertex,ret);
			}
		}
		for(int vertex : possibleWays.keySet()) {
			possibleWays.put(vertex,possibleWays.get(vertex)/sumKram/*epic name*//**/);
		}
		return possibleWays;
	}
	
	private int choosePath(IAnt ant, Map<Integer,Double> possibleWays) {
		double currentSum = 0;
		double rand = this.rand.nextDouble();
		for(int vertex : possibleWays.keySet()) {
			if((currentSum+=possibleWays.get(vertex)) >= rand) {
				return vertex;
			}
		}
		return 0;	//never reached
	}
	
	private double[][] vaporize(double[][] pheroMatrix, double[][] tempPheroMat) {
		for(int x = 0; x < pheroMatrix.length; ++x)
			for(int y = 0; y < pheroMatrix.length; ++y)
				pheroMatrix[x][y] = pheroMatrix[x][y]*(1-VAPORIZE_RATE) + tempPheroMat[x][y];
		return pheroMatrix;
	}
	
	private double[][] markPath(double[][] pheroMatrix, List<Integer> path, double value/*to Increment*/) {
		for(int i = 0; i < path.size()-1; ++i) {
			pheroMatrix[path.get(i)][path.get(i+1)] += value;
		}
		return pheroMatrix;
	}
	
	int getPathLength(List<Integer> path, IGraph graph) {
		int wayLength = 0;
		for(int i = 0; i < path.size()-1; ++i) {
			wayLength += graph.getEdgeWeighting(path.get(i), path.get(i+1));
		}
		return wayLength;
	}
	
	public double f(int weight, double phero) {
		return Math.pow(weight,-1/*-2*/)*Math.pow(phero,1);
	}
}