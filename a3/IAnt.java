package a3;

import java.util.ArrayList;
import java.util.List;

public interface IAnt {
	
	List<Integer> getPath();
	int numberOfVisitedVertices();
	boolean hasVisited(int vertex);
	void moveTo(int vertex);
	int currentPosition();
	void reset();

}

class Ant implements IAnt {
	
	private List<Integer> path;
	
	Ant(Integer start) {
		path = new ArrayList<Integer>();
		path.add(start);
	}
	
	@Override
	public int numberOfVisitedVertices() {
		return path.size();
	}

	@Override
	public List<Integer> getPath() {
		return new ArrayList<Integer>(path);
	}
	
	@Override
	public boolean hasVisited(int vertex) {
		return path.contains(vertex);
	}

	@Override
	public void moveTo(int vertex) {
		//System.out.println(path.get(path.size()-1) +" -> "+vertex);
		path.add(vertex);
	}

	@Override
	public int currentPosition() {
		return path.get(path.size()-1);
	}

	@Override
	public void reset() {
		Integer temp = path.get(0);
		path.clear();
		path.add(temp);
	}
	
}
