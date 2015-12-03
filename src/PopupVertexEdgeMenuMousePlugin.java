import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.security.InvalidParameterException;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

public class PopupVertexEdgeMenuMousePlugin<V, E> extends AbstractPopupGraphMousePlugin {
	private JPopupMenu vertexPopup;

	public PopupVertexEdgeMenuMousePlugin() {
		this(MouseEvent.BUTTON3_DOWN_MASK);
	}

	public PopupVertexEdgeMenuMousePlugin(int modifiers) {
		super(modifiers);
	}

	protected void handlePopup(MouseEvent e) {
		final VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e.getSource();
		if (null == vv)
			return;
		Point2D p = e.getPoint();

		GraphElementAccessor<V, E> pickSupport = vv.getPickSupport();
		if (pickSupport != null) {
			final V v = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
			if (v != null) {
				JPopupMenu vertexPopup = createVertexPopupMenu(v, vv);
				vertexPopup.show(vv, e.getX(), e.getY());
			} else {
				final E edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(), p.getY());
				// edge not supported
				if (null == edge) {
					// blank field: general menu
					createGeneralPopupMenu(vv).show(vv, e.getX(), e.getY());
				}
			}
		}
	}

	public JPopupMenu getVertexPopup() {
		return vertexPopup;
	}

	public void setVertexPopup(JPopupMenu vertexPopup) {
		this.vertexPopup = vertexPopup;
	}

	private JPopupMenu createVertexPopupMenu(V v, VisualizationViewer<V, E> vv) {
		Graph<V, E> g = vv.getGraphLayout().getGraph();
		JPopupMenu vertexMenu = new JPopupMenu("Vertex menu");

		vertexMenu.add(createDeleteVertex(v, vv, g));

		if (v.getClass() == Event.class)
		{
			vertexMenu.add(createChangeEventype((Event) v, vv, g));
			vertexMenu.add(createSetName((Event) v, vv, g));
			vertexMenu.add(createSetDescription((Event) v, vv, g));
			vertexMenu.add(createSetProbability((Event) v, vv, g));
		}
		else if (v.getClass() == Gate.class)
			vertexMenu.add(createChangeGatetype((Gate) v, vv, g));

		return vertexMenu;
	}

	private JPopupMenu createGeneralPopupMenu(VisualizationViewer<V, E> vv) {
		Graph<V, E> g = vv.getGraphLayout().getGraph();
		JPopupMenu menu = new JPopupMenu("Menu");

		menu.add(createDoLayout(vv, g));

		return menu;
	}

	private JMenuItem createDoLayout(VisualizationViewer<V, E> vv, Graph<V, E> g) {
		JMenuItem deleteVertex = new JMenuItem();
		AbstractAction deleteAction = new AbstractAction("Розташувати") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				TreeLayout<V, E> layout = new TreeLayout<V, E>((Forest<V, E>) g);
				vv.setGraphLayout(layout);
				vv.getGraphLayout().initialize();
				vv.repaint();
			}
		};
		deleteVertex.setAction(deleteAction);
		return deleteVertex;
	}

	private JMenuItem createDeleteVertex(V v, VisualizationViewer<V, E> vv, Graph<V, E> g) {
		JMenuItem deleteVertex = new JMenuItem("Видалити");
		AbstractAction deleteAction = new AbstractAction("Видалити") {
			private static final long serialVersionUID = 5410126442976698368L;

			public void actionPerformed(ActionEvent e) {
				g.removeVertex(v);
				vv.repaint();
			}
		};
		deleteVertex.setAction(deleteAction);
		return deleteVertex;
	}

	private JMenu createChangeEventype(Event v, VisualizationViewer<V, E> vv, Graph<V, E> g) {
		JMenu menu = new JMenu("Змінити на");
		for (PrimaryEvents event : PrimaryEvents.values()) {
			JMenuItem item = new JMenuItem(event.toString());
			AbstractAction action = new AbstractAction(event.toString()) {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
					v.setEvent(event);
					vv.repaint();
				}
			};
			item.setAction(action);
			menu.add(item);
		}
		return menu;
	}
	private JMenu createChangeGatetype(Gate v, VisualizationViewer<V, E> vv, Graph<V, E> g) {
		JMenu menu = new JMenu("Змінити на");
		for (Gates gate : Gates.values()) {
			JMenuItem item = new JMenuItem(gate.toString());
			AbstractAction action = new AbstractAction(gate.toString()) {
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
					v.setGate(gate);
					vv.repaint();
				}
			};
			item.setAction(action);
			menu.add(item);
		}
		return menu;
	}
	private JMenuItem createSetName(Event v, VisualizationViewer<V, E> vv, Graph<V, E> g) {
		JMenuItem item = new JMenuItem();
		AbstractAction action = new AbstractAction("Задати ім'я") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				String name= JOptionPane.showInputDialog("Введіть коротке ім'я: ");
				if(null != name)
				{
					v.setShortName(name);
					vv.repaint();
				}
			}
		};
		item.setAction(action);
		return item;
	}
	private JMenuItem createSetDescription(Event v, VisualizationViewer<V, E> vv, Graph<V, E> g) {
		JMenuItem item = new JMenuItem();
		AbstractAction action = new AbstractAction("Задати опис") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				String description= JOptionPane.showInputDialog("Введіть опис події: ");
				if(null != description)
				{
					v.setDescription(description);
					vv.repaint();
				}
			}
		};
		item.setAction(action);
		return item;
	}
	private JMenuItem createSetProbability(Event v, VisualizationViewer<V, E> vv, Graph<V, E> g) {
		JMenuItem item = new JMenuItem();
		AbstractAction action = new AbstractAction("Задати оцінку ймовірності") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				String name= JOptionPane.showInputDialog("Введіть оцінку ймовірності події: ");
				if(null != name)
				{
					try {
						double p = Double.parseDouble(name);
						v.setProbability(p);
						vv.repaint();
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null, "Неправильно введене значення", "Помилка", JOptionPane.ERROR_MESSAGE);
					} catch (InvalidParameterException e2) {
						JOptionPane.showMessageDialog(null, "Ймовірність має бути в межах відрізка [0,1]", "Помилка", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		};
		item.setAction(action);
		return item;
	}
}
