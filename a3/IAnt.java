package a3;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface einer Ameise f�r den Ant Colony Optimization Algorithmus.
 */
public interface IAnt {
	/**
	 * Gibt den derzeitigen Weg der Ameise zur�ck.
	 * @return Liste aus Indizes der Ecken als Weg
	 */
	List<Integer> getPath();
	
	/**
	 * Gibt die Anzahl der besuchten Ecken zur�ck.
	 * @return Anzahl der besuchten Ecken
	 */
	int numberOfVisitedVertices();
	
	/**
	 * Liefert den boolschen Wert zur�ck, ob die gegebene Ecke bereits besucht wurde oder nicht.
	 * @param vertex 	Index einer Ecke, die �berpr�ft werden soll
	 * @return true, falls die Ecke bereits besucht wurde oder false, sonst
	 */
	boolean hasVisited(int vertex);
	
	/**
	 * Bewegt die Ameise zu der gegebenen Ecke.
	 * @param vertex  	Index einer Ecke
	 */
	void moveTo(int vertex);
	
	/**
	 * Liefert den Index der aktuellen Ecke zur�ck.
	 * @return Index der Ecke, auf der sich die Ameise derzeitig befindet.
	 */
	int currentPosition();
	
	/**
	 * Setzt die Ameise auf die Startecke zur�ck.
	 */
	void reset();

}

/**
 * Implementierung des Ameiseninterfaces.
 */
class Ant implements IAnt {
	
	private List<Integer> path;
	
	/**
	 * Konstuktor einer Ameise mit �bergabe der Startecke.
	 * @param start		Index einer Ecke
	 */
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
