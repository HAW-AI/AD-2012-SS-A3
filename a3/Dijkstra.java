/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package a3;

import java.util.*;


/**
 *
 * @author maiwald
 */
public class Dijkstra {
    
    private final IGraph g;
    private final int source;

    private Set<Integer> remaining = new HashSet();

    private Map<Integer,Double> distances = new HashMap();
    private Map<Integer,Integer> predecessors = new HashMap();

    public int counter = 0;

    public Dijkstra(IGraph g, int source)
    {
        this.g = g;
        this.source = source;

        calculateShortestPaths();
    }

    public List<Integer> getShortestPathHome(int source)
    {
        List<Integer> path = new LinkedList();        

        int tmp = source;
        while (this.predecessors.get(tmp) != null) 
        {
            tmp = this.predecessors.get(tmp);
            path.add(tmp);
        }

        return path;
    }

    private void calculateShortestPaths()
    {
        this.remaining = new HashSet<Integer>(this.g.getVertices());

        for (int elem : this.remaining)
            this.distances.put(elem, Double.POSITIVE_INFINITY);

        this.distances.put(this.source, 0d);

        while(!this.remaining.isEmpty())
        {
            int closest = getClosestVertex();
            Set<Integer> neighbors = g.reachableAdjacencyVerticesOf(closest);
            this.remaining.remove(closest);

            for (int neighbor : neighbors)
            {
                if (this.remaining.contains(neighbor))
                    updateDistance(closest, neighbor);
            }
        }
    }

    private int getClosestVertex()
    {
        return Collections.min(this.remaining, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return distances.get(a).compareTo(distances.get(b));
            }
        });
    }

    private void updateDistance(int source, int target)
    {
        this.counter++;

        double alternative = this.distances.get(source) + 
                this.g.edgeWeight(source, target);

        if (alternative < this.distances.get(target))
        {
            this.distances.put(target, alternative);
            this.predecessors.put(target, source);
        }
    }
}
