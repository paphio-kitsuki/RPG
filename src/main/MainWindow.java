package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.BGMList;
import game.GamePanel;
import game.Sound;
import utility.Data;
import utility.Gallery;
import utility.KeyList;
import utility.MyColor;

public class MainWindow extends JFrame implements NextListener {

	public static final String title = "Vtuber Adventure";
	public static final Rectangle size = new Rectangle(200, 100, 433 * 2, 265 * 2);
	private final KeyList keyList = new KeyList();
	private final JPanel errorPanel = new JPanel();
	private final JLabel errorLabel = new JLabel("画像データの読み込みに失敗しました！");
	private boolean isEffecting = false;
	private Color curtain = MyColor.toAlpha(Color.BLACK, 255);
	private static int lagtime = 350;

	public static void main(String[] args) {
		new MainWindow();
	}

	public MainWindow() {
		super();
		setTitle(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		Insets in = getInsets();
		setVisible(false);
		setBounds(size.x, size.y, size.width + in.left + in.right, size.height + in.bottom + in.top);
		if (!Gallery.tryReadAll()) {
			errorPanel.setLayout(null);
			errorLabel.setBounds(50, size.height / 2 - 50, 800, 100);
			errorLabel.setFont(new Font(Font.MONOSPACED, Font.ITALIC, 40));
			errorLabel.setForeground(Color.RED);
			errorPanel.add(errorLabel);
			add(errorPanel);
			setVisible(true);
			return;
		}
		setIconImage(Gallery.roman.image().getSubimage(35, 66, 24, 29));
		new Thread(new Runnable() {
			public void run() {
				JPanel tmp = new JPanel() {
					public void paint(Graphics g) {
						super.paint(g);
						if (!isEffecting)
							return;
						Graphics2D g2 = (Graphics2D) g;
						g2.setPaint(Color.WHITE);
						g2.fill(new Rectangle2D.Double(0, 0, size.width, size.height));
						BufferedImage bi = Gallery.nonlinear.image();
						g2.drawImage(bi, 100, size.height / 2 - 93, size.width - 100, size.height / 2 + 92, 0, 0,
								bi.getWidth(),
								bi.getHeight(), null);
						if (curtain != null) {
							g2.setPaint(curtain);
							g2.fill(new Rectangle2D.Double(0, 0, size.width, size.height));
						}
					}
				};
				add(tmp);
				isEffecting = true;
				setVisible(true);
				brightin();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				darkout();
				start();
				remove(tmp);
				isEffecting = false;
				repaint();
			}
		}).start();
	}

	public void darkout() {
		for (int i = 1; i <= 32; i++) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
			curtain = MyColor.toAlpha(Color.BLACK, i * 8 - 1);
			repaint();
		}
	}

	public void brightin() {
		curtain = MyColor.toAlpha(Color.BLACK, 255);
		repaint();
		try {
			Thread.sleep(lagtime);
		} catch (InterruptedException e1) {
		}
		for (int i = 32; i >= 1; i--) {
			curtain = MyColor.toAlpha(Color.BLACK, i * 8 - 1);
			repaint();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
		}
		curtain = null;
	}

	private void start() {
		setTitlePanel();
		addKeyListener(keyList);
		Sound.setBGM(BGMList.CITY);
	}

	private void setTitlePanel() {
		add(new TitlePanel(this));
		setFocusable(true);
		for (Component c : getComponents()) {
			c.setFocusable(false);
		}
	}

	public void next(JPanel p) {
		remove(p);
		if (p instanceof TitlePanel) {
			if (((TitlePanel) p).getFlag()) {
				ContinuePanel tmp = new ContinuePanel(this);
				add(tmp);
				keyList.addChildKeyListener(tmp);
			} else {
				Sound.setBGM(BGMList.NONE);
				add(new GamePanel(this, null, keyList));
			}
		} else if (p instanceof ContinuePanel) {
			Data data = ((ContinuePanel) p).getData();
			keyList.removeChildKeyListener((KeyListener) p);
			if (data != null) {
				Sound.setBGM(BGMList.NONE);
				add(new GamePanel(this, data, keyList));
			} else
				setTitlePanel();
		} else if (p instanceof GamePanel) {
			setTitlePanel();
			Sound.setBGM(BGMList.CITY);
		}
		setVisible(true);
	}

	public KeyList getKeyList() {
		return this.keyList;
	}

	public static void drawBackground(Graphics2D g) {
		BufferedImage background = Gallery.matama_back.image();
		if (background == null)
			return;
		background = Gallery.filter(background);
		g.drawImage(background, 0, 0, size.width, size.height,
				100, 150, 100 + (int) (GamePanel.showRange.width * 1.5), 150 + (int) (GamePanel.showRange.height * 1.5),
				null);
		g.setPaint(new GradientPaint(0, 0, MyColor.toAlpha(Color.LIGHT_GRAY, 100), 100, 100,
				MyColor.toAlpha(Color.GRAY, 100), true));
		g.fill(new Rectangle2D.Double(0, 0, size.width, size.height));
		g.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
		g.setPaint(Color.BLACK);
		g.drawString("↑↓W S で選択　Z Enter で決定　X で戻る", size.width - 450, size.height - 15);
	}

}
