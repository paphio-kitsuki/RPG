package game.choose;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import game.FreezePanel;
import game.GamePanel;
import utility.KeyList;
import utility.MyColor;

public abstract class MovingChooseLabel extends ChooseLabel {

	protected final int max = 200 * GamePanel.FPS / 1000;
	protected int count = 1;
	private FreezePanel destroy = null;

	public void setVisible(boolean visible) {
		if (visible != isVisible()) {
			if (visible) {
				count = 1;
			}
		}
		super.setVisible(visible);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (0 < count && count < max) {
			double width = (double) (getWidth() * count) / max;
			double height = (double) (getHeight() * count) / max;
			g2.setColor(MyColor.toAlpha(Color.BLACK, 150));
			g2.draw(new Rectangle2D.Double((getWidth() - width - 2) / 2, (getHeight() - height - 2) / 2, width + 2,
					height + 2));
			g2.setColor(MyColor.toAlpha(Color.WHITE, 150));
			g2.fill(new Rectangle2D.Double((getWidth() - width) / 2, (getHeight() - height) / 2, width, height));
		}
	}

	public void reborn() {
		super.reborn();
		count = 1;
	}

	public void addFrame(KeyList key) {
		if (isDestroying()) {
			count--;
			if (count <= 0) {
				super.destroy(destroy);
			}
		} else if (count < max && isVisible()) {
			count++;
		}
	}

	protected void endOfAddFrame(KeyList key) {
		super.addFrame(key);
	}

	public void destroy(FreezePanel g) {
		destroy = g;
		count = max;
		isDestroying = true;
	}

}
