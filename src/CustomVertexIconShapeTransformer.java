import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.FourPassImageShaper;
import edu.uci.ics.jung.visualization.decorators.VertexIconShapeTransformer;

public class CustomVertexIconShapeTransformer<V> extends VertexIconShapeTransformer<V> implements Transformer<V, Shape> {
	public CustomVertexIconShapeTransformer() {
		super(null);
	}

	@Override
	public Shape transform(V v) {
		if(v.getClass() == Event.class)
		{
			switch (((Event)v).getEvent()) {
			case BASIC_EVENT:
				return transformIcon(IconVertexIconTransformer.getBasic_event());
			case CONDITIONING_EVENT:
				return transformIcon(IconVertexIconTransformer.getConditioning_event());
			case EXTERNAL_EVENT:
				return transformIcon(IconVertexIconTransformer.getExternal_event());
			case INTERMEDIATE_EVENT:
				return  transformIcon(IconVertexIconTransformer.getIntermediate_event());
			case UNDEVELOPED_EVENT:
				return transformIcon(IconVertexIconTransformer.getUndeveloped_event());
			default:
				return null;
			}
		}
		else if(v.getClass() == Gate.class)
			switch (((Gate)v).getGate()) {
			case AND:
				return transformIcon(IconVertexIconTransformer.getAnd_gate());
			case OR:
				return transformIcon(IconVertexIconTransformer.getOr_gate());
			case XOR:
				return transformIcon(IconVertexIconTransformer.getXor_gate());
			case PRIORITY_AND:
				return transformIcon(IconVertexIconTransformer.getPriority_and_gate());
			case INHIBIT:
				return transformIcon(IconVertexIconTransformer.getInhibit_gate());
			default:
				return null;
			}
		else
			return null;
	}

	private Shape transformIcon(Icon icon) {
		if (icon != null && icon instanceof ImageIcon) {
			Image image = ((ImageIcon) icon).getImage();
			Shape shape = (Shape) shapeMap.get(image);
			if (shape == null) {
				shape = FourPassImageShaper.getShape(image);
				if(shape.getBounds().getWidth() > 0 && 
						shape.getBounds().getHeight() > 0) {
					int width = image.getWidth(null);
					int height = image.getHeight(null);
					AffineTransform transform = AffineTransform
							.getTranslateInstance(-width / 2, -height / 2);
					shape = transform.createTransformedShape(shape);
					shapeMap.put(image, shape);
				}
			}
			return shape;
		}
		return null;
	}
}
