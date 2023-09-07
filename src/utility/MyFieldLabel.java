package utility;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;

public class MyFieldLabel extends JLabel implements KeyListener {

	private static final int offsetX = 15;
	private static final int offsetY = 30;

	private StringBuilder str = new StringBuilder();
	private int index = 0;

	public MyFieldLabel() {
		setFocusable(false);
	}

	public void addInput(String origin) {
		this.str.append(origin);
		index = str.length();
	}

	public void reset() {
		str = new StringBuilder();
		index = 0;
	}

	public String getInput() {
		return str.toString();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.draw(new Rectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2));
		String print = str.toString();
		int cursorIndex = 0;
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		if (index > 0)
			cursorIndex = g.getFontMetrics().stringWidth(str.substring(0, index));
		int startIndex = Math.min(getWidth() - offsetX * 2 - g.getFontMetrics().stringWidth(print), 0);
		startIndex = Math.max(startIndex, -cursorIndex);
		g.setColor(Color.BLACK);
		g.drawString(print, startIndex + offsetX, offsetY);
		g2.setPaint(Color.DARK_GRAY);
		g2.setStroke(new BasicStroke(2));
		g2.draw(new Line2D.Double(cursorIndex + startIndex + offsetX, 6, cursorIndex + startIndex + offsetX,
				getHeight() - 6));
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (Character.isLetterOrDigit(e.getKeyChar())
				|| (0x20 <= e.getKeyChar() && e.getKeyChar() <= 0x7e) && e.getKeyChar() != '\\') {
			this.str.insert(index, e.getKeyChar());
			index++;
		} else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE && this.str.length() > 0 && index > 0) {
			index--;
			this.str.deleteCharAt(index);
		} else if (e.getKeyChar() == KeyEvent.VK_DELETE && this.str.length() > 0 && index < this.str.length()) {
			this.str.deleteCharAt(index);
		}
		repaint(1);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_KP_LEFT) && index > 0) {
			index--;
		} else if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_KP_RIGHT)
				&& index < this.str.length()) {
			index++;
		}
		repaint(1);
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
