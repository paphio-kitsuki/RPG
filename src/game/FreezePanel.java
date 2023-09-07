package game;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JPanel;

public class FreezePanel extends JPanel {

	private ArrayList<java.lang.Object> stopper = new ArrayList<>();

	public boolean isPlaying() {
		return stopper.isEmpty();
	}

	public void suspend(java.lang.Object o) {
		if (stopper.isEmpty()) {
			for (Component c : getComponents())
				c.setVisible(false);
		}
		if (!stopper.contains(o)) {
			stopper.add(o);
		}
	}

	public void start(java.lang.Object o) {
		if (stopper.contains(o)) {
			stopper.remove(o);
		}
		if (stopper.isEmpty()) {
			for (Component c : getComponents())
				c.setVisible(true);
		}
	}

	public java.lang.Object[] getStoppers() {
		java.lang.Object out[] = new java.lang.Object[stopper.size()];
		stopper.toArray(out);
		return out;
	}

}
