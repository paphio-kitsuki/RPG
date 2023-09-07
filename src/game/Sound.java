package game;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	public static final double originVolume = 0.1;

	private static BGMList bgmList = BGMList.NONE;
	private static Clip BGM = null;
	private static double volume = 0.1;

	public static void setBGM(BGMList bgm) {
		bgmList = bgm;
		if (BGM != null) {
			if (BGM.isRunning())
				BGM.stop();
			BGM.close();
		}
		BGM = null;
		if (bgmList != BGMList.NONE) {
			BGM = createAudioClip(bgmList.getFile());
			if (BGM == null)
				return;
			reflectVolume(BGM);
			BGM.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}

	public static BGMList getBGM() {
		return bgmList;
	}

	public static void setVolume(double volume) {
		if (volume < 0)
			return;
		Sound.volume = volume;
		reflectVolume(BGM);
	}

	public static double getVolume() {
		return Sound.volume;
	}

	public static void suspend() {
		if (BGM != null && BGM.isRunning())
			BGM.stop();
	}

	public static void restart() {
		if (BGM != null && !BGM.isRunning())
			BGM.start();
	}

	private static void reflectVolume(Clip c) {
		if (c == null)
			return;
		FloatControl ctrl = (FloatControl)c.getControl(FloatControl.Type.MASTER_GAIN);
		ctrl.setValue((float)Math.log10(volume * bgmList.getVolume()) * 20);
	}

	private static Clip createAudioClip(File path) {
		try(AudioInputStream ais = AudioSystem.getAudioInputStream(path)){
			AudioFormat af = ais.getFormat();
			DataLine.Info dataLine = new DataLine.Info(Clip.class, af);
			Clip c = (Clip)AudioSystem.getLine(dataLine);
			c.open(ais);
			return c;
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
		}
		return null;
	}
}
