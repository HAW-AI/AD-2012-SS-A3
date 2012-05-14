package graph;
public final class GraphFactory {
	private GraphFactory() {
	}

	/**
	 * Erstellt einen Graphen aus einer Distanzmatrix
	 * 
	 * @preConditions alle Werte der Matrix x | x > 0 <br>
	 *                distanceMatrix muss quadratisch sein <br>
	 *                distanceMatrix muss symmetrisch sein <br>
	 *                alle Werte der Hauptdiagonalen m�ssen den Wert 0 besitzen <br>
	 *                ansonsten wird ein normalisierter Graph der Größe 3
	 *                erstellt
	 */
	public static IGraph createGraph(int[][] distanceMatrix) {
		// Prüfung ob gültige Arrays erstellt werden!!!
		// Es muss eine quadratische Matrix sein
		if (distanceMatrix.length != distanceMatrix[0].length)
			return createNormalizedGraph(3);

		// Alle werte m�ssen positiv sein und die Matrix muss symmetrisch sein
		for (int i = 0; i < distanceMatrix.length; i += 1)
			for (int j = i; j < distanceMatrix.length; j += 1)
				if (distanceMatrix[i][j] < 0 || distanceMatrix[i][j] != distanceMatrix[j][i] || (i == j && distanceMatrix[i][j] != 0))
					return createNormalizedGraph(3);

		return new GraphImpl(distanceMatrix);
	}

	/**
	 * Erstellt einen symmetrischen Graphen mit zuf�llig generierten
	 * Kantengewichtung im Bereich von 1-100
	 * 
	 * @preConditions es muss gelten: numberOfVertices >= 3 <br>
	 *                ansonsten wird 3 als Standardwert angenommen
	 */
	public static IGraph createRandomGraph(int numberOfVertices) {
		// Pr�fung auf g�ltige Werten, ansonsten Standardwert
		if (numberOfVertices < 3)
			numberOfVertices = 3;

		int[][] distanceMatrix = new int[numberOfVertices][numberOfVertices];

		for (int i = 0; i < numberOfVertices; i += 1)
			for (int j = i; j < numberOfVertices; j += 1) {
				if (i == j)
					distanceMatrix[i][j] = 0;
				else {
					distanceMatrix[i][j] = (int) (Math.random() * 100) + 1;
					distanceMatrix[j][i] = distanceMatrix[i][j];
				}
			}

		return new GraphImpl(distanceMatrix);
	}

	/**
	 * Erstellt symmetrischen Graphen mit einer konstanten Kantengewichtung <br>
	 * Die Gewichtung wird durch die Differenz zweier Knoten bestimmt <br>
	 * z.B. distance(1,2) = 1; distance(1,4) = 3; etc.
	 * 
	 * @preConditions es muss gelten: numberOfVertices >= 3 <br>
	 *                ansonsten wird 3 als Standardwert angenommen
	 */
	public static IGraph createNormalizedGraph(int numberOfVertices) {
		// Pr�fung auf g�ltige Werten, ansonsten Standardwert
		if (numberOfVertices < 3)
			numberOfVertices = 3;

		int[][] distanceMatrix = new int[numberOfVertices][numberOfVertices];

		for (int i = 0; i < numberOfVertices; i += 1)
			for (int j = i; j < numberOfVertices; j += 1) {
				distanceMatrix[i][j] = j - i;
				distanceMatrix[j][i] = distanceMatrix[i][j];
			}

		return new GraphImpl(distanceMatrix);
	}
}
