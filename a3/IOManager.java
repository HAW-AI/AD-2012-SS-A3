package a3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;

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

}