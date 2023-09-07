package game.obj;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import game.Direction;
import game.FreezePanel;
import game.GamePanel;
import utility.Gallery;

public class Mochiri extends Creature {

	private static final Random random = new Random();
	private static final int originInterval = 500;//ms

	private int interval = originInterval + (random.nextInt(3) - 1) * 100;//ms
	private boolean flag = false;
	private int x = 0;
	private int y = 0;
	private boolean verticalMovable = true;
	private boolean horizontalMovable = true;

	public Mochiri(Point p) {
		super(Gallery.mochiri_sprite.image(), new Rectangle(p.x, p.y, 25, 25));
		setSpeed(80);
		setCollider(2, 7, 21, 14);
		transform(Direction.BACK);
		setShowCount(random.nextInt(4));
	}

	public void passTime() {
		super.passTime();
		if (getPassCount() < (double) interval / 1000 * GamePanel.FPS) {
			if (!flag) {
				lockDirection(false);
				do
					resetDirection();
				while (x == 0 && y == 0);
				flag = true;
			}
		} else {
			if (getPassCount() >= (double) interval * 4 / 1000 * GamePanel.FPS) {
				resetPassCount();
				interval = originInterval + (random.nextInt(3) - 1) * 100;
			}
			x = 0;
			y = 0;
			if (flag) {
				lockDirection(true);
				flag = false;
			}
		}
		move(x, y);
	}

	private void resetDirection() {
		Random r = new Random();
		int i = r.nextInt(4);
		if (this.horizontalMovable) {
			switch (i) {
			case 0:
			case 1:
				x = 0;
				break;
			case 2:
				x = 1;
				break;
			case 3:
				x = -1;
				break;
			}
		}
		if (this.verticalMovable) {
			switch (i) {
			case 0:
				y = 1;
				break;
			case 1:
				y = -1;
				break;
			case 2:
			case 3:
				y = 0;
				break;
			}
		}
	}

	public void setHorizontalMovable(boolean horizontalMovable) {
		this.horizontalMovable = horizontalMovable;
	}

	public void setVerticalMovable(boolean verticalMovable) {
		this.verticalMovable = verticalMovable;
	}

	@Override
	public void Hit(GamePanel p) {
	}

	@Override
	public boolean pressEnter(Player p, FreezePanel f) {
		return false;
	}

}
