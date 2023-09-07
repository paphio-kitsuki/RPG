package utility;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import game.FrameUpdateListener;
import game.FreezePanel;
import game.choose.ChosenEvent;
import game.choose.ChosenListener;

public class ChooseGroup extends JPanel implements FrameUpdateListener, KeyListener {

	private int index = 0;
	private ArrayList<ChosenListener> chosen = new ArrayList<>();

	public ChooseGroup() {
		super();
		setLayout(null);
		setOpaque(false);
	}

	public void addChosenListener(ChosenListener c) {
		this.chosen.add(c);
	}

	public void removeChosenListener(ChosenListener c) {
		this.chosen.remove(c);
	}

	public void setChosenIndex(int index) {
		if (0 <= index && index <= getComponents().length)
			this.index = index;
	}

	public int getChosenIndex() {
		return index;
	}

	@Override
	public void addFrame(KeyList e) {
		for (Component c : getComponents()) {
			if (c instanceof FrameUpdateListener)
				((FrameUpdateListener) c).addFrame(e);
		}
	}

	@Override
	public boolean isPlayable() {
		return true;
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
		for (Component c : getComponents()) {
			if (c instanceof FrameUpdateListener)
				((FrameUpdateListener) c).destroy(f);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		Component c = getComponent(index);
		if (c == null)
			return;
		g2.setPaint(new GradientPaint(0, 0, MyColor.toAlpha(MyColor.PURPLE.COLOR, 200), 10, 10,
				MyColor.toAlpha(MyColor.LAVENDER.COLOR, 200), true));
		g2.setStroke(new BasicStroke(3));
		g2.draw(new Rectangle(c.getX() - 3, c.getY() - 3, c.getWidth() + 6, c.getHeight() + 6));
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_KP_UP:
		case KeyEvent.VK_W:
			index--;
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_KP_DOWN:
		case KeyEvent.VK_S:
			index++;
			break;
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_Z:
			for (int i = 0; i < chosen.size(); i++)
				chosen.get(i).chosen(new ChosenEvent(this, index));
			break;
		}
		index = (index + getComponents().length) % getComponents().length;
		repaint(0);
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
