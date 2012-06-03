package graph;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementierung des Grapheninterfaces.
 */
public class GraphImpl implements IGraph {

	private final int[] distanceMatrix;
	private int[] capacities;

	/**
     * Konstruktor eines Graphen mit Uebergabe der Distanzmatrix und der
		Eckenkapazitäten
     * @precondition Die Anzahl der Elemente der Matrix muss quadratisch sein.
     * @precondition Die Anzahl der Elemente der Kapazitäten muss der
		Eckenanzahl entsprechen.
     * @param distanceMatrix
     * @param capacities
     */
    GraphImpl(int[] distanceMatrix, int[] capacities) {
            this.distanceMatrix = distanceMatrix;
            this.capacities = capacities;
    }

    @Override
    public int getCapacityOfVertex(int vertex) {
            return this.capacities[vertex];
    }

    @Override
    public boolean customersLeft() {
            for (int capacity : capacities) {
                    if (capacity > 0)
                            return true;
            }
            return false;
    }

	@Override
	public int edgeWeight(int vert1, int vert2) {
		// Pruefung auf gueltigen Wertebereich
		int size = getNumberOfVertices();
		if ((vert1 < 0) || (vert2 < 0) || (vert1 >= size) || (vert2 >= size))
			return 0;
		else
			return distanceMatrix[(vert1 * size + vert2)];
	}

	@Override
	public String toString() {
		String s = "";
		int size = getNumberOfVertices();
		for (int i = 0; i < size; i += 1)
			for (int j = 0; j < size; j += 1) {

				if (j == size - 1)
					s += distanceMatrix[(size * i + j)] + "]\n";
				else if (j == 0)
					s += "[" + distanceMatrix[(size * i + j)] + ", ";
				else
					s += distanceMatrix[(size * i + j)] + ", ";
			}
		return s;

	}

	@Override
	public int hashCode() {
		return 31 + Arrays.hashCode(distanceMatrix);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphImpl other = (GraphImpl) obj;
		if (!Arrays.equals(distanceMatrix, other.distanceMatrix))
			return false;
		return true;
	}

	@Override
	public int getNumberOfVertices() {
		return capacities.length;
	}
	
	@Override
	public Set<Integer> reachableAdjacencyVerticesOf(int vertice) {
		Set<Integer> reachable = new HashSet<Integer>();
		int size = getNumberOfVertices();
		for (int i = 0; i < size; i++){
			int check = distanceMatrix[(size * vertice + i)];
			if(check != NON_EXISTING_EDGE && vertice != i)
					reachable.add(i);
			}
		return reachable;
	}

	@Override
	public int getPathLength(List<Integer> path) {
		int wayLength = 0;
		if(path.size() > 0 && path.get(0) >= getNumberOfVertices()) return 0;
		for (int i = 0; i < path.size() - 1; ++i) {
			int check = path.get(i + 1);
			if(check >= getNumberOfVertices()) return 0;
			wayLength += edgeWeight(path.get(i), check);
		}
		return wayLength;
	}

}