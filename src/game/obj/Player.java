package game.obj;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.Direction;
import game.FreezePanel;
import game.GamePanel;
import game.status.CharacterStatus;
import game.status.PlayerStatus;
import utility.KeyList;

public final class Player extends Creature {

	public static final int staticFinalSpeed = 200;
	public static int staticSpeed = 200;

	private final KeyList keyList;
	private ArrayList<PlayerStatus> partyStatusList = new ArrayList<>();
	private int moveCount = 0;
	private Point before = null;
	private boolean isButtlable = true;

	public Player(BufferedImage i, Rectangle r, Dimension showd, Direction dir, KeyList keyList) {
		super(i, r, showd, dir);
		this.keyList = keyList;
		partyStatusList.add(PlayerStatus.roman.clone());
	}

	public Player(BufferedImage i, Rectangle r, Dimension showd, Direction dir, KeyList keyList,
			Rectangle cameraLimit) {
		super(i, r, showd, dir);
		this.keyList = keyList;
		setCameraLimit(cameraLimit);
		fixCamera();
		partyStatusList.add(PlayerStatus.roman.clone());
	}

	public ArrayList<PlayerStatus> getPartyStatusList() {
		return partyStatusList;
	}

	public void setPartyStatusList(ArrayList<PlayerStatus> status) {
		this.partyStatusList.clear();
		this.partyStatusList.addAll(status);
	}

	public CharacterStatus getStatus(String name) {
		for (CharacterStatus out : partyStatusList) {
			if (out.getName().equals(name))
				return (out);
		}
		return null;
	}

	public void setButtlable(boolean isButtlable) {
		this.isButtlable = isButtlable;
	}

	@Override
	public void passTime() {
		before = getBounds().getLocation();
		super.passTime();
		int l = 0, r = 0, u = 0, d = 0;
		if (keyList.containsLeft())
			l = 1;
		if (keyList.containsRight())
			r = 1;
		if (keyList.containsUp())
			u = 1;
		if (keyList.containsDown())
			d = 1;
		move(r - l, d - u);
	}

	public void setSpeed(int speed) {
		Player.staticSpeed = speed;
	}

	public int getSpeed() {
		return Player.staticSpeed;
	}

	@Override
	public void move(int x, int y) {
		super.move(x, y);
	}

	public int getMoveCount() {
		return moveCount;
	}

	public void resetMoveCount() {
		moveCount = 0;
	}

	public void reflectMoveCount() {
		if (!isButtlable)
			return;
		Point after = getBounds().getLocation();
		moveCount += Math.abs(after.x - before.x) + Math.abs(after.y - before.y);
	}

	@Override
	public void happen(GamePanel p) {
	}

	public void Hit(GamePanel p) {
	}

	@Override
	public boolean pressEnter(Player p, FreezePanel f) {
		return false;
	}
}
