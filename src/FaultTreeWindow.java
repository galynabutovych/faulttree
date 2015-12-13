import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.awt.event.InputEvent;

public class FaultTreeWindow {

	private JFrame frmFaultTree;
	private GraphPanel graphPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FaultTreeWindow window = new FaultTreeWindow();
					window.frmFaultTree.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FaultTreeWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFaultTree = new JFrame();
		frmFaultTree.setTitle("Дерево відмов");
		frmFaultTree.setBounds(100, 100, 822, 639);
		frmFaultTree.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		graphPanel = new GraphPanel();
		frmFaultTree.getContentPane().add(graphPanel, BorderLayout.CENTER);
		
		JMenuBar menuBar = new JMenuBar();
		frmFaultTree.setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("Файл");
		menuBar.add(menu);
		
		JMenuItem menuItemSave = new JMenuItem("Зберегти");
		menuItemSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		menu.add(menuItemSave);
		menuItemSave.setIcon(new ImageIcon(FaultTreeWindow.class.getResource("/resources/icons/save.png")));
		
		JMenuItem menuItemSaveAs = new JMenuItem("Зберегти як");
		menuItemSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				graphPanel.saveAs();
			}
		});
		menu.add(menuItemSaveAs);
		menuItemSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		menuItemSaveAs.setIcon(new ImageIcon(FaultTreeWindow.class.getResource("/resources/icons/save-as.png")));
		
		JMenuItem menuItemOpen = new JMenuItem("Відкрити");
		menu.add(menuItemOpen);
		menuItemOpen.setIcon(new ImageIcon(FaultTreeWindow.class.getResource("/resources/icons/open.png")));
		
		
		JMenu mnNewMenu = new JMenu("Інструкція");
		menuBar.add(mnNewMenu);
		
		JMenuItem menuItemHelp = new JMenuItem("");
		mnNewMenu.add(menuItemHelp);
		menuItemHelp.setIcon(new ImageIcon(FaultTreeWindow.class.getResource("/resources/icons/help.png")));
		menuItemHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JTextArea msg = new JTextArea(getFile("/resources/instruction.txt"));
				msg.setLineWrap(true);
				msg.setWrapStyleWord(true);

				JScrollPane scrollPane = new JScrollPane(msg);

				JOptionPane.showMessageDialog(null, scrollPane);
			}
		});
	}
	private String getFile(String fileName) {

		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		File file = new File(FaultTreeWindow.class.getResource(fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}

			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return result.toString();

	  }

}
