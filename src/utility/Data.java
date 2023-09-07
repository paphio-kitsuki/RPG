package utility;

import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import game.background.Background;
import game.status.PlayerStatus;

public class Data {

	public static String rootdir = "save\\";
	public static final String extension = ".vta";
	private static final SecureRandom random = new SecureRandom();
	private static final String key = "VTA is simple RPG.I did my best!";

	private String path;
	private Background background = Background.MATAMA;
	private Point player = new Point(0, 0);
	private ArrayList<PlayerStatus> status = new ArrayList<>();
	private int eventFlag = 0;
	private long latestTime = -1;

	public Data(String path) {
		new File(rootdir).mkdir();
		this.path = path;
	}

	public void save() {
		File file;
		try {
			file = new File(rootdir + path + extension);
			file.createNewFile();
		} catch (Exception e) {
			return;
		}
		try (BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(file))) {
			StringBuilder tmp = new StringBuilder();
			tmp.append(background.ordinal() + ",");
			tmp.append(player.x + "," + player.y);
			for (PlayerStatus s : status) {
				tmp.append("," + s.getHP() + "," + s.getMP() + "," + s.getEx());
			}
			tmp.append(",e");
			tmp.append("," + eventFlag);
			tmp.append("," + System.currentTimeMillis());

			byte iv[] = new byte[16];
			random.nextBytes(iv);
			bw.write(iv);
			SecretKey key = generateKey(Data.key);
			byte b[] = encrypto(tmp.toString(), key, generateIV(iv));
			bw.write(b);
			//System.out.println(decrypto(b, key, generateIV(iv)));
			bw.close();
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
	}

	public boolean load() {
		File file = new File(rootdir + path + extension);
		if (!file.exists())
			return false;
		try (BufferedInputStream br = new BufferedInputStream(new FileInputStream(file))) {
			byte iv[] = new byte[16];
			br.read(iv);
			String data[] = decrypto(br.readAllBytes(), generateKey(key),
					generateIV(iv)).split(",");
			background = Background.values()[Integer.parseInt(data[0])];
			player.setLocation(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
			status.clear();
			PlayerStatus tmp;
			int i;
			for (i = 1; !data[i * 3].equals("e"); i++) {
				switch (i) {
				case 1:
					tmp = PlayerStatus.roman.clone();
					break;
				case 2:
					tmp = PlayerStatus.paruto.clone();
					break;
				default:
					throw new IOException();
				}
				tmp.addEx(Integer.parseInt(data[i * 3 + 2]));
				tmp.setHP(Integer.parseInt(data[i * 3]));
				tmp.setMP(Integer.parseInt(data[i * 3 + 1]));
				status.add(tmp);
			}
			i = i * 3 + 1;
			eventFlag = Integer.parseInt(data[i]);
			latestTime = Long.parseLong(data[data.length - 1]);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public Background getBackground() {
		return background;
	}

	public void setBackground(Background background) {
		this.background = background;
	}

	public Point getLocation() {
		return player;
	}

	public void setLocation(Point player) {
		this.player = player;
	}

	public void setStatus(ArrayList<PlayerStatus> status) {
		this.status.clear();
		this.status.addAll(status);
	}

	public ArrayList<PlayerStatus> getStatus() {
		return this.status;
	}

	public void setEventFlag(int flag) {
		this.eventFlag = flag;
	}

	public int getEventFlag() {
		return this.eventFlag;
	}

	public long getLatestTime() {
		return latestTime;
	}

	public void setFilename(String path) {
		this.path = path;
	}

	public String getFilename() {
		return path;
	}

	public static boolean existsFilename(String path) {
		return new File(rootdir + path + extension).exists();
	}

	public static Data[] searchFiles() {
		new File(rootdir).mkdir();

		File filenames[] = new File(rootdir).listFiles(new DataFilter());
		Data datas[] = new Data[filenames.length];

		for (int i = 0; i < datas.length; i++) {
			datas[i] = new Data(filenames[i].getName().substring(0,
					filenames[i].getName().length() - extension.length()));
			datas[i].load();
		}
		return datas;
	}

	public static String searchLatestName() {
		new File(rootdir).mkdir();
		File filenames[] = new File(rootdir).listFiles(new DataFilter());
		int count = 1;
		boolean notFound = true;
		while (true) {
			notFound = true;
			for (File f : filenames) {
				if (f.getName().equals("data" + count + extension)) {
					notFound = false;
					count++;
					break;
				}
			}
			if (notFound)
				break;
		}
		return ("data" + count);
	}

	public static Data[] sortOnFilename(Data filenames[], boolean isAscendingOrder) {
		Data output[] = sort(filenames, (Data d1, Data d2) -> (d1.getFilename().compareTo(d2.getFilename())));
		if (!isAscendingOrder)
			output = reverse(output);
		return output;
	}

	public static Data[] sortOnDate(Data filenames[], boolean isAscendingOrder) {
		Data output[] = sort(filenames, (Data d1, Data d2) -> (d1.getLatestTime() > d2.getLatestTime() ? 1
				: (d1.getLatestTime() == d2.getLatestTime() ? 0 : -1)));
		if (!isAscendingOrder)
			output = reverse(output);
		return output;
	}

	private static Data[] sort(Data filenames[], Comparator<Data> c) {
		Data output[] = new Data[filenames.length];
		ArrayList<Data> sortnames = new ArrayList<>();

		for (int i = 0; i < filenames.length; i++)
			sortnames.add(filenames[i]);
		sortnames.sort(c);
		sortnames.toArray(output);
		return output;
	}

	private static Data[] reverse(Data filenames[]) {
		Data output[] = new Data[filenames.length];
		Data tmp;

		for (int i = 0; i < output.length; i++) {
			output[i] = filenames[i];
		}
		for (int i = 0; i < output.length / 2; i++) {
			tmp = output[output.length - i - 1];
			output[output.length - i - 1] = output[i];
			output[i] = tmp;
		}
		return output;
	}

	public SecretKeySpec generateKey(String key) throws UnsupportedEncodingException {
		byte tmp[] = key.getBytes("UTF-8");
		if (tmp.length <= 16) {
			byte tmp2[] = new byte[16];
			for (int i = 0; i < tmp2.length; i++) {
				if (i >= tmp.length)
					tmp2[i] = (byte) (64 + i);
				else
					tmp2[i] = tmp[i];
			}
			tmp = tmp2;
		} else if (tmp.length <= 24) {
			byte tmp2[] = new byte[24];
			for (int i = 0; i < tmp2.length; i++) {
				if (i >= tmp.length)
					tmp2[i] = (byte) (64 + i);
				else
					tmp2[i] = tmp[i];
			}
			tmp = tmp2;
		} else {
			byte tmp2[] = new byte[32];
			for (int i = 0; i < tmp2.length; i++) {
				if (i >= tmp.length)
					tmp2[i] = (byte) (64 + i);
				else
					tmp2[i] = tmp[i];
			}
			tmp = tmp2;
		}
		return new SecretKeySpec(tmp, "AES");
	}

	public IvParameterSpec generateIV(byte iv[]) {
		return new IvParameterSpec(iv);
	}

	public byte[] encrypto(String plainText, SecretKey key, IvParameterSpec iv) throws GeneralSecurityException {
		// 書式:"アルゴリズム/ブロックモード/パディング方式"
		Cipher encrypter = Cipher.getInstance("AES/CBC/PKCS5Padding");
		encrypter.init(Cipher.ENCRYPT_MODE, key, iv);
		return encrypter.doFinal(plainText.getBytes());
	}

	public String decrypto(byte[] cryptoText, SecretKey key, IvParameterSpec iv) throws GeneralSecurityException {
		// 書式:"アルゴリズム/ブロックモード/パディング方式"
		Cipher decrypter = Cipher.getInstance("AES/CBC/PKCS5Padding");
		decrypter.init(Cipher.DECRYPT_MODE, key, iv);
		return new String(decrypter.doFinal(cryptoText));
	}

}
