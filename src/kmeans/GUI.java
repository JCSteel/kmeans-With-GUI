package kmeans;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class GUI extends JFrame implements ActionListener {
	
	//Some variables
	private JButton startButton, clearBoard;
	private JTextField k;
	//private JTextArea width;
	Board boardGUI;
	
	
	
	//Main function. Calls function to create the GUI
	public static void main(String[] args) {
		createAndShowGUI();
	}
	
	//Creating some settings for the GUI.
	public GUI() {
		super("KMeans Clustering");
		setResizable(false);
	}
	
	//Adds all of the components to the GUI.
	public void addComponents(Container window) {
		
		
		// Configuring the row layouts
		boardGUI = new Board();
		JPanel buttons1 = new JPanel(new GridLayout(0,5));
		

		//Adding the major components to the GUI
		//First the settings menus
		// Setting up the first row of buttons and settings
		startButton = new JButton("Find Clusters");
		startButton.addActionListener(this);
		clearBoard = new JButton("Clear Board");
		clearBoard.addActionListener(this);
		k = new JTextField("1", 16);
		
		
		// Add the panel of buttons to the frame
		buttons1.add(startButton);
		buttons1.add(new JLabel(""));
		buttons1.add(clearBoard);
		buttons1.add(new JLabel(""));
		buttons1.add(k);
		
		//Add panels to the frame
		window.add(boardGUI, BorderLayout.NORTH);
		boardGUI.setPreferredSize(new Dimension(750,800));
		window.add(buttons1, BorderLayout.CENTER);
		
	}
	
	//Create, pack, show the GUI
	private static void createAndShowGUI() {
		GUI GUI = new GUI();
		GUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GUI.addComponents(GUI.getContentPane());
		GUI.pack();
		GUI.setVisible(true);
	}
	
	//Adding actions for each button on the GUI.
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startButton) {
			try {
				boardGUI.k = Integer.parseInt(this.k.getText());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Error. Must be an integer less than or equal to 4.");
			}
			boardGUI.startGame();
		}
		else if (e.getSource() == clearBoard) {
			boardGUI.clearBoard();
		}
	}
}