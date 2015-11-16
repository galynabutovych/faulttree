
public class Gate extends MyVertex {
	private Gates gate;
	Gate(String name)
    {
        super(name);
    }
	Gate(Gates gate, String name)
    {
        super(name);
        this.gate = gate;
    }
	public Gates getGate() {
		return gate;
	}
	public void setGate(Gates gate) {
		this.gate = gate;
	}
	@Override
	public String toString() {
		return gate.name();
	}
}
