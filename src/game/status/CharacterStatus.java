package game.status;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Random;

import utility.MyImage;

public class CharacterStatus {

	public static final int printLines = 4;
	public static final Random random = new Random();

	private BufferedImage face = null;
	private BufferedImage originFace = null;
	private Dimension faceSize = new Dimension();
	private String name = "";
	private int HP = 0;
	private int maxHP = 1;
	private int MP = 0;
	private int maxMP = 1;
	private int level = 1;
	private int speed = 1;
	private int power = 1;
	private int guard = 1;
	private int experience = 0;

	private boolean isGuard = false;

	public CharacterStatus(BufferedImage face, Dimension faceSize, String name, int level, int maxHP, int maxMP,
			int power, int guard,
			int speed, int ex) {
		setFace(face);
		originFace = face;
		setFaceSize(faceSize);
		setName(name);
		setMaxHP(maxHP);
		setHP(maxHP);
		setMaxMP(maxMP);
		setMP(maxMP);
		setLevel(level);
		setPower(power);
		setGuard(guard);
		setSpeed(speed);
		this.experience = ex;
	}

	public BufferedImage getFace() {
		return face;
	}

	public void setFace(BufferedImage face) {
		this.face = face;
	}

	public void resetFace() {
		if (!isGuard)
			this.face = originFace;
	}

	public Dimension getFaceSize() {
		return new Dimension(faceSize);
	}

	public void setFaceSize(Dimension faceSize) {
		this.faceSize.setSize(faceSize);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDead() {
		return getHP() <= 0;
	}

	public int getHP() {
		return HP;
	}

	public void setHP(int hP) {
		if (hP < 0)
			hP = 0;
		if (hP > maxHP)
			hP = maxHP;
		HP = hP;
	}

	public void damage(int damage) {
		setHP(getHP() - damage);
	}

	public int getMP() {
		return MP;
	}

	public void setMP(int mP) {
		if (mP < 0)
			mP = 0;
		if (mP > maxMP)
			mP = maxMP;
		MP = mP;
	}

	public int getLevel() {
		return level;
	}

	protected void setLevel(int level) {
		if (level < 1)
			level = 1;
		this.level = level;
	}

	public int getMaxHP() {
		return maxHP;
	}

	protected void setMaxHP(int maxHP) {
		if (maxHP < 1)
			maxHP = 1;
		this.maxHP = maxHP;
		if (HP > maxHP)
			HP = maxHP;
	}

	public int getMaxMP() {
		return maxMP;
	}

	protected void setMaxMP(int maxMP) {
		if (maxMP < 0)
			maxMP = 0;
		this.maxMP = maxMP;
		if (MP > maxMP)
			MP = maxMP;
	}

	@Override
	public CharacterStatus clone() {
		CharacterStatus out = new CharacterStatus(face, faceSize, name, level, maxHP, maxMP, power, guard, speed,
				experience);
		out.setHP(HP);
		out.setMP(MP);
		return (out);
	}

	public int getSpeed() {
		return speed;
	}

	protected void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getPower() {
		return power;
	}

	protected void setPower(int power) {
		this.power = power;
	}

	public int getGuard() {
		return guard;
	}

	protected void setGuard(int guard) {
		this.guard = guard;
	}

	protected void addEx(int ex) {
		this.experience += ex;
	}

	public int getEx() {
		return this.experience;
	}

	public void resetCondition() {
		if (isGuard()) {
			isGuard = false;
			face = originFace;
		}
	}

	public boolean isGuard() {
		return isGuard;
	}

	public void Guard() {
		if (isGuard)
			return;
		this.isGuard = true;
		if (face != null)
			face = MyImage.addColor(originFace, new Color(0, 100, 0, 0));
	}

	public CharacterStatus getRandomStatus() {
		CharacterStatus tmp = clone();
		tmp.setMaxHP(tmp.getMaxHP() + random.nextInt(5) - 2);
		tmp.setHP(tmp.getMaxHP());
		tmp.setMaxMP(tmp.getMaxMP() + random.nextInt(5) - 2);
		tmp.setMP(tmp.getMaxMP());
		tmp.setSpeed(tmp.getSpeed() + random.nextInt(5) - 2);
		tmp.setPower(tmp.getPower() + random.nextInt(3) - 1);
		tmp.setGuard(tmp.getGuard() + random.nextInt(3) - 1);
		return tmp;
	}

	public CharacterStatus getSuperStatus(double magnification) {
		CharacterStatus tmp = clone();
		tmp.setMaxHP((int) (tmp.getMaxHP() * magnification));
		tmp.setHP(tmp.getMaxHP());
		tmp.setMaxMP((int) (tmp.getMaxMP() * magnification));
		tmp.setMP(tmp.getMaxMP());
		tmp.setSpeed((int) (tmp.getSpeed() * magnification));
		tmp.setPower((int) (tmp.getPower() * magnification));
		tmp.setGuard((int) (tmp.getGuard() * magnification));
		tmp.addEx((int) (tmp.getEx() * (magnification - 1)));
		tmp.setLevel((int) (tmp.getLevel() * magnification * 1.5));
		return tmp;
	}

}
