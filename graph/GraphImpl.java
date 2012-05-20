package graph;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GraphImpl implements IGraph {

	private final int[] distanceMatrix;

	GraphImpl(int[] distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
	}

	@Override
	public int edgeWeight(int vert1, int vert2) {
		// Prï¿½fung auf gï¿½ltigen Wertebereich
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
		return (int) Math.sqrt(distanceMatrix.length);
	}
	
	@Override
	public Set<Integer> reachableAdjacencyVerticesOf(int vertice) {
		Set<Integer> reachable = new HashSet<Integer>();
		int size = getNumberOfVertices();
		for (int i = 0; i < size; i++){
			int check = distanceMatrix[(size * vertice + i)];
			if(check > 0 && vertice != i)
					reachable.add(i);
			}
		return reachable;
	}

}