package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import game.action.Action;
import game.background.Background;
import game.background.BackgroundColliderColor;
import game.chat.ChatPanel;
import game.obj.Creature;
import game.obj.Grass;
import game.obj.HealPort;
import game.obj.Mochiri;
import game.obj.Object;
import game.obj.Player;
import game.status.CharacterStatus;
import game.status.EnemyStatusList;
import game.status.PlayerStatus;
import utility.Gallery;

public class MapEventStrage {

	private int flag = 0;
	private Background nowMap = Background.MATAMA;

	public void setEventFlag(int flag) {
		this.flag = flag;
	}

	public void setFirstObjects(GamePanel g) {
		switch (nowMap) {
		case MATAMA:
			g.setButtlable(false);
			if (flag < 2) {
				Object ozi = new Object(Gallery.IMG_0175.image(), new Rectangle(750, 800, 32, 32)) {

					@Override
					public void Hit(GamePanel p) {
					}

					@Override
					public boolean pressEnter(Player p, FreezePanel f) {
						if (p.getSubjectivePosition(this.getColliderOnMap()) == Direction.FRONT) {
							if (flag == 1) {
								f.suspend(new Event(g, 1));
							} else {
								goodbyeChat();
							}
							return true;
						}
						return false;
					}

					private void goodbyeChat() {
						ChatPanel p = new ChatPanel(
								new String[] { "ついに、ロマンも村を出てしまうんだな...", "...何でもない、\n魔物には気をつけて旅をするんだよ。" });
						p.setName("おじさん");
						p.setBounds(ChatPanel.normalStyle);
						g.suspend(p);
						g.add(p);
					}

				};
				ozi.setLabel("ozi");
				g.addObject(ozi);
				Object paruto = new Creature(Gallery.paruto.image(), new Rectangle(750, 832, 32, 32),
						new Dimension(32, 32), Direction.BACK) {

					@Override
					public void Hit(GamePanel p) {
					}

					@Override
					public boolean pressEnter(Player p, FreezePanel f) {
						return false;
					}

				};
				paruto.setLabel("paruto");
				g.addObject(paruto);
			} else {
				/*
				Object man = new Object(Gallery.mochiri_back.image(),
						new Rectangle(700, 600, 32, 32), new Rectangle(0, 0, 250, 250)) {
					boolean b = false;
				
					public void Hit(GamePanel p) {
						if (b) {
							return;
						}
						b = true;
						/*ButtlePanel b = new ButtlePanel();
						b.setBounds(100, MainWindow.size.height - 150, MainWindow.size.width - 200, 100);
						JLabel l = new JLabel("hit");
						l.setFont(new Font("", Font.PLAIN, 50));
						l.setBounds(100, 0, 200, 100);
						b.add(l);
						p.add(b);
					}
				
					private int count = 0;
				
					@Override
					public boolean pressEnter(Player p) {
						count++;
						g.setCamera(count % 2 == 0 ? p.getCamera() : this.getCamera(), false);
						if (!(p.getSubjectivePosition(this.getColliderOnMap()) == Direction.FRONT
								&& this.getSubjectivePosition(p.getColliderOnMap()) == Direction.FRONT)) {
							return false;
						}
						sampleChat();
						return true;
					}
				
					private void sampleChat() {
						ChatPanel p = new ChatPanel(new String[] { "ハハッ、夢の国へようこそ！楽しんでいってね！", "...えっ、続きはないよ？" });
						p.setBounds(ChatPanel.normalStyle);
						g.suspend(p);
						g.add(p);
					}
				
				};
				man.setCollider(14, 5, 20, 50);
				g.addObject(man);
				*/
				Object old = new Object(Gallery.IMG_0176.image(), new Rectangle(835, 605, 32, 32)) {
					public void Hit(GamePanel p) {
					}

					@Override
					public boolean pressEnter(Player p, FreezePanel f) {
						if (p.getSubjectivePosition(this.getColliderOnMap()) == Direction.FRONT
								&& this.getSubjectivePosition(p.getColliderOnMap()) == Direction.FRONT) {
							unchikuChat();
							return true;
						}
						return false;
					}

					private void unchikuChat() {
						ChatPanel p = new ChatPanel(
								new String[] { "ここはどんな場所かだって？", "ここはな、店になる予定だった、跡地じゃよ...",
										"...ふぉっふぉっ\n若者よ、気にするでないぞ。" });
						p.setName("物知りなお爺さん");
						p.setBounds(ChatPanel.normalStyle);
						g.suspend(p);
						g.add(p);
					}
				};
				old.setCollider(new Rectangle(6, 20, 20, 10));
				g.addObject(old);
				Object star = new Object(Gallery.star.image(),
						new Rectangle(1265, 400, 10, 10), new Rectangle(0, 0, 20, 20)) {
					public void Hit(GamePanel p) {
					}

					@Override
					public boolean pressEnter(Player p, FreezePanel f) {
						if (p.getSubjectivePosition(this.getColliderOnMap()) == Direction.CENTER) {
							gomiNotice();
							g.removeObject(this);
							return true;
						}
						return false;
					}

					private void gomiNotice() {
						ChatPanel p = new ChatPanel(new String[] { "ただのごみだった！！" });
						p.setBounds(ChatPanel.normalStyle);
						g.suspend(p);
						g.add(p);
					}
				};
				star.setPassable(true);
				g.addObject(star);
				g.addObject(new Object(Gallery.IMG_0166.image(),
						new Rectangle(815, 500, 32, 32)) {
					public void Hit(GamePanel p) {
					}

					@Override
					public boolean pressEnter(Player p, FreezePanel f) {
						if (p.getSubjectivePosition(this.getColliderOnMap()) == Direction.FRONT) {
							helloChat();
							return true;
						}
						return false;
					}

					private void helloChat() {
						ChatPanel p = new ChatPanel(new String[] { "いってらっしゃい、ロマン！", "...えっ、別に何も起きないよ？" });
						p.setName("ただの一般人");
						p.setBounds(ChatPanel.normalStyle);
						g.suspend(p);
						g.add(p);
					}
				});
				g.addObject(new Object(Gallery.IMG_0172.image(), new Rectangle(450, 500, 32, 32)) {
					public void Hit(GamePanel p) {
					}

					@Override
					public boolean pressEnter(Player p, FreezePanel f) {
						if (p.getSubjectivePosition(this.getColliderOnMap()) == Direction.FRONT) {
							helloChat();
							return true;
						}
						return false;
					}

					private void helloChat() {
						ChatPanel p = new ChatPanel(new String[] { "あぁ、大事な収入源が...", "なんてね。\nしっかり君の親から巻き上げてるわよ(*^^)v" });
						p.setName("大家さん");
						p.setBounds(ChatPanel.normalStyle);
						g.suspend(p);
						g.add(p);
					}
				});
				g.addObject(new Object(Gallery.IMG_0167.image(),
						new Rectangle(285, 608, 32, 32)) {

					private int count = 0;
					private String chats[][] = new String[][] { new String[] { "何見てんのよ！\nこれは私の畑なんだからね！！" },
							new String[] { "...いや、そんなにじっと見つめられても、あげないんだからね！" },
							new String[] { "...", "...", "...やっぱり、だめなんだからね！" },
							new String[] { "...", "うぅ、分かったわよ！あげればいいんでしょ！\nほら、ありがたく受け取りなさい！" },
							new String[] { "なんなのよ！しつこいわね！\nもうあげないんだから、あっち行きなさい！" }
					};

					public void Hit(GamePanel p) {
					}

					@Override
					public boolean pressEnter(Player p, FreezePanel f) {
						if (p.getSubjectivePosition(this.getColliderOnMap()) == Direction.FRONT) {
							helloChat();
							return true;
						}
						return false;
					}

					private void helloChat() {
						ChatPanel p = null;
						if (count < 4)
							p = new ChatPanel(chats[count]);
						else
							p = new ChatPanel(chats[4]);
						if (count == 3) {
							p.setAction(new Action[] { null, () -> {
								for (int i = 0; i < g.getPlayer().getPartyStatusList().size(); i++) {
									PlayerStatus tmp = g.getPlayer().getPartyStatusList().get(i);
									tmp.setHP(tmp.getMaxHP());
									tmp.setMP(tmp.getMaxMP());
								}
								ChatPanel p2 = new ChatPanel(new String[] { "\n　パーティ全員のHPが全回復しました！" });
								p2.setBounds(ChatPanel.normalStyle);
								g.suspend(p2);
								g.add(p2);
							} });
						}
						count++;
						p.setName("生意気な女の子");
						p.setBounds(ChatPanel.normalStyle);
						g.suspend(p);
						g.add(p);
					}
				});
			}
			break;
		case WASHO:
			g.setButtlable(true);

			//make_kusa
			for (int i = 0; i < 25; i++) {
				for (int j = 0; j < 47; j++) {
					int x = i * (Grass.width - 20) + 195;
					int y = j * (Grass.height - 20) + 41;
					if (Background.WASHO.getBack()[0].getRGB(x + Grass.width / 5 * 2,
							y + Grass.height / 3 * 2) == new Color(30, 67, 3).getRGB()
							&& Background.WASHO.getBack()[0].getRGB(x + Grass.width / 5 * 2,
									y + Grass.height) == new Color(30, 67, 3).getRGB()
							&& Background.WASHO.getBack()[0].getRGB(x + Grass.width / 4 * 3,
									y + Grass.height / 3 * 2) == new Color(30, 67, 3).getRGB()
							&& Background.WASHO.getBack()[0].getRGB(x + Grass.width / 4 * 3,
									y + Grass.height) == new Color(30, 67, 3).getRGB()
							&& !(i == 10 && j == 9) && !(i == 7 && j == 42) || i == 1)
						g.addObject(new Grass(x, y));
				}
			}

			g.addObject(new HealPort(new Point(710, 450)));
			break;
		case MITSUKUBI1:
			g.setButtlable(true);

			//make_kusa
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 6; j++) {
					int x = i * (Grass.width - 20) + 860;
					int y = j * (Grass.height - 20) + 750;
					g.addObject(new Grass(x, y));
				}
			}

			if (flag <= 2) {
				Object bonus = new Object(Gallery.IMG_0170.image(), new Rectangle(1020, 105, 32, 32)) {
					private Object thisObj = this;

					@Override
					public void Hit(GamePanel p) {
					}

					@Override
					public boolean pressEnter(Player p, FreezePanel f) {
						if (p.getSubjectivePosition(getColliderOnMap()) == Direction.FRONT
								&& getSubjectivePosition(p.getColliderOnMap()) == Direction.FRONT) {
							bonusChat(f);
							return true;
						}
						return false;
					}

					private void bonusChat(FreezePanel f) {
						ChatPanel p = new ChatPanel(new String[] { "よく見つけたな。君には素質がある。\n...よし、私が鍛えてやろう。" });
						p.setName("謎の女性");
						p.setAction(new Action[] { () -> {
							ChatPanel p2 = new ChatPanel(new String[] { "ロマンたちは500経験値を手に入れた！！" });
							p2.setBounds(ChatPanel.normalStyle);
							for (PlayerStatus ps : g.getPlayer().getPartyStatusList()) {
								ps.addEx(500);
							}
							g.setEventFlag(3);
							f.suspend(p2);
							f.add(p2);
							g.removeObject(thisObj);
						} });
						p.setBounds(ChatPanel.normalStyle);
						f.suspend(p);
						f.add(p);
					}

				};
				bonus.setDirection(Direction.FRONT);
				g.addObject(bonus);
			}
			g.addObject(new HealPort(new Point(375, 595)));
			g.addObject(new HealPort(new Point(305, 300)));
			break;
		case MITSUKUBI2:
			g.addObject(new HealPort(new Point(260, 385)));
			g.setButtlable(true);
			break;
		case MITSUKUBI3:
			g.addObject(new HealPort(new Point(650, 1110)));
			g.addObject(new HealPort(new Point(435, 180)));
			g.setButtlable(true);
			break;
		case BITTER:
			g.addObject(new HealPort(new Point(600, 485)));
			g.addObject(new HealPort(new Point(840, 725)));
			Object area = new Object(null, new Rectangle(908, 500, 30, 93)) {

				@Override
				public void Hit(GamePanel p) {
				}

				@Override
				public boolean pressEnter(Player p, FreezePanel f) {
					if (p.getSubjectivePosition(getColliderOnMap()) == Direction.FRONT
							&& getSubjectivePosition(p.getColliderOnMap()) == Direction.FRONT) {
						instigateChat(f);
						return true;
					}
					return false;
				}

				private void instigateChat(FreezePanel f) {
					ChatPanel p = new ChatPanel(new String[] { "うぇーい！！\nこっちには来れないだろう？" });
					p.setName("モチリたち");
					p.setBounds(ChatPanel.normalStyle);
					f.suspend(p);
					f.add(p);
				}

			};
			area.setDirection(Direction.RIGHT);
			area.setPassable(true);
			g.addObject(area);
			for (int i = 0; i < 4; i++)
				g.addObject(new Mochiri(new Point(995 + (i % 2 * 60), 553 + (i / 2 * 15))));
			Mochiri mochi = new Mochiri(new Point(330, 430)) {
				private int count = 0;
				private Mochiri me = this;

				public boolean pressEnter(Player p, FreezePanel f) {
					if (p.getSubjectivePosition(getColliderOnMap()) == Direction.FRONT) {
						boolean tmp = lockDirection(false);
						transform(getAbsolutePosition(p.getColliderOnMap()));
						lockDirection(tmp);
						count++;
						if (count <= 3)
							friendlyChat();
						else
							startButtle();
						return true;
					}
					return false;
				}

				private void friendlyChat() {
					ChatPanel p = new ChatPanel(new String[] { "...ひえっ！！", "いじめないで！\nぼく、悪いモチリじゃないよ。" });
					p.setName("弱気なモチリ");
					p.setBounds(ChatPanel.normalStyle);
					g.suspend(p);
					g.add(p);
				}

				private void startButtle() {
					ChatPanel p = new ChatPanel(new String[] { "いじめるなって言ってるだろ！！" });
					p.setName("強気なモチリ");
					p.setBounds(ChatPanel.normalStyle);
					p.setAction(new Action[] { () -> {
						ButtlePanel tmp = g.startButtle(new CharacterStatus[] {
								EnemyStatusList.DOMOCHIRI.getStatus().getSuperStatus(1.5).getRandomStatus(),
								EnemyStatusList.MOCHIRI.getStatus().getSuperStatus(15).getRandomStatus(),
								EnemyStatusList.DOMOCHIRI.getStatus().getSuperStatus(1.5).getRandomStatus(),
								EnemyStatusList.MOCHIRI.getStatus().getSuperStatus(15).getRandomStatus(),
								EnemyStatusList.DOMOCHIRI.getStatus().getSuperStatus(1.5).getRandomStatus() });
						tmp.setEscapable(false);
						tmp.addAction(() -> {
							g.removeObject(me);
						});
					}
					});
					g.suspend(p);
					g.add(p);
				}
			};
			//mochi.setVerticalMovable(false);
			g.addObject(mochi);
			g.setButtlable(true);
			break;
		case NYUIN:
			if (flag >= 2) {
				Object maou = new Object(Gallery.boss_sprite.image(), new Rectangle(733, 495, 32, 48)) {

					@Override
					public void Hit(GamePanel p) {
					}

					@Override
					public boolean pressEnter(Player p, FreezePanel f) {
						if (p.getSubjectivePosition(this.getColliderOnMap()) == Direction.FRONT && flag >= 2) {
							f.suspend(new Event(g, 2));
							return true;
						}
						return false;
					}

				};
				maou.setCollider(5, 4, 22, 40);
				maou.setLabel("maou");
				g.addObject(maou);
			}
			Object dodon = new Object(null, new Rectangle(1286, 737, 100, 70)) {
				private Object me = this;

				public boolean pressEnter(Player p, FreezePanel f) {
					if (p.getSubjectivePosition(getColliderOnMap()) == Direction.FRONT) {
						startButtle();
						return true;
					}
					return false;
				}

				public void Hit(GamePanel p) {
				}

				private void startButtle() {
					ChatPanel p = new ChatPanel(new String[] { "ドドーンドドーン！！" });
					p.setName("？？？");
					p.setBounds(ChatPanel.normalStyle);
					p.setAction(new Action[] { () -> {
						ButtlePanel tmp = g.startButtle(new CharacterStatus[] {
								EnemyStatusList.DODONNNU.getStatus().getSuperStatus(1.5).getRandomStatus(),
								EnemyStatusList.DODONNNU.getStatus().getSuperStatus(1.5).getRandomStatus(),
								EnemyStatusList.BIKURINKO.getStatus().getSuperStatus(1.5).getRandomStatus(),
								EnemyStatusList.DODONNNU.getStatus().getSuperStatus(1.5).getRandomStatus(),
								EnemyStatusList.DODONNNU.getStatus().getSuperStatus(1.5).getRandomStatus() });
						tmp.setEscapable(false);
						tmp.addAction(() -> {
							g.removeObject(me);
						});
					}
					});
					g.suspend(p);
					g.add(p);
				}

			};
			g.addObject(dodon);
			g.setButtlable(false);
			break;
		default:
			break;
		}
	}

	public void setNowMap(Background now) {
		this.nowMap = now;
	}

	public void happenEvent(BackgroundColliderColor flag, GamePanel g) {
		ChatPanel p;
		switch (nowMap) {
		case WASHO:
			switch (flag) {
			case EVENT1:
				if (g.getPlayer().getDirection() == Direction.BACK) {
					p = new ChatPanel(new String[] { "↑　この先　ミツクビ山\n　　登山難易度：　高" });
					p.setBounds(ChatPanel.normalStyle);
					g.suspend(p);
					g.add(p);
				}
				break;
			case EVENT2:
				if (g.getPlayer().getDirection() == Direction.BACK) {
					p = new ChatPanel(new String[] { "　ここは　ワショー平原\n　草がいっぱい、重くなるかも" });
					p.setBounds(ChatPanel.normalStyle);
					g.suspend(p);
					g.add(p);
				}
				break;
			case EVENT3:
				if (g.getPlayer().getDirection() == Direction.FRONT) {
					p = new ChatPanel(new String[] { "裏には何も書かれていなかった。" });
					p.setBounds(ChatPanel.normalStyle);
					g.suspend(p);
					g.add(p);
				}
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}

	public RecordOfMap getNextMap(BackgroundColliderColor flag) {
		switch (nowMap) {
		case MATAMA:
			switch (flag) {
			case MOVE1:
				return new RecordOfMap(Background.WASHO, new Point(277, 770), Direction.BACK);
			default:
				break;
			}
			break;
		case WASHO:
			switch (flag) {
			case MOVE1:
				return new RecordOfMap(Background.MITSUKUBI1, new Point(915, 790), Direction.BACK);
			case MOVE2:
				return new RecordOfMap(Background.MATAMA, new Point(745, 900), Direction.BACK);
			default:
				break;
			}
			break;
		case MITSUKUBI1:
			switch (flag) {
			case MOVE1:
				return new RecordOfMap(Background.WASHO, new Point(343, 200), Direction.FRONT);
			case MOVE2:
				return new RecordOfMap(Background.MITSUKUBI2, new Point(890, 725), Direction.BACK);
			default:
				break;
			}
			break;
		case MITSUKUBI2:
			switch (flag) {
			case MOVE1:
				return new RecordOfMap(Background.MITSUKUBI1, new Point(810, 40), Direction.FRONT);
			case MOVE2:
				return new RecordOfMap(Background.MITSUKUBI3, new Point(597, 1560), Direction.BACK);
			default:
				break;
			}
			break;
		case MITSUKUBI3:
			switch (flag) {
			case MOVE1:
				return new RecordOfMap(Background.MITSUKUBI2, new Point(940, 100), Direction.FRONT);
			case MOVE2:
				return new RecordOfMap(Background.BITTER, new Point(105, 357), Direction.LEFT);
			default:
				break;
			}
			break;
		case BITTER:
			switch (flag) {
			case MOVE1:
				return new RecordOfMap(Background.MITSUKUBI3, new Point(950, 210), Direction.RIGHT);
			case MOVE2:
				return new RecordOfMap(Background.NYUIN, new Point(70, 515), Direction.LEFT);
			default:
				break;
			}
			break;
		case NYUIN:
			switch (flag) {
			case MOVE1:
				return new RecordOfMap(Background.BITTER, new Point(1110, 448), Direction.RIGHT);
			default:
				break;
			}
			break;
		default:
			break;
		}
		return null;
	}

}
