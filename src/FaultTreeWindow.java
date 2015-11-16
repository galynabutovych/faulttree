import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.BorderLayout;

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
		frmFaultTree.setTitle("Fault Tree");
		frmFaultTree.setBounds(100, 100, 450, 300);
		frmFaultTree.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GraphPanel graphPanel = new GraphPanel();
		frmFaultTree.getContentPane().add(graphPanel, BorderLayout.CENTER);
	}

}
