package game.status;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import utility.Gallery;

public class PlayerStatus extends CharacterStatus {

	public static final PlayerStatus roman = new PlayerStatus(Gallery.roman.image(), new Dimension(100, 100), "ロマン", 20,
			15, 6, 2, 12);
	public static final PlayerStatus paruto = new PlayerStatus(Gallery.paruto.image(), new Dimension(100, 100), "パルト",
			22, 0, 8, 2, 6);
	static {
		roman.setExLaw(30, 14);
		roman.setRisingValue(new RisingStatus[] { new RisingStatus(5, 2, 2, 1, 2), new RisingStatus(4, 2, 3, 2, 2),
				new RisingStatus(5, 2, 2, 2, 2), new RisingStatus(4, 2, 3, 1, 2), new RisingStatus(5, 2, 2, 2, 2),
						new RisingStatus(4, 2, 3, 2, 2) });
		paruto.setExLaw(51, 13);
		paruto.setRisingValue(new RisingStatus[] { new RisingStatus(5, 0, 3, 1, 2), new RisingStatus(5, 0, 3, 1, 3),
				new RisingStatus(5, 0, 3, 2, 2), new RisingStatus(5, 0, 3, 1, 3), new RisingStatus(5, 0, 3, 1, 2),
						new RisingStatus(5, 0, 3, 2, 3)});
		paruto.setHP(paruto.getMaxHP());
	}

	private int ExBorder = 0;
	private int risingExBorder = 0;
	private int count = 0;
	private RisingStatus rise[] = {};

	public PlayerStatus(BufferedImage face, Dimension faceSize, String name, int maxHP, int maxMP, int power, int guard,
			int speed) {
		super(face, faceSize, name, 1, maxHP, maxMP, power, guard, speed, 0);
	}

	public void addEx(int ex) {
		super.addEx(ex);
		while (getExBorder(getLevel() + 1) <= getEx()) {
			levelUp();
		}
	}

	public void setExLaw(int border, int risingBorder) {
		this.ExBorder = border;
		this.risingExBorder = risingBorder;
	}

	public int getExBorder() {
		return getExBorder(getLevel() + 1);
	}

	private int getExBorder(int level) {
		int out = 0;
		for (int i = 0; i < level - 1; i++) {
			out += ExBorder + risingExBorder * i;
		}
		return out;
	}

	public void setRisingValue(RisingStatus rise[]) {
		this.rise = rise;
	}

	private void levelUp() {
		setLevel(getLevel() + 1);
		if (rise == null || rise.length == 0)
			return;
		count = (count + 1) % rise.length;
		setMaxHP(getMaxHP() + rise[count].risingHP());
		setMaxMP(getMaxMP() + rise[count].risingMP());
		setPower(getPower() + rise[count].risingPower());
		setGuard(getGuard() + rise[count].risingGuard());
		setSpeed(getSpeed() + rise[count].risingSpeed());
	}

	public PlayerStatus clone() {
		PlayerStatus out = new PlayerStatus(getFace(), getFaceSize(), getName(), getMaxHP(), getMaxMP(), getPower(),
				getGuard(), getSpeed());
		out.setExLaw(ExBorder, risingExBorder);
		out.addEx(getEx());
		out.setHP(getHP());
		out.setMP(getMP());
		out.setRisingValue(rise);
		out.count = this.count;
		return (out);
	}
}
