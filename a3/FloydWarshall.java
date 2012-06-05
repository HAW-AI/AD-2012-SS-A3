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
public class FloydWarshall
{
    private final IGraph g;
    private final List<Integer> vertices;

    private Map<Integer, Map<Integer, Double>> d;
    private Map<Integer, Map<Integer, Integer>> t;

    public static class NegativeCircleFoundException extends RuntimeException
    {};

    public FloydWarshall(IGraph g)
    {
        this.g = g;
        this.vertices = new ArrayList<Integer>(g.getVertices());

        initializeDMatrix();
        initializeTMatrix();
        calculateShortestPaths();
    }

    public List<Integer> getShortestPath(int source, int target)
    {
        if (this.d.get(source).get(target) == Double.POSITIVE_INFINITY)
            return null;

        Integer intermediate = this.t.get(source).get(target);
        List<Integer> result = new LinkedList();

        if (intermediate == null)
        {
            result.add(0, target);
            result.add(0, source);
        }
        else
        {   
            result.addAll(getShortestPath(source, intermediate));

            List<Integer> temp = getShortestPath(intermediate, target);
            temp.remove(0);
            result.addAll(temp);
        }

        return result;
    }

    private void initializeDMatrix()
    {
        this.d = new HashMap<Integer, Map<Integer, Double>>();
        for (Integer row : this.vertices)
        {
            this.d.put(row, new HashMap<Integer, Double>());

            for (Integer col : this.vertices)
            {
                Double value = null;

                if (col.equals(row))
                    value = 0d;

                else if (this.g.edgeWeight(row, col) != 0)
                    value = (double)this.g.edgeWeight(row, col);

                else
                    value = Double.POSITIVE_INFINITY;

                this.d.get(row).put(col, value);
            }
        }
    }

    private void initializeTMatrix()
    {
        this.t = new HashMap<Integer, Map<Integer, Integer>>();
        for (Integer row : this.vertices)
        {
            this.t.put(row, new HashMap<Integer, Integer>());

            for (Integer col : this.vertices)
                this.t.get(row).put(col, null);
        }
    }

    private void calculateShortestPaths()
    {
        for (int j : this.vertices)
        {
            for (int i : this.vertices)
            {
                if (i != j)
                {
                    for (int k : this.vertices)
                    {
                        if (k != j)
                        {
                            Double new_value = this.d.get(i).get(j) + this.d.get(j).get(k);
                            if (new_value < this.d.get(i).get(k))
                            {
                                this.d.get(i).put(k, new_value);
                                this.t.get(i).put(k, j);
                            }
                        }
                    }

                    if (this.d.get(i).get(i) < 0)
                    {
                        System.out.println("Zyklische Ecke: " + i);
                        throw new NegativeCircleFoundException();
                    }
                }
            }
        }
    }
}


