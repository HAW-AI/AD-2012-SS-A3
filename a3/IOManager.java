package adp33;

import java.io.*;
import java.lang.Exception;
import java.sql.Timestamp;
import java.util.Random;
import javax.swing.text.StyledEditorKit.ForegroundAction;

/**
 * IOManager zum Laden von Graphen aus Dateien und zur Ausgabe von Logs.
 */
public class IOManager {

    private final String filePath;
    private int[] failMatrix = {0, 1, 2, 1, 0, 1, 1, 2, 0};

    /**
     * Erstellt den IOManager zur Ausgabe ins Logfile und auf die Konsole <br>
     * Erzeugt die Ausgabedatei
     * 
     * @param filePath
     *            der Pfad zur Eingabedatei relativ zum src-Verzeichnis
     */
    public IOManager(String filePath) {
        this.filePath = filePath;
    }

    /**********************************************
     * 
     *      getEdgeWeightSection()
     *      getDemandSection()
     *      getDimension()
     *      getAntCapacity()
     * 
     **********************************************/
    public int[] getEdgeWeightSection() {
        int[] edgeDistance = null;
        int[][] testAry = null;

        try (BufferedReader br = new BufferedReader(new FileReader(new File(filePath)))) {
            String line = br.readLine();

            while (!(line = br.readLine()).equals("EOF")) {
                if (line.equals("EDGE_WEIGHT_SECTION")) {
                    edgeDistance = new int[getDimension() * getDimension()];
                    testAry = new int[getDimension()][getDimension()];
                    int index = 0;
                    int j = 0;
                    while (!(line = br.readLine()).equals("DEMAND_SECTION") && !line.equals("EOF")) {
                        String unspaced = line.trim();
                        String[] splited = unspaced.split("(\\s)+");
                        for (int i = 0; i < splited.length; i++) {
                            edgeDistance[index] = Integer.parseInt(splited[i].trim());
                            index++;
                            testAry[i][j] = Integer.parseInt(splited[i].trim());
                        }
                        j++;
                    }
                    break;
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Fehler beim Lesen der Datei!");
        }
        //Der Weg von Ecke 1 nach Ecke 2 endet in einer Sackgasse => sollte nicht geladen werden

        int counter = 0; //um -1's zu zahlen
        for (int i = 0; i < testAry.length; i++) {
            for (int j = 0; j < testAry.length; j++) {
                if (testAry[i][j] == -1) {
                    counter++;
                }
            }
            if (counter == testAry.length - 1) {
                fehlerausgabe("Eine Sackgasse oder nichtzusammenhaendender Graph.");
                return failMatrix;
            }
            counter = 0;
        }
        return edgeDistance;
    }

    public int[] getDemandSection() {
        int[] edgeCapacity = null;

        try (BufferedReader br = new BufferedReader(new FileReader(new File(filePath)))) {
            String line = br.readLine();

            while (!(line = br.readLine()).equals("EOF")) {

                if (line.equals("DEMAND_SECTION")) {
                    edgeCapacity = new int[getDimension()];
                    int index = 0;
                    while (!(line = br.readLine()).equals("EOF")) {
                        String[] splited = line.split("(\\s)+");
                        edgeCapacity[index] = Integer.parseInt(splited[1].trim());
                        index++;
                    }
                    break;
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Fehler beim Lesen der Datei!");
        }
        //Kapazitaet von Ecke ist groesser als die Ameisenkapazitaet => sollte nicht geladen werden
        int capacity = getAntCapacity();
        for (int customer : edgeCapacity) {
            if (customer > capacity) {
                fehlerausgabe("Die Kapazitaet des LKWs ist kleiner als die groesste Kapazitaet der Kunden.");
                return failMatrix;
            }
        }
        return edgeCapacity;
    }

    public int getDimension() {
        int dimension = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(new File(filePath)))) {
            String line = br.readLine();
            while (!(line = br.readLine()).equals("EOF")) {

                String[] splited = line.split(":");
                splited[0] = splited[0].trim();

                if (splited[0].equals("DIMENSION")) {
                    dimension = Integer.parseInt(splited[1].trim());
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Fehler beim Lesen der Datei!");
        }
        return dimension;
    }

    public int getAntCapacity() {
        int ant = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(new File(filePath)))) {
            String line = br.readLine();

            while (!(line = br.readLine()).equals("EOF")) {
                String[] splited = line.split(":");
                splited[0] = splited[0].trim();

                if (splited[0].equals("CAPACITY")) {
                    ant = Integer.parseInt(splited[1].trim());
                    break;
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Fehler beim Lesen der Datei!");
        }
        return ant;
    }

    private void fehlerausgabe(String text) {
        System.out.println("Fehler beim Einlesen der Datei: " + text);
    }
}