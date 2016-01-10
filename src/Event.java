import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections15.MultiMap;
import org.apache.commons.collections15.multimap.MultiHashMap;

import com.ibm.icu.impl.StringPrepDataReader;

public class Event extends MyVertex {
	/*!
	 * Every Event has unique id and not necessarily unique name.
	 * Every uniquely named event has its description and probability stored in maps.
	 */
	private PrimaryEvents event;
	
	final private static Map<String, String> descriptionsByName = new HashMap<String, String>();
	final private static Map<String, Double> probabilitiesByName = new HashMap<String,Double>();
	
	Event(String name)
    {
        super(name);
    }
	Event(int id, String name)
    {
        super(id, name);
    }
	Event(PrimaryEvents event, String name)
    {
        super(name);
        this.event = event;
    }
	public PrimaryEvents getEvent() {
		return event; 
	}
	public void setEvent(PrimaryEvents event) {
		this.event = event;
	}
	public double getProbability() {
		return probabilitiesByName.get(name) != null ? probabilitiesByName.get(name) : 0;
	}
	public void setProbability(double probability) {
		if(probability < 0 || probability > 1)
			throw new InvalidParameterException();
		probabilitiesByName.put(name, probability);
	}
	public String getDescription() {
		return descriptionsByName.get(name) != null ? descriptionsByName.get(name) : "";
	}
	public void setDescription(String description) {
		descriptionsByName.put(name, description);
	}
	public String getShortName() {
		return name;
	}
	public void setShortName(String shortName) {
		if(!descriptionsByName.containsKey(shortName))
		{
			descriptionsByName.put(shortName, descriptionsByName.get(name));
			probabilitiesByName.put(shortName, probabilitiesByName.get(name));
			//TODO: clear unused values in maps
		}
		name = shortName;
	}
	@Override
	String getToolTip() {
		return "<html><center>"
				+ (name != null ? "<p><b>Ім'я</b>: " + name + "</p>" : "")
				+ "<p><b>Оцінка ймовірності:</b> " + ((Double)(probabilitiesByName.get(name) != null ? probabilitiesByName.get(name) : 0)).toString()+ "</p>"
				+ (descriptionsByName.get(name) != null ? "<p><b>Опис</b>: " + descriptionsByName.get(name) + "</p>" : "")
                + "</center></html>";
	}
	static void clear()
	{
		descriptionsByName.clear();
		probabilitiesByName.clear();
	}
}
