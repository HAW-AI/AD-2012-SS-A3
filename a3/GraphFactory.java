package a3;

import java.util.Random;

/**
 * Klasse zur Erstellung von Graphen aus Distanzmatrizen oder mit Zufallswerten.
 */
public abstract class GraphFactory {

    /**
     * Erstellt einen Graphen aus einer Distanzmatrix..
     * @preCondition alle Werte der Matrix x | x > 0
     * @preCondition distanceMatrix muss quadratisch sein
     * @preCondition distanceMatrix muss symmetrisch sein
     * @preCondition alle Werte der Hauptdiagonalen m�ssen den Wert 0 besitzen
     * @param distanceMatrix	Distanzmatrix
     * @return erzeugter Graph (normalisierter Graph der Gr��e 3, falls die preConditions nicht erf�llt wurden)
     */
    public static IGraph createGraph(int[][] distanceMatrix) {
        // int[] Matrix erzeugen
        int[] dMatrix = new int[distanceMatrix.length * distanceMatrix.length];
        for (int i = 0; i < distanceMatrix.length; i += 1) {
            for (int j = 0; j < distanceMatrix.length; j += 1) {
                dMatrix[(distanceMatrix.length * i + j)] = distanceMatrix[i][j];
            }
        }

        // Alle Werte m�ssen positiv sein, die Matrix muss symmetrisch sein und die Hauptdiagonale 0
        for (int i = 0; i < distanceMatrix.length; i += 1) {
            for (int j = i; j < distanceMatrix.length; j += 1) {
                if (dMatrix[(distanceMatrix.length * i + j)] < 0
                        || dMatrix[(distanceMatrix.length * i + j)] != dMatrix[(distanceMatrix.length * j + i)]
                        || (i == j && dMatrix[(distanceMatrix.length * i + j)] != 0)) {
                    return createNormalizedGraph(3);
                }
            }
        }
        return new GraphImpl(dMatrix);
    }
    
    /**
     * Erstellt einen DiGraphen aus einer Distanzmatrix.
     * @preCondition alle Werte der Matrix x | x > 0
     * @preCondition distanceMatrix muss quadratisch sein
     * @preCondition alle Werte der Hauptdiagonalen m�ssen den Wert 0 besitzen
     * @param distanceMatrix	Distanzmatrix
     * @return erzeugter Graph (normalisierter Graph der Gr��e 3, falls die preConditions nicht erf�llt wurden)
     */
    public static IGraph createDiGraph(int[][] distanceMatrix) {
        // int[] Matrix erzeugen
        int[] dMatrix = new int[distanceMatrix.length * distanceMatrix.length];
        for (int i = 0; i < distanceMatrix.length; i += 1) {
            for (int j = 0; j < distanceMatrix.length; j += 1) {
                dMatrix[(distanceMatrix.length * i + j)] = distanceMatrix[i][j];
            }
        }

        // Alle Werte m�ssen positiv sein (au�er bei einer nicht existenten Kante), Hauptdiagonale muss 0 sein
        for (int i = 0; i < distanceMatrix.length; i += 1) {
            for (int j = i; j < distanceMatrix.length; j += 1) {
                if ((dMatrix[(distanceMatrix.length * i + j)] < 0 && dMatrix[(distanceMatrix.length * i + j)] != IGraph.NON_EXISTING_EDGE)
                		|| ((i == j) && dMatrix[(distanceMatrix.length * i + j)] != 0)){
                    return createNormalizedGraph(3);
                }
             }
        }
        return new GraphImpl(dMatrix);
    }

    /**
     * Erstellt einen symmetrischen Graphen mit zuf�llig generierten Kantengewichtung im Bereich von 1-100.
     * @preCondition numberOfVertices >= 3
     * @param numberOfVertices	Anzahl der Ecken
     * @param randSeed			Eingabe f�r den Zufallsgenerator
     * @return erzeugter Graph (Graph mit 3 Ecken, falls die preCondition nicht erf�llt wurde)
     */
    public static IGraph createRandomGraph(int numberOfVertices, long randSeed) {
        Random rand = new Random(randSeed);
        // Pr�fung auf g�ltige Werten, ansonsten Standardwert
        if (numberOfVertices < 3) {
            numberOfVertices = 3;
        }

        int[] distanceMatrix = new int[numberOfVertices * numberOfVertices];

        for (int i = 0; i < numberOfVertices; i += 1) {
            for (int j = i; j < numberOfVertices; j += 1) {
                if (i == j) {
                    distanceMatrix[(numberOfVertices * i + j)] = 0;
                } else {
                    distanceMatrix[(numberOfVertices * i + j)] = rand.nextInt(100) + 1;
                    distanceMatrix[(numberOfVertices * j + i)] = distanceMatrix[(numberOfVertices * i + j)];
                }
            }
        }

        return new GraphImpl(distanceMatrix);
    }

    /**
     * Erstellt einen symmetrischen Graphen mit einer konstanten Kantengewichtung. 
     * Die Gewichtung wird durch die Differenz zweier Knoten bestimmt z.B. distance(1,2) = 1; distance(1,4) = 3; etc.
     * @preCondition numberOfVertices >= 3
     * @param numberOfVertices	Anzahl der Ecken
     * @return erzeugter Graph (Graph mit 3 Ecken, falls die preCondition nicht erf�llt wurde)
     */
    public static IGraph createNormalizedGraph(int numberOfVertices) {
        // Pr�fung auf g�ltige Werten, ansonsten Standardwert
        if (numberOfVertices < 3) {
            numberOfVertices = 3;
        }

        int[] distanceMatrix = new int[numberOfVertices * numberOfVertices];

        for (int i = 0; i < numberOfVertices; i += 1) {
            for (int j = i; j < numberOfVertices; j += 1) {
                distanceMatrix[(numberOfVertices * i + j)] = j - i;
                distanceMatrix[(numberOfVertices * j + i)] = distanceMatrix[(numberOfVertices * i + j)];
            }
        }

        return new GraphImpl(distanceMatrix);
    }
}