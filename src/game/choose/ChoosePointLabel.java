package game.choose;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import utility.KeyList;

public class ChoosePointLabel extends ChooseLabel {

	private Point points[];
	private HashMap<Integer, Long> pastKeyList = new HashMap<>();
	private final long resetTime;

	public ChoosePointLabel(Point points[]) {
		super();
		resetTime = 300;
		this.points = points;
		setSize(points.length);
		setup();
	}

	public ChoosePointLabel(Point points[], long resetTime) {
		super();
		this.resetTime = resetTime;
		this.points = points;
		setSize(points.length);
		setup();
	}

	private void setup() {
		setOpaque(false);
		setText("▽");
		setForeground(Color.WHITE);
		setSize(15, 15);
	}

	public void decrease() {
		if (size > 0) {
			setSize(size - 1);
			resetIndex();
			updateLocation();
		}
	}

	public void updateLocation() {
		if (size != 0)
			setLocation(points[getChooseIndex()]);
	}

	public void setLocation(Point p) {
		super.setLocation(p.x - getWidth() / 2, p.y - getHeight());
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		updateLocation();
	}

	@Override
	public void addFrame(KeyList e) {
		if (!isSelectable())
			return;
		if (!(e.containsLeft() && e.containsRight())) {
			if (e.containsLeft()) {
				if (!pastKeyList.containsKey(KeyEvent.VK_LEFT)
						|| pastKeyList.get(KeyEvent.VK_LEFT) + resetTime <= System.currentTimeMillis()) {
					pastKeyList.remove(KeyEvent.VK_LEFT);
					pastKeyList.put(KeyEvent.VK_LEFT, System.currentTimeMillis());
					addIndexCount(-1);
				}
			} else {
				pastKeyList.remove(KeyEvent.VK_LEFT);
			}
			if (e.containsRight()) {
				if (!pastKeyList.containsKey(KeyEvent.VK_RIGHT)
						|| pastKeyList.get(KeyEvent.VK_RIGHT) + resetTime <= System.currentTimeMillis()) {
					pastKeyList.remove(KeyEvent.VK_RIGHT);
					pastKeyList.put(KeyEvent.VK_RIGHT, System.currentTimeMillis());
					addIndexCount(1);
				}
			} else {
				pastKeyList.remove(KeyEvent.VK_RIGHT);
			}
		}
		super.addFrame(e);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

}
