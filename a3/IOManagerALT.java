package a3;

import java.io.*;
import java.sql.Timestamp;
import java.util.Random;

/**
 * IOManager zum Laden von Graphen aus Dateien und zur Ausgabe von Logs.
 */
public class IOManagerALT {

    private final File logFile;
    private final String filePath;
    private final int[][] failMatrix = {{0, 1, 2}, {1, 0, 1}, {2, 1, 0}};

    /**
     * Erstellt den IOManager zur Ausgabe ins Logfile und auf die Konsole <br>
     * Erzeugt die Ausgabedatei
     *
     * @param filePath der Pfad zur Eingabedatei relativ zum src-Verzeichnis
     */
    public IOManagerALT(String filePath) {
        this.filePath = filePath;
        Timestamp tstamp = new Timestamp(System.currentTimeMillis());
        logFile = new File("SimResult_" + tstamp.toString().replace(":", "-")
                + ".txt");
    }

    /**
     * Liest einen Matrix ein
     *
     * @return Es wird ein quadratisches, symmetrisches int[][] zurueckgegeben
     * <br> falls ein Fehler auftritt, wird ein normalisierter Graph der Groesse
     * 3 zurueckgegeben
     */
    public int[][] readGraphMatrixFromSimFile() {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String[] xAry = input.readLine().split(" 0 | 0|^0 ");

            // normale Graphen
            if (xAry.length != 0) {
                int[][] matrix = new int[xAry.length][xAry.length];
                String[] elem;
                for (int i = 1; i < matrix.length; i += 1) {
                    elem = xAry[i].split(" ");
                    for (int j = 0; j < elem.length; j += 1) {
                        matrix[i][j] = Integer.parseInt(elem[j]);
                        matrix[j][i] = matrix[i][j];
                    }
                }
                input.close();
                return matrix;
            } else {// DiGraphen
                xAry = input.readLine().split(" ");
                int size = Integer.parseInt(xAry[0]);
                int[][] matrix = new int[size][size];
                for (int i = 1; i < matrix.length; i += 1) {
                    String[] elem = xAry[i].split(" ");
                    for (int j = 0; j < elem.length; j += 1) {
                        int tmp = Integer.parseInt(elem[j]);
                        if (tmp % 9 == 0) {
                            matrix[i][j] = -1;
                        } else {
                            matrix[i][j] = tmp;
                        }
                    }
                }
                input.close();
                System.out.print(matrix);
                return matrix;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            return failMatrix;
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            return failMatrix;
        }
    }

    public int readBestSolution() {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String elem = input.readLine();
            while (elem != null) {
                if (elem.startsWith("BEST_SOLUTION")) {
                    input.close();
                    return Integer.parseInt(elem.split("BEST_SOLUTION ")[1]);
                }
                elem = input.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            return 0;
        } catch (IOException e) {
            System.out.println("IOExecption!");
            return 0;
        }
        return 0;
    }

    /**
     * Schreibt den String ins Logfile
     *
     * @param logString der String, der ins Logfile geschrieben werden soll
     */
    public void logToFile(String logString) {
        try {
            try (FileWriter writer = new FileWriter(logFile, true)) {
                writer.write(logString);
                // Platformunabhaengiger Zeilenumbruch wird in den Stream
                // geschrieben
                writer.write(System.getProperty("line.separator"));
                // Schreibt den Stream in die Datei
                // Sollte immer am Ende ausgefuehrt werden, sodass der Stream
                // leer ist und alles in der Datei steht.
                writer.flush();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    /**
     * Gibt einen String auf der Konsole aus
     *
     * @param logString der String, der auf der Konsole ausgegeben werden soll
     */
    public void logToConsole(String logString) {
        System.out.println(logString);
    }

    /**
     * Erzeugt eine vollstaendige oder unvollsaendige unsymetrische zufaellige
     * Distanzmatrix.
     *
     * @preCondition maxdistance >=1
     *
     * @param seed Der Seed der fuer den Aufruf von Random benutzt werden soll
     * @param maxdistance Maximale Entfernung zwischen den Kanten
     * @param size Anzahl der gewuenschten Zeilen und Spalten
     * @param chanceofcorrupted Wahrscheinlichkeit mit der Kanten im Graphen mit
     * -1 belegt werden sollen Bei 0, entsteht ein vollständiger unsymetrischer
     * Graph Bei 1>x>0, entsteht ein unvollständiger unsymetrischer Graph
     */
    public static int[][] RandomDistanceMatrix(int seed, int maxdistance, int size, double chanceofcorrupted) {
        Random rand = new Random(seed);
        int[][] result = new int[size][size];
        int[] pool = new int[(int) (maxdistance + (1 - chanceofcorrupted) * maxdistance)];

        for (int i = 0; i < (maxdistance); i++) {
            pool[i] = -1;
        }
        for (int i = maxdistance; i < (int) (maxdistance + (1 - chanceofcorrupted) * maxdistance); i++) {
            pool[i] = i + 1 - maxdistance;
        }
        for (int i = 0; size > i; i++) {
            for (int j = 0; size > j; j++) {
                if (i == j) {
                    result[i][j] = 0;
                } else {
                    int ran = (int) (rand.nextDouble() * pool.length);
                    result[i][j] = pool[ran];
                }
            }
        }
        return result;
    }

    /**
     * Liest eine Matrix im Standardformat ein
     *
     * @return Es wird ein quadratisches, symmetrisches int[][] zurückgegeben
     * <br> falls ein Fehler auftritt, wird ein normalisierter Graph der Größe 3
     * zurückgegeben
     */
    public int[][] readDigraphMatrixFromUniFile() {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            int dimension = readDimension();
            StringBuilder sb = buildMatrix(input);

            String[] xAry = sb.toString().trim().split(" ");
            int[][] matrix = new int[dimension][dimension];

            for (int y = 0; y < matrix.length; y++) {
                for (int x = 0; x < matrix[0].length; x++) {
                    if (y != x) {
                        matrix[y][x] = Integer.parseInt(xAry[y * matrix.length + x]);
                    }
                }
            }
            input.close();
            return matrix;
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            return failMatrix;
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            return failMatrix;
        }
    }

    public int[][] readGraphMatrixFromUniFile() {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {

            int dimension = readDimension();
            StringBuilder sb = buildMatrix(input);

            String[] xAry = sb.toString().split(" ");
            String[] clean = new String[xAry.length - 1];

            for (int i = 1; i < xAry.length - 1; i++) {
                clean[i - 1] = xAry[i];
            }
            int[][] matrix = new int[dimension][dimension];
            int zeile = 1;
            int spalte = 0;
            int i = 1;
            while (i < clean.length - 1) {
                int temo = Integer.parseInt(clean[i]);
                if (temo == 0) {
                    matrix[zeile][spalte] = 0;
                    spalte = 0;
                    zeile++;
                } else {
                    matrix[zeile][spalte] = temo;
                    matrix[spalte][zeile] = temo;
                    spalte++;

                }
                i++;
            }
            input.close();
            return matrix;
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            return failMatrix;
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            return failMatrix;
        }
    }

    private StringBuilder buildMatrix(BufferedReader input) throws IOException {
        String s;
        do {
            s = input.readLine();
        } while (s != null && !s.contains("EDGE_WEIGHT_SECTION"));
        StringBuilder sb = new StringBuilder();
        s = input.readLine();
        while (s != null && !s.trim().equals("EOF")) {
            sb.append(s);
            s = input.readLine();
        }
        return sb;
    }

    public int readDimension() {
        int dimension = 0;
        try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String s;
            do {
                s = input.readLine();
            } while (s != null && !s.contains("DIMENSION:"));
            try {
                dimension = Integer.parseInt(s.substring(
                        ("DIMENSION:").length()).trim());
            } catch (NumberFormatException e) {
                System.out.println("NumberFormatException!");
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("IOExecption!");
        }
        return dimension;
    }
}
