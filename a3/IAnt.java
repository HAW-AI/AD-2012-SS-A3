package a3;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface einer Ameise für den Ant Colony Optimization Algorithmus.
 */
public interface IAnt {
	/**
	 * Gibt den derzeitigen Weg der Ameise zurück.
	 * @return Liste aus Indizes der Ecken als Weg
	 */
	List<Integer> getPath();
	
	/**
	 * Gibt die Anzahl der Pakete zurück, die die Ameise tragen kann.
	 * @return Anzahl der Pakete, die die Ameise trägt
	 */
	int getLoad();
	
	/**
	 * Zieht Pakete von der Ladung der Ameise ab.
	 * @param decreaseValue		Kapazität einer Ecke, die beliefert werden soll
	 */
	void decreaseLoad(int decreaseValue);
	
	/**
	 * Gibt die Anzahl der besuchten Ecken zurück.
	 * @return Anzahl der besuchten Ecken
	 */
	int numberOfVisitedVertices();
	
	/**
	 * Liefert den boolschen Wert zurück, ob die gegebene Ecke bereits besucht wurde oder nicht.
	 * @param vertex 	Index einer Ecke, die überprüft werden soll
	 * @return true, falls die Ecke bereits besucht wurde oder false, sonst
	 */
	boolean hasVisited(int vertex);
	
	/**
	 * Bewegt die Ameise zu der gegebenen Ecke.
	 * @param vertex  	Index einer Ecke
	 */
	void moveTo(int vertex);
	
	/**
	 * Liefert den Index der aktuellen Ecke zurück.
	 * @return Index der Ecke, auf der sich die Ameise derzeitig befindet.
	 */
	int currentPosition();
	
	/**
	 * Setzt die Ameise auf die Startecke zurück.
	 */
	void reset();

}

/**
 * Implementierung des Ameiseninterfaces.
 */
class Ant implements IAnt {
	
	private List<Integer> path;
	private int load;
	
	/**
	 * Konstuktor einer Ameise mit Übergabe der Startecke.
	 * @param start		Index einer Ecke
	 */
	Ant(Integer start, int load) {
		path = new ArrayList<Integer>();
		path.add(start);
		this.load = load;
	}
	
	@Override
	public int getLoad() {
		return load;
	}
	
	@Override
	public void decreaseLoad(int decreaseValue) {
		load -= decreaseValue;
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
