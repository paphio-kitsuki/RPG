package utility;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JLabel;

import game.FrameUpdateListener;
import game.FreezePanel;
import game.GamePanel;

public class VolumeLabel extends JLabel implements FrameUpdateListener {

	private int count = 0;
	private int max = 5;
	private int min = 0;
	private boolean isChoosable = false;

	private final int interval = (int) ((double) 200 / 1000 * GamePanel.FPS);
	private int rightCount = 0;
	private int leftCount = 0;

	public VolumeLabel() {
		super();
	}

	public VolumeLabel(int min, int max, int count) {
		super();
		setMaximumCount(max);
		setMinimumCount(min);
		setCount(count);
	}

	public void setMinimumCount(int min) {
		if (min < 0 || min > max)
			return;
		this.min = min;
		if (min > count)
			count = min;
	}

	public void setMaximumCount(int max) {
		if (max < 0 || max < min)
			return;
		this.max = max;
		if (max < count)
			count = max;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		if (count < min || max < count)
			return;
		this.count = count;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		int width = g2.getFontMetrics().stringWidth(getText()) + 15;
		g2.setPaint(new GradientPaint(0, 10, MyColor.toAlpha(Color.CYAN.darker(), 180), 10, 0, MyColor.toAlpha(Color.BLUE.brighter(), 180),
				true));
		for (int i = 0; i < count; i++) {
			g2.fill(new RoundRectangle2D.Double(width, 20, 40, getHeight() - 40, 40, 40));
			width += 40 + 5;
		}
	}

	public void setChoosable(boolean flag) {
		this.isChoosable = flag;
	}

	public void addFrame(KeyList key) {
		if (rightCount > 0)
			rightCount--;
		if (leftCount > 0)
			leftCount--;
		if (isChoosable) {
			if (key.containsRight() && rightCount <= 0) {
				setCount(getCount() + 1);
				rightCount = interval;
			}
			if (key.containsLeft() && leftCount <= 0) {
				setCount(getCount() - 1);
				leftCount = interval;
			}
		}
	}

	@Override
	public boolean isPlayable() {
		return false;
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public boolean isDestroying() {
		return false;
	}

	@Override
	public void destroy(FreezePanel f) {
	}
}
