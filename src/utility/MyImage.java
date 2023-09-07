package utility;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class MyImage {
	public static BufferedImage addColor(BufferedImage bi, Color c) {
		BufferedImage out = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
		for (int i = 0; i < bi.getWidth(); i++) {
			for (int j = 0; j < bi.getHeight(); j++) {
				int color = addColor(new Color(bi.getRGB(i, j), true), c).getRGB();
				out.setRGB(i, j, color);
			}
		}
		return out;
	}

	public static Color addColor(Color dst, Color src) {
		int color[] = new int[4];
		color[0] = dst.getRed() + src.getRed();
		color[1] = dst.getGreen() + src.getGreen();
		color[2] = dst.getBlue() + src.getBlue();
		color[3] = dst.getAlpha() + src.getAlpha();
		for (int i = 0; i < color.length; i++) {
			if (color[i] > 255)
				color[i] = 255;
			else if (color[i] < 0)
				color[i] = 0;
		}
		return new Color(color[0], color[1], color[2], color[3]);
	}
}
