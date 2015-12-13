import edu.uci.ics.jung.graph.DirectedSparseGraph;

public class Helper {
	public static <V,E> V getRoot(DirectedSparseGraph<V, E> g)
	{
		for (V iterable_element : g.getVertices()) {
			if(g.getPredecessorCount(iterable_element) == 0)
			{
				return iterable_element;
			}
		}
		return null;
	}

}
