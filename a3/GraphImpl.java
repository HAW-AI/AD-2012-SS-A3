package a3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementierung des Grapheninterfaces.
 */
public class GraphImpl implements IGraph
{
    private final int[] distanceMatrix;
    private final int[] demand;
    private final int numberOfVertices;
    
    /**
     * Konstruktor eines Graphen mit Uebergabe der Distanzmatrix und der
     * Eckenkapazitäten @precondition Die Anzahl der Elemente der Matrix muss
     * quadratisch sein. @precondition Die Anzahl der Elemente der Kapazit�ten
     * muss der Eckenanzahl entsprechen.
     *
     * @param distanceMatrix
     * @param capacities
     */
    public GraphImpl(int[] distanceMatrix, int[] demandList)
    {
        this.distanceMatrix = distanceMatrix;
        this.demand = demandList;
        this.numberOfVertices = (int)Math.sqrt(distanceMatrix.length);
    }

    @Override
    public int getDemandOfCustomer(int vertex)
    {
        if (vertex < 0 || vertex >= this.demand.length)
            return 0;

        return this.demand[vertex];
    }

    @Override
    public int edgeWeight(int vert1, int vert2)
    {
        // Pruefung auf gueltigen Wertebereich
        int size = getNumberOfVertices();
        if ((vert1 < 0) || (vert2 < 0) || (vert1 >= size) || (vert2 >= size))
        {
            return 0;
        } else
        {
            return distanceMatrix[(vert1 * size + vert2)];
        }
    }

    @Override
    public String toString()
    {
        String s = "";
        int size = getNumberOfVertices();
        for (int i = 0; i < size; i += 1)
        {
            for (int j = 0; j < size; j += 1)
            {

                if (j == size - 1)
                {
                    s += distanceMatrix[(size * i + j)] + "]\n";
                } else
                {
                    if (j == 0)
                    {
                        s += "[" + distanceMatrix[(size * i + j)] + ", ";
                    } else
                    {
                        s += distanceMatrix[(size * i + j)] + ", ";
                    }
                }
            }
        }

        return s;
    }

    @Override
    public int hashCode()
    {
        return 31 + Arrays.hashCode(distanceMatrix);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        GraphImpl other = (GraphImpl) obj;
        if (!Arrays.equals(distanceMatrix, other.distanceMatrix))
        {
            return false;
        }
        return true;
    }

    
    @Override
    public int getNumberOfVertices()
    {
    	return numberOfVertices;
    }
    
    @Override
    public Set<Integer> getCustomers()
    {
        Set customers = new HashSet<Integer>();
        for (int i = 0; i < getNumberOfVertices(); i++)
        {
            if (this.demand[i] > 0)
                customers.add(i);
        }

        return customers;
    }
    
    @Override
    public Set<Integer> getVertices()
    {
        Set vertices = new HashSet<Integer>();
        for (int i = 0; i < getNumberOfVertices(); i++)
        {
            vertices.add(i);
        }

        return vertices;
    }

    @Override
    public Set<Integer> getNeighboursOf(int vertice)
    {
        Set<Integer> reachable = new HashSet<Integer>();
        int size = getNumberOfVertices();
        for (int i = 0; i < size; i++)
        {
        	try {
        		int check = distanceMatrix[(size * vertice + i)];
                if (check != NON_EXISTING_EDGE && vertice != i)
                {
                    reachable.add(i);
                }
        	} catch (ArrayIndexOutOfBoundsException e) {
        		throw new ArrayIndexOutOfBoundsException(e.getMessage() + " while reading distanceMatrix(size = " + size + ", vertice = " + vertice + ", i = " + i + "), its size is " + size);
        	}
        }
        return reachable;
    }

    @Override
    public int getPathLength(List<Integer> path)
    {
        int wayLength = 0;
        for (int i = 0; i < path.size() - 1; ++i)
        {
            int check = path.get(i + 1);
            if (check >= getNumberOfVertices())
            {
                return 0;
            }
            wayLength += edgeWeight(path.get(i), check);
        }
        return wayLength;
    }
}