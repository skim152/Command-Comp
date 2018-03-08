import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


public class ClassRecompGUI extends JFrame implements ActionListener
{	

	public static void main(String[] args) throws IOException
	{
		new ClassRecompGUI();
	}

	public ClassRecompGUI() throws IOException
	{	
		// Creates an instance of the Graph class to be used to perform all the operations of this application.
		Graph<String> graph = new Graph<String>();
		
	    // Creates the label to show where the user can enter the input file name that will be read in, to build the directed graph. 
	    JLabel inputFileNameLabel = new JLabel("Input file name:");
	    
	    // Creates the textfield which will hold the input file name 
		JTextField inputFileNameTextField = new JTextField();
		inputFileNameTextField.setPreferredSize(new Dimension(180, 25));
		
		// Button that will be used to build the directed graph
		JButton buildDirectedGraphButton = new JButton("Build Directed Graph");
		
		
	    // Creates the label which will be used to show where the user can enter the class that need to be recompiled.  
	    JLabel classToRecompileLabel = new JLabel("Class to recompile:");
	    
	    // Creates the textfield which will hold the class that needs recompiling
		JTextField classToRecompileTextField = new JTextField();
		classToRecompileTextField.setPreferredSize(new Dimension(180, 25));
		
		// Button that will be used to sort the graph in topological order, starting with the class from the classToRecompileTextField. 
		JButton topologicalOrderButton = new JButton("Topological Order");
		
			
        // Create a panel, which will be used to contain the label and textField for entering a file name, and the button to build the directed graph.   
	    JPanel topRowPanel = new JPanel();
	    topRowPanel.add(inputFileNameLabel, BorderLayout.EAST);
	    topRowPanel.add(inputFileNameTextField , BorderLayout.EAST);
	    topRowPanel.add(buildDirectedGraphButton , BorderLayout.EAST);
	    
        // Create a panel, which will be used to contain the label and textField for entering the class that needs recompiling,
	    // and the button to sort that class in topological order. 
	    JPanel bottomRowPanel = new JPanel();
	    bottomRowPanel.add(classToRecompileLabel, BorderLayout.EAST);
	    bottomRowPanel.add(classToRecompileTextField , BorderLayout.EAST);
	    bottomRowPanel.add(topologicalOrderButton , BorderLayout.EAST);
	    
	    
	    // Create a topSection panel, which will be used to nest the topRowPanel and bottomRowPanel on top of each other in a column
	    JPanel topSection = new JPanel();
	    topSection.setBorder ( new TitledBorder ( new EtchedBorder (), " " ) );
	    topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
	    topSection.add(topRowPanel);
	    topSection.add(bottomRowPanel);

	    
	    // Create a bottomSection panel, which will be used to contain the Text area for displaying the output. 
	    JPanel bottomSection = new JPanel(new BorderLayout());
	    bottomSection.setBorder ( new TitledBorder ( new EtchedBorder (), "Recompilation Order" ) );

	    JTextArea textArea = new JTextArea ();
	    textArea.setBackground(Color.white);
	    textArea.setEditable ( false ); // set textArea non-editable
	    
	    //Add text area into the bottom section pane of the GUI
	    bottomSection.add(textArea);

	    
		// Event handler for when the user clicks on the "Build Directed Graph" button.
	    buildDirectedGraphButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				// extract the file name entered by the user from the inputFileNameTextField
		        String fileName = inputFileNameTextField.getText();
		        
		        // build the directed graph using the contents from the txt file
		        graph.buildGraph(fileName);
			}
		});
	     
		// Event handler for when the user clicks on the "Topological Order" button.
	    topologicalOrderButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				textArea.setText("");
				
				// extract the class that needs recompiling from the classToRecompileTextField
				String classtoRecompile = classToRecompileTextField.getText();
				
				// And use it as the starting vertex to sort the graph in topological order,
				String sorted =  graph.topologicalSort(classtoRecompile);			
				
				//Provided a valid class name has been supplied, the list of classes that need to be recompiled should be
				//listed in the order they are to be recompiled in the text area at the bottom of the GUI.
				textArea.setText(sorted);
			}
		});
	    
	    		
	    // Create a main pane that will hold all the sections of the GUI in a column 
	    JPanel mainPane = new JPanel();
	    mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
	    mainPane.add( topSection,BorderLayout.NORTH );
	    mainPane.add(bottomSection,BorderLayout.SOUTH );

	  
		// Create a frame that will be used to properly display all the components in the GUI 
	    JFrame displayFrame = new JFrame ();
	    displayFrame.setTitle("Class Dependency Graph |Shinyeob Kim|");
	    displayFrame.setPreferredSize(new Dimension(600, 328));
	    displayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    // Add the main pane holding the contents of the GUI onto the diplayFrame, which will display it. 
	    displayFrame.add(mainPane);

	    displayFrame.pack ();
	    displayFrame.setLocationRelativeTo(null);
	    displayFrame.setVisible(true); 
	}

	@Override
	public void actionPerformed(ActionEvent e){
	}

}