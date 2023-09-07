package utility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;

public class MyButton extends JButton {

	public MyButton(String string) {
		super(string);
		setBorderPainted(false);
		setBackground(MyColor.toAlpha(Color.RED, 255));
		setForeground(Color.BLACK);
		setOpaque(false);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Paint p = g2.getPaint();
		Dimension size = getSize();
		/*
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
		g2.draw(new RoundRectangle2D.Double(1, 1, size.width - 2, size.height - 2, 150, 70));
		g2.setPaint(new RadialGradientPaint(size.width / 2, size.height / 2, 30, new float[] { 0.1f, 0.7f },
				new Color[] { MyColor.toAlpha(Color.CYAN, 100), MyColor.toAlpha(Color.GREEN, 100) },
				CycleMethod.REFLECT));
		g2.fill(new RoundRectangle2D.Double(2, 2, size.width - 4, size.height - 4, 150, 70));*/
		final int width = 8;
		int y = size.height / width * width / 2;
		for (int x = width; x < size.width - width;) {
			drawOval(g2, x, y, width);
			x += width / 2;
			y += width / 2;
			if (y >= size.height - width) {
				while (y > width) {
					drawOval(g2, x, y, width);
					x -= width / 4;
					y -= width / 2;
				}
			}
		}
		g2.setPaint(p);
		super.paintComponent(g);
	}

	public static void drawOval(Graphics2D g, int x, int y, int radius) {
		g.setPaint(new RadialGradientPaint(x, y, radius, new float[] { 0f, 1f },
				new Color[] { MyColor.toAlpha(Color.GREEN, 150), MyColor.toAlpha(Color.GREEN, 0) },
				CycleMethod.NO_CYCLE));
		g.fill(new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2));
	}
}
