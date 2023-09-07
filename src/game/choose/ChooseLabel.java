package game.choose;

import java.util.ArrayList;

import javax.swing.JLabel;

import game.FrameUpdateListener;
import game.FreezePanel;
import utility.KeyList;

public abstract class ChooseLabel extends JLabel implements FrameUpdateListener {

	private int chooseIndex = 0;
	protected int size = 0;
	private boolean isSelectable = true;
	protected ArrayList<ChosenListener> listenerList = new ArrayList<>();
	protected boolean isDestroying = false;
	protected boolean isFinished = false;
	protected boolean selectableList[] = new boolean[0];

	public int getChooseIndex() {
		return chooseIndex;
	}

	public void resetIndex() {
		chooseIndex = 0;
		while (!selectableList[chooseIndex]) {
			chooseIndex++;
			if (chooseIndex >= selectableList.length) {
				chooseIndex = 0;
				break;
			}
		}
	}

	protected void addIndexCount(int count) {
		if (size == 0) {
			chooseIndex = 0;
			return;
		}
		while (count != 0) {
			chooseIndex += count / Math.abs(count);
			if (chooseIndex < 0)
				chooseIndex += size;
			if (chooseIndex >= size)
				chooseIndex -= size;
			if (selectableList[chooseIndex])
				count -= count / Math.abs(count);
		}
	}

	protected void setSize(int size) {
		this.size = size;
		boolean newList[] = new boolean[size];
		for (int i = 0; i < size; i++) {
			if (i < selectableList.length)
				newList[i] = selectableList[i];
			else
				newList[i] = true;
		}
		this.selectableList = newList;
		if (chooseIndex >= size)
			chooseIndex = size - 1;
	}

	public void setSelectable(boolean isSelectable) {
		this.isSelectable = isSelectable;
	}

	public void setSelectable(int index, boolean isSelectable) {
		if (index < 0 || size <= index)
			return;
		this.selectableList[index] = isSelectable;
	}

	public boolean isSelectable() {
		return this.isSelectable;
	}

	public boolean isSelectable(int index) {
		if (index < 0 || size <= index)
			return false;
		return this.selectableList[index];
	}

	public void addChosenListener(ChosenListener l) {
		listenerList.add(l);
	}

	public void removeChosenListener(ChosenListener l) {
		listenerList.remove(l);
	}

	@Override
	public void addFrame(KeyList e) {
		if (!isSelectable())
			return;
		if (e.containsEnter()) {
			for (ChosenListener l : listenerList)
				l.chosen(new ChosenEvent(this, chooseIndex));
		} else if (e.containsBack()) {
			for (ChosenListener l : listenerList)
				l.chosen(new ChosenEvent(this, -1));
		}
	}

	public void reborn() {
		isFinished = false;
		isDestroying = false;
	}

	@Override
	public boolean isFinished() {
		return isFinished;
	}

	@Override
	public void destroy(FreezePanel g) {
		isFinished = true;
		g.remove(this);
		g.start(this);
	}

	@Override
	public boolean isDestroying() {
		return isDestroying;
	}

	@Override
	public boolean isPlayable() {
		return isVisible();
	}

}
