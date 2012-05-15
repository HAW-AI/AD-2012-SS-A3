package graph;
import java.util.Arrays;

public class GraphImpl implements IGraph {

	private final int[][] distanceMatrix;

	GraphImpl(int[][] distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
	}

	@Override
	public int edgeWeight(int vert1, int vert2) {
		// Pr�fung auf g�ltigen Wertebereich
		if (vert1 < 0 || vert2 < 0 || vert1 >= distanceMatrix.length || vert2 >= distanceMatrix.length)
			return 0;
		else
			return distanceMatrix[vert1][vert2];
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < distanceMatrix.length; i += 1) {
			s += Arrays.toString(distanceMatrix[i]);
			if (i != distanceMatrix.length - 1)
				s += "\n";
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
        return distanceMatrix.length;
    }

}
