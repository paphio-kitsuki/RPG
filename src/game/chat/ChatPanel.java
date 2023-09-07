package game.chat;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;

import game.FreezePanel;
import game.action.Action;
import main.MainWindow;
import utility.MyColor;

public class ChatPanel extends MovingMessagePanel {

	public static final Rectangle normalStyle = new Rectangle(50, MainWindow.size.height - 180,
			MainWindow.size.width - 100, 150);

	private String messages[] = new String[] {};
	private int count = 0;
	private boolean isFinished = false;
	private boolean isFinishable = true;
	private Action action[] = null;
	private boolean isDestroying = false;
	private String name = "";

	public ChatPanel() {
		super();
		setup();
	}

	public ChatPanel(String messages[]) {
		super(messages[0]);
		this.messages = messages;
		setup();
	}

	private void setup() {
		setFont(new Font("", Font.PLAIN, 30));
		setBackground(MyColor.toAlpha(MyColor.DARK_BROWN.COLOR.darker(), 150));
		setForeground(Color.WHITE);
	}

	@Override
	protected void next() {
		if (isFinished())
			return;
		if (isDone()) {
			int tmpCount = count;
			count++;
			if (action != null && action[tmpCount] != null) {
				action[tmpCount].action();
			}
			if (count >= messages.length) {
				if (isFinishable)
					isFinished = true;
			} else {
				setMessage(messages[count]);
			}
		} else {
			done();
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAllPrinted() {
		return (isDone() && count + 1 >= messages.length);
	}

	public void resetMessages(String messages[]) {
		if (messages == null || messages.length == 0)
			return;
		this.messages = messages;
		super.resetBuffer();
		count = 0;
		setMessage(messages[count]);
		isFinished = false;
		action = null;
	}

	public boolean setAction(Action action[]) {
		if (action == null || action.length != messages.length)
			return false;
		this.action = action;
		return true;
	}

	@Override
	public boolean isFinished() {
		return isFinished;
	}

	public void setFinishable(boolean flag) {
		this.isFinishable = flag;
	}

	@Override
	public void destroy(FreezePanel g) {
		g.remove(this);
		g.start(this);
	}

	@Override
	public boolean isDestroying() {
		return isDestroying;
	}

	@Override
	public boolean isPlayable() {
		return isVisible();
	}

	public void paintComponent(Graphics g) {
		if (name.equals("")) {
			super.paintComponent(g);
			return;
		}
		Point tmp = getStartPoint();
		setStartPoint(new Point(tmp.x, tmp.y + g.getFontMetrics().getHeight()));
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(Color.LIGHT_GRAY);
		g2.fill(new RoundRectangle2D.Double(tmp.x - 11, tmp.y - g2.getFontMetrics().getHeight() * 5 / 6 - 4,
				g2.getFontMetrics().stringWidth(name) + 22, g2.getFontMetrics().getHeight() + 8, 50, 50));
		g2.setPaint(MyColor.toAlpha(getBackground(), 255));
		g2.fill(new RoundRectangle2D.Double(tmp.x - 10, tmp.y - g2.getFontMetrics().getHeight() * 5 / 6 - 3,
				g2.getFontMetrics().stringWidth(name) + 20, g2.getFontMetrics().getHeight() + 6, 50, 50));
		g2.setPaint(getForeground());
		g.drawString(name, tmp.x, tmp.y);
		setStartPoint(tmp);
	}
}
