package game.obj;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import game.Direction;
import game.GamePanel;

public abstract class Object implements HitListener, EnterListener {

	private BufferedImage i;
	private Rectangle r = new Rectangle();
	private Rectangle before = new Rectangle();
	private Rectangle showR = new Rectangle();
	private Rectangle collider = new Rectangle();//rのlocationからのcollider
	private Rectangle limit = null;
	private long count = 0;
	private boolean isPassable = false;
	private boolean isReversible = true;
	protected Direction direction = Direction.NONE;
	private static boolean isDrawing = false;
	protected int eventFlag;
	private Point camera = new Point();
	private Rectangle cameraLimit = new Rectangle();
	private String label = "";

	public Object(BufferedImage i, Rectangle r) {
		this.setImage(i);
		setBounds(r);
		setCollider(0, 0, r.width, r.height);
		setShowBounds(0, 0, r.width, r.height);
	}

	public Object(BufferedImage i, Rectangle r, double magnification) {
		this.setImage(i);
		setBounds(r.x, r.y, (int) (r.width * magnification), (int) (r.height * magnification));
		setCollider(0, 0, this.r.width, this.r.height);
		setShowBounds(0, 0, r.width, r.height);
	}

	public Object(BufferedImage i, Rectangle r, Rectangle showr) {
		this.setImage(i);
		setBounds(r);
		setCollider(0, 0, r.width, r.height);
		setShowBounds(showr);
	}

	public void setLabel(String s) {
		this.label = s;
	}

	public String getLabel() {
		return this.label;
	}

	public BufferedImage getImage() {
		return i;
	}

	public void setImage(BufferedImage i) {
		this.i = i;
	}

	public Rectangle getBounds() {
		return new Rectangle(r);
	}

	public void setLocation(Point p) {
		while (isDrawing)
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
		if (isReversible)
			before.setBounds(r);
		this.r.setLocation(p);
		fixBounds();
		fixCamera();
	}

	public void setBounds(Rectangle r) {
		while (isDrawing)
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
		if (isReversible)
			before.setBounds(r);
		this.r.setBounds(r);
		fixBounds();
		fixCamera();
	}

	public void setBounds(int x, int y, int width, int height) {
		while (isDrawing)
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
		if (isReversible)
			before.setBounds(r);
		this.r.setBounds(x, y, width, height);
		fixBounds();
		fixCamera();
	}

	public void fixCamera() {
		if (cameraLimit == null) {
			return;
		}
		int x = getBounds().x - (GamePanel.showRange.width - getBounds().width) / 2;
		int y = getBounds().y - (GamePanel.showRange.height - getBounds().height) / 2;
		if (x < cameraLimit.x) {
			x = cameraLimit.x;
		} else if (x > cameraLimit.x + cameraLimit.width - GamePanel.showRange.width) {
			x = cameraLimit.x + cameraLimit.width - GamePanel.showRange.width;
		}
		if (y < cameraLimit.y) {
			y = cameraLimit.y;
		} else if (y > cameraLimit.y + cameraLimit.height - GamePanel.showRange.height) {
			y = cameraLimit.y + cameraLimit.height - GamePanel.showRange.height;
		}
		camera.setLocation(x, y);
	}

	public void setCameraLimit(Rectangle limit) {
		cameraLimit.setBounds(limit);
		if (cameraLimit.x + cameraLimit.width < GamePanel.showRange.width) {
			cameraLimit.setSize(GamePanel.showRange.width - cameraLimit.x, cameraLimit.height);
		}
		if (cameraLimit.y + cameraLimit.height < GamePanel.showRange.height) {
			cameraLimit.setSize(cameraLimit.width, GamePanel.showRange.height - cameraLimit.y);
		}
	}

	public Point getCamera() {
		return camera;
	}

	public Rectangle getCameraLimit() {
		return new Rectangle(cameraLimit);
	}

	public static void setDrawing(boolean isDrawing) {
		Object.isDrawing = isDrawing;
	}

	public void setLimit(Rectangle limit) {
		this.limit = new Rectangle(limit);
	}

	public Rectangle getShowBounds() {
		return new Rectangle(showR);
	}

	public void setShowBounds(Rectangle r) {
		this.showR.setBounds(r);
	}

	public void setShowBounds(int x, int y, int width, int height) {
		this.showR.setBounds(x, y, width, height);
	}

	public void passTime() {
		count++;
	}

	public void resetPassCount() {
		count = 0;
	}

	public void happen(GamePanel p) {

	}

	public long getPassCount() {
		return count;
	}

	public Rectangle getColliderOnMap() {
		return new Rectangle(r.x + collider.x, r.y + collider.y, collider.width, collider.height);
	}

	public void setCollider(Rectangle r) {
		this.collider.setBounds(r);
	}

	public void setCollider(int x, int y, int width, int height) {
		this.collider.setBounds(x, y, width, height);
	}

	private void fixBounds() {
		if (limit == null) {
			return;
		}
		Rectangle r = getColliderOnMap();
		if (r.x < limit.x) {
			r.x = limit.x;
		} else if (r.x + r.width > limit.x + limit.width) {
			r.x = limit.x + limit.width - r.width;
		}
		if (r.y < limit.y) {
			r.y = limit.y;
		} else if (r.y + r.height > limit.y + limit.height) {
			r.y = limit.y + limit.height - r.height;
		}
		this.r.setBounds(r.x - collider.x, r.y - collider.y, this.r.width, this.r.height);
	}

	public boolean isPassable() {
		return isPassable;
	}

	public void setPassable(boolean isPassable) {
		this.isPassable = isPassable;
	}

	public void setDirection(Direction d) {
		this.direction = d;
	}

	public Direction getAbsolutePosition(Rectangle position) {
		Rectangle r = getColliderOnMap();
		if (position.intersects(r)) {
			return Direction.CENTER;
		} else if (position.intersects(new Rectangle(r.x + r.width, r.y, r.width, r.height))) {
			return Direction.LEFT;
		} else if (position.intersects(new Rectangle(r.x - r.width, r.y, r.width, r.height))) {
			return Direction.RIGHT;
		} else if (position.intersects(new Rectangle(r.x, r.y + r.height, r.width, r.height))) {
			return Direction.FRONT;
		} else if (position.intersects(new Rectangle(r.x, r.y - r.height, r.width, r.height))) {
			return Direction.BACK;
		} else {
			return Direction.NONE;
		}
	}

	public Direction getDirection() {
		return direction;
	}

	public Direction getSubjectivePosition(Rectangle position) {
		Direction absolute = getAbsolutePosition(position);
		if (absolute != Direction.CENTER && absolute != Direction.NONE)
			return Direction.getSubjectiveDirection(direction, absolute);
		else
			return absolute;
	}

	public void setReversible(boolean isReversible) {
		this.isReversible = isReversible;
	}

	public Rectangle getBeforeCollider() {
		return new Rectangle(before.x + collider.x, before.y + collider.y, collider.width, collider.height);
	}

}
