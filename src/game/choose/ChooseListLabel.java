package game.choose;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import game.GamePanel;
import utility.KeyList;
import utility.MyColor;

public class ChooseListLabel extends MovingChooseLabel {

	private static int offsetY = 30;
	private static int interval = 200;

	private ArrayList<String> labels = new ArrayList<>();
	private int charHeight;
	private HashMap<Integer, Long> pastKeyList = new HashMap<>();
	private final long resetTime;
	private long frameCount = 0;

	public ChooseListLabel() {
		super();
		resetTime = 300;
		setup();
	}

	public ChooseListLabel(long resetTime) {
		super();
		this.resetTime = resetTime;
		setup();
	}

	private void setup() {
		setOpaque(false);
		setFont(new Font("", Font.PLAIN, 30));
	}

	public void setFont(Font f) {
		super.setFont(f);
		charHeight = getFontMetrics(getFont()).getHeight();
	}

	public void addLabel(String text) {
		labels.add(text);
		fixSize();
		addIndexCount(0);
	}

	public void removeLabel(String text) {
		labels.remove(text);
		fixSize();
		addIndexCount(0);
	}

	public void clearLabels() {
		labels.clear();
		fixSize();
		resetIndex();
	}

	private void fixSize() {
		int width = 0;
		for (String s : labels) {
			width = Math.max(width, getFontMetrics(getFont()).stringWidth(s));
		}
		setSize(width, charHeight * labels.size());
		setSize(labels.size());
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (0 < count && count < max)
			return;
		for (int i = 0; i < labels.size(); i++) {
			if (getChooseIndex() == i
					&& (!isSelectable() || ((double)frameCount * 1000 / GamePanel.FPS) < interval))
				g2.setColor(MyColor.toAlpha(Color.LIGHT_GRAY, 150));
			else if (!isSelectable(i))
				g2.setColor(MyColor.toAlpha(Color.BLACK, 150));
			else
				g2.setColor(MyColor.toAlpha(Color.WHITE, 150));
			g2.fill(new Rectangle2D.Double(0, i * charHeight, getWidth(), charHeight));
			if (getChooseIndex() == i
					&& (!isSelectable() || ((double)frameCount * 1000 / GamePanel.FPS) < interval))
				g2.setColor(Color.BLACK);
			else
				g2.setColor(Color.DARK_GRAY);
			g2.drawString(labels.get(i), 0, offsetY + i * charHeight);
		}
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(2));
		g2.draw(new Rectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2));
	}

	@Override
	public void addFrame(KeyList e) {
		int tmp = count;
		super.addFrame(e);
		if (isDestroying() || (tmp < max && isVisible()))
			return;
		frameCount++;
		if ((double)frameCount * 1000 / GamePanel.FPS >= interval * 2)
			frameCount = 0;
		if (!isSelectable())
			return;
		if (!(e.containsUp() && e.containsDown())) {
			if (e.containsUp()) {
				if (!pastKeyList.containsKey(KeyEvent.VK_UP)
						|| pastKeyList.get(KeyEvent.VK_UP) + resetTime <= System.currentTimeMillis()) {
					pastKeyList.remove(KeyEvent.VK_UP);
					pastKeyList.put(KeyEvent.VK_UP, System.currentTimeMillis());
					addIndexCount(-1);
				}
			} else {
				pastKeyList.remove(KeyEvent.VK_UP);
			}
			if (e.containsDown()) {
				if (!pastKeyList.containsKey(KeyEvent.VK_DOWN)
						|| pastKeyList.get(KeyEvent.VK_DOWN) + resetTime <= System.currentTimeMillis()) {
					pastKeyList.remove(KeyEvent.VK_DOWN);
					pastKeyList.put(KeyEvent.VK_DOWN, System.currentTimeMillis());
					addIndexCount(1);
				}
			} else {
				pastKeyList.remove(KeyEvent.VK_DOWN);
			}
		}
		super.endOfAddFrame(e);
	}

}
