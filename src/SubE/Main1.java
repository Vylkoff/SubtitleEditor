package SubE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.xml.soap.Text;

public class Main1 extends JFrame implements ActionListener {

	public static void main(String[] args) {
		new Main1();
	}

	// Menus
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenuItem openFile, saveFile, saveAsFile, exit;
	private JMenuItem selectAll, copy, paste, cut;
	private JRadioButton rb1, rb2;
	private JTextField tf;
	private JCheckBox cb;
	private JButton run;

	// Window
	private JFrame editorWindow;

	// Text Area
	private Border textBorder;
	private JScrollPane scroll;
	private JTextArea textArea;
	private Font textFont;
	private Text textContent;

	// Window
	private JFrame window;

	// Is File Saved/Opened
	private boolean opened = false;
	private boolean saved = false;

	// Record Open File for quick saving
	private File openedFile;

	// CONSTRUCTOR

	public Main1() {

		// Create Menus
		fileMenu();
		editMenu();
		setRadioButton();
		setTextField();
		setCheckBox();
		setButtonRun();
		// Create Text Area
		createTextArea();

		// Create Window
		createEditorWindow();
	}

	private JFrame createEditorWindow() {
		editorWindow = new JFrame("JavaEdit");
		editorWindow.setVisible(true);
		editorWindow.setExtendedState(Frame.MAXIMIZED_BOTH);
		editorWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Create Menu Bar
		editorWindow.setJMenuBar(createMenuBar());
		editorWindow.add(scroll, BorderLayout.CENTER);
		editorWindow.pack();
		// Centers application on screen
		editorWindow.setLocationRelativeTo(null);

		return editorWindow;
	}

	private JTextArea createTextArea() {
		textBorder = BorderFactory.createBevelBorder(0, Color.BLUE, Color.BLUE);
		textArea = new JTextArea(30, 50);
		textArea.setBackground(Color.WHITE);
		textArea.setEditable(true);
		textArea.setBorder(BorderFactory.createCompoundBorder(textBorder, BorderFactory.createEmptyBorder(2, 5, 0, 0)));

		textFont = new Font("Verdana", 0, 14);
		textArea.setFont(textFont);

		scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		return textArea;
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(rb1);
		menuBar.add(rb2);
		menuBar.add(tf);
		menuBar.add(cb);
		menuBar.add(run);
		return menuBar;
	}

	private void fileMenu() {
		// Create File Menu
		fileMenu = new JMenu("File");
		fileMenu.setPreferredSize(new Dimension(40, 20));

		// Add file menu items

		openFile = new JMenuItem("Open...");
		openFile.addActionListener(this);
		openFile.setPreferredSize(new Dimension(100, 20));
		openFile.setEnabled(true);

		saveFile = new JMenuItem("Save");
		saveFile.addActionListener(this);
		saveFile.setPreferredSize(new Dimension(100, 20));
		saveFile.setEnabled(true);

		saveAsFile = new JMenuItem("Save As...");
		saveAsFile.addActionListener(this);
		saveAsFile.setPreferredSize(new Dimension(100, 20));
		saveAsFile.setEnabled(true);

		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		exit.setPreferredSize(new Dimension(100, 20));
		exit.setEnabled(true);

		// Add items to menu

		fileMenu.add(openFile);
		fileMenu.add(saveFile);
		fileMenu.add(saveAsFile);
		fileMenu.add(exit);
	}

	private void editMenu() {
		editMenu = new JMenu("Edit");
		editMenu.setPreferredSize(new Dimension(40, 20));

		// Add file menu items
		selectAll = new JMenuItem("Select All");
		selectAll.addActionListener(this);
		selectAll.setPreferredSize(new Dimension(100, 20));
		selectAll.setEnabled(true);

		copy = new JMenuItem("Copy");
		copy.addActionListener(this);
		copy.setPreferredSize(new Dimension(100, 20));
		copy.setEnabled(true);

		paste = new JMenuItem("Paste");
		paste.addActionListener(this);
		paste.setPreferredSize(new Dimension(100, 20));
		paste.setEnabled(true);

		cut = new JMenuItem("Cut");
		cut.addActionListener(this);
		cut.setPreferredSize(new Dimension(100, 20));
		cut.setEnabled(true);

		// Add items to menu
		editMenu.add(selectAll);
		editMenu.add(copy);
		editMenu.add(paste);
		editMenu.add(cut);
	}

	private void setRadioButton() {
		ButtonGroup group = new ButtonGroup();
		rb1 = new JRadioButton("Faster");
		rb2 = new JRadioButton("Slower");
		group.add(rb1);
		group.add(rb2);
		rb1.addActionListener(this);
		rb1.setEnabled(true);
		rb2.addActionListener(this);
		rb2.setEnabled(true);

	}

	private void setTextField() {
		tf = new JTextField("ms");

	}

	private void setCheckBox() {
		cb = new JCheckBox("Delete Tags");
		cb.addActionListener(this);
		cb.setEnabled(true);

	}

	private void setButtonRun() {
		run = new JButton("Run");
		run.addActionListener(this);
		run.setEnabled(true);
	}

	// Method for saving files - Removes duplication of code
	private void saveFile(File filename) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(textArea.getText());
			writer.close();
			saved = true;
			window.setTitle("JavaText - " + filename.getName());
		} catch (IOException err) {
			err.printStackTrace();
		}
	}

	// Method for quick saving files
	private void quickSave(File filename) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			writer.write(textArea.getText());
			writer.close();
		} catch (IOException err) {
			err.printStackTrace();
		}
	}

	// Method for opening files
	private void openingFiles(File filename) {
		try {
			openedFile = filename;
			FileReader reader = new FileReader(filename);
			textArea.read(reader, null);
			opened = true;
			window.setTitle("JavaEdit - " + filename.getName());
		} catch (IOException err) {
			err.printStackTrace();
		}
		// JFileChooser fs=new JFileChooser(new File("c:\\"));
		// fs.setDialogTitle("Open a File");
		// fs.setFileFilter(new FileTypeFilter(".sub","Sub file"));
		// fs.setFileFilter(new FileTypeFilter(".srt","Srt file"));
		// int result=fs.showOpenDialog(null);
		// if(result==JFileChooser.APPROVE_OPTION){
		// try{
		// File fi =fs.getSelectedFile();
		// BufferedReader br=new BufferedReader(new FileReader(fi.getPath()));
		// String line="";
		// String s="";
		// AbstractButton textContent = null;
		// textContent.setText(s);
		// if (br!=null)
		// br.close();
		// }catch(Exception e2){
		// JOptionPane.showMessageDialog(null,e2.getMessage());
		// }
		// }
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource() == openFile) {
			JFileChooser open = new JFileChooser();
			open.showOpenDialog(null);
			File file = open.getSelectedFile();
			openingFiles(file);

		} else if (event.getSource() == saveFile) {
			JFileChooser save = new JFileChooser();
			File filename = save.getSelectedFile();
			if (opened == false && saved == false) {
				save.showSaveDialog(null);
				int confirmationResult;
				if (filename.exists()) {
					confirmationResult = JOptionPane.showConfirmDialog(saveFile, "Replace existing file?");
					if (confirmationResult == JOptionPane.YES_OPTION) {
						saveFile(filename);
					}
				} else {
					saveFile(filename);
				}
			} else {
				quickSave(openedFile);
			}
		} else if (event.getSource() == saveAsFile) {
			JFileChooser saveAs = new JFileChooser();
			saveAs.showSaveDialog(null);
			File filename = saveAs.getSelectedFile();
			int confirmationResult;
			if (filename.exists()) {
				confirmationResult = JOptionPane.showConfirmDialog(saveAsFile, "Replace existing file?");
				if (confirmationResult == JOptionPane.YES_OPTION) {
					saveFile(filename);
				}
			} else {
				saveFile(filename);
			}

		} else if (event.getSource() == exit) {
			System.exit(0);
		} else if (event.getSource() == selectAll) {
			textArea.selectAll();
		} else if (event.getSource() == copy) {
			textArea.copy();
		} else if (event.getSource() == paste) {
			textArea.paste();
		} else if (event.getSource() == cut) {
			textArea.cut();

		} else if ((event.getSource() == rb1) && (event.getSource() == run)) {
			String line = textArea.getText();

			// Find closing brace
			int bracketFromIndex = line.indexOf('}');
			// Extract 'from' time
			String fromTime = line.substring(1, bracketFromIndex);
			// Calculate new 'from' time
			String milisecs = tf.getText();
			int milsec = Integer.parseInt(milisecs);
			int newFromTime = Integer.parseInt(fromTime) + milsec;
			// Find the following closing brace
			int bracketToIndex = line.indexOf('}', bracketFromIndex + 1);
			// Extract 'to' time
			String toTime = line.substring(bracketFromIndex + 2, bracketToIndex);
			String milisecs1 = tf.getText();
			int milsec1 = Integer.parseInt(milisecs);
			// Calculate new 'to' time
			int newToTime = Integer.parseInt(toTime) + milsec1;
			// Create a new line using the new 'from' and 'to' times
			String fixedLine = "{" + newFromTime + "}" + "{" + newToTime + "}" + line.substring(bracketToIndex + 1);

			textArea.setText(fixedLine);
		} else if ((event.getSource() == rb2) && (event.getSource() == run)) {
			String line = textArea.getText();
			// Find closing brace
			int bracketFromIndex = line.indexOf('}');
			// Extract 'from' time
			String fromTime = line.substring(1, bracketFromIndex);
			// Calculate new 'from' time
			String milisecs = tf.getText();
			int milsec = Integer.parseInt(milisecs);
			int newFromTime = Integer.parseInt(fromTime) - milsec;
			// Find the following closing brace
			int bracketToIndex = line.indexOf('}', bracketFromIndex + 1);
			// Extract 'to' time
			String toTime = line.substring(bracketFromIndex + 2, bracketToIndex);
			String milisecs1 = tf.getText();
			int milsec1 = Integer.parseInt(milisecs);
			// Calculate new 'to' time
			int newToTime = Integer.parseInt(toTime) - milsec1;
			// Create a new line using the new 'from' and 'to' times
			String fixedLine = "{" + newFromTime + "}" + "{" + newToTime + "}" + line.substring(bracketToIndex + 1);

			textArea.setText(fixedLine);
		} else if (event.getSource() == cb) {
			String text = textArea.getText();
			String fixedSubtitles = text.replaceAll("\\<.*?\\>", "");
			textArea.setText(fixedSubtitles);
		}
	}

	// GETTERS AND SETTERS

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea text) {
		textArea = text;
	}
}
