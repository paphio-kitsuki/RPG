package game.status;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class StrongEnemyStatus extends CharacterStatus {
	private int count = 0;

	public StrongEnemyStatus(BufferedImage face, Dimension faceSize, String name, int level, int maxHP, int maxMP,
			int power, int guard, int speed, int ex) {
		super(face, faceSize, name, level, maxHP, maxMP, power, guard, speed, ex);
	}

	public boolean isCharging() {
		count++;
		return (count % 2 == 1);
	}

	@Override
	public StrongEnemyStatus clone() {
		StrongEnemyStatus out = new StrongEnemyStatus(getFace(), getFaceSize(), getName(), getLevel(), getMaxHP(),
				getMaxMP(), getPower(), getGuard(), getSpeed(), getEx());
		out.setHP(getHP());
		out.setMP(getMP());
		return (out);
	}

}
