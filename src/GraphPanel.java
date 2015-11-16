import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JComboBox;

import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.DefaultVertexIconTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.decorators.VertexIconShapeTransformer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.JSeparator;

public class GraphPanel extends JPanel {

	private static final long serialVersionUID = -8752440317807009185L;
	private DelegateForest<MyVertex, Integer> tree;
	private VisualizationViewer<MyVertex, Integer> vv;
	final private JPanel graph;
	final private JList<PrimaryEvents> listPrimaryEvents;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel modePanel;
	private JPanel scaleGrid;
	private JPanel panel_3;
	private JButton btnDoLayout;
	private JSeparator separator;

	/**
	 * Create the panel.
	 */
	public GraphPanel() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		panel_1 = new JPanel();
		add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

		graph = new JPanel();
		panel_1.add(graph);

		panel_2 = new JPanel();
		panel_1.add(panel_2);
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		modePanel = new JPanel();
		modePanel.setBorder(BorderFactory.createTitledBorder("Mouse Mode"));
		panel_2.add(modePanel);

		scaleGrid = new JPanel();
		scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));
		panel_2.add(scaleGrid);

		panel_3 = new JPanel();
		panel_3.setAlignmentX(0.0f);
		panel_3.setAlignmentY(0.0f);
		panel_3.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel_3.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel lblAddNode = new JLabel("Add Child");
		lblAddNode.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblAddNode);

		listPrimaryEvents = new JList<PrimaryEvents>();
		panel.add(listPrimaryEvents);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("clicked");
				if(listPrimaryEvents.getSelectedIndex() != -1)
				{
					PrimaryEvents event = PrimaryEvents.values()[listPrimaryEvents.getSelectedIndex()];
					if(tree.getVertexCount()==0)
						// create root
					{
						tree.addVertex(new Event(event,"no name"));
					}
					else
					{
						final PickedState<MyVertex> pickedState = vv.getPickedVertexState();
						Set<MyVertex> vertices = pickedState.getPicked();
						if(vertices.size() == 1)
						{
							MyVertex source = vertices.iterator().next();
							Event target = new Event(event,"no name");

							if(source.getClass() == Event.class)
							{
								//add intermediate default gate
								Collection<MyVertex> children = tree.getChildren(source);
								if(children.isEmpty())
								{
									Gate gate = new Gate(Gates.AND,"");
									tree.addEdge(MyEdgeFactory.getInstance().create(),source,gate);
									source = gate;
								}
								else
								{
									MyVertex v = children.iterator().next();
									if(v.getClass() == Gate.class)
										source = v;
								}
							}
							tree.addEdge(MyEdgeFactory.getInstance().create(),source,target);
							vv.getGraphLayout().initialize();
							vv.repaint();
						}
						//get one picked vertex and add child
					}
				}
			}
		});
		panel.add(btnAdd);

		listPrimaryEvents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		separator = new JSeparator();
		panel_3.add(separator);

		final CrossoverScalingControl scaler = new CrossoverScalingControl();
		JButton plus = new JButton("+");
		plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1.1F, vv.getCenter());
			}
		});
		JButton minus = new JButton("-");
		minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 0.9090909F, vv.getCenter());
			}
		});
		scaleGrid.add(plus);
		scaleGrid.add(minus);

		createGraph();

		populatePrimaryEvents();
	}

	private void populatePrimaryEvents()
	{
		listPrimaryEvents.setListData(PrimaryEvents.values());
	}

	private void createGraph()
	{
		tree = new DelegateForest<MyVertex,Integer>();
		setGraph(tree);
	}

	public void setGraph(DelegateForest<MyVertex, Integer> g) {
		TreeLayout<MyVertex,Integer> layout = new TreeLayout<MyVertex,Integer>((Forest<MyVertex,Integer>)g);

		graph.setBackground(Color.LIGHT_GRAY);

		vv = new VisualizationViewer<MyVertex, Integer>(layout);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
		vv.getRenderContext().setVertexIconTransformer(new DefaultVertexIconTransformer<>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		vv.getRenderContext().setEdgeShapeTransformer(
				new EdgeShape.Line<MyVertex,Integer>());
		vv.setForeground(Color.magenta);
		vv.setBackground(Color.white);
		DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
		//		EditingModalGraphMouse<MyVertex, Integer> graphMouse = new EditingModalGraphMouse<MyVertex, Integer>(vv.getRenderContext(), 
		//				MyVertexFactory.getInstance(),
		//				MyEdgeFactory.getInstance());
		PopupVertexEdgeMenuMousePlugin<MyVertex, Integer> myPlugin = new PopupVertexEdgeMenuMousePlugin<MyVertex, Integer>();
		//		graphMouse.remove(graphMouse.getPopupEditingPlugin());
		graphMouse.add(myPlugin);
		vv.setGraphMouse(graphMouse);

		vv.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<Integer>(this.vv.getPickedEdgeState(), Color.black, Color.cyan));
		vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.cyan));
		vv.getRenderContext().setEdgeLabelRenderer(new DefaultEdgeLabelRenderer(Color.cyan));
		vv.setVertexToolTipTransformer(new ToStringLabeller<MyVertex>());
		vv.setEdgeToolTipTransformer(new ToStringLabeller<Integer>());

		PickableVertexPaintTransformer<MyVertex> vpf = 
				new PickableVertexPaintTransformer<MyVertex>(vv.getPickedVertexState(), Color.red, Color.yellow);
		vv.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<Integer>(vv.getPickedEdgeState(), Color.black, Color.cyan));
		vv.getRenderContext().setVertexFillPaintTransformer(vpf);


		IconVertexIconTransformer<MyVertex> vertexIconFunction = new IconVertexIconTransformer<MyVertex>();
		VertexIconShapeTransformer<MyVertex> vertexIconShapeFunction = new CustomVertexIconShapeTransformer<MyVertex>();
		vv.getRenderContext().setVertexShapeTransformer(vertexIconShapeFunction);
		vv.getRenderContext().setVertexIconTransformer(vertexIconFunction);

		graphMouse.setMode(Mode.PICKING);

		JComboBox modeBox = graphMouse.getModeComboBox();
		modePanel.add(modeBox);
		graph.setLayout(new BoxLayout(graph, BoxLayout.X_AXIS));

		graph.add(vv);
	}
}
