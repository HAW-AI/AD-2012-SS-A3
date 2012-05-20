package a3;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IOManager {
	private final File logFile;
	private final String filePath;

	/**
	 * Erstellt den IOManager zur Ausgabe ins Logfile und auf die Konsole <br>
	 * Erzeugt die Ausgabedatei
	 * 
	 * @param filePath
	 *            der Pfad zur Eingabedatei relativ zum src-Verzeichnis
	 */
	public IOManager(String filePath) {
		this.filePath = filePath;
		Timestamp tstamp = new Timestamp(System.currentTimeMillis());
		logFile = new File("SimResult_" + tstamp.toString().replace(":", "-") + ".txt");
	}

	/**
	 * Liest einen Matrix ein
	 * 
	 * @return Es wird ein quadratisches, symmetrisches int[][] zur�ckgegeben <br>
	 *         falls ein Fehler auftritt, wird ein normalisierter Graph der
	 *         Gr��e 3 zur�ckgegeben
	 */
	public int[][] readMatrix() {

		int[][] failMatrix = { { 0, 1, 2 }, { 1, 0, 1 }, { 2, 1, 0 } };
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			try {
				String[] xAry = input.readLine().split(" 0 | 0|^0 ");

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
			} catch (IOException e) {
				System.out.println("IOException: " + e.getMessage());
				return failMatrix;
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			return failMatrix;
		}
	}

	
	public int readBestSolution() {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			try {
				String elem = input.readLine();
				while (elem != null) {
					if (elem.startsWith("BEST_SOLUTION")) {
						input.close();
						return Integer.parseInt(elem.split("BEST_SOLUTION ")[1]);
					}
					elem = input.readLine();
				}
			} catch (IOException e) {
				System.out.println("IOExecption!");
				return 0;
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			return 0;
		}
		return 0;
	}

	/**
	 * Schreibt den String ins Logfile
	 * 
	 * @param logString
	 *            der String, der ins Logfile geschrieben werden soll
	 */
	public void logToFile(String logString) {
		try {
			// new FileWriter(file ,true) - falls die Datei bereits existiert
			// werden die Bytes an das Ende der Datei geschrieben
			FileWriter writer = new FileWriter(logFile, true);
			// Text wird in den Stream geschrieben
			writer.write(logString);
			// Platformunabhaengiger Zeilenumbruch wird in den Stream
			// geschrieben
			writer.write(System.getProperty("line.separator"));
			// Schreibt den Stream in die Datei
			// Sollte immer am Ende ausgefuehrt werden, sodass der Stream
			// leer ist und alles in der Datei steht.
			writer.flush();
			// Schliesst den Stream
			writer.close();
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}

	/**
	 * Gibt einen String auf der Konsole aus
	 * 
	 * @param logString
	 *            der String, der auf der Konsole ausgegeben werden soll
	 */
	public void logToConsole(String logString) {
		System.out.println(logString);
	}
        
	/**
	 * Erzeugt eine vollständige oder unvollständige unsymetrische zufällige Distanzmatrix.
	 * @preConditions maxdistance >=1
	 * @param seed
	 *            Der Seed der für den Aufruf von Random benutzt werden soll
         * @param maxdistance
         *           Maximale Entfernung zwischen den Kanten
         * @param size
         *           Anzahl der gewünschten Zeilen und Spalten
         * @param chanceofcorrupted
         *          Wahrscheinlichkeit mit der Kanten im Graphen mit -1 belegt werden sollen
         *          Bei 0, entsteht ein vollständiger unsymetrischer Graph
         *          Bei >0, entsteht ein unvollständiger unsymetrischer Graph
	 */        
        
        public static int[][] RandomDistanceMatrix(int seed,  int maxdistance,int size,double chanceofcorrupted ) {
             Random rand = new Random(seed);
             int[][] result = new int[size][size];
             // Wertepoole erzeugen
             int[] pool = new int [maxdistance+(int) (chanceofcorrupted*maxdistance)];
             
            for (int i = 0;i<(maxdistance);i++) {
                pool[i]=-1;
           }
            
           for (int i = maxdistance;i<(int) (maxdistance+chanceofcorrupted*maxdistance);i++) {
                 pool[i]=i+1-maxdistance;
            }           
             for (int i = 0;size > i;i++) {
                 for (int j = 0; size > j;j++) {
                     if (i==j){    result[i][j]=0;}
                     else
                     {
                         int ran = (int) (rand.nextDouble()*pool.length);
                         result[i][j]=pool[ran];
                     }
                 }
             }
           return result;
        }
        /**
     * Liest eine Matrix als Digraph ein
     *
     * @return Es wird ein quadratisches int[][] zurückgegeben
     * <br> falls ein Fehler auftritt, wird ein normalisierter Graph der Größe 3
     * zurückgegeben
     */
    public int[][] readDigraphMatrix() {

        int[][] failMatrix = {{0, 1, 2}, {1, 0, 1}, {2, 1, 0}};
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            try {
                String s;
                // -- Folgender Codeblock sucht den Anfang der EDGE_WEIGHT_SECTION
                do {
                    s = input.readLine();
                } while (s != null && !s.contains("EDGE_WEIGHT_SECTION"));

                // -- Folgender Codeblock sucht das Ende der EDGE_WEIGHT_SECTION
                // -- Markierung ist EOF
                // -- und speicert alle Zeilen mit Informationen zu Kantenlängen
                // -- in einer Liste
                List<String> l = new ArrayList<String>();
                StringBuilder sb = new StringBuilder();
                s = input.readLine();
                while (s != null && !s.trim().equals("EOF")) {
                    sb.append(s);
                    int index = sb.indexOf("  ");
                    while (index >= 0) {
                        sb.deleteCharAt(index);
                        index = sb.indexOf("  ");
                    }
                    l.add(sb.toString());
                    sb.delete(0, sb.length());
                    s = input.readLine();
                }

                int[][] matrix = new int[l.size()][l.size()];
                String[] elem;

                for (int i = 0; i < matrix.length; i++) {
                    elem = l.get(i).split(" ");
                    for (int j = 0; j < elem.length; j++) {
                        matrix[i][j] = Integer.parseInt(elem[j]);
                    }
                }
                input.close();
                return matrix;
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
                return failMatrix;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            return failMatrix;
        }
    }
}