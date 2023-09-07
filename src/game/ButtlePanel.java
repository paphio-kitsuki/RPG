package game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import game.action.Action;
import game.action.ActionAndStatus;
import game.action.ActionFlag;
import game.chat.ChatPanel;
import game.choose.ChooseListLabel;
import game.choose.ChoosePointLabel;
import game.choose.ChosenEvent;
import game.choose.ChosenListener;
import game.status.CharacterStatus;
import game.status.PlayerStatus;
import game.status.StrongEnemyStatus;
import main.MainWindow;
import utility.Gallery;
import utility.KeyList;
import utility.MyColor;
import utility.MyImage;

public class ButtlePanel extends FreezePanel implements FrameUpdateListener, ChosenListener {

	public static final int space = 20;
	public static final int vibeInterval = 50 * GamePanel.FPS / 1000;
	public static final int changeInterval = 100 * GamePanel.FPS / 1000;
	private static final Random random = new Random();

	private ActionAndStatus party[], enemy[];
	private ButtleStatusLabel blabel[];
	private ChatPanel chat = null;
	private ChooseListLabel choose = null;
	private ChooseListLabel partyCommand;
	private ChoosePointLabel enemyTarget;
	private BufferedImage back = null;
	private ButtleStatus buttleStatus = ButtleStatus.CHOOSING;
	private ArrayList<Integer> partyChoise = new ArrayList<>();
	private int choiseIndex;
	private Point enemyPoints[];
	private ArrayList<ActionAndStatus> actionStatus = new ArrayList<>();
	private boolean isDestroying = false;
	private boolean isEscapable = true;
	private ArrayList<Action> startingActions = new ArrayList<>();
	private int maxLv = 0;
	private ArrayList<ActionFlag> actions = new ArrayList<>();
	private Point origin = null;

	public ButtlePanel() {
		super();
	}

	public ButtlePanel(PlayerStatus party[], CharacterStatus enemy[]) {
		super();
		setup(party, enemy);
	}

	public void addAction(Action a) {
		startingActions.add(a);
	}

	public void setup(PlayerStatus party[], CharacterStatus enemy[]) {
		back = Gallery.filter(Gallery.heigen_back.image());
		setLayout(null);
		setFont(new Font("", Font.BOLD, 20));
		this.party = new ActionAndStatus[party.length];
		this.enemy = new ActionAndStatus[enemy.length];
		if (party != null)
			for (int i = 0; i < party.length; i++)
				this.party[i] = new ActionAndStatus(party[i]);
		if (enemy != null)
			for (int i = 0; i < enemy.length; i++) {
				this.enemy[i] = new ActionAndStatus(enemy[i]);
				maxLv = Math.max(maxLv, enemy[i].getLevel());
			}

		String messages[] = new String[1];
		StringBuilder enemyMessage = new StringBuilder();
		//enemyMessage.append("あっ！野生の ");
		for (CharacterStatus cs : enemy) {
			enemyMessage.append(cs.getName());
			if (!cs.equals(enemy[enemy.length - 1]))
				enemyMessage.append(" と ");
		}
		enemyMessage.append(" が現れた！");
		messages[0] = enemyMessage.toString();
		//messages[1] = this.party[0].getName() + " はどうする？";
		chat = new ChatPanel(messages);
		chat.setBounds(200 + space, MainWindow.size.height - 110 + space, MainWindow.size.width - 270, 80);
		chat.setStartPoint(new Point(30, 30));
		add(chat);
		chat.setVisible(false);

		choose = new ChooseListLabel();
		choose.addLabel("たたかう");
		choose.addLabel("にげる");
		choose.setSelectable(1, isEscapable);
		//choose.addLabel("ねる");
		choose.setLocation(50 + space, MainWindow.size.height - 110 + space);
		add(choose);
		choose.setVisible(false);
		choose.addChosenListener(this);

		partyCommand = new ChooseListLabel();
		for (int i = 0; i < 3; i++) {
			switch (i) {
			case ActionAndStatus.FIGHT:
				partyCommand.addLabel("コウゲキ");
				break;
			/*case ActionAndStatus.SKILL:
				partyCommand.addLabel("ワザ");
				break;*/
			case ActionAndStatus.GUARD:
				partyCommand.addLabel("マモル");
				break;
			}
		}
		add(partyCommand);
		partyCommand.setVisible(false);
		partyCommand.setSelectable(false);
		partyCommand.addChosenListener(this);
		resetChoiseIndex();

		int allSize = 0;
		int maxHeight = 0;
		for (CharacterStatus s : enemy) {
			allSize += s.getFaceSize().width + space;
			maxHeight = Math.max(maxHeight, s.getFaceSize().height);
		}
		Point origin = new Point((getWidth() - allSize + space) / 2 + space, 280 - maxHeight);
		enemyPoints = new Point[enemy.length];
		for (int i = 0; i < enemyPoints.length; i++) {
			origin.x += enemy[i].getFaceSize().width / 2;
			enemyPoints[i] = new Point(origin);
			origin.x += enemy[i].getFaceSize().width / 2 + space;
		}
		enemyTarget = new ChoosePointLabel(enemyPoints);
		add(enemyTarget);
		enemyTarget.addChosenListener(this);
		enemyTarget.setVisible(false);
		enemyTarget.setSelectable(false);

		blabel = new ButtleStatusLabel[party.length];
		int all = 0;
		int width = 0;
		for (int i = 0; i < party.length; i++) {
			blabel[i] = new ButtleStatusLabel(party[i]);
			all += blabel[i].getSize().width + 20;
		}
		for (int i = 0; i < party.length; i++) {
			add(blabel[i]);
			blabel[i].setLocation((getWidth() - all + 20) / 2 + width + space, 300 + space);
			blabel[i].setVisible(false);
			width += blabel[i].getSize().width + 20;
		}
	}

	public void start() {
		chat.setVisible(true);
		for (ButtleStatusLabel l : blabel)
			l.setVisible(true);
		for (Action a : startingActions)
			a.action();
	}

	public void setEscapable(boolean flag) {
		this.isEscapable = flag;
		if (choose != null)
			choose.setSelectable(1, flag);
	}

	private void setCommandList(int move) {
		if (move == 0)
			return;
		int command = 0;
		int skill = 0;
		CharacterStatus target = null;
		for (int i = 0; i < partyChoise.size(); i++) {
			switch (i) {
			case 0:
				command = partyChoise.get(i);
				break;
			case 1:
				if (command == 1)
					skill = partyChoise.get(i);
				else
					target = enemy[partyChoise.get(i)].getStatus();
				break;
			}
		}
		int sign = 0;
		if (move != 0)
			sign = move / Math.abs(move);
		while (choiseIndex >= 0 && choiseIndex < blabel.length && move != 0) {
			int tmp = choiseIndex;
			choiseIndex += sign;
			if (!party[tmp].getStatus().isDead()) {
				party[tmp].setCommand(command, skill, target);
				move -= sign;
			}
		}
		while (choiseIndex >= 0 && choiseIndex < blabel.length) {
			if (party[choiseIndex].getStatus().isDead())
				choiseIndex += sign;
			else
				break;
		}
		partyChoise.clear();
		if (choiseIndex < 0) {
			resetChoiseIndex();
			choose.setSelectable(true);
			partyCommand.setSelectable(false);
			partyCommand.setVisible(false);
			chat.setVisible(true);
			return;
		}
		if (choiseIndex >= blabel.length)
			return;
		partyCommand.setVisible(false);
		partyCommand.setLocation(blabel[choiseIndex].getX(),
				blabel[choiseIndex].getY() + blabel[choiseIndex].getHeight() + 5);
		partyCommand.setVisible(true);
		partyCommand.resetIndex();
	}

	private void resetChoiseIndex() {
		choiseIndex = 0;
		while (choiseIndex < party.length) {
			if (party[choiseIndex].getStatus().isDead())
				choiseIndex++;
			else
				break;
		}
	}

	private void setEnemyCommand() {
		for (ActionAndStatus s : enemy) {
			if (isAllDead(party) || random.nextDouble() < 0.1
					+ (double) (s.getStatus().getLevel() - party[0].getStatus().getLevel()) / 100) {
				s.setCommand(ActionAndStatus.GUARD, 0, null);
				continue;
			}
			while (true) {
				int index = random.nextInt(party.length);
				if (party[index].getStatus().isDead())
					continue;
				s.setCommand(ActionAndStatus.FIGHT, 0, party[index].getStatus());
				break;
			}
		}
	}

	private void preButtle() {
		resetChoiseIndex();
		buttleStatus = ButtleStatus.FIGHTING;
		setEnemyCommand();
		actionStatus.clear();
		for (ActionAndStatus s : this.party)
			actionStatus.add(s);
		for (ActionAndStatus s : this.enemy)
			actionStatus.add(s);
		actionStatus.sort(
				(ActionAndStatus s1, ActionAndStatus s2) -> {
					int diff = s2.getStatus().getSpeed() - s1.getStatus().getSpeed() + random.nextInt(3) - 1;
					if (diff != 0)
						return diff;
					return random.nextInt(2) * 2 - 1;
				});
		choose.setVisible(false);
		ArrayList<String> tmp = new ArrayList<>();
		String buffer[];
		ActionAndStatus actionBuffer[] = new ActionAndStatus[actionStatus.size()];
		actionStatus.toArray(actionBuffer);
		for (ActionAndStatus s : actionBuffer) {
			if (s.getStatus().isDead()) {
				actionStatus.remove(s);
				continue;
			}
			if (s.getCommand() == ActionAndStatus.GUARD) {
				tmp.add(s.getStatus().getName() + "は身を守っている！");
				s.getStatus().Guard();
				actionStatus.remove(s);
			}
		}
		buffer = new String[tmp.size()];
		tmp.toArray(buffer);
		chat.resetMessages(buffer);
		chat.setVisible(true);
	}

	private void afterButtle() {
		resetChoiseIndex();
		for (ActionAndStatus s : this.party)
			s.getStatus().resetCondition();
		for (ActionAndStatus s : this.enemy)
			s.getStatus().resetCondition();
	}

	private boolean isAllDead(ActionAndStatus inputs[]) {
		for (ActionAndStatus s : inputs) {
			if (!s.getStatus().isDead())
				return false;
		}
		return true;
	}

	private boolean buttleTurn() {
		if (isAllDead(party)) {
			buttleStatus = ButtleStatus.FINISH;
			chat.resetMessages(new String[] { "全滅した..." });
			return false;
		}

		if (isAllDead(enemy)) {
			buttleStatus = ButtleStatus.FINISH;
			chat.resetMessages(new String[] { "勝ち申した！！" });
			return false;
		}

		while (!actionStatus.isEmpty()) {
			if (actionStatus.get(0).getStatus().isDead())
				actionStatus.remove(0);
			else
				break;
		}

		if (actionStatus.size() == 0) {
			buttleStatus = ButtleStatus.CHOOSING;
			choose.setVisible(true);
			choose.setSelectable(true);
			return false;
		}

		ActionAndStatus now = actionStatus.get(0);
		switch (now.getCommand()) {
		case ActionAndStatus.FIGHT:
			if (now.getTarget().isDead()) {
				CharacterStatus next = getNextTarget(now.getTarget());
				if (next == null)
					break;
				now.setTarget(next);
			}
			if (now.getStatus() instanceof StrongEnemyStatus && ((StrongEnemyStatus) now.getStatus()).isCharging()) {
				chat.resetMessages(new String[] { now.getStatus().getName() + "は、力をためている！" });
			} else {
				ArrayList<String> tmp = new ArrayList<>();
				String buffer[];
				ArrayList<Action> tmp2 = new ArrayList<>();
				Action buffer2[];
				final int damage = getDamage(now);
				if (origin == null)
					origin = getLocation();
				tmp2.add(() -> {
					if (now.getTarget() instanceof PlayerStatus) {
						now.getTarget().damage(damage);
						addLoopAction(new ActionFlag() {
							private int vibeCount = 0;

							public boolean action() {
								vibeCount++;
								if (vibeCount % vibeInterval == 0) {
									if (vibeCount / vibeInterval <= 9) {
										setLocation(origin.x + (random.nextInt(3) - 1) * space,
												origin.y + (random.nextInt(3) - 1) * space);
										return false;
									} else {
										setLocation(origin);
										return true;
									}
								}
								return false;
							}
						});
					} else {
						addLoopAction(new ActionFlag() {
							private BufferedImage src = now.getTarget().getFace();
							private BufferedImage tmpImage = MyImage.addColor(src, new Color(200, 100, 100, 0));
							private int changeCount = 0;

							public boolean action() {
								changeCount++;
								if (changeCount % changeInterval == 0) {
									if (changeCount / changeInterval <= 6) {
										if ((changeCount / changeInterval) % 2 == 1)
											now.getTarget().setFace(tmpImage);
										else
											now.getTarget().setFace(src);
										return false;
									} else {
										now.getTarget().resetFace();
										return true;
									}
								}
								return false;
							}
						});
					}
				});
				tmp.add(now.getStatus().getName() + "の攻撃！");
				tmp.add(now.getTarget().getName() + "は" + damage + "ダメージを受けた！");
				tmp2.add(() -> {
					if (!(now.getTarget() instanceof PlayerStatus))
						now.getTarget().damage(damage);
					if (now.getTarget().isDead()) {
						ArrayList<String> tmp3 = new ArrayList<>();
						String buffer3[];
						Action tmp4[] = null;
						tmp3.add(now.getTarget().getName() + "は倒れた！");
						for (int i = 0; i < enemy.length; i++) {
							if (enemy[i].getStatus().equals(now.getTarget())) {
								enemyTarget.setSelectable(i, false);
								enemyTarget.resetIndex();
								final int ex = now.getTarget().getEx();
								int count = 0;
								for (int j = 0; j < party.length; j++) {
									if (!party[j].getStatus().isDead())
										count++;
								}
								String str = count > 1 ? "たちは" : "は";
								tmp3.add(party[choiseIndex].getStatus().getName() + str + ex + "経験値を手に入れた！");
								tmp4 = new Action[] { null, () -> {
									for (int j = 0; j < party.length; j++) {
										if (!party[j].getStatus().isDead())
											((PlayerStatus) party[j].getStatus()).addEx(ex);
									}
								} };
								break;
							}
						}
						buffer3 = new String[tmp3.size()];
						tmp3.toArray(buffer3);
						chat.resetMessages(buffer3);
						chat.setAction(tmp4);
					}
				});
				buffer = new String[tmp.size()];
				tmp.toArray(buffer);
				chat.resetMessages(buffer);
				buffer2 = new Action[tmp2.size()];
				tmp2.toArray(buffer2);
				chat.setAction(buffer2);
			}
			break;
		/*case ActionAndStatus.SKILL:
			break;*/
		case ActionAndStatus.GUARD:
			break;
		default:
			break;
		}
		actionStatus.remove(now);
		return true;
	}

	public static int getDamage(ActionAndStatus s) {
		int damage = s.getStatus().getPower() - s.getTarget().getGuard();
		if (s.getTarget().isGuard())
			damage /= 2;
		if (damage < 0)
			damage = 0;
		return damage;
	}

	private CharacterStatus getNextTarget(CharacterStatus target) {
		ActionAndStatus tmp[] = null;
		int start = 0;
		for (int i = 0; i < enemy.length; i++) {
			if (enemy[i].getStatus().equals(target)) {
				tmp = enemy;
				start = i;
				break;
			}
		}
		for (int i = 0; i < party.length; i++) {
			if (party[i].getStatus().equals(target)) {
				tmp = party;
				start = i;
				break;
			}
		}
		if (tmp == null)
			return null;
		int index = start + 1;
		while (true) {
			if (index >= tmp.length)
				index = 0;
			if (start == index)
				break;
			if (!tmp[index].getStatus().isDead())
				return tmp[index].getStatus();
			index++;
		}
		return null;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(back, 0, 0, getWidth() + space * 2, getHeight() + space * 2, null);
		g.drawImage(back, space, space, getWidth(), getHeight(), null);
		g.drawImage(Gallery.heigen_front.image(), space, space, getWidth(), getHeight(), null);
		Graphics2D g2 = (Graphics2D) g;
		//int x = (getWidth() - enemy.length * (100 + 20) + 20) / 2 + space;
		int all = 0;
		int w = 0;
		for (ButtleStatusLabel bs : blabel)
			all += bs.getSize().width + 20;
		for (int i = 0; i < party.length; i++) {
			blabel[i].setLocation((getWidth() - all + 20) / 2 + w + space, 300 + space);
			w += blabel[i].getSize().width + 20;
		}
		for (int i = 0; i < enemy.length; i++) {
			CharacterStatus c = enemy[i].getStatus();
			if (c.isDead())
				continue;
			Point p = new Point(enemyPoints[i]);
			Dimension size = c.getFaceSize();
			p.x -= size.width / 2;
			int width = g2.getFontMetrics().stringWidth(c.getName()) + 6;
			int height = g2.getFontMetrics().getHeight() + 2;
			g2.setPaint(new GradientPaint(p.x + (-width) / 2, p.y, MyColor.toAlpha(Color.WHITE, 150),
					p.x + (size.width - width) / 2 + width, p.y + height,
					MyColor.toAlpha(Color.WHITE, 100)));
			g2.fill(new RoundRectangle2D.Double(p.x + (size.width - width) / 2, p.y, width, height, 10, 10));
			g2.setPaint(Color.BLACK);
			g2.drawString(c.getName(), p.x + 3 + (size.width - width) / 2, p.y + height - 7);
			int maxHeight = 0;
			for (ActionAndStatus s : enemy) {
				maxHeight = Math.max(maxHeight, s.getStatus().getFaceSize().height);
			}
			p.y += maxHeight - size.height;
			//p.x = x;
			g2.drawImage(c.getFace(), p.x, p.y + height + 5, p.x + size.width,
					p.y + size.height + height + 5, 0, 0,
					c.getFace().getWidth(), c.getFace().getHeight(), null);
			/*
			 * p.x += 100 / 2;
			g2.setColor(MyColor.toAlpha(MyColor.LAVENDER.COLOR, 100));
			g2.fill(new Rectangle2D.Double(x, 100, 100, 100));
			x += 100 + 20;
			*/
		}
	}

	public void addLoopAction(ActionFlag action) {
		actions.add(action);
	}

	@Override
	public void addFrame(KeyList e) {
		for (Component c : getComponents()) {
			if (c instanceof FrameUpdateListener && c.isVisible())
				((FrameUpdateListener) c).addFrame(e);
		}
		if (!isPlaying())
			return;
		if (chat.isAllPrinted() && !chat.isFinished() && buttleStatus == ButtleStatus.CHOOSING) {
			KeyList tmp = new KeyList();
			tmp.add(KeyEvent.VK_ENTER);
			chat.addFrame(tmp);
			choose.setVisible(true);
			choose.setSelectable(true);
		}
		if (chat.isFinished() && buttleStatus == ButtleStatus.FIGHTING) {
			if (!buttleTurn())
				afterButtle();
		}
		ActionFlag tmps[] = new ActionFlag[actions.size()];
		actions.toArray(tmps);
		for (ActionFlag a : tmps) {
			if (a.action())
				actions.remove(a);
		}
	}

	@Override
	public boolean isFinished() {
		if (buttleStatus == ButtleStatus.FINISH && chat.isFinished())
			return true;
		return false;
	}

	@Override
	public void chosen(ChosenEvent e) {
		if (e.getSource().equals(choose)) {
			choose.setSelectable(false);
			switch (e.getIndex()) {
			case 0:
				partyCommand.setLocation(blabel[choiseIndex].getX(),
						blabel[choiseIndex].getY() + blabel[choiseIndex].getHeight() + 5);
				partyCommand.setSelectable(true);
				partyCommand.setVisible(true);
				partyChoise.clear();
				chat.setVisible(false);
				break;
			case 1:
				ArrayList<String> tmp = new ArrayList<>();
				String buf[];
				tmp.add(party[choiseIndex].getStatus().getName() + "たちは逃げ出した！！");
				int nowLv = 0;
				for (ActionAndStatus s : party) {
					if (!s.getStatus().isDead())
						nowLv = Math.max(nowLv, s.getStatus().getLevel());
				}
				buttleStatus = ButtleStatus.FINISH;
				double a = random.nextDouble();
				double b = 0.5 + (double) (nowLv - maxLv) / 40;
				//System.out.println(a + " > " + b);
				if (a > b) {
					tmp.add("しかし、逃げ切れなかった！");
					buttleStatus = ButtleStatus.FIGHTING;
				}
				buf = new String[tmp.size()];
				tmp.toArray(buf);
				chat.resetMessages(buf);
				chat.setAction(new Action[] { null, () -> {
					for (ActionAndStatus s : party) {
						s.setCommand(ActionAndStatus.NONE, 0, null);
					}
					preButtle();
				} });
				break;
			default:
				choose.setSelectable(true);
				break;
			}
		} else if (e.getSource().equals(partyCommand)) {
			partyChoise.add(e.getIndex());
			switch (e.getIndex()) {
			case -1:
				setCommandList(-1);
				break;
			case ActionAndStatus.FIGHT:
				partyCommand.setSelectable(false);
				enemyTarget.setSelectable(true);
				enemyTarget.updateLocation();
				enemyTarget.setVisible(true);
				break;
			//case ActionAndStatus.SKILL:
			case ActionAndStatus.GUARD:
				setCommandList(1);
				if (choiseIndex >= party.length) {
					partyCommand.setSelectable(false);
					partyCommand.setVisible(false);
					preButtle();
				}
				break;
			}
		} else if (e.getSource().equals(enemyTarget)) {
			enemyTarget.setSelectable(false);
			enemyTarget.setVisible(false);
			if (e.getIndex() == -1)
				partyChoise.remove(partyChoise.size() - 1);
			else {
				partyChoise.add(e.getIndex());
				setCommandList(1);
				if (choiseIndex >= party.length) {
					partyCommand.setVisible(false);
					preButtle();
					return;
				}
			}
			partyCommand.setSelectable(true);
		}
	}

	@Override
	public void destroy(FreezePanel g) {
		if (g instanceof GamePanel) {
			boolean flag = true;
			for (ActionAndStatus s : party) {
				if (!s.getStatus().isDead()) {
					flag = false;
					break;
				}
			}
			if (flag) {
				((GamePanel) g).destroyAll();
				Sound.setBGM(BGMList.NONE);
			} else
				Sound.setBGM(((GamePanel) g).getMyBackground().getBGM());
		} else
			Sound.setBGM(BGMList.FIELD);
		g.remove(this);
		g.start(this);
		for (Component c : getComponents()) {
			if (c instanceof FrameUpdateListener && c.isVisible())
				((FrameUpdateListener) c).destroy(this);
		}
	}

	@Override
	public boolean isDestroying() {
		return isDestroying;
	}

	@Override
	public boolean isPlayable() {
		return isVisible();
	}
}

enum ButtleStatus {
	CHOOSING, FIGHTING, FINISH;
}