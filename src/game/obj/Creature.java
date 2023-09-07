package game.obj;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import game.Direction;
import game.GamePanel;

public abstract class Creature extends Object {

	public static int transformTime = 200;

	private int speed = 1;// pixel per second
	private double Xstack = 0;
	private double Ystack = 0;
	private int showCount = 1;
	private long showPassCount = 1;
	private Rectangle before = new Rectangle();
	private boolean lock = false;

	public Creature(BufferedImage i, Rectangle r) {
		super(i, r);
		super.setShowBounds(r.width, 0, r.width, r.height);
	}

	public Creature(BufferedImage i, Rectangle r, Dimension showd, Direction dir) {
		super(i, r);
		super.setShowBounds(0, 0, showd.width, showd.height);
		transform(dir);
	}

	public void move(int x, int y) { // 1~-1 -> x
		if (x != 0 || y != 0) {
			Xstack += (double) (x * getSpeed() / Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))) / GamePanel.FPS;
			Ystack += (double) (y * getSpeed() / Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2))) / GamePanel.FPS;
		}
		before.setBounds(getBounds());
		setBounds(before.x + (int) (Xstack / 1.0), before.y + (int) (Ystack / 1.0), before.width, before.height);
		Xstack %= 1.0;
		Ystack %= 1.0;
		if (x > 0) {
			transform(Direction.LEFT);
		} else if (x < 0) {
			transform(Direction.RIGHT);
		} else if (y > 0) {
			transform(Direction.FRONT);
		} else if (y < 0) {
			transform(Direction.BACK);
		} else {
			transform(Direction.NONE);
		}
	}

	/*
		public void resetBounds() {
			super.setBounds(before);
		}
	*/

	public boolean lockDirection(boolean flag) {
		boolean tmp = this.lock;
		this.lock = flag;
		return tmp;
	}

	public void transform(Direction d) {
		if (d == direction || lock) {
			if ((showPassCount * 1000 / GamePanel.FPS) > transformTime) {
				showCount++;
				showPassCount = 1;
			}
			showPassCount++;
		} else {
			showPassCount = 1;
			showCount = 1;
			if (d != Direction.NONE) {
				direction = d;
			}
		}
		showCount %= 4;
		Rectangle r = super.getShowBounds();
		super.setShowBounds((showCount % 3 + showCount / 3) * r.width, Direction.getImagePosition(direction) * r.height,
				r.width, r.height);
	}

	public void resetShowCount() {
		setShowCount(1);
	}

	protected void setShowCount(int count) {
		showCount = count;
		Rectangle r = super.getShowBounds();
		super.setShowBounds((showCount % 3 + showCount / 3) * r.width, Direction.getImagePosition(direction) * r.height,
				r.width, r.height);
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

}
