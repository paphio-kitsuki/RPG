package utility;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import game.FrameUpdateListener;

public class KeyList extends ArrayList<Integer> implements KeyListener {

	public static int interval = 10000000;//≒∞

	private HashMap<Integer, Long> playedList = new HashMap<>();
	private ArrayList<FrameUpdateListener> update = new ArrayList<>();
	private ArrayList<KeyListener> child = new ArrayList<>();

	public KeyList() {
		super();
	}

	public boolean containsUp() {
		if (contains(KeyEvent.VK_UP) || contains(KeyEvent.VK_W))
			return true;
		return false;
	}

	public boolean containsDown() {
		if (contains(KeyEvent.VK_DOWN) || contains(KeyEvent.VK_S))
			return true;
		return false;
	}

	public boolean containsLeft() {
		if (contains(KeyEvent.VK_LEFT) || contains(KeyEvent.VK_A))
			return true;
		return false;
	}

	public boolean containsRight() {
		if (contains(KeyEvent.VK_RIGHT) || contains(KeyEvent.VK_D))
			return true;
		return false;
	}

	public boolean containsEnter() {
		if ((contains(KeyEvent.VK_ENTER) || contains(KeyEvent.VK_Z)) && isPlayableKey(KeyEvent.VK_ENTER)) {
			playKey(KeyEvent.VK_ENTER);
			return true;
		}
		return false;
	}

	public boolean containsBack() {
		if (contains(KeyEvent.VK_X) && isPlayableKey(KeyEvent.VK_X)) {
			playKey(KeyEvent.VK_X);
			return true;
		}
		return false;
	}

	public boolean containsMenu() {
		if (contains(KeyEvent.VK_C) && isPlayableKey(KeyEvent.VK_C)) {
			playKey(KeyEvent.VK_C);
			return true;
		}
		return false;
	}

	public boolean containsOnlyEnter() {
		if (contains(KeyEvent.VK_ENTER) && isPlayableKey(KeyEvent.VK_ENTER)) {
			playKey(KeyEvent.VK_ENTER);
			return true;
		}
		return false;
	}

	public boolean containsEspace() {
		if (contains(KeyEvent.VK_ESCAPE) && isPlayableKey(KeyEvent.VK_ESCAPE)) {
			playKey(KeyEvent.VK_ESCAPE);
			return true;
		}
		return false;
	}

	private boolean isPlayableKey(Integer key) {
		if (!playedList.containsKey(key))
			return true;
		HashMap<Integer, Long> tmp = new HashMap<>();
		try {
			tmp.putAll(playedList);
		} catch (ConcurrentModificationException e) {
			return false;
		}
		if (!tmp.containsKey(key) || tmp.get(key) + interval <= System.currentTimeMillis())
			return true;
		return false;
	}

	private void playKey(Integer key) {
		if (playedList.containsKey(key))
			playedList.replace(key, System.currentTimeMillis());
		else
			playedList.put(key, System.currentTimeMillis());
	}

	private void resetKey(Integer key) {
		if (key.intValue() == KeyEvent.VK_Z || key.intValue() == KeyEvent.VK_ENTER) {
			if (contains(KeyEvent.VK_Z) || contains(KeyEvent.VK_ENTER))
				return;
			key = KeyEvent.VK_ENTER;
		}
		if (playedList.containsKey(key))
			playedList.remove(key);
	}

	public void addUpdateListener(FrameUpdateListener l) {
		update.add(l);
	}

	public void removeUpdateListener(FrameUpdateListener l) {
		update.remove(l);
	}

	public void addChildKeyListener(KeyListener l) {
		child.add(l);
	}

	public void removeChildKeyListener(KeyListener l) {
		child.remove(l);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		for (int i = 0; i < child.size(); i++)
			child.get(i).keyTyped(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!this.contains(e.getKeyCode())) {
			this.add(e.getKeyCode());
			for (int i = 0; i < update.size(); i++)
				update.get(i).addFrame(this);
		}
		for (int i = 0; i < child.size(); i++)
			child.get(i).keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (this.contains(e.getKeyCode())) {
			this.remove((Integer) e.getKeyCode());
			for (int i = 0; i < update.size(); i++)
				update.get(i).addFrame(this);
			for (int i = 0; i < child.size(); i++)
				child.get(i).keyReleased(e);
		}
		resetKey(e.getKeyCode());
	}

}
