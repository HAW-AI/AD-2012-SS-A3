package graph;

import java.util.Random;

public final class GraphFactory {

    private GraphFactory() {
    }

    /**
     * Erstellt einen Graphen aus einer Distanzmatrix
     *
     * @preConditions alle Werte der Matrix x | x > 0 <br> distanceMatrix muss
     * quadratisch sein <br> distanceMatrix muss symmetrisch sein <br> alle
     * Werte der Hauptdiagonalen mï¿½ssen den Wert 0 besitzen <br> ansonsten
     * wird ein normalisierter Graph der GrÃ¶ÃŸe 3 erstellt
     */
    public static IGraph createGraph(int[][] distanceMatrix) {
        // int[] Matrix erzeugen
        int[] dMatrix = new int[distanceMatrix.length * distanceMatrix.length];
        for (int i = 0; i < distanceMatrix.length; i += 1) {
            for (int j = 0; j < distanceMatrix.length; j += 1) {
                dMatrix[(distanceMatrix.length * i + j)] = distanceMatrix[i][j];
            }
        }

        // Alle werte müssen positiv sein und die Matrix muss symmetrisch sein
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
     * Erstellt einen DiGraphen aus einer Distanzmatrix
     *
     * @preConditions alle Werte der Matrix x | x > -1 <br> distanceMatrix muss
     * quadratisch sein <br> alle  Werte der Hauptdiagonalen müssen den Wert 0 besitzen <br> ansonsten
     * wird ein normalisierter Graph der Größe 3 erstellt
     */
    public static IGraph createDiGraph(int[][] distanceMatrix) {
        // int[] Matrix erzeugen
        int[] dMatrix = new int[distanceMatrix.length * distanceMatrix.length];
        for (int i = 0; i < distanceMatrix.length; i += 1) {
            for (int j = 0; j < distanceMatrix.length; j += 1) {
                dMatrix[(distanceMatrix.length * i + j)] = distanceMatrix[i][j];
            }
        }

        // Alle werte müssen größer gleich -1 sein, Hauptdiagonale muss 0 sein
        for (int i = 0; i < distanceMatrix.length; i += 1) {
            for (int j = i; j < distanceMatrix.length; j += 1) {
                if (dMatrix[(distanceMatrix.length * i + j)] < -1 || ((i==j) && dMatrix[(distanceMatrix.length * i + j)]!=0 )){
                    return createNormalizedGraph(3);
                }
             }
        }
        return new GraphImpl(dMatrix);
    }

    /**
     * Erstellt einen symmetrischen Graphen mit zufï¿½llig generierten
     * Kantengewichtung im Bereich von 1-100
     *
     * @preConditions es muss gelten: numberOfVertices >= 3 <br> ansonsten wird
     * 3 als Standardwert angenommen
     */
    public static IGraph createRandomGraph(int numberOfVertices, long randSeed) {
        Random rand = new Random(randSeed);
        // Prï¿½fung auf gï¿½ltige Werten, ansonsten Standardwert
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
     * Erstellt symmetrischen Graphen mit einer konstanten Kantengewichtung <br>
     * Die Gewichtung wird durch die Differenz zweier Knoten bestimmt <br> z.B.
     * distance(1,2) = 1; distance(1,4) = 3; etc.
     *
     * @preConditions es muss gelten: numberOfVertices >= 3 <br> ansonsten wird
     * 3 als Standardwert angenommen
     */
    public static IGraph createNormalizedGraph(int numberOfVertices) {
        // Prüfung auf gültige Werten, ansonsten Standardwert
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