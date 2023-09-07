package game.choose;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;

import game.FreezePanel;
import utility.Data;
import utility.KeyList;
import utility.MyColor;
import utility.MyFieldLabel;

public class SaveLabel extends MovingChooseLabel {

	private static final String saveInfo = "セーブデータの名前を入力してください。";
	private static final String caution[] = { "警告：同じ名前のデータが既に存在します。", "既存のデータに上書きしますか？",
			"Esc : キャンセル　　Enter : 上書き" };
	private static final String error[] = { "セーブに失敗しました！", "他のデータ名を試してください。" };
	private static final String writeInfo = "Esc : 保存せずに戻る　　Enter : 保存する";

	private MyFieldLabel fileField = new MyFieldLabel();
	private KeyList key;
	private boolean isCaution = false;
	private boolean isError = false;

	public SaveLabel(KeyList key) {
		this.key = key;
	}

	public void setField(String str) {
		fileField.addInput(str);
	}

	public void setup() {
		fileField.setBounds(50, getHeight() - 150, getWidth() - 100, 50);
		key.addChildKeyListener(fileField);
		add(fileField);
		isCaution = false;
	}

	public void addFrame(KeyList e) {
		int tmp = count;
		super.addFrame(e);
		if (isDestroying())
			return;
		if (tmp < max && isVisible()) {
			if (tmp + 1 == max) {
				setup();
			}
			return;
		}
		if (!isSelectable())
			return;
		if (e.containsOnlyEnter()) {
			if (isError) {
				isError = false;
				key.addChildKeyListener(fileField);
			} else if (isCaution || !Data.existsFilename(fileField.getInput())) {
				isCaution = false;
				try {
					File f = new File(Data.rootdir + fileField.getInput() + Data.extension);
					f.createNewFile();
					if (!f.canWrite())
						throw new IOException();
				} catch (IOException | SecurityException e2) {
					isError = true;
					key.removeChildKeyListener(fileField);
					return;
				}
				for (ChosenListener l : listenerList)
					l.chosen(new ChosenEvent(this, fileField.getInput()));
			} else if (Data.existsFilename(fileField.getInput())) {
				key.removeChildKeyListener(fileField);
				isCaution = true;
			}
		} else if (e.containsEspace()) {
			if (isError) {
				isError = false;
				key.addChildKeyListener(fileField);
			} else if (isCaution) {
				isCaution = false;
				key.addChildKeyListener(fileField);
			} else {
				for (ChosenListener l : listenerList)
					l.chosen(new ChosenEvent(this, null));
			}
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (0 < count && count < max)
			return;
		g2.setPaint(new GradientPaint(0, 0, Color.BLACK, 50, 50, Color.GRAY, true));
		g2.setStroke(new BasicStroke(2));
		g2.draw(new RoundRectangle2D.Double(2, 2, getWidth() - 4, getHeight() - 4, 50, 50));
		g2.setPaint(MyColor.toAlpha(Color.WHITE, 200));
		g2.fill(new RoundRectangle2D.Double(3, 3, getWidth() - 6, getHeight() - 6, 50, 50));
		g2.setPaint(Color.DARK_GRAY);
		g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		g2.drawString(writeInfo, getWidth() - 400, getHeight() - 30);
		g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 28));
		g2.drawString(saveInfo, 30, 50);
		if (isCaution) {
			g2.setPaint(MyColor.toAlpha(Color.BLACK, 150));
			g2.fill(new RoundRectangle2D.Double(3, 3, getWidth() - 6, getHeight() - 6, 50, 50));
			g2.setPaint(MyColor.toAlpha(Color.YELLOW, 200));
			g2.fill(new Rectangle2D.Double(30, 80, getWidth() - 50,
					(g2.getFontMetrics().getHeight() + 10) * caution.length));
			g2.setPaint(Color.RED);
			for (int i = 0; i < caution.length; i++) {
				g2.drawString(caution[i], 30, 120 + (g2.getFontMetrics().getHeight() + 5) * i);
			}
		}
		if (isError) {
			g2.setPaint(MyColor.toAlpha(Color.BLACK, 150));
			g2.fill(new RoundRectangle2D.Double(3, 3, getWidth() - 6, getHeight() - 6, 50, 50));
			g2.setPaint(MyColor.toAlpha(Color.BLACK, 200));
			g2.fill(new Rectangle2D.Double(30, 100, getWidth() - 50,
					(g2.getFontMetrics().getHeight() + 10) * error.length));
			g2.setPaint(Color.RED);
			for (int i = 0; i < error.length; i++) {
				g2.drawString(error[i], 30, 140 + (g2.getFontMetrics().getHeight() + 5) * i);
			}
		}
	}

	public void reborn() {
		super.reborn();
		fileField.reset();
	}

	public void destroy(FreezePanel f) {
		super.destroy(f);
		remove(fileField);
		key.removeChildKeyListener(fileField);
	}

}
