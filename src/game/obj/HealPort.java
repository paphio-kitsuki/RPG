package game.obj;

import java.awt.Point;
import java.awt.Rectangle;

import game.Direction;
import game.FreezePanel;
import game.GamePanel;
import game.chat.ChatPanel;
import game.status.PlayerStatus;
import utility.Gallery;

public class HealPort extends Object {

	private static final Gallery healPort = Gallery.heal_port;
	private static final int interval = 100;//ms
	private static final int max = 4;
	private int count = 0;

	public HealPort(Point p) {
		super(healPort.image(),
				new Rectangle(p.x, p.y, healPort.image().getWidth() / max, healPort.image().getHeight()));
		setCollider(4, 36, getBounds().width - 8, getBounds().height - 40);
	}

	@Override
	public void passTime() {
		super.passTime();
		if (getPassCount() >= (double)interval / 1000 * GamePanel.FPS) {
			resetPassCount();
			count = (count + 1) % max;
			setShowBounds(getBounds().width * (3 - count), 0, getBounds().width, getBounds().height);
		}
	}

	@Override
	public void Hit(GamePanel p) {
	}

	@Override
	public boolean pressEnter(Player p, FreezePanel f) {
		if (p.getSubjectivePosition(this.getColliderOnMap()) == Direction.FRONT) {
			for (int i = 0; i < p.getPartyStatusList().size(); i++) {
				PlayerStatus tmp = p.getPartyStatusList().get(i);
				tmp.setHP(tmp.getMaxHP());
				tmp.setMP(tmp.getMaxMP());
			}
			notify(f);
			return true;
		}
		return false;
	}

	private void notify(FreezePanel f) {
		ChatPanel p = new ChatPanel(new String[] { "\n　パーティ全員のHPが全回復しました！" });
		p.setBounds(ChatPanel.normalStyle);
		f.suspend(p);
		f.add(p);
	}

}
