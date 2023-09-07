package game.choose;

public class ChosenEvent {

	private final Object source;
	private final String index;

	public ChosenEvent(Object s, String index) {
		this.source = s;
		this.index = index;
	}

	public ChosenEvent(Object s, int index) {
		this.source = s;
		this.index = String.valueOf(index);
	}

	public java.lang.Object getSource() {
		return source;
	}

	public int getIndex() {
		try {
			return Integer.parseInt(index);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	public String getString() {
		return index;
	}

}
