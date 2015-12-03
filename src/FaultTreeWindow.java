import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

public class FaultTreeWindow {

	private JFrame frmFaultTree;

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
		
		GraphPanel graphPanel = new GraphPanel();
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
	}

}
