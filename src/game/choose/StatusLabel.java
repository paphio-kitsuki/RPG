package game.choose;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import game.FreezePanel;
import game.GamePanel;
import game.status.CharacterStatus;
import game.status.PlayerStatus;
import utility.KeyList;
import utility.MyColor;

public class StatusLabel extends ChooseLabel {

	private static final Dimension size = new Dimension(400, 400);
	private static final int max = 200 * GamePanel.FPS / 1000;

	private CharacterStatus status;
	private FreezePanel destroy;
	private int count = 1;

	public StatusLabel(CharacterStatus status) {
		super();
		this.status = status;
		this.setSize(size);
	}

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
			g2.setColor(MyColor.toAlpha(Color.WHITE, 150));
			double width = (double) (getWidth() * count) / max;
			double height = (double) (getHeight() * count) / max;
			g2.fill(new Rectangle2D.Double((getWidth() - width) / 2, (getHeight() - height) / 2, width, height));
			return;
		}
		g2.setColor(MyColor.toAlpha(Color.WHITE, 200));
		g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
		int y = 100;
		g2.setPaint(Color.DARK_GRAY);
		g2.setStroke(new BasicStroke(2));
		g2.setFont(new Font("", Font.PLAIN, 30));
		g2.draw(new Rectangle2D.Double(0, 0, 400, 40));
		g2.draw(new Rectangle2D.Double(150, 0, 250, 100));
		g2.draw(new Rectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2));
		for (int j = 0; y < size.height; j++) {
			g2.setPaint(Color.DARK_GRAY);
			g2.draw(new Rectangle2D.Double(0, y, 100, 50));
			g2.draw(new Rectangle2D.Double(100, y, 300, 50));
			String label = "";
			switch (j) {
			case 0:
				label = "Lv";
				g2.drawString("" + status.getLevel(), 230, y + 50 - 15);
				break;
			case 1:
				label = "HP";
				g2.drawString(status.getHP() + "　/　" + status.getMaxHP(), 180, y + 50 - 15);
				break;
			case 2:
				label = "MP";
				g2.drawString(status.getMP() + "　/　" + status.getMaxMP(), 180, y + 50 - 15);
				break;
			case 3:
				label = "ATK";
				g2.drawString("" + status.getPower(), 230, y + 50 - 15);
				break;
			case 4:
				label = "DEF";
				g2.drawString("" + status.getGuard(), 230, y + 50 - 15);
				break;
			case 5:
				label = "SPD";
				g2.drawString("" + status.getSpeed(), 230, y + 50 - 15);
				break;
			}
			g2.setPaint(Color.BLACK);
			g2.drawString(label, 15, y + 50 - 15);
			y += 50;
		}
		g2.drawString("NAME", 30, 30);
		g2.drawString(status.getName(), 30, 80);
		if (status instanceof PlayerStatus) {
			g2.drawString("次のLvまで", 200, 30);
			String ex = "" + (((PlayerStatus) status).getExBorder() - status.getEx());
			g2.drawString(ex, (getWidth() + 150 - g2.getFontMetrics().stringWidth(ex)) / 2, 80);
		}

	}

	@Override
	public void addFrame(KeyList e) {
		if (isDestroying()) {
			count--;
			if (count <= 0) {
				super.destroy(destroy);
				isDestroying = false;
			}
			return;
		} else if (count < max && isVisible()) {
			count++;
			return;
		}
		super.addFrame(e);
	}

	@Override
	public void destroy(FreezePanel f) {
		destroy = f;
		count = max;
		isDestroying = true;
	}

}
