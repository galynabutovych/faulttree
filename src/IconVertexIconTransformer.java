import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.commons.collections15.Transformer;
import edu.uci.ics.jung.visualization.decorators.DefaultVertexIconTransformer;

public class IconVertexIconTransformer<V> extends DefaultVertexIconTransformer<V> implements Transformer<V, Icon> {
	// events
	static private Icon basic_event = null;
	static private Icon conditioning_event = null;
	static private Icon external_event = null;
	static private Icon intermediate_event = null;
	static private Icon undeveloped_event = null;
	// gates
	static private Icon and_gate = null;
	static private Icon inhibit_gate = null;
	static private Icon or_gate = null;
	static private Icon priority_and_gate = null;
	static private Icon xor_gate = null;

	@Override
	public Icon transform(V v)
	{
		if(v.getClass() == Event.class)
		{
			switch (((Event)v).getEvent()) {
			case BASIC_EVENT:
				return getBasic_event();
//			case CONDITIONING_EVENT:
//				return getConditioning_event();
			case EXTERNAL_EVENT:
				return getExternal_event();
			case INTERMEDIATE_EVENT:
				return  getIntermediate_event();
			case UNDEVELOPED_EVENT:
				return getUndeveloped_event();
			default:
				return null;
			}
		}
		else if(v.getClass() == Gate.class)
			switch (((Gate)v).getGate()) {
			case AND:
				return getAnd_gate();
			case OR:
				return getOr_gate();
//			case XOR:
//				return getXor_gate();
//			case PRIORITY_AND:
//				return getPriority_and_gate();
//			case INHIBIT:
//				return getInhibit_gate();
			default:
				return null;
			}
		else
			return null;
	}

	static public Icon getBasic_event() {
		if(null == basic_event)
			basic_event = new ImageIcon(IconVertexIconTransformer.class.getResource("/resources/basic_event.png"));
		return basic_event;
	}

	static public Icon getConditioning_event() {
		if(null == conditioning_event)
			conditioning_event = new ImageIcon(IconVertexIconTransformer.class.getResource("/resources/conditioning_event.png"));
		return conditioning_event;
	}

	static public Icon getExternal_event() {
		if(null == external_event)
			external_event = new ImageIcon(IconVertexIconTransformer.class.getResource("/resources/external_event.png"));
		return external_event;
	}

	static public Icon getIntermediate_event() {
		if(null == intermediate_event)
			intermediate_event = new ImageIcon(IconVertexIconTransformer.class.getResource("/resources/intermediate_event.png"));
		return intermediate_event;
	}

	static public Icon getUndeveloped_event() {
		if(null == undeveloped_event)
			undeveloped_event = new ImageIcon(IconVertexIconTransformer.class.getResource("/resources/undeveloped_event.png"));
		return undeveloped_event;
	}

	public static Icon getAnd_gate() {
		if(null == and_gate)
			and_gate = new ImageIcon(IconVertexIconTransformer.class.getResource("/resources/and_gate.png"));
		return and_gate;
	}

	public static Icon getInhibit_gate() {
		if(null == inhibit_gate)
			inhibit_gate = new ImageIcon(IconVertexIconTransformer.class.getResource("/resources/inhibit_gate.png"));
		return inhibit_gate;
	}

	public static Icon getOr_gate() {
		if(null == or_gate)
			or_gate = new ImageIcon(IconVertexIconTransformer.class.getResource("/resources/or_gate.png"));
		return or_gate;
	}

	public static Icon getPriority_and_gate() {
		if(null == priority_and_gate)
			priority_and_gate = new ImageIcon(IconVertexIconTransformer.class.getResource("/resources/priority_and_gate.png"));
		return priority_and_gate;
	}

	public static Icon getXor_gate() {
		if(null == xor_gate)
			xor_gate = new ImageIcon(IconVertexIconTransformer.class.getResource("/resources/xor_gate.png"));
		return xor_gate;
	}

}
