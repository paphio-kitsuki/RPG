package game;

import java.io.File;

public enum BGMList {
	NONE("", 1.0), FIELD("field.wav", 0.5), BUTTLE("buttle.wav", 0.5), CITY("City.wav", 1.2);

	private final String dir;
	{
		boolean flag = false;
		try {
			if (!new File("music\\").isDirectory())
				flag = true;
		} catch (SecurityException e) {
		}
		if (flag)
			dir = "app\\music\\";
		else
			dir = "music\\";
	}
	private final File path;
	private final double originVolume;

	private BGMList(String path, double Volume) {
		this.path = new File(dir + path);
		this.originVolume = Volume;
	}

	public File getFile() {
		return this.path;
	}

	public double getVolume() {
		return originVolume;
	}
}
