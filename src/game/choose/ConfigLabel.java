package game.choose;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import game.FreezePanel;
import game.Sound;
import game.obj.Player;
import utility.ChooseGroup;
import utility.KeyList;
import utility.MyColor;
import utility.VolumeLabel;

public class ConfigLabel extends MovingChooseLabel {

	private static final String configStr = "～設定～";

	private ChooseGroup group;
	private VolumeLabel speed;
	private VolumeLabel volume;
	private double before;
	private KeyList key;

	public ConfigLabel() {
		setLayout(null);
		group = new ChooseGroup();
		speed = new VolumeLabel(1, 7, 1);
		volume = new VolumeLabel(0, 8, 0) {
			public void addFrame(KeyList key) {
				int count = getCount();
				super.addFrame(key);
				if (count != getCount())
					Sound.setVolume((double) getCount() / 4 * Sound.originVolume);
			}
		};
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 30);
		speed.setFont(font);
		volume.setFont(font);
		speed.setText("歩行速度：");
		volume.setText("　　音量：");
		group.add(speed);
		group.add(volume);
	}

	public void addFrame(KeyList key) {
		this.key = key;
		int tmp = count;
		super.addFrame(key);
		if (isDestroying())
			return;
		else if (tmp < max && isVisible()) {
			if (tmp + 1 == max) {
				before = Sound.getVolume();
				group.setBounds(20, 75, getWidth() - 40, 300);
				speed.setBounds(10, 25, group.getWidth() - 20, 100);
				volume.setBounds(10, 175, group.getWidth() - 20, 100);
				speed.setCount((int) ((double) Player.staticSpeed / Player.staticFinalSpeed * 4 - 1));
				volume.setCount((int) (Sound.getVolume() / Sound.originVolume * 4));
				speed.setChoosable(true);
				volume.setChoosable(false);
				group.setChosenIndex(0);
				key.addChildKeyListener(group);
				add(group);
			}
			return;
		}
		if (!isSelectable())
			return;
		speed.setChoosable(false);
		volume.setChoosable(false);
		if (group.getChosenIndex() == 0)
			speed.setChoosable(true);
		else
			volume.setChoosable(true);
		endOfAddFrame(key);
	}

	protected void endOfAddFrame(KeyList key) {
		if (key.containsEnter()) {
			Player.staticSpeed = (int) ((double) (speed.getCount() + 1) / 4 * Player.staticFinalSpeed);
			for (ChosenListener l : listenerList)
				l.chosen(new ChosenEvent(this, 0));
		} else if (key.containsBack()) {
			Sound.setVolume(before);
			for (ChosenListener l : listenerList)
				l.chosen(new ChosenEvent(this, -1));
		} else
			group.addFrame(key);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (0 < count && count < max)
			return;
		g2.setPaint(new GradientPaint(0, 0, Color.BLACK, 50, 50, Color.GRAY, true));
		g2.setStroke(new BasicStroke(2));
		g2.draw(new RoundRectangle2D.Double(2, 2, getWidth() - 4, getHeight() - 4, 50, 50));
		g2.setPaint(MyColor.toAlpha(Color.WHITE, 200));
		g2.fill(new RoundRectangle2D.Double(3, 3, getWidth() - 6, getHeight() - 6, 50, 50));
		g2.setPaint(Color.DARK_GRAY);
		g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
		g2.drawString(configStr, 200, 50);
	}

	public void destroy(FreezePanel g) {
		super.destroy(g);
		key.removeChildKeyListener(group);
		group.destroy(g);
		remove(group);
	}

}
