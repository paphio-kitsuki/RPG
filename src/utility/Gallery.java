package utility;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum Gallery {

	//@formatter:off
	bittervalley_back("background\\bittervalley_back.png"),
	bittervalley_collider("background\\bittervalley_collider.png"),
	bittervalley_front("background\\bittervalley_front.png"),
	matama_back("background\\firsttown_back.png"),
	matama_collider("background\\firsttown_collider.png"),
	matama_front("background\\firsttown_front.png"),
	grass("sprite\\grass.png"),
	heigen_front("background\\heigen_front.png"),
	heigen_back("background\\heigen_back.png"),
	mitsukubi1_back("background\\mitukubi1_back.png"),
	mitsukubi1_collider("background\\mitukubi1_collider.png"),
	mitsukubi1_front("background\\mitukubi1_front.png"),
	mitsukubi2_back("background\\mitukubi2_back.png"),
	mitsukubi2_backforest("background\\mitukubi2_backforest.png"),
	mitsukubi2_collider("background\\mitukubi2_collider.png"),
	mitsukubi3_back("background\\mitukubi3_back.png"),
	mitsukubi3_backtree("background\\mitukubi3_backtree.png"),
	mitsukubi3_collider("background\\mitukubi3_collider.png"),
	mitsukubi3_front("background\\mitukubi3_front.png"),
	nyuin_back("background\\nyuin_back.png"),
	nyuin_collider("background\\nyuin_collider.png"),
	nyuin_front("background\\nyuin_front.png"),
	washouheigen_front("background\\washouheigen_front.png"),
	washouheigen_collider("background\\washouheigen_collider.png"),
	washouheigen_back("background\\washouheigen_back.png"),

	roman("sprite\\roman.png"),
	paruto("sprite\\paruto.png"),
	IMG_0166("sprite\\IMG_0166.png"),
	IMG_0167("sprite\\IMG_0167.png"),
	IMG_0168("sprite\\IMG_0168.png"),
	IMG_0170("sprite\\IMG_0170.png"),
	IMG_0171("sprite\\IMG_0171.png"),
	IMG_0172("sprite\\IMG_0172.png"),
	IMG_0173("sprite\\IMG_0173.png"),
	IMG_0174("sprite\\IMG_0174.png"),
	IMG_0175("sprite\\IMG_0175.png"),
	IMG_0176("sprite\\IMG_0176.png"),
	heal_port("sprite\\heal_port.png"),
	star("sprite\\star.png"),
	camera("sprite\\camera.png"),

	bakeo("zoom\\bakeo.png"),
	bikurinko("zoom\\bikurinko.png"),
	boss("zoom\\boss.png"),
	boss_sprite("sprite\\boss_sprite.png"),
	dodonnnu("zoom\\dodonnnu.png"),
	domochiri("zoom\\domochiri.png"),
	hatchi("zoom\\hatchi.png"),
	hitodestar("zoom\\hitodestar.png"),
	meme("zoom\\meme.png"),
	mochiri_sprite("sprite\\mochiri_sprite.png"),
	mochiri("zoom\\mochiri.png"),
	nyoronri("zoom\\nyoronri.png"),

	nonlinear("nonlinear-logo.png"),
	;
	//@formatter:on

	private static String rootFolder = "images\\";

	private final String url;
	private BufferedImage image = null;

	private static BufferedImageOp bio;
	static {
		try {
			if (!new File(rootFolder).isDirectory()) {
				rootFolder = "app\\images\\";
				Data.rootdir = "app\\" + Data.rootdir;
			}
		} catch (SecurityException e) {
		}
		int size = 7;
		float f[] = new float[size * size];
		for (int i = 0; i < size * size; i++) {
			f[i] = 1.0f / (size * size + 1);
		}
		f[size * size / 2] *= 2;
		bio = new ConvolveOp(new Kernel(size, size, f), ConvolveOp.EDGE_NO_OP, null);
	}

	Gallery(String url) {
		this.url = url;
	}

	public BufferedImage image() {
		if (image == null) {
			read();
		}
		return image;
	}

	public static boolean tryReadAll() {
		boolean flag = readAll();
		freeAll();
		return flag;
	}

	public static boolean readAll() {
		for (Gallery g : Gallery.values()) {
			if (g.image() == null)
				return false;
		}
		return true;
	}

	public void free() {
		this.image = null;
	}

	public static void freeAll() {
		for (Gallery g : Gallery.values())
			g.free();
		Runtime.getRuntime().gc();
	}

	private void read() {
		try {
			image = ImageIO.read(new File(rootFolder + url));
		} catch (IOException e) {
			image = null;
		}
	}

	public static BufferedImage filter(BufferedImage bi) {
		if (bi == null)
			return null;
		return (bio.filter(bi, null));
	}

}
