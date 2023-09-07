package game.action;

import game.status.CharacterStatus;

public class ActionAndStatus {

	public static final int NONE = -1;
	public static final int FIGHT = 0;
	//public static final int SKILL = 1;
	public static final int GUARD = 1;

	private final CharacterStatus c;
	private int command = 0;
	private int skill = 0;
	private CharacterStatus target = null;

	public ActionAndStatus(CharacterStatus status) {
		this.c = status;
	}

	public void setCommand(int command, int skill, CharacterStatus target) {
		this.command = command;
		this.skill = skill;
		this.target = target;
	}

	public void setTarget(CharacterStatus target) {
		this.target = target;
	}

	public int getCommand() {
		return command;
	}

	public int getSkill() {
		return skill;
	}

	public CharacterStatus getTarget() {
		return target;
	}

	public CharacterStatus getStatus() {
		return c;
	}
}
