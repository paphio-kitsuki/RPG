package game;

public enum Direction {
	FRONT, LEFT, RIGHT, BACK, CENTER, NONE;

	public static int getImagePosition(Direction d) {
		switch (d) {
		case BACK:
			return 0;
		case LEFT:
			return 1;
		case FRONT:
			return 2;
		case RIGHT:
			return 3;
		default:
			return -1;
		}
	}

	public static Direction getSubjectiveDirection(Direction main, Direction target) {
		switch (main) {
		case FRONT:
		case NONE:
			return target;
		case BACK:
			switch (target) {
			case FRONT:
				return BACK;
			case BACK:
				return FRONT;
			case LEFT:
				return RIGHT;
			case RIGHT:
				return LEFT;
			default:
				return NONE;
			}
		case LEFT:
			switch (target) {
			case FRONT:
				return RIGHT;
			case BACK:
				return LEFT;
			case LEFT:
				return FRONT;
			case RIGHT:
				return BACK;
			default:
				return NONE;
			}
		case RIGHT:
			switch (target) {
			case FRONT:
				return LEFT;
			case BACK:
				return RIGHT;
			case LEFT:
				return BACK;
			case RIGHT:
				return FRONT;
			default:
				return NONE;
			}
		default:
			return NONE;
		}
	}
}
