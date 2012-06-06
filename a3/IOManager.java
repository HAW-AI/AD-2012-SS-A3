package a3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * IOManager zum Laden von Graphen aus Dateien und zur Ausgabe von Logs.
░░░░░▄▄▄▄▀▀▀▀▀▀▀▀▄▄▄▄▄▄░░░░░░░░
░░░░░█░░░░▒▒▒▒▒▒▒▒▒▒▒▒░░▀▀▄░░░░
░░░░█░░░▒▒▒▒▒▒░░░░░░░░▒▒▒░░█░░░
░░░█░░░░░░▄██▀▄▄░░░░░▄▄▄░░░░█░░
░▄▀▒▄▄▄▒░█▀▀▀▀▄▄█░░░██▄▄█░░░░█░
█░▒█▒▄░▀▄▄▄▀░░░░░░░░█░░░▒▒▒▒▒░█
█░▒█░█▀▄▄░░░░░█▀░░░░▀▄░░▄▀▀▀▄▒█
░█░▀▄░█▄░█▀▄▄░▀░▀▀░▄▄▀░░░░█░░█░
░░█░░░▀▄▀█▄▄░█▀▀▀▄▄▄▄▀▀█▀██░█░░
░░░█░░░░██░░▀█▄▄▄█▄▄█▄████░█░░░
░░░░█░░░░▀▀▄░█░░░█░█▀██████░█░░
░░░░░▀▄░░░░░▀▀▄▄▄█▄█▄█▄█▄▀░░█░░
░░░░░░░▀▄▄░▒▒▒▒░░░░░░░░░░▒░░░█░
░░░░░░░░░░▀▀▄▄░▒▒▒▒▒▒▒▒▒▒░░░░█░
░░░░░░░░░░░░░░▀▄▄▄▄▄░░░░░░░░█░░
 */
public class IOManager {

	private static final int HEADER = 0;
	private static final int EDGE_WEIGHT_SECTION = 1;
	private static final int DEMAND_SECTION = 2;
	private static final int EOF = 3;
	
    private final String filePath;
    private int[] failMatrix = {0, 1, 2, 1, 0, 1, 1, 2, 0};
    
    private final String name;
    private final String comment;
    private final String type;
    private final int dimension;
    private final String edge_weight_type;
    private final String edge_weight_format;
    private final String display_data_type;
    private final int capacity;
    private final int[] edge_weight_section;
    private final int[] demand_section;
    
    private static String[] resolve(String input) {
    	String[] s = input.split(":", 2);
    	s[0] = s[0].trim();
    	if (s.length > 1)
    		s[1] = s[1].trim();
    	return s;
    }

    /**
     * Erstellt den IOManager zur Ausgabe ins Logfile und auf die Konsole <br>
     * Erzeugt die Ausgabedatei
     * 
     * @param filePath
     *            der Pfad zur Eingabedatei relativ zum src-Verzeichnis
     */
    public IOManager(String filePath) {
        this.filePath = filePath;
    	String line = null;
        String name = "Unnamed";
        String comment = "none";
        String type = "unknown";
        int dimension = -1;
        String edge_weight_type = "unspecified";
        String edge_weight_format = "unspecified";
        String display_data_type = "unspecified";
        int capacity = -1;
        int[] edge_weight_section = null;
        int[] demand_section = null;
        int state = HEADER;
        int counter = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(new File(filePath)))) {
            while (!(line = br.readLine()).equals("EOF")) {
            	String[] ss = resolve(line);
            	switch (state) {
            	case HEADER:
                	if (ss[0].equals("NAME")) name = ss[1];
                	else if (ss[0].equals("COMMENT")) comment = ss[1];
                	else if (ss[0].equals("TYPE")) type = ss[1];
                	else if (ss[0].equals("DIMENSION")) dimension = Integer.parseInt(ss[1]);
                	else if (ss[0].equals("EDGE_WEIGHT_TYPE")) edge_weight_type = ss[1];
                	else if (ss[0].equals("EDGE_WEIGHT_FORMAT")) edge_weight_format = ss[1];
                	else if (ss[0].equals("DISPLAY_DATA_TYPE")) display_data_type = ss[1];
                	else if (ss[0].equals("CAPACITY")) capacity = Integer.parseInt(ss[1]);
                	else if (ss[0].equals("EDGE_WEIGHT_SECTION")) {
                		state = EDGE_WEIGHT_SECTION;
                        edge_weight_section = new int[dimension * dimension];
                        counter = 0;
                	}
                	break;
            	case EDGE_WEIGHT_SECTION:
            		if (ss[0].equals("DEMAND_SECTION")) {
            			state = DEMAND_SECTION;
            			demand_section = new int[dimension];
            			counter = 0;
            		} else {
                        String[] splitted = line.trim().split("(\\s)+");
                        for (String s : splitted)
                        	edge_weight_section[counter++] = Integer.parseInt(s.trim());
            		}
            		break;
            	case DEMAND_SECTION:
            		if (ss[0].equals("EOF")) {
            			state = EOF;
            		} else {
                        String[] splitted = line.trim().split("(\\s)+");
                    	demand_section[counter++] = Integer.parseInt(splitted[1].trim());
            		}
            		break;
            	}
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Fehler beim Lesen der Datei!");
        } catch (Exception e) {
        	System.out.println("Fehler beim Lesen eines Attributs aus der Datei: " + line);
        	e.printStackTrace();
        }
        this.name = name;
        this.comment = comment;
        this.type = type;
        this.dimension = dimension;
        this.edge_weight_type = edge_weight_type;
        this.edge_weight_format = edge_weight_format;
        this.display_data_type = display_data_type;
        this.capacity = capacity;
        this.edge_weight_section = edge_weight_section;
        this.demand_section = demand_section;
        
        //Der Weg von Ecke 1 nach Ecke 2 endet in einer Sackgasse => sollte nicht geladen werden

        counter = 0; //um -1's zu zahlen
        for (int i : edge_weight_section)
            if (i == -1)
                counter++;
        if (counter == dimension - 1) {
            fehlerausgabe("Eine Sackgasse oder nichtzusammenhaendender Graph.");
            System.exit(0);
        }

        //Kapazitaet von Ecke ist groesser als die Ameisenkapazitaet => sollte nicht geladen werden
        for (int i : demand_section)
            if (i > capacity) {
                fehlerausgabe("Die Kapazitaet des LKWs ist kleiner als die groesste Kapazitaet der Kunden.");
                System.exit(0);
            }
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
    	return edge_weight_section;
/*        int[] edgeDistance = null;
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
                            testAry[j][i] = Integer.parseInt(splited[i].trim());
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
                System.exit(0);
            }
            counter = 0;
        }
        return edgeDistance;*/
    }

    public int[] getDemandSection() {
    	return demand_section;
    	/*
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
                System.exit(0);
            }
        }
        return edgeCapacity;
        */
    }

    public int getDimension() {
    	return dimension;
/*        int dimension = 0;
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
        return dimension;*/
    }

    public int getAntCapacity() {
    	return capacity;
/*        int ant = 0;

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
        return ant;*/
    }

    private void fehlerausgabe(String text) {
        System.out.println("Fehler beim Einlesen der Datei aus : " + filePath + "   " + text);
    }
}