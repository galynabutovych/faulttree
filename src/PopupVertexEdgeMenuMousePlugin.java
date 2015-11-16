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
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
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
		final VisualizationViewer<V,E> vv = (VisualizationViewer<V,E>)e.getSource();
		if(null == vv) 
			return;
		Point2D p = e.getPoint();

		GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
		if(pickSupport != null) {
			final V v = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
			if(v != null) {
				JPopupMenu vertexPopup = createVertexPopupMenu(v,vv);
				vertexPopup.show(vv, e.getX(), e.getY());
			} else {
				final E edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(), p.getY());
				// edge not supported
				if(null == edge)
				{
					//blank field: general menu
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

	private JPopupMenu createVertexPopupMenu(V v,VisualizationViewer<V,E> vv)
	{
		Graph<V, E> g = vv.getGraphLayout().getGraph();
		JPopupMenu vertexMenu = new JPopupMenu("Vertex menu");

		vertexMenu.add(createDeleteVertex(v, vv, g));
		
		if(v.getClass() == Event.class)
			vertexMenu.add(createChangeEventype((Event)v, vv, g));
		else if(v.getClass() == Gate.class)
			vertexMenu.add(createChangeGatetype((Gate)v, vv, g));
		
		return vertexMenu;
	}
	
	private JPopupMenu createGeneralPopupMenu(VisualizationViewer<V,E> vv)
	{
		Graph<V, E> g = vv.getGraphLayout().getGraph();
		JPopupMenu menu = new JPopupMenu("Menu");

		menu.add(createDoLayout(vv, g));
		
		return menu;
	}
	
	private JMenuItem createDoLayout(VisualizationViewer<V, E> vv, Graph<V, E> g) {
		JMenuItem deleteVertex = new JMenuItem();
		AbstractAction deleteAction = new AbstractAction("Do Layout") {
			public void actionPerformed(ActionEvent arg0) {
				TreeLayout<V,E> layout = new TreeLayout<V,E>((Forest<V,E>)g);
				vv.setGraphLayout(layout);
				vv.getGraphLayout().initialize();
				vv.repaint();
			}
		};
		deleteVertex.setAction(deleteAction);
		return deleteVertex;
	}

	private JMenuItem createDeleteVertex(V v, VisualizationViewer<V, E> vv, Graph<V, E> g) {
		JMenuItem deleteVertex = new JMenuItem("Delete vertex");
		AbstractAction deleteAction = new AbstractAction("Delete vertex") {
			private static final long serialVersionUID = 5410126442976698368L;
			public void actionPerformed(ActionEvent e) {
				g.removeVertex(v);
				vv.repaint();
			}
		};
		deleteVertex.setAction(deleteAction);
		return deleteVertex;
	}
	
	private JMenuItem createChangeEventype(Event v, VisualizationViewer<V, E> vv, Graph<V, E> g) {
		JMenuItem menu = new JMenuItem("Convert to");
		PrimaryEvents[] events = PrimaryEvents.values();
		JComboBox selectType = new JComboBox(events);
		menu.add(selectType);
		AbstractAction deleteAction = new AbstractAction("Delete vertex") {
			private static final long serialVersionUID = 5410126442976698368L;
			public void actionPerformed(ActionEvent e) {
				vv.repaint();
			}
		};
		menu.setAction(deleteAction);
		return menu;
	}
	
	private JMenu createChangeGatetype(Gate v, VisualizationViewer<V, E> vv, Graph<V, E> g) {
		JMenu menu = new JMenu("Convert to");
		Gates[] gates = Gates.values();
		JComboBox selectType = new JComboBox(gates);
		selectType.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	                JComboBox comboBox = (JComboBox) event.getSource();

	                Object selected = comboBox.getSelectedItem();
	                if(selected.getClass() == Gates.class)
	                {
	                	System.out.println("yes");
	                	v.setGate((Gates)selected);
	                	vv.repaint();
	                }
	                else System.out.println("no");
	            }
	        });
		menu.add(selectType);
		return menu;
	}
	
	
}
