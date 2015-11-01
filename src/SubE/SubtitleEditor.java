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
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

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

public class SubtitleEditor extends JFrame implements ActionListener {

	public static void main(String[] args) {

		new SubtitleEditor();
	}

	// Menus
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenuItem openFile, saveFile, saveAsFile, exit;
	private JMenuItem selectAll, copy, paste, cut;
	private JRadioButton radioButtonFaster, radioButtonSlower;
	private static JTextField textField;
	private JCheckBox checkBox;
	private JButton buttonRun;

	// Window
	private JFrame editorWindow;

	// Text Area
	private Border textBorder;
	private JScrollPane scroll;
	private static JTextArea textArea;
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

	public SubtitleEditor() {

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
		editorWindow = new JFrame("Subtitles Editor");
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
		menuBar.add(radioButtonFaster);
		menuBar.add(radioButtonSlower);
		menuBar.add(textField);
		menuBar.add(checkBox);
		menuBar.add(buttonRun);
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
		radioButtonFaster = new JRadioButton("Faster");
		radioButtonSlower = new JRadioButton("Slower");
		group.add(radioButtonFaster);
		group.add(radioButtonSlower);
		radioButtonFaster.addActionListener(this);
		radioButtonFaster.setEnabled(true);
		radioButtonSlower.addActionListener(this);
		radioButtonSlower.setEnabled(true);

	}

	private void setTextField() {
		textField = new JTextField("ms");

	}

	private void setCheckBox() {
		checkBox = new JCheckBox("Delete Tags");
		checkBox.addActionListener(this);
		checkBox.setEnabled(true);

	}

	private void setButtonRun() {
		buttonRun = new JButton("Run");
		buttonRun.addActionListener(this);
		buttonRun.setEnabled(true);
	}

	// Method for saving files
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
			editorWindow.setTitle("Subtitles Editor - " + filename.getName());
		} catch (IOException err) {
			err.printStackTrace();
		}

	}

	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource() == openFile) {
			openFile();
		} else if (event.getSource() == saveFile) {
			saveFile();
		} else if (event.getSource() == saveAsFile) {
			saveAsFile();
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

		} else if (radioButtonFaster.isSelected() && (event.getSource() == buttonRun)) {
			Scanner fileInput = null;
			try {
				// Create scanner with the Cyrillic encoding
				fileInput = new Scanner(new File("Subtitles.sub", "UTF-8"));
				while (fileInput.hasNextLine()) {
					String line = fileInput.nextLine();
					String fixedLine = fixSubtitleFaster(line);
					textArea.setText(fixedLine);
				}
			} catch (FileNotFoundException fnfe) {
				System.err.println(fnfe.getMessage());
			} finally {
				if (null != fileInput) {
					fileInput.close();
				}
			}

		} else if (radioButtonSlower.isSelected() && (event.getSource() == buttonRun)) {
			Scanner fileInput = null;
			try {
				// Create scanner with the Cyrillic encoding
				fileInput = new Scanner(new File("Subtitles.sub", "UTF-8"));
				while (fileInput.hasNextLine()) {
					String line = fileInput.nextLine();
					String fixedLine = fixSubtitleSlower(line);
					textArea.setText(fixedLine);
				}
			} catch (FileNotFoundException fnfe) {
				System.err.println(fnfe.getMessage());
			} finally {
				if (null != fileInput) {
					fileInput.close();
				}
			}

		} else if (event.getSource() == checkBox) {
			deleteTags();
		}
	}

	private void openFile() {
		JFileChooser open = new JFileChooser();
		open.setFileFilter(new FileTypeFilter(".sub", ""));
		open.setFileFilter(new FileTypeFilter(".srt", ""));
		open.showOpenDialog(null);
		File file = open.getSelectedFile();
		openingFiles(file);
	}

	private void saveFile() {
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
	}

	private void saveAsFile() {
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
	}

	private static String fixSubtitleFaster(String line) {
		// Find closing brace
		int bracketFromIndex = line.indexOf('}');
		// Extract 'from' time
		String fromTime = line.substring(1, bracketFromIndex);
		// Calculate new 'from' time
		String milisecs = textField.getText();
		int milsec = Integer.parseInt(milisecs);
		int newFromTime = Integer.parseInt(fromTime) + milsec;

		// Find the following closing brace
		int bracketToIndex = line.indexOf('}', bracketFromIndex + 1);
		// Extract 'to' time
		String toTime = line.substring(bracketFromIndex + 2, bracketToIndex);
		String milisecs1 = textField.getText();
		int milsec1 = Integer.parseInt(milisecs);
		// Calculate new 'to' time
		int newToTime = Integer.parseInt(toTime) + milsec1;
		// Create a new line using the new 'from' and 'to' times
		String fixedLine = "{" + newFromTime + "}" + "{" + newToTime + "}" + line.substring(bracketToIndex + 1);
		return fixedLine;
	}

	private static String fixSubtitleSlower(String line) {
		// Find closing brace
		int bracketFromIndex = line.indexOf('}');
		// Extract 'from' time
		String fromTime = line.substring(1, bracketFromIndex);
		// Calculate new 'from' time
		String milisecs = textField.getText();
		int milsec = Integer.parseInt(milisecs);
		int newFromTime = Integer.parseInt(fromTime) - milsec;
		// Find the following closing brace
		int bracketToIndex = line.indexOf('}', bracketFromIndex + 1);
		// Extract 'to' time
		String toTime = line.substring(bracketFromIndex + 2, bracketToIndex);
		String milisecs1 = textField.getText();
		int milsec1 = Integer.parseInt(milisecs);
		// Calculate new 'to' time
		int newToTime = Integer.parseInt(toTime) - milsec1;
		// Create a new line using the new 'from' and 'to' times
		String fixedLine = "{" + newFromTime + "}" + "{" + newToTime + "}" + line.substring(bracketToIndex + 1);
		return fixedLine;
	}

	private void deleteTags() {
		String text = textArea.getText();
		String fixedSubtitles = text.replaceAll("\\<.*?\\>", "");
		textArea.setText(fixedSubtitles);
	}

	// GETTERS AND SETTERS

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea text) {
		textArea = text;
	}
}
