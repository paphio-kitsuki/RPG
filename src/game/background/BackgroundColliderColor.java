package game.background;

import java.awt.Color;

public enum BackgroundColliderColor {

	//@formatter:off
	WALL(Color.BLACK, false), FOREST(new Color(20, 243, 152), false), SEA(new Color(0, 36, 255), false),
	MOVE1(new Color(210, 0, 0), true), MOVE2(new Color(220, 0, 0), true), MOVE3(new Color(230, 0, 0), true), MOVE4(new Color(240, 0, 0), true),
	EVENT1(new Color(0, 210, 0), true), EVENT2(new Color(0, 220, 0), true), EVENT3(new Color(0, 230, 0), true), EVENT4(new Color(0, 240, 0), true),
	NONE(Color.WHITE, true), NONE2(new Color(246, 250, 254), true), OTHER(null, true);
	//@formatter:on

	private final Color color;
	private final boolean isPassable;

	BackgroundColliderColor(Color color, boolean isPassable) {
		this.color = color;
		this.isPassable = isPassable;
	}

	public static BackgroundColliderColor getStatus(Color color) {
		for (BackgroundColliderColor b : values()) {
			if (!b.equals(OTHER) && b.color.equals(color)) {
				return b;
			}
		}
		return OTHER;
	}

	public boolean isPassable() {
		return isPassable;
	}

	public boolean isMoveArea() {
		if (this == MOVE1 || this == MOVE2 || this == MOVE3 || this == MOVE4) {
			return true;
		}
		return false;
	}

	public boolean isEventArea() {
		if (this == EVENT1 || this == EVENT2 || this == EVENT3 || this == EVENT4) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		switch (this) {
		case WALL:
			return "1";
		case FOREST:
			return "2";
		case SEA:
			return "3";
		case MOVE1:
			return "a";
		case MOVE2:
			return "b";
		case MOVE3:
			return "c";
		case MOVE4:
			return "d";
		case EVENT1:
			return "v";
		case EVENT2:
			return "w";
		case EVENT3:
			return "x";
		case EVENT4:
			return "z";
		case NONE:
		case NONE2:
			return "0";
		case OTHER:
			return "99";
		default:
			return "?";
		}
	}

}
