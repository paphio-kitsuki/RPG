package utility;

import java.awt.Color;

public enum MyColor {

	BROWN(new Color(200, 100, 0)), DARK_BROWN(new Color(100, 50, 0)), LAVENDER(new Color(127, 130, 187)), TURQUOISE(
			new Color(54, 126, 127)), BEIGE(new Color(239, 228, 176)), PURPLE(new Color(163, 73, 164));

	public final Color COLOR;

	private MyColor(Color c) {
		this.COLOR = c;
	}

	public static Color toAlpha(Color c, int alpha) {
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}
}
