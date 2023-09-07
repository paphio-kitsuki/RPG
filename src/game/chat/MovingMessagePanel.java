package game.chat;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import game.FrameUpdateListener;
import game.GamePanel;
import utility.KeyList;

public abstract class MovingMessagePanel extends JPanel implements FrameUpdateListener {

	private static int offsetY = 30;

	private String message = "Error";
	private String buffer = "";
	private String bufferedMessages[] = {};
	private double speed = 10; // mozi per seconds
	private double count = 0;
	private Point startPoint = new Point(50, 50);

	public MovingMessagePanel() {
		super();
		setLayout(null);
	}

	public MovingMessagePanel(String message) {
		super();
		setLayout(null);
		this.setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	protected void setMessage(String message) {
		count = 0;
		this.message = message;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		if (speed < 1) {
			this.speed = 1;
		} else {
			this.speed = speed;
		}
	}

	public Point getStartPoint() {
		return new Point(startPoint);
	}

	public void setStartPoint(Point p) {
		startPoint.setLocation(p);
	}

	protected boolean isDone() {
		return count > message.length();
	}

	protected void done() {
		count = message.length();
	}

	protected abstract void next();

	public void addFrame(KeyList e) {
		if (isFinished())
			return;
		if (e.containsEnter()) {
			next();
		}
		String tmp;
		count += (double) speed / GamePanel.FPS;
		if (count > message.length()) {
			tmp = message;
		} else if (count < 1) {
			tmp = "";
		} else {
			tmp = message.substring(0, (int) count);
		}
		if (tmp.equals(buffer))
			return;
		buffer = tmp;
		bufferedMessages = getStrings(buffer.toCharArray());
	}

	private String[] getStrings(char dst[]) {
		String out[];
		ArrayList<String> tmp = new ArrayList<>();
		Character str[] = null;
		StringBuilder build;
		ArrayList<Character> c = new ArrayList<>();

		for (int i = 0; i < dst.length; i++) {
			str = new Character[c.size()];
			c.toArray(str);
			build = new StringBuilder();
			build.append(transString(str));
			build.append(dst[i]);
			if (dst[i] == '\n'
					|| getFontMetrics(getFont()).stringWidth(build.toString()) > getWidth() - startPoint.x * 2) {
				tmp.add(transString(str));
				c.clear();
			}
			if (dst[i] != '\n')
				c.add(dst[i]);
		}
		if (!c.isEmpty()) {
			str = new Character[c.size()];
			c.toArray(str);
			tmp.add(transString(str));
		}
		out = new String[tmp.size()];
		tmp.toArray(out);
		return out;
	}

	private static String transString(Character[] dst) {
		char tmp[] = new char[dst.length];
		for (int i = 0; i < dst.length; i++)
			tmp[i] = dst[i].charValue();
		return String.copyValueOf(tmp);
	}

	public void resetBuffer() {
		bufferedMessages = new String[] {};
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(new LinearGradientPaint(0, 0, 150, 400, new float[] { 0.45f, 0.53f, 0.61f },
				new Color[] { Color.BLACK, Color.GRAY, Color.BLACK }, CycleMethod.NO_CYCLE));
		g2.setStroke(new BasicStroke(5));
		g2.draw(new Rectangle2D.Double(2, 2, getWidth() - 4, getHeight() - 4));
		g2.setPaint(getForeground());
		int height = g2.getFontMetrics().getHeight();
		int startY = startPoint.y;
		if (startPoint.y + height * bufferedMessages.length > getHeight() + offsetY)
			startY = offsetY + getHeight() - height * bufferedMessages.length;
		for (String buffer : bufferedMessages) {
			g2.drawString(buffer, startPoint.x, startY);
			startY += height;
		}
	}
}
