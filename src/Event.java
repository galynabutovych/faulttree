import java.security.InvalidParameterException;

public class Event extends MyVertex {
	private PrimaryEvents event;
	private String description;
	private double probability;
	
	Event(String name)
    {
        super(name);
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
		return probability;
	}
	public void setProbability(double probability) {
		if(probability < 0 || probability > 1)
			throw new InvalidParameterException();
		this.probability = probability;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getShortName() {
		return name;
	}
	public void setShortName(String shortName) {
		name = shortName;
	}
	@Override
	String getToolTip() {
		return "<html><center>"
				+ (name != null ? "<p><b>Ім'я</b>: " + name + "</p>" : "")
				+ "<p><b>Оцінка ймовірності:</b> " + ((Double)probability).toString()+ "</p>"
				+ (description != null ? "<p><b>Опис</b>: " + description + "</p>" : "")
                + "</center></html>";
	}
}
