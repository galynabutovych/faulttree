
public class Event extends MyVertex {
	private PrimaryEvents event;
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

}
