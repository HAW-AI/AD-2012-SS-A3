package gui;
import a3.IGraph;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.LayoutStyle;

/**
 * Hauptfenster der GUI, welches das DrawPanel initialisiert und eine Checkbox 
 * zur Anzeige der Kanten bereitstellt.
 */
public class MainFrame extends javax.swing.JFrame {
	private static final long serialVersionUID = 1L;
    private static final int HEIGHT = 900;
    private static final int WIDTH = 1400;
    private JCheckBox jCheckBox;
	private DrawPanel jDrawPanel;

	/**
	 * Konstruktor ohne Fensternamen.
	 * @param graph 	der anzuzeigende Graph
	 */
	public MainFrame(IGraph graph) {
		super();
		initGUI(graph);
	}
	
	/**
	 * Konstruktor mit Fensternamen.
	 * @param graph 	der anzuzeigende Graph
	 * @param name		Name des Fensters
	 */
	public MainFrame(IGraph graph, String name) {
		super(name);
		initGUI(graph);
	}
	
	/**
	 * Initialisiert die grafischen Elemente.
	 * @param graph		der anzuzeigende Graph
	 */
	private void initGUI(IGraph graph) {
		try {
			GroupLayout thisLayout = new GroupLayout((JComponent)getContentPane());
			getContentPane().setLayout(thisLayout);
			{
				jDrawPanel = new DrawPanel(graph);
			}
			{
				jCheckBox = new JCheckBox();
				jCheckBox.setText("Alle Kanten anzeigen?");
				jCheckBox.setSelected(true);
				jCheckBox.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent arg0) {
						jDrawPanel.toggleEdgeDisplay();
					}
				});
			}
			
			// dieser Codeblock wurde von "Jigloo" geschrieben
			thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.addComponent(jDrawPanel, 0, 811, Short.MAX_VALUE)
				.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 1, GroupLayout.PREFERRED_SIZE)
				.addComponent(jCheckBox, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addContainerGap());
			thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
				.addContainerGap()
				.addGroup(thisLayout.createParallelGroup()
				    .addComponent(jDrawPanel, GroupLayout.Alignment.LEADING, 0, 1360, Short.MAX_VALUE)
				    .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
				        .addComponent(jCheckBox, 0, 212, Short.MAX_VALUE)
				        .addGap(1148)))
				.addContainerGap());
					
			pack();
			setSize(WIDTH, HEIGHT);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
