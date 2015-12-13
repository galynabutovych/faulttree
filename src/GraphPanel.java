import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.GraphMLMetadata;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;
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
import javax.swing.Icon;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.collections15.Transformer;

import javax.swing.JSeparator;

public class GraphPanel extends JPanel {

	private static final long serialVersionUID = -8752440317807009185L;
	private  DirectedSparseGraph<MyVertex, Integer> tree;
	private DelegateTree<MyVertex, Integer> td;
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
	private JButton btnCalculate;

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

		JLabel lblAddNode = new JLabel("Додати базисну подію");
		lblAddNode.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblAddNode);

		listPrimaryEvents = new JList<PrimaryEvents>();
		panel.add(listPrimaryEvents);

		JButton btnAdd = new JButton("Додати");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (listPrimaryEvents.getSelectedIndex() != -1) {
					PrimaryEvents event = PrimaryEvents.values()[listPrimaryEvents.getSelectedIndex()];
					if (tree.getVertexCount() == 0)
						// create root
					{
						Event v = new Event(event, "");
						tree.addVertex(v);
					} else {
						final PickedState<MyVertex> pickedState = vv.getPickedVertexState();
						Set<MyVertex> vertices = pickedState.getPicked();
						if (vertices.size() == 1) {
							MyVertex source = vertices.iterator().next();
							Event target = new Event(event, "");

							if (source.getClass() == Event.class) {
								// add intermediate default gate
								Collection<MyVertex> children = tree.getSuccessors(source);
								if (children.isEmpty()) {
									Gate gate = new Gate(Gates.AND, "");
									tree.addEdge(MyEdgeFactory.getInstance().create(), source, gate);
									source = gate;
								} else {
									MyVertex v = children.iterator().next();
									if (v.getClass() == Gate.class)
										source = v;
								}
							}
							tree.addEdge(MyEdgeFactory.getInstance().create(), source, target);
						}
						// get one picked vertex and add child
					}
					TreeLayout<MyVertex, Integer> layout = new TreeLayout<MyVertex, Integer>(td);
					vv.setGraphLayout(layout);
					vv.getGraphLayout().initialize();
					vv.repaint();
				}
			}
		});
		panel.add(btnAdd);

		listPrimaryEvents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		separator = new JSeparator();
		panel_3.add(separator);

		btnCalculate = new JButton("Розрахувати");
		panel_3.add(btnCalculate);
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MyVertex root = Helper.getRoot(tree);
				if (root != null)
					calculateTopProbability(root);
			}
		});

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

	private void populatePrimaryEvents() {
		listPrimaryEvents.setListData(PrimaryEvents.values());
	}

	private void createGraph() {
		tree = new DirectedSparseGraph<MyVertex, Integer>();
		setGraph(tree);
	}
	
	double calculateTopProbability(MyVertex v) {
		double probability = 0;
		if (v.getClass() == Event.class)
			probability = ((Event) v).getProbability();
		else if (v.getClass() == Gate.class) {
			switch (((Gate) v).getGate()) {
			case AND:
				probability = 1;
				break;
			case OR:
				probability = 0;
				break;
			}
		}
		Iterator<Integer> i = tree.getOutEdges(v).iterator();
		while (i.hasNext()) {
			double p = calculateTopProbability(tree.getOpposite(v, i.next()));
			if (v.getClass() == Gate.class) {
				switch (((Gate) v).getGate()) {
				case AND:
					probability = probability * p;
					break;
				case OR:
					probability = probability + p;
					break;
				}
			} else
				probability = p;
		}
		if (v.getClass() == Event.class)
			((Event) v).setProbability(probability);
		return probability;

	}

	public void setGraph(DirectedSparseGraph<MyVertex, Integer> g) {
		System.out.println(g.getVertexCount());
		td = new DelegateTree<MyVertex,Integer>(tree);
		Event root = new Event(PrimaryEvents.INTERMEDIATE_EVENT, "root");
		g.addVertex(root);
		td.setRoot(root);
		TreeLayout<MyVertex,Integer> layout = new TreeLayout<MyVertex,Integer>(td);
		

		graph.setBackground(Color.LIGHT_GRAY);

		vv = new VisualizationViewer<MyVertex, Integer>(layout);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
		vv.getRenderContext().setVertexIconTransformer(new DefaultVertexIconTransformer<>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<MyVertex, Integer>());
		vv.setForeground(Color.blue);
		vv.setBackground(Color.white);
		DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
		// EditingModalGraphMouse<MyVertex, Integer> graphMouse = new
		// EditingModalGraphMouse<MyVertex, Integer>(vv.getRenderContext(),
		// MyVertexFactory.getInstance(),
		// MyEdgeFactory.getInstance());
		PopupVertexEdgeMenuMousePlugin<MyVertex, Integer> myPlugin = new PopupVertexEdgeMenuMousePlugin<MyVertex, Integer>();
		// graphMouse.remove(graphMouse.getPopupEditingPlugin());
		graphMouse.add(myPlugin);
		vv.setGraphMouse(graphMouse);

		vv.getRenderContext().setEdgeDrawPaintTransformer(
				new PickableEdgePaintTransformer<Integer>(this.vv.getPickedEdgeState(), Color.black, Color.red));
		vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.red));
		vv.getRenderContext().setEdgeLabelRenderer(new DefaultEdgeLabelRenderer(Color.red));
		vv.setVertexToolTipTransformer(new ToStringLabeller<MyVertex>());
		vv.setEdgeToolTipTransformer(new ToStringLabeller<Integer>());
		vv.setVertexToolTipTransformer(new ToStringLabeller<MyVertex>() {
			public String transform(MyVertex v) {
				return v.getToolTip();
			}
		});

		PickableVertexPaintTransformer<MyVertex> vpf = new PickableVertexPaintTransformer<MyVertex>(
				vv.getPickedVertexState(), Color.red, Color.yellow);
		vv.getRenderContext().setEdgeDrawPaintTransformer(
				new PickableEdgePaintTransformer<Integer>(vv.getPickedEdgeState(), Color.black, Color.cyan));
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

	void saveAs() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".graphml", "graph ml"));
		fileChooser.setDialogTitle("Specify a file to save");

		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			if(!fileToSave.getAbsolutePath().endsWith(".graphml")){
				fileToSave = new File(fileToSave + ".graphml");
			}
			try {
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileToSave.getAbsoluteFile())));
				GraphMLWriter<MyVertex, Integer> graphWriter = new GraphMLWriter<MyVertex, Integer>();

				graphWriter.setVertexIDs(new Transformer<MyVertex, String>() {
					@Override
					public String transform(MyVertex v)
					{
						return Integer.toString(v.getId());
					}
				});
				graphWriter.addVertexData("name","name of vertex, not nessesary unique","",
						new Transformer<MyVertex, String>() {
					@Override
					public String transform(MyVertex v)
					{
						return Event.class == v.getClass() ? v.toString() : "";
					}
				});
				graphWriter.addVertexData("type", "type of vertex", "",
						new Transformer<MyVertex, String>() {
					@Override
					public String transform(MyVertex v)
					{
						if(Event.class == v.getClass())
							return Event.class.getName();
						else
							return Gate.class.getName();
					}
				});
				graphWriter.addVertexData("subtype", "type of vertex", "",
						new Transformer<MyVertex, String>() {
					@Override
					public String transform(MyVertex v)
					{
						if(Event.class == v.getClass())
							return ((Event)v).getEvent().name();
						else
							return ((Gate)v).getGate().name();
					}
				});
				graphWriter.addVertexData("description", null, "",
						new Transformer<MyVertex, String>() {
					public String transform(MyVertex v) {
						return v.getClass() == Event.class ? ((Event)v).getDescription() : "";
					}
				}
						);
				graphWriter.addVertexData("probability", null, "0",
						new Transformer<MyVertex, String>() {
					public String transform(MyVertex v) {
						return v.getClass() == Event.class ? Double.toString(((Event)v).getProbability()) : "0";
					}
				}
						);
				graphWriter.addVertexData("x", null, "0",
						new Transformer<MyVertex, String>() {
					public String transform(MyVertex v) {
						return Double.toString(vv.getGraphLayout().transform(v).getX());
					}
				}
						);
				graphWriter.addVertexData("y", null, "0",
						new Transformer<MyVertex, String>() {
					public String transform(MyVertex v) {
						return Double.toString(vv.getGraphLayout().transform(v).getY());
					}
				}
						);
				graphWriter.save(tree, out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Save as file: " + fileToSave.getAbsolutePath());
		}
	}
	void loadGraph()
	{
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".graphml", "graph ml"));
		fileChooser.setDialogTitle("Specify a file to open");

		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			BufferedReader fileReader;
			try {
				fileReader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			Event.clear();

			Transformer<GraphMetadata, DelegateForest<MyVertex, Integer>> graphTransformer = new Transformer<GraphMetadata, DelegateForest<MyVertex, Integer>>() {
				public DelegateForest<MyVertex, Integer> transform(GraphMetadata metadata) {
					if (metadata.getEdgeDefault().equals(metadata.getEdgeDefault().DIRECTED)) {
						DelegateForest<MyVertex, Integer> t = new DelegateForest<MyVertex, Integer>();
						return t;
					} else {
						return null;
					}
				}
			};

			Transformer<NodeMetadata, MyVertex> vertexTransformer = new Transformer<NodeMetadata, MyVertex>() {
				public MyVertex transform(NodeMetadata metadata) {
					MyVertex v = null;
					if(metadata.getProperty("type").equals(Event.class.getName()))
					{
						Event e = new Event(Integer.parseInt(metadata.getId()),metadata.getProperty("name"));
						e.setProbability(Double.parseDouble(metadata.getProperty("probability")));
						e.setEvent(PrimaryEvents.valueOf(metadata.getProperty("subtype")));
						e.setDescription(metadata.getProperty("description"));
						v = e;
					}
					else if(metadata.getProperty("type").equals(Gate.class.getName()))
					{
						v = new Gate(Integer.parseInt(metadata.getId()),Gates.valueOf(metadata.getProperty("subtype")),metadata.getProperty("name"));
					}
					//			        v.setX(Double.parseDouble(metadata.getProperty("x")));
					//			        v.setY(Double.parseDouble(metadata.getProperty("y")));
					return v;
				}
			};

			Transformer<EdgeMetadata, Integer> edgeTransformer = new Transformer<EdgeMetadata, Integer>() {
				public Integer transform(EdgeMetadata metadata) {
					Integer e = MyEdgeFactory.getInstance().create();
					return e;
				}
			};
			
			Transformer<HyperEdgeMetadata, Integer> hyperEdgeTransformer = new Transformer<HyperEdgeMetadata, Integer>() {
			    public Integer transform(HyperEdgeMetadata metadata) {
			        Integer e = MyEdgeFactory.getInstance().create();
			        return e;
			    }
			};
			
			Transformer<GraphMetadata, DirectedSparseGraph<MyVertex, Integer>> gTransformer = new Transformer<GraphMetadata, DirectedSparseGraph<MyVertex, Integer>>() {
			    public DirectedSparseGraph<MyVertex, Integer> transform(GraphMetadata metadata) {
			        if (metadata.getEdgeDefault().equals(metadata.getEdgeDefault().DIRECTED)) {
			            return new DirectedSparseGraph<MyVertex, Integer>();
			        } else {
			            return null;
			        }
			    }
			};

			GraphMLReader2<DirectedSparseGraph<MyVertex, Integer>, MyVertex, Integer> graphReader = new GraphMLReader2<DirectedSparseGraph<MyVertex, Integer>, MyVertex, Integer>(fileReader, gTransformer, vertexTransformer, 
edgeTransformer,hyperEdgeTransformer);

			try {
				/* Get the new graph object from the GraphML file */
				tree = graphReader.readGraph();
				td = new DelegateTree<MyVertex,Integer>(tree);
				td.setRoot(Helper.getRoot(tree));
				TreeLayout<MyVertex, Integer> layout = new TreeLayout<MyVertex, Integer>(td);
				vv.setGraphLayout(layout);
				vv.getGraphLayout().initialize();
				vv.repaint();
			} catch (GraphIOException ex) {}
		}
	}
}
