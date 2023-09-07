package game.obj;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import game.FreezePanel;
import game.GamePanel;
import utility.Gallery;

public class Grass extends Object {

	private static BufferedImage image = Gallery.grass.image();
	public static final int width = image.getWidth();
	public static final int height = image.getHeight();

	public Grass(int x, int y) {
		super(image, new Rectangle(x, y, image.getWidth(), image.getHeight()));
		this.setPassable(true);
	}

	@Override
	public void passTime() {
	}

	@Override
	public void Hit(GamePanel p) {
	}

	@Override
	public boolean pressEnter(Player p, FreezePanel f) {
		return false;
	}

}
