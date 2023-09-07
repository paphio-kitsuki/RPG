package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import game.action.Action;
import game.action.ActionFlag;
import game.background.Background;
import game.chat.ChatPanel;
import game.chat.ChatRecord;
import game.obj.Creature;
import game.obj.Mochiri;
import game.obj.Object;
import game.obj.Player;
import game.status.CharacterStatus;
import game.status.EnemyStatusList;
import game.status.PlayerStatus;
import utility.Gallery;
import utility.KeyList;

public class Event implements FrameUpdateListener {

	private boolean isPlayable = true;
	private final GamePanel target;
	private final int flag;
	private int index = 1;
	private Creature monstar = new Creature(Gallery.mochiri_sprite.image(), new Rectangle(740, 1000, 25, 25)) {

		@Override
		public void Hit(GamePanel p) {
		}

		@Override
		public boolean pressEnter(Player p, FreezePanel f) {
			return false;
		}
	};

	public void setChat(ChatPanel p, ChatRecord r) {
		if (r != null) {
			p.resetMessages(r.chats());
			p.setName(r.name());
			switch (r.name()) {
			case "ロマン":
				target.setCamera(target.getPlayer().getCamera(), false);
				break;
			case "おじさん":
				target.setCamera(target.searchObject("ozi").getCamera(), false);
				break;
			case "？？？":
			case "パルト":
				target.setCamera(target.searchObject("paruto").getCamera(), false);
				break;
			case "魔王":
				target.setCamera(target.searchObject("maou").getCamera(), false);
				break;
			case "配下Ａ":
				target.setCamera(target.searchObject("haikaA").getCamera(), false);
				break;
			case "配下たち":
			case "配下Ｂ":
				target.setCamera(target.searchObject("haikaB").getCamera(), false);
				break;
			case "配下Ｂ２":
				target.setCamera(target.searchObject("haikaB2").getCamera(), false);
				break;
			case "村人Ａ":
				target.setCamera(target.searchObject("muraA").getCamera(), false);
				break;
			case "村人Ｂ":
				target.setCamera(target.searchObject("muraB").getCamera(), false);
				break;
			case "村人Ｂ２":
				target.setCamera(target.searchObject("muraB2").getCamera(), false);
				break;
			}
		}
		target.suspend(p);
		target.add(p);
	}

	private final ActionFlag events[][] = new ActionFlag[][] { { new ActionFlag() {

		public boolean action() {
			target.setBackground(Background.MATAMA);
			Player p = target.getPlayer();
			p.setLocation(new Point(327, 450));
			target.setButtlable(false);
			target.setMovable(false);
			return true;
		}
	}, new ActionFlag() {
		private ChatPanel p = null;

		public boolean action() {
			if (p == null) {
				p = new ChatPanel(new String[] { "今日もご視聴ありがとうございましたー！" });
				p.setName("ロマン");
				p.setBounds(ChatPanel.normalStyle);
				setChat(p, null);
				return false;
			}
			return p.isFinished();
		}
	}, new ActionFlag() {
		private boolean flag = false;

		public boolean action() {
			if (!flag) {
				flag = true;
				target.brightin(Color.BLACK, null);
				return false;
			}
			return !target.isEffecting();
		}
	}, new ActionFlag() {
		private ChatPanel p = null;
		private ChatRecord chats[] = new ChatRecord[] {
				new ChatRecord("ロマン", new String[] { "はあ...\n今日も再生回数伸びなかったなあ..." }),
				new ChatRecord("", new String[] { "私の名前はロマン\n田舎に住んでいる、しがない動画配信者だ", "配信者と言ったって、動画の視聴回数は少ない\n今日は9回だった",
						"どうにかしてバズりたい！\nみんなにチヤホヤされたい！", "\nだって、そのために配信者になったのだから！" }),
				new ChatRecord("ロマン", new String[] {
						"うむむ...かくなる上は、炎上商法か？\nいや、人様に迷惑をかける訳にはいかないし...", "でも、こんな田舎でできることなんて、限られているからなぁ...",
						"都会に行くには、山を越えなきゃいけないし...\nいっそのこと、都会から誰か来ればいいのにな～" }),
		};
		private int index = 0;

		public boolean action() {
			if (p == null) {
				p = new ChatPanel();
				p.setBounds(ChatPanel.normalStyle);
				setChat(p, chats[0]);
				return false;
			}
			if (p.isFinished()) {
				index++;
				if (index >= chats.length)
					return true;
				p.setBounds(ChatPanel.normalStyle);
				if (index == 1)
					p.setLocation(p.getX(), p.getY() - 200);
				setChat(p, chats[index]);
			}
			return false;
		}
	}, new ActionFlag() {
		private ChatPanel p = null;
		private final int interval = 5;
		private int count = 0;
		private Point location = null;

		public boolean action() {
			if (p == null) {
				p = new ChatPanel(new String[] {
						"\n『都会から人が来たぞ！！！』" });
				p.setFont(new Font("", Font.BOLD, p.getFont().getSize() + 10));
				p.setBounds(ChatPanel.normalStyle);
				setChat(p, null);
				location = p.getLocation();
				return false;
			}
			count++;
			if (count % interval == 0 && count < 10 * interval)
				p.setLocation(vibrate(location));
			else if (count >= 10 * interval)
				p.setLocation(location);
			return p.isFinished();
		}
	}, new ActionFlag() {
		private ChatPanel p = null;

		public boolean action() {
			if (p == null) {
				p = new ChatPanel(new String[] {
						"ぴゃっ！？\n今の声...外から？", "都会の人が来たって、ホントかな？\n行ってみよう！" });
				p.setName("ロマン");
				p.setBounds(ChatPanel.normalStyle);
				setChat(p, null);
				p.setAction(new Action[] { null, new Action() {
					public void action() {
						target.setEventFlag(1);
						target.setCamera(target.getPlayer().getCamera(), false);
					}
				} });
				return false;
			}
			return p.isFinished();
		}
	} }, new ActionFlag[] { new ActionFlag() {

		public boolean action() {
			target.setButtlable(false);
			target.setMovable(false);
			return true;
		}

	}, new ActionFlag() {
		private ChatPanel p = null;
		private ChatRecord chats[] = new ChatRecord[] {
				new ChatRecord("ロマン", new String[] { "おじさん、都会の人が来たってホント？" }),
				new ChatRecord("おじさん", new String[] { "ああ、ロマン。この人が、いきなり村に来たんだ\n格好から見て、都会の人だろ？" }),
				new ChatRecord("ロマン", new String[] { "息を切らして、急いでたみたいだけど..." + "こんな田舎に、わざわざ山を越えてどうしたんだろう？" }),
				new ChatRecord("？？？", new String[] { "魔物が、魔物がぁ！！" }),
				new ChatRecord("おじさん", new String[] { "お、おちつけって！" }),
				new ChatRecord("ロマン", new String[] { "そうだよ、深呼吸、深呼吸！" }),
				new ChatRecord("？？？", new String[] { "すー、はー、すー、はー。" }),
				new ChatRecord("ロマン", new String[] { "おちついた？" }),
				new ChatRecord("？？？", new String[] { "ああ、ありがとう" }),
				new ChatRecord("おじさん", new String[] { "それで、一体何があったんだ？" }),
				new ChatRecord("？？？", new String[] { "ああ、それがな...", "俺は城塞都市、ニューインから来たんだ" }),
				new ChatRecord("おじさん", new String[] { "ニューインって言ったら、この村から山を一つ越えるくらい遠くじゃないか" }),
				new ChatRecord("？？？", new String[] { "ああ。それで、その都市がたくさんの魔物に侵略されたんだ" }),
				new ChatRecord("ロマン", new String[] { "な、なんだって～！？" }),
				new ChatRecord("？？？", new String[] { "...なんか、わざとらしくないか？" }),
				new ChatRecord("おじさん", new String[] { "まあ、この子のことは放っておいてくれ。\nそれで、ここまで逃げてきたってわけか。" }),
				new ChatRecord("？？？", new String[] { "そうだ。無我夢中で逃げてきたんだ。" }),
				new ChatRecord("おじさん", new String[] { "疲れているだろうし、ウチに来るか？" }),
				new ChatRecord("？？？", new String[] { "ああ、お願いするよ。" }),
		};
		private int index = 0;

		public boolean action() {
			if (p == null) {
				p = new ChatPanel();
				p.setBounds(ChatPanel.normalStyle);
				setChat(p, chats[0]);
				return false;
			}
			if (p.isFinished()) {
				index++;
				if (index >= chats.length)
					return true;
				setChat(p, chats[index]);
			}
			return false;
		}
	}, new ActionFlag() {
		private ChatPanel p = null;
		private final int interval = 5;
		private int count = 0;
		private Point location = null;
		private final Point goal = new Point(740, 842);
		private Point parutoGoal;
		private Point romanGoal = null;
		private Creature paruto = null;
		private Player roman = null;

		public boolean action() {
			if (p == null) {
				p = new ChatPanel(new String[] { "グワオォォォォォ！！！" });
				p.setBounds(ChatPanel.normalStyle);
				p.setLocation(p.getX(), p.getY() - 300);
				setChat(p, null);
				p.setStartPoint(new Point(p.getStartPoint().x + 10, p.getStartPoint().y + 50));
				p.setFont(new Font("", Font.BOLD, p.getFont().getSize() + 10));
				p.setFinishable(false);
				location = p.getLocation();
				roman = target.getPlayer();
				roman.transform(Direction.FRONT);
				if (roman.getBounds().y + roman.getBounds().height > 832)
					romanGoal = new Point(roman.getBounds().x, 832 - roman.getBounds().height);
				parutoGoal = roman.getColliderOnMap().x < 750 ? new Point(780, 810) : new Point(720, 810);
				paruto = (Creature) target.searchObject("paruto");
				paruto.setSpeed(150);
				paruto.transform(Direction.FRONT);
				monstar.setSpeed(100);
				monstar.transform(Direction.BACK);
				Rectangle origin = Background.MATAMA.getCamera();
				target.addObject(monstar);
				monstar.setLimit(new Rectangle(origin.x, origin.y, origin.width, origin.height + 100));
				return false;
			}
			count++;
			if (count % interval == 0 && count < 10 * interval)
				p.setLocation(vibrate(location));
			else if (count >= 10 * interval)
				p.setLocation(location);
			boolean flag = forceToMove(monstar, goal, true, false);
			flag &= forceToMove(paruto, parutoGoal, true, true);
			flag &= forceToMove(roman, romanGoal, true, true);
			if (flag)
				p.setFinishable(true);
			return p.isFinished();
		}
	}, new ActionFlag() {
		private ChatPanel p = null;
		private ChatRecord chats[] = new ChatRecord[] {
				new ChatRecord("おじさん", new String[] { "ま、魔物だぁ！？" }),
				new ChatRecord("？？？", new String[] { "こんなところまで...！\n(さすがにこの村に迷惑はかけたくない...！)" }),
				new ChatRecord("ロマン", new String[] { "..." }),
				new ChatRecord("？？？", new String[] { "何をしている、子供！\nここは俺に任せて逃げろ！" }),
				new ChatRecord("ロマン", new String[] { "そうだ、魔物だ！" }),
				new ChatRecord("おじさん", new String[] { "は？" }),
				new ChatRecord("ロマン", new String[] { "魔物を倒すんだよ！\nその様子を、配信するんだ！" }),
				new ChatRecord("？？？", new String[] { "この子供は何を言っているんだ？" }),
				new ChatRecord("おじさん", new String[] { "さぁ..." }),
				new ChatRecord("ロマン", new String[] { "善は急げ！　戦うぞ！" }),
				new ChatRecord("おじさん", new String[] { "おい待て、ロマン！" }),
				new ChatRecord("？？？", new String[] { "それ、木の枝じゃないか！\nそんなので魔物と戦うなんて正気か！？" }),
				new ChatRecord("ロマン", new String[] { "大丈夫、問題はない！" }),
				new ChatRecord("？？？", new String[] { "大アリだろぉぉぉ！？" }),
		};
		private int index = 0;
		private final int interval = 5;
		private int count = 0;
		private Point location = null;
		private Font font = null;

		public boolean action() {
			if (p == null) {
				p = new ChatPanel();
				font = p.getFont();
				p.setBounds(ChatPanel.normalStyle);
				p.setLocation(p.getX(), p.getY() - 300);
				setChat(p, chats[0]);
				location = p.getLocation();
				return false;
			}
			if (p.isFinished()) {
				index++;
				if (index >= chats.length)
					return true;
				p.setBounds(ChatPanel.normalStyle);
				p.setLocation(p.getX(), p.getY() - 300);
				setChat(p, chats[index]);
				if (index == 4 || index == 12 || index == 13) {
					p.setFont(new Font("", Font.BOLD, font.getSize() + 10));
					count = 0;
				} else {
					p.setFont(font);
				}
			}
			if (index == 4 || index == 12 || index == 13) {
				count++;
				if (count % interval == 0 && count < 10 * interval) {
					p.setLocation(vibrate(location));
				} else if (count >= 10 * interval)
					p.setLocation(location);
			}
			return false;
		}
	}, new ActionFlag() {
		private ButtlePanel b = null;

		public boolean action() {
			if (b == null) {
				b = target.startButtle(new CharacterStatus[] { EnemyStatusList.MOCHIRI.getStatus() });
				b.setEscapable(false);
				return false;
			}
			return b.isFinished();
		}
	}, new ActionFlag() {
		private ChatPanel p = null;
		private ChatRecord chats[] = new ChatRecord[] {
				new ChatRecord("ロマン", new String[] { "はぁ、はぁ..." }),
				new ChatRecord("？？？", new String[] { "本当に魔物に勝ったぞ、この子供..." }),
				new ChatRecord("ロマン", new String[] { "いやいや、あなたのおかげだよ！\nありがとう！" }),
				new ChatRecord("？？？", new String[] { "あ、ああ..." }),
				new ChatRecord("ロマン", new String[] { "そういえば、あなたの名前は？" }),
				new ChatRecord("？？？", new String[] { "俺か？　俺の名前は「パルト」だ。" }),
				new ChatRecord("ロマン", new String[] { "そっか！　私の名前はロマン！\nそれでパルト、お願いがあるんだけど...", "私をニューインまで連れてってくれない？" }),
				new ChatRecord("パルト", new String[] { "......は？" }),
				new ChatRecord("ロマン", new String[] { "ニューインには、魔物がたくさんいるんだよね？\nそいつらを倒して、その様子を配信すれば...！" }),
				new ChatRecord("ロマン", new String[] { "大バズり！私はチヤホヤされる！！" }),
				new ChatRecord("パルト",
						new String[] { "何を言っているんだ！？\n侵略してきた魔物はこいつの比じゃ無い！", "今回はたまたま勝てただけだ。\n冗談抜きで死ぬぞ！？",
								"というか、あなたからも何とか言ってくれ！\nこの子、この村の子だろ？" }),
				new ChatRecord("おじさん", new String[] { "いや～、ロマンは言っても聞かないし...\n連れてってあげてくれないか？" }),
				new ChatRecord("パルト", new String[] { "まさかの放任主義！？\n命が関わってるのに！？" }),
				new ChatRecord("ロマン", new String[] { "お願い、パルト！！" }),
				new ChatRecord("パルト", new String[] { "ぐぬぬ...わかったよ。その代わり、危険だと感じたらすぐに引き返すからな？" }),
				new ChatRecord("ロマン", new String[] { "ありがとう、パルト！" }),
				new ChatRecord("", new String[] { "\nパルト　が　なかまになった！" }),
		};
		private int index = 0;
		private final int interval = 5;
		private int count = 0;
		private Point location = null;
		private Font font = null;

		public boolean action() {
			if (p == null) {
				target.removeObject(monstar);
				p = new ChatPanel();
				font = p.getFont();
				p.setBounds(ChatPanel.normalStyle);
				p.setLocation(p.getX(), p.getY() - 300);
				setChat(p, chats[0]);
				location = p.getLocation();

				return false;
			}
			if (p.isFinished()) {
				index++;
				if (index >= chats.length) {
					target.removeObject(target.searchObject("paruto"));
					PlayerStatus paruto = PlayerStatus.paruto.clone();
					paruto.addEx(207);
					paruto.setHP(paruto.getMaxHP());
					target.getPlayer().getPartyStatusList().add(paruto);
					target.addPartyLabel(PlayerStatus.paruto.getName());
					target.setMovable(true);
					target.setCamera(target.getPlayer().getCamera(), false);
					target.setEventFlag(2);
					return true;
				}
				p.setBounds(ChatPanel.normalStyle);
				p.setLocation(p.getX(), p.getY() - 300);
				setChat(p, chats[index]);
				if (index == 9) {
					p.setFont(new Font("", Font.BOLD, font.getSize() + 10));
					count = 0;
				} else {
					p.setFont(font);
				}
			}
			if (index == 9) {
				count++;
				if (count % interval == 0 && count < 10 * interval) {
					p.setLocation(vibrate(location));
				} else if (count >= 10 * interval)
					p.setLocation(location);
			}
			return false;
		}
	} }, { () -> {
		return true;
	}, new ActionFlag() {
		private Point romanGoal = null;
		private Creature paruto = null;
		private Player roman = null;
		private Object maou = null;

		public boolean action() {
			if (roman == null) {
				roman = target.getPlayer();
				maou = target.searchObject("maou");
				romanGoal = new Point(maou.getBounds().x - 60, maou.getBounds().y);
				return false;
			}
			boolean flag = forceToMove(roman, romanGoal, true, false);
			if (flag) {
				roman.transform(Direction.LEFT);
				paruto = new Creature(Gallery.paruto.image(), new Rectangle(romanGoal.x, romanGoal.y + 40, 32, 32),
						new Dimension(32, 32), Direction.BACK) {

					@Override
					public void Hit(GamePanel p) {
					}

					@Override
					public boolean pressEnter(Player p, FreezePanel f) {
						return false;
					}

				};
				paruto.transform(Direction.LEFT);
				paruto.setLabel("paruto");
				target.addObject(paruto);
			}
			return flag;
		}
	}, new ActionFlag() {
		private ChatPanel p = null;
		private ChatRecord chats[] = new ChatRecord[] {
				new ChatRecord("ロマン", new String[] { "お前が魔王だな～？" }), //0
				new ChatRecord("魔王", new String[] { "そうとも、私が魔王だ" }),
				new ChatRecord("パルト", new String[] { "俺たちのニューインから出ていけ！" }),
				new ChatRecord("魔王", new String[] { "出ていけと言われて出ていく奴がいるか！" }),
				new ChatRecord("パルト", new String[] { "なら力づくだ！" }),
				new ChatRecord("ロマン", new String[] { "ちょっと待って、二人とも～" }), //5
				new ChatRecord("魔王", new String[] { "こいつは何をやっているんだ？" }),
				new ChatRecord("パルト", new String[] { "決戦くらい、配信はやめてくれよ" }),
				new ChatRecord("ロマン", new String[] { "何言ってんだ、決戦こそ花形じゃろ！" }),
				new ChatRecord("魔王", new String[] { "戦闘を配信するのか？" }),
				new ChatRecord("ロマン", new String[] { "そうだよ！" }), //10
				new ChatRecord("パルト", new String[] { "というか、ずっと配信してきたんよこの子..." }),
				new ChatRecord("魔王", new String[] { "私との戦闘を、配信するだと...？", "大衆娯楽の一部にするだと...？" }),
				new ChatRecord("パルト", new String[] { "やべ、めっちゃ怒ってるぞ" }),
				new ChatRecord("魔王", new String[] { "そんなの...そんなの..." }),
				new ChatRecord("魔王", new String[] { "めっちゃいいじゃないか！" }), //15
				new ChatRecord("パルト", new String[] { "すごく肯定的だ！？" }),
				new ChatRecord("魔王", new String[] { "配信でのコラボレイション、素晴らしい！", "お前たち、すぐにその配信を開け！" }),
				new ChatRecord("配下たち", new String[] { "合点承知の助！" }),
				new ChatRecord("パルト", new String[] { "何！？　その掛け声！？" }),
				new ChatRecord("配下Ａ", new String[] { "魔王様、配信を見つけました！" }), //20
				new ChatRecord("配下Ｂ", new String[] { "魔王様、配信リンクを本部に送りました！" }),
				new ChatRecord("配下Ｂ２", new String[] { "魔王様、配信リンクがSNSで拡散されました！" }),
				new ChatRecord("パルト", new String[] { "いや、そこは配下Ｃじゃないのかよ！？" }),
				new ChatRecord("配下Ｂ２", new String[] { "配下Ｂ２がいてもいいじゃないですか！" }),
				new ChatRecord("ロマン", new String[] { "おお！　同接数がどんどん伸びていく...！" }), //25
				new ChatRecord("魔王", new String[] { "さぁ、舞台は整った！\n容赦せず、私の強さを見せつけてやるぞ！" }),
				new ChatRecord("ロマン", new String[] { "こっちこそ！　行くよ、パルト！" }),

				new ChatRecord("パルト", new String[] { "はぁ...まぁ、最後まで付き合うよ。" }),
		};
		private int index = 0;
		private final int interval = 5;
		private int count = 0;
		private Point location = null;
		private Font font = null;
		private Point romanGoal = null;

		public boolean action() {
			if (p == null) {
				p = new ChatPanel();
				font = p.getFont();
				p.setBounds(ChatPanel.normalStyle);
				setChat(p, chats[0]);
				location = p.getLocation();

				return false;
			}
			if (p.isFinished()) {
				index++;
				if (index >= chats.length) {
					target.setCamera(target.getPlayer().getCamera(), false);
					return true;
				}
				if (index == 18) {
					Object maou = target.searchObject("maou");
					Point defaultP = new Point(maou.getBounds().x + maou.getBounds().width + 15,
							maou.getBounds().y);
					Mochiri mochi = new Mochiri(defaultP);
					mochi.transform(Direction.RIGHT);
					mochi.setLabel("haikaA");
					target.addObject(mochi);
					mochi = new Mochiri(new Point(defaultP.x, defaultP.y + 25));
					mochi.transform(Direction.RIGHT);
					mochi.setLabel("haikaB");
					target.addObject(mochi);
					mochi = new Mochiri(new Point(defaultP.x, defaultP.y + 50));
					mochi.transform(Direction.RIGHT);
					mochi.setLabel("haikaB2");
					target.addObject(mochi);
				}
				p.setBounds(ChatPanel.normalStyle);
				setChat(p, chats[index]);
				if (index == 5) {
					romanGoal = new Point(target.getPlayer().getBounds().x - 70, target.getPlayer().getBounds().y);
					p.setFinishable(false);
				}
				if (index == 27) {
					romanGoal.translate(70, 0);
					p.setFinishable(false);
				}
				if (index == 15 || index == 18) {
					p.setFont(new Font("", Font.BOLD, font.getSize() + 10));
					count = 0;
				} else {
					p.setFont(font);
				}
				if (index > 18) {
					((Creature) target.searchObject("haikaA")).resetShowCount();
					((Creature) target.searchObject("haikaB")).resetShowCount();
					((Creature) target.searchObject("haikaB2")).resetShowCount();
				}
			}
			switch (index) {
			case 5:
				if (forceToMove(target.getPlayer(), romanGoal, true, false)) {
					Object camera = new Object(Gallery.camera.image(),
							new Rectangle(target.getPlayer().getBounds().x - 32,
									target.getPlayer().getBounds().y, 32, 32)) {
						public void Hit(GamePanel p) {
						}

						public boolean pressEnter(Player p, FreezePanel f) {
							return false;
						}
					};
					camera.setLabel("camera");
					target.addObject(camera);
					p.setFinishable(true);
				}
				break;
			case 27:
				if (forceToMove(target.getPlayer(), romanGoal, true, false))
					p.setFinishable(true);
				break;
			case 15:
			case 18:
				count++;
				if (count % interval == 0 && count < 10 * interval) {
					p.setLocation(vibrate(location));
				} else if (count >= 10 * interval)
					p.setLocation(location);
				if (index == 15)
					break;
			case 20:
				((Creature) target.searchObject("haikaA")).transform(Direction.RIGHT);
				if (index != 18)
					break;
			case 21:
				((Creature) target.searchObject("haikaB")).transform(Direction.RIGHT);
				if (index != 18)
					break;
			case 22:
			case 24:
				((Creature) target.searchObject("haikaB2")).transform(Direction.RIGHT);
				if (index != 18)
					break;
				break;
			}
			return false;
		}
	}, new ActionFlag() {
		private ButtlePanel b = null;

		public boolean action() {
			if (b == null) {
				CharacterStatus haikaA = EnemyStatusList.MOCHIRI.getStatus().getSuperStatus(20);
				haikaA.setName("配下Ａ");
				CharacterStatus haikaB = EnemyStatusList.MOCHIRI.getStatus().getSuperStatus(20);
				haikaB.setName("配下Ｂ");
				CharacterStatus haikaB2 = EnemyStatusList.MOCHIRI.getStatus().getSuperStatus(20);
				haikaB2.setName("配下Ｂ２");
				b = target.startButtle(new CharacterStatus[] { haikaA,
						EnemyStatusList.BOSS.getStatus(), haikaB,
						haikaB2 });
				b.setEscapable(false);
				return false;
			}
			return b.isFinished();
		}
	}, new ActionFlag() {
		private ChatPanel p = null;
		private ChatRecord chats[] = new ChatRecord[] {
				new ChatRecord("魔王", new String[] { "ぐわ～っ！！！" }), //0
				new ChatRecord("ロマン", new String[] { "やった！魔王を倒した！" }),
				new ChatRecord("村人Ａ", new String[] { "ありがとうございます！" }),
				new ChatRecord("村人Ｂ", new String[] { "これで自由の身だ！" }),
				new ChatRecord("村人Ｂ２", new String[] { "今日から遊び放題だ！" }),
				new ChatRecord("パルト", new String[] { "村人も同じ仕組みかよ！？" }), //5
				new ChatRecord("ロマン", new String[] { "おお！登録者数が増えた！\n少しだけ...", "よしっ、もっと旅をしてたくさん魔物を倒すよ！パルト！" }),
				new ChatRecord("パルト", new String[] { "ええっ！？" }),
				new ChatRecord("ロマン", new String[] { "私たちの冒険はこれからよっ！！" }),
				new ChatRecord("", new String[] { "\n　　　　　　　～ＥＮＤ～...？" }),
				new ChatRecord("パルト", new String[] { "打ち切りエンドじゃねぇか！！" }), //10
		};
		private int index = 0;
		private final int interval = 5;
		private int count = 0;
		private Point location = null;
		private Font font = null;
		private Point romanGoal = null;
		private long time = 0;
		private JLabel staffLabel;

		public boolean action() {
			if (p == null) {
				boolean flag = true;
				for (PlayerStatus ps : target.getPlayer().getPartyStatusList()) {
					if (!ps.isDead()) {
						flag = false;
						break;
					}
				}
				if (flag)
					return true;
				p = new ChatPanel();
				font = p.getFont();
				p.setBounds(ChatPanel.normalStyle);
				setChat(p, chats[0]);
				location = p.getLocation();

				return false;
			}
			if (p.isFinished()) {
				index++;
				if (index >= chats.length) {
					if (index == chats.length) {
						target.setCamera(target.getPlayer().getCamera(), false);
						target.suspend(new Stopper());
						target.darkout(Color.BLACK, () -> {
							Sound.setBGM(BGMList.NONE);
							target.setLayout(new BoxLayout(target, BoxLayout.Y_AXIS));
							JLabel finishLabel = new JLabel("Congratulations!");
							finishLabel.setForeground(Color.GREEN);
							finishLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
							finishLabel.setPreferredSize(new Dimension(500, 150));
							JLabel explainLabel = new JLabel("Thank you for playing!");
							explainLabel.setForeground(Color.GREEN);
							explainLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
							explainLabel.setPreferredSize(new Dimension(800, 250));
							staffLabel = new JLabel(
									"<html>制作者<br><br>プログラム・デザイン(UI)：paphio<br>PL・デザイン：T.O.<br>"
											+ "BGM・デザイン(ﾛﾏﾝ･ﾊﾟﾙﾄ)：ひしみ<br>シナリオ：K山");
							staffLabel.setForeground(Color.WHITE);
							staffLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
							staffLabel.setPreferredSize(new Dimension(800, 400));
							target.add(finishLabel);
							target.add(explainLabel);
							target.add(staffLabel);
							time = System.currentTimeMillis();
						});
					}
					if (time != 0 && System.currentTimeMillis() - time >= 2000) {
						staffLabel.setText(staffLabel.getText() + "&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;"
								+ "&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;Z or Enter でタイトルへ戻る");
						target.repaint();
						target.finish();
						return true;
					}
					return false;
				}
				if (index == 2) {
					Object player = target.getPlayer();
					Point defaultP = new Point(player.getBounds().x + player.getBounds().width - 10,
							player.getBounds().y - 70);
					Object mura = new Object(Gallery.IMG_0172.image(), new Rectangle(defaultP, new Dimension(32, 32))) {

						@Override
						public void Hit(GamePanel p) {
						}

						@Override
						public boolean pressEnter(Player p, FreezePanel f) {
							return false;
						}
					};
					mura.setLabel("muraA");
					target.addObject(mura);
					mura = new Object(Gallery.IMG_0176.image(), new Rectangle(defaultP.x + 35, defaultP.y, 32, 32)) {

						@Override
						public void Hit(GamePanel p) {
						}

						@Override
						public boolean pressEnter(Player p, FreezePanel f) {
							return false;
						}
					};
					mura.setLabel("muraB");
					target.addObject(mura);
					mura = new Object(Gallery.IMG_0173.image(), new Rectangle(defaultP.x + 70, defaultP.y, 32, 32)) {

						@Override
						public void Hit(GamePanel p) {
						}

						@Override
						public boolean pressEnter(Player p, FreezePanel f) {
							return false;
						}
					};
					mura.setLabel("muraB2");
					target.addObject(mura);
				}
				p.setBounds(ChatPanel.normalStyle);
				setChat(p, chats[index]);
				p.setFont(font);
				switch (index) {
				case 1:
					target.removeObject(target.searchObject("maou"));
					target.removeObject(target.searchObject("haikaA"));
					target.removeObject(target.searchObject("haikaB"));
					target.removeObject(target.searchObject("haikaB2"));
					break;
				case 6:
					p.setAction(new Action[] { () -> {
						target.removeObject(target.searchObject("camera"));
						target.getPlayer().transform(Direction.LEFT);
					}, null });
					break;
				case 7:
					((Creature) target.searchObject("paruto")).transform(Direction.RIGHT);
					break;
				case 5:
					romanGoal = new Point(target.getPlayer().getBounds().x - 70, target.getPlayer().getBounds().y);
					p.setFinishable(false);
				case 8:
					target.getPlayer().transform(Direction.FRONT);
				case 10:
					p.setFont(new Font("", Font.BOLD, font.getSize() + 10));
					count = 0;
					break;
				}
			}
			switch (index) {
			case 5:
				if (forceToMove(target.getPlayer(), romanGoal, true, false))
					p.setFinishable(true);
			case 8:
			case 10:
				count++;
				if (count % interval == 0 && count < 10 * interval) {
					p.setLocation(vibrate(location));
				} else if (count >= 10 * interval)
					p.setLocation(location);
				break;
			}
			return false;
		}
	} } };

	public Event(GamePanel g, int flag) {
		this.target = g;
		this.flag = flag;
		if (events.length > flag && events[flag][0] != null) {
			events[flag][0].action();
		}
	}

	private Point vibrate(Point origin) {
		final Random r = new Random();
		return new Point((r.nextInt(3) - 1) * 20 + origin.x, (r.nextInt(3) - 1) * 20 + origin.y);
	}

	@Override
	public void addFrame(KeyList e) {
		if (events.length <= flag || index >= events[flag].length)
			return;
		if (events[flag][index].action())
			index++;
	}

	private boolean forceToMove(Creature c, Point goal, boolean passable, boolean isLock) {
		if (c == null || goal == null)
			return true;
		Point start = c.getBounds().getLocation();
		if (start.equals(goal))
			return true;
		int x = goal.x - start.x;
		if (x != 0)
			x /= Math.abs(x);
		int y = goal.y - start.y;
		if (y != 0)
			y /= Math.abs(y);
		c.lockDirection(isLock);
		c.move(x, y);
		if (!passable)
			target.reflectWallandObject(c);
		start = c.getBounds().getLocation();
		if ((goal.x - start.x) * x < 0)
			c.setLocation(new Point(goal.x, start.y));
		start = c.getBounds().getLocation();
		if ((goal.y - start.y) * y < 0)
			c.setLocation(new Point(start.x, goal.y));
		c.lockDirection(false);
		if (c.getBounds().getLocation().equals(goal)) {
			c.resetShowCount();
			return true;
		}
		return false;
	}

	@Override
	public boolean isPlayable() {
		return isPlayable;
	}

	@Override
	public boolean isFinished() {
		return index >= events[flag].length;
	}

	@Override
	public boolean isDestroying() {
		return false;
	}

	@Override
	public void destroy(FreezePanel f) {
		f.start(this);
	}

}
