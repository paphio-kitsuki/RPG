package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;

import game.status.CharacterStatus;
import utility.MyColor;

public class ButtleStatusLabel extends JLabel {

	private static final int statusWidth = 120;
	private static final int offsetX = 10;
	private static final int offsetY = 20;

	private CharacterStatus status;
	private int charHeight = 0;

	public ButtleStatusLabel(CharacterStatus status) {
		super();
		this.status = status;
		setOpaque(false);
		setFont(new Font("", Font.PLAIN, 20));
		setSize(statusWidth, charHeight * CharacterStatus.printLines);
	}

	public void setFont(Font f) {
		super.setFont(f);
		charHeight = getFontMetrics(getFont()).getHeight();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		int width = Math.max(statusWidth - offsetX - 5, g2.getFontMetrics().stringWidth(status.getName()));
		width = Math.max(width, g2.getFontMetrics().stringWidth("HP: " + status.getHP() + " / " + status.getMaxHP()));
		width = Math.max(width, g2.getFontMetrics().stringWidth("MP: " + status.getMP() + " / " + status.getMaxMP()));
		width = Math.max(width, g2.getFontMetrics().stringWidth("Lv. " + status.getLevel()));
		setSize(width + offsetX + 5, getHeight());
		g2.setColor(MyColor.toAlpha(MyColor.TURQUOISE.COLOR, 100));
		g2.fill(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
		g2.setColor(status.isGuard() ? Color.BLUE : Color.GREEN);
		g2.setStroke(new BasicStroke(3));
		g2.draw(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
		if (status.isDead())
			g2.setPaint(Color.RED.darker());
		else if (status.getHP() <= status.getMaxHP() / 6)
			g2.setColor(Color.ORANGE);
		else if (status.getHP() <= status.getMaxHP() / 2)
			g2.setColor(Color.YELLOW);
		else
			g2.setPaint(Color.WHITE);
		for (int i = 0; i < CharacterStatus.printLines; i ++) {
			switch (i) {
			case 0:
				g2.drawString(status.getName(), offsetX, offsetY + i * charHeight);
				break ;
			case 1:
				g2.drawString("HP: " + status.getHP() + " / " + status.getMaxHP(), offsetX, offsetY + i * charHeight);
				break ;
			case 2:
				g2.drawString("MP: " + status.getMP() + " / " + status.getMaxMP(), offsetX, offsetY + i * charHeight);
				break ;
			case 3:
				g2.drawString("Lv. " + status.getLevel(), offsetX, offsetY + i * charHeight);
				break ;
			}
		}
	}
}
