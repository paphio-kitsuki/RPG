package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

import game.action.Action;
import game.background.Background;
import game.background.BackgroundColliderColor;
import game.choose.ChooseListLabel;
import game.choose.ChosenEvent;
import game.choose.ChosenListener;
import game.choose.ConfigLabel;
import game.choose.SaveLabel;
import game.choose.StatusLabel;
import game.obj.EnterListener;
import game.obj.Object;
import game.obj.Player;
import game.status.CharacterStatus;
import game.status.PlayerStatus;
import main.MainWindow;
import main.NextListener;
import utility.Data;
import utility.Gallery;
import utility.KeyList;
import utility.MyColor;

public class GamePanel extends FreezePanel implements ChosenListener {

	public static int FPS = 60;
	//public static int MPS = FPS;//100 > FPS ? 100 : FPS;
	public static Dimension showRange = new Dimension(433, 265);
	public static double magnification = (double) (MainWindow.size.width) / showRange.width;
	private static int lagtime = 350;
	private static int curtainTime = 40;
	private static int minEncounter = 300;
	private static int maxEncounter = 600;

	private final Random r = new Random();
	private final KeyList keyList;
	private Rectangle limit = new Rectangle();
	private Rectangle cameraLimit = new Rectangle();
	private final double playerPerWorld = 1;
	private final int playerPixel = 32;
	private Background background;
	private ArrayList<Object> objects = new ArrayList<>();
	private ArrayList<Object> affectObjects = new ArrayList<>();
	private Player player;
	private MapEventStrage mapStrage;
	private boolean isLoading = false;
	private Color originCurtain = Color.BLACK;
	private Color curtain = MyColor.toAlpha(Color.BLACK, 255);
	private Action curtainAction = null;
	private int curtainCount = 16;
	private long nowCurtain = 0;
	private int curtainSign = 0;
	private Color curtain2 = null;
	private Rectangle wipe = null;
	private Action wipeAction = null;
	private int wipeCount = 0;
	private int wipeTime = 20;
	private long nowWipe = 0;
	private Data data;
	//private JButton button = new JButton("save");
	private ChooseListLabel menu = new ChooseListLabel();
	private ChooseListLabel partyList = new ChooseListLabel();
	private StatusLabel statusLabel = null;
	private ConfigLabel config = new ConfigLabel();
	private SaveLabel saveLabel;
	private int enemyEncounter = r.nextInt(maxEncounter - minEncounter) + minEncounter;
	private final GamePanel g = this;
	private boolean isEffecting = false;
	private boolean isMovable = true;
	private boolean isMenuable = true;
	private int eventFlag = 0;
	private Point originCamera = new Point();
	private Point camera = new Point();
	private final NextListener parentWindow;
	private boolean isDestroyed = false;
	private boolean isError = false;
	private boolean isFinished = false;

	private double nowFPS = FPS;
	private double printFPS = FPS;
	private long updateTime = 0;
	private double tmpFPS = 0;
	private int tmpCount = 0;

	public GamePanel(NextListener nl, Data data, KeyList keyList) {
		super();
		this.parentWindow = nl;
		setLayout(null);
		setBackground(Color.WHITE);
		/*
		button.setBounds(MainWindow.size.width - 150, 30, 100, 50);
		button.setFont(new Font("", Font.PLAIN, 30));
		button.setBackground(MyColor.LAVENDER.COLOR);
		button.setFocusable(false);
		button.addActionListener(this);
		add(button);
		*/
		this.keyList = keyList;
		this.data = data;
		menu.addChosenListener(this);
		menu.setSelectable(false);
		menu.addLabel("ステータス");
		menu.addLabel("セーブ");
		menu.addLabel("設定");
		menu.addLabel("ゲーム終了");
		menu.setLocation(50, 50);
		partyList.addChosenListener(this);
		partyList.setSelectable(false);
		partyList.setLocation(menu.getX(), menu.getY() + menu.getHeight() + 30);
		config.addChosenListener(this);
		config.setSelectable(false);
		config.setBounds(menu.getX() + menu.getWidth() + 50, menu.getY() + 50, 600, 400);
		saveLabel = new SaveLabel(keyList);
		saveLabel.addChosenListener(this);
		saveLabel.setSelectable(false);
		saveLabel.setBounds(menu.getX() + menu.getWidth() + 50, menu.getY() + 50, 600, 400);
		Stopper stop = new Stopper();
		suspend(stop);
		isLoading = true;

		new Thread(new Runnable() {
			public void run() {
				double startTime = System.currentTimeMillis();
				while (!isDestroyed) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
					}

					try {
						long now = System.currentTimeMillis();
						if (wipe != null && now - nowWipe >= wipeTime)
							doWipe();
						if (isEffecting() && now - nowCurtain >= curtainTime)
							doCurtain();
						if (now - startTime >= (double) 1000 / FPS) {
							nowFPS = (double) 1000 / (now - startTime);
							if (now - startTime >= (double) 1000 / FPS * 1.5 && FPS > 30)
								FPS -= 5;
							else if (now - startTime <= (double) 1000 / FPS * 1.1 && FPS < 60)
								FPS += 5;
							startTime += (double) 1000 / FPS;
							if (isFinished && keyList.containsEnter()) {
								removeAll();
								isDestroyed = true;
								parentWindow.next(g);
							}
							affectObjects.clear();
							Object tmp[] = new Object[objects.size()];
							objects.toArray(tmp);
							for (Object o : tmp) {
								if (!isPlaying())
									break;
								o.passTime();
								if (!o.isPassable())
									affectObjects.add(o);
							}
							if (isPlaying())
								hitJudge();
							Object objs[] = new Object[objects.size()];
							objects.toArray(objs);
							for (int i = objs.length - 1; i >= 0 && isPlaying(); i--) {
								if (!objs[i].isPassable())
									reflectWallandObject(objs[i]);
							}
							for (Object o : objs) {
								if (!o.isPassable())
									o.setBounds(o.getBounds());
							}
							if (isPlaying())
								reflectBackgroundStatus(player);
							if (isPlaying())
								playEvents();
							if (keyList.containsMenu() && isPlaying() && isMenuable) {
								menu.reborn();
								suspend(menu);
								add(menu);
								menu.setSelectable(true);
							}
							if (!isLoading) {
								for (java.lang.Object o : getStoppers()) {
									if (o instanceof FrameUpdateListener && ((FrameUpdateListener) o).isPlayable()) {
										FrameUpdateListener f = ((FrameUpdateListener) o);
										if (f.isFinished() && !f.isDestroying()) {
											f.destroy(g);
										}
										f.addFrame(keyList);
									}
								}
							}
							repaint();
						}
					} catch (Exception e) {
					}
				}
			}
		}).start();

		player = new Player(Gallery.roman.image(),
				new Rectangle(800, 900, (int) (playerPixel * playerPerWorld),
						(int) (playerPixel * playerPerWorld)),
				new Dimension(playerPixel, playerPixel), Direction.FRONT, keyList, new Rectangle(limit));
		player.setCollider(6, 4, 20, 27);
		//new Rectangle(100, 100, 48, 60));
		addObject(player);
		mapStrage = new MapEventStrage();
		if (data != null) {
			if (!load()) {
				isDestroyed = true;
				isError = true;
				repaint();
				return;
			}
		} else {
			setData(new Data(Data.searchLatestName()));
			partyList.addLabel(player.getPartyStatusList().get(0).getName());
			suspend(new Event(g, 0));
		}
		setCamera(player.getCamera(), true);
		mapStrage.setFirstObjects(g);
		isLoading = false;
		if (data != null) {
			brightin(Color.BLACK, () -> {
				afterSetup(stop);
			});
		} else
			afterSetup(stop);
	}

	private void afterSetup(Stopper stop) {
		Sound.setBGM(background.getBGM());
		keyList.containsMenu();
		keyList.containsBack();
		keyList.containsEnter();
		start(stop);
	}

	public void setCamera(Point p, boolean isReset) {
		originCamera = p;
		if (isReset) {
			camera.setLocation(originCamera);
		}
	}

	private void setData(Data d) {
		this.data = d;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setButtlable(boolean flag) {
		this.player.setButtlable(flag);
	}

	public void setMovable(boolean flag) {
		this.isMovable = flag;
	}

	public void setMenuable(boolean flag) {
		this.isMenuable = flag;
	}

	public void setEventFlag(int flag) {
		this.eventFlag = flag;
		mapStrage.setEventFlag(flag);
		if (flag < 2) {
			setButtlable(false);
			setMovable(false);
		}
	}

	public void addPartyLabel(String s) {
		partyList.addLabel(s);
	}

	public void save() {
		data.setBackground(background);
		data.setLocation(player.getBounds().getLocation());
		data.setStatus(player.getPartyStatusList());
		data.setEventFlag(eventFlag);
		data.save();
	}

	public boolean load() {
		if (!data.load())
			return false;
		setBackground(data.getBackground());
		player.setLocation(data.getLocation());
		player.setPartyStatusList(data.getStatus());
		for (CharacterStatus s : player.getPartyStatusList())
			partyList.addLabel(s.getName());
		setEventFlag(data.getEventFlag());
		return true;
	}

	public void setBackground(Background b) {
		FPS = 60;
		this.background = b;
		mapStrage.setNowMap(b);
		limit.setBounds(0, 0, b.getSize().width, b.getSize().height);
		cameraLimit.setBounds(b.getCamera());
		setLimitToComponents();
		background.load();
		Runtime.getRuntime().gc();
	}

	public Background getMyBackground() {
		return background;
	}

	public ButtlePanel startButtle(CharacterStatus enemy[]) {
		PlayerStatus cs[] = new PlayerStatus[player.getPartyStatusList().size()];
		player.getPartyStatusList().toArray(cs);
		ButtlePanel b = new ButtlePanel();
		int space = ButtlePanel.space;
		b.setBounds(-space, -space, MainWindow.size.width + space * 2, MainWindow.size.height + space * 2);
		suspend(b);
		b.setup(cs, enemy);
		Sound.setBGM(BGMList.BUTTLE);
		wipe = new Rectangle(-getWidth(), 0, getWidth(), getHeight());
		wipeCount = 0;
		wipeTime = 10 + Math.abs(15 - 1) * 2;
		nowWipe = System.currentTimeMillis();
		wipeAction = () -> {
			add(b);
			wipeCount = 0;
			wipeTime = 10 + Math.abs(15 - 1) * 2;
			wipeAction = () -> {
				wipe = null;
				b.start();
			};
		};
		return b;
	}

	private void doWipe() {
		wipeCount++;
		nowWipe = System.currentTimeMillis();
		wipeTime = 10 + Math.abs(15 - wipeCount - 1) * 2;
		wipe.x += getWidth() / 30;
		if (30 <= wipeCount) {
			Action tmp = wipeAction;
			wipeAction = null;
			if (tmp != null)
				tmp.action();
			return;
		}
	}

	public void addObject(Object o) {
		objects.add(0, o);
		o.setLimit(limit);
		o.setCameraLimit(cameraLimit);
		o.fixCamera();
	}

	public void removeObject(Object o) {
		objects.remove(o);
		affectObjects.remove(o);
	}

	public Object[] getObjects() {
		Object o[] = new Object[objects.size()];
		objects.toArray(o);
		return o;
	}

	public Object searchObject(String label) {
		Object obj[] = new Object[objects.size()];
		objects.toArray(obj);
		for (Object o : obj) {
			if (o.getLabel().equals(label))
				return o;
		}
		return null;
	}

	private void setLimitToComponents() {
		Object tmps[] = new Object[objects.size()];
		objects.toArray(tmps);
		for (Object o : tmps) {
			o.setLimit(limit);
			o.setCameraLimit(cameraLimit);
		}
	}

	public void reflectWallandObject(Object o) {
		Dimension d = background.getClosestMoveDistance(o, affectObjects);
		Rectangle r = o.getBounds();
		o.setReversible(false);
		o.setBounds(r.x + d.width, r.y + d.height, r.width, r.height);
		o.setReversible(true);
		if (o instanceof Player)
			((Player) o).reflectMoveCount();
	}

	private void reflectBackgroundStatus(Object o) {
		ArrayList<BackgroundColliderColor> list = background.getColorOnCollider(o.getColliderOnMap());
		if (o instanceof Player) {
			for (BackgroundColliderColor bcc : list) {
				if (bcc.isMoveArea() && isMovable) {
					Stopper stop = new Stopper();
					suspend(stop);
					Sound.suspend();
					darkout(Color.BLACK, () -> {
						isLoading = true;
						RecordOfMap record = mapStrage.getNextMap(bcc);
						objects.clear();
						addObject(player);
						setBackground(record.background());
						mapStrage.setFirstObjects(g);
						player.setLocation(record.next());
						setCamera(player.getCamera(), true);
						player.transform(record.direction());
						isLoading = false;
						brightin(Color.BLACK, () -> {
							Sound.setBGM(background.getBGM());
							start(stop);
						});
					});
				} else if (bcc.isEventArea()) {
					if (keyList.containsEnter())
						mapStrage.happenEvent(bcc, this);
				}
			}
		}
	}

	private void hitJudge() {
		for (Object o : getObjects()) {
			if (o == player) {
				continue;
			}
			if (player.getColliderOnMap().intersects(o.getColliderOnMap())) {
				o.Hit(this);
			}
		}
	}

	private void playEvents() {
		boolean flag = false;
		if (keyList.containsEnter()) {
			for (EnterListener e : objects) {
				flag = e.pressEnter(player, this);
				if (flag)
					break;
			}
		}
		if (player.getMoveCount() >= enemyEncounter) {
			enemyEncounter = r.nextInt(maxEncounter - minEncounter) + minEncounter;
			player.resetMoveCount();
			startButtle(background.getEnemies());
		}
	}

	public void darkout(Color c, Action action) {
		isEffecting = true;
		curtainSign = 1;
		curtainCount = 0;
		originCurtain = c;
		curtainAction = action;
		curtain = MyColor.toAlpha(c, 0);
		nowCurtain = System.currentTimeMillis();
	}

	public void brightin(Color c, Action action) {
		isEffecting = true;
		curtainSign = -1;
		curtainCount = 16;
		originCurtain = c;
		curtainAction = action;
		curtain = MyColor.toAlpha(c, 255);
		nowCurtain = System.currentTimeMillis() + lagtime;
	}

	private void doCurtain() {
		curtainCount += curtainSign;
		if (curtainCount < 0 || curtainCount > 16) {
			if (curtainCount < 0)
				curtain = null;
			originCurtain = null;
			isEffecting = false;
			Action tmp = curtainAction;
			curtainAction = null;
			if (tmp != null)
				tmp.action();
			return;
		}
		nowCurtain = System.currentTimeMillis();
		curtain = MyColor.toAlpha(originCurtain, curtainCount * 16 - 1);
	}

	public boolean isEffecting() {
		return isEffecting;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (isLoading) {
			for (Component c : getComponents())
				c.setVisible(false);
			g2.setColor(Color.BLACK);
			g2.fill(new Rectangle2D.Double(0, 0, MainWindow.size.width, MainWindow.size.height));
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("", Font.PLAIN, 40));
			g2.drawString("Now Loading...", MainWindow.size.width - 300, MainWindow.size.height - 50);
			if (isError) {
				g2.setPaint(Color.RED);
				g2.setFont(new Font(Font.MONOSPACED, Font.ITALIC, 40));
				g2.drawString("データの読み込みに失敗しました！", 100, MainWindow.size.height / 2 - 50);
			}
			return;
		}
		Rectangle tmp;
		Rectangle showtmp;
		Object.setDrawing(true);
		if (!camera.equals(originCamera)) {
			final int border = 3;
			int x = (originCamera.x - camera.x);
			x = x / border + x % border / 2 + x % border % 2;
			int y = (originCamera.y - camera.y);
			y = y / border + y % border / 2 + y % border % 2;
			//System.out.println(x + "," + y);
			camera.translate(x, y);
			/*camera.translate((originCamera.x - camera.x) / 8 + (originCamera.x - camera.x) % 2,
					(originCamera.y - camera.y) / 8 + (originCamera.y - camera.y) % 2);
					*/
		}
		for (Image i : background.getBack()) {
			g2.drawImage(i, 0, 0, (int) (showRange.width * magnification), (int) (showRange.height * magnification),
					camera.x, camera.y, camera.x + showRange.width, camera.y + showRange.height, null);
		}
		for (Object o : objects.stream()
				.sorted((Object o1, Object o2) -> (o1.getColliderOnMap().y + o1.getColliderOnMap().height)
						- (o2.getColliderOnMap().y + o2.getColliderOnMap().height))
				.collect(Collectors.toList())) {
			if (!o.getBounds().intersects(new Rectangle(camera, showRange))) {
				continue;
			}
			tmp = o.getBounds();//o.getColliderOnMap();
			tmp = zoom(new Rectangle(tmp.x - camera.x, tmp.y - camera.y, tmp.width, tmp.height), magnification);
			showtmp = o.getShowBounds();
			//g2.fill(new Rectangle2D.Double(tmp.x, tmp.y, tmp.width, tmp.height));
			if (o.getImage() != null) {
				g2.drawImage(o.getImage(), tmp.x, tmp.y, tmp.x + tmp.width, tmp.y + tmp.height, showtmp.x, showtmp.y,
						showtmp.x + showtmp.width, showtmp.y + showtmp.height, null);
			}
		}
		Object.setDrawing(false);
		for (Image i : background.getFront()) {
			g2.drawImage(i, 0, 0, (int) (showRange.width * magnification), (int) (showRange.height * magnification),
					camera.x, camera.y, camera.x + showRange.width, camera.y + showRange.height, null);
		}
		if (curtain2 != null) {
			g2.setColor(curtain2);
			g2.fill(new Rectangle2D.Double(0, 0, MainWindow.size.width, MainWindow.size.height));
		}
		if (curtain != null) {
			g2.setColor(curtain);
			g2.fill(new Rectangle2D.Double(0, 0, MainWindow.size.width, MainWindow.size.height));
		}
	}

	public void paint(Graphics g) {
		try {
			super.paint(g);
		} catch (Exception e) {
		}
		Graphics2D g2 = (Graphics2D) g;
		if (wipe != null) {
			g2.setColor(Color.DARK_GRAY.darker().darker());
			g2.fill(new Rectangle2D.Double(wipe.x, wipe.y, wipe.width, wipe.height));
		}
		tmpFPS += nowFPS;
		tmpCount++;
		double now = System.currentTimeMillis();
		if (updateTime + 1000 <= now) {
			updateTime = now;
			printFPS = tmpFPS / (double) tmpCount;
			tmpFPS = 0;
			tmpCount = 0;
		}
		g2.setPaint(MyColor.toAlpha(Color.WHITE, 200));
		g2.setStroke(new BasicStroke(2));
		g2.setFont(new Font(Font.DIALOG, Font.PLAIN, 15));
		g2.drawString("FPS : " + ((double) ((int) (printFPS * 1000)) / 1000), 50, MainWindow.size.height - 15);
	}

	public static Rectangle zoom(Rectangle src, double magnification) {
		return (new Rectangle((int) (src.x * magnification), (int) (src.y * magnification),
				(int) (src.width * magnification), (int) (src.height * magnification)));
	}

	public void destroyAll() {
		removeAll();
		suspend(new Stopper());
		darkout(Color.BLACK, () -> {
			isDestroyed = true;
			parentWindow.next(g);
		});
	}

	@Override
	public void chosen(ChosenEvent e) {
		if (e.getSource().equals(menu)) {
			switch (e.getIndex()) {
			case -1:
				menu.resetIndex();
				menu.setSelectable(false);
				menu.destroy(this);
				break;
			case 0:
				menu.setSelectable(false);
				partyList.reborn();
				suspend(partyList);
				add(partyList);
				partyList.setSelectable(true);
				break;
			case 1:
				menu.setSelectable(false);
				saveLabel.reborn();
				saveLabel.setField(data.getFilename());
				g.suspend(saveLabel);
				g.add(saveLabel);
				saveLabel.setSelectable(true);
				break;
			case 2:
				menu.setSelectable(false);
				config.reborn();
				suspend(config);
				add(config);
				config.setSelectable(true);
				break;
			case 3:
				Sound.setBGM(BGMList.NONE);
				menu.destroy(this);
				destroyAll();
				break;
			default:
				break;
			}
		} else if (e.getSource().equals(partyList)) {
			if (e.getIndex() != -1) {
				partyList.setSelectable(false);
				statusLabel = new StatusLabel(player.getPartyStatusList().get(e.getIndex()));
				statusLabel.setLocation(menu.getX() + menu.getWidth() + 50, menu.getY() + 50);
				statusLabel.addChosenListener(this);
				statusLabel.reborn();
				suspend(statusLabel);
				add(statusLabel);
				statusLabel.setSelectable(true);
			} else {
				partyList.resetIndex();
				partyList.setSelectable(false);
				partyList.destroy(this);
				menu.setSelectable(true);
			}
		} else if (e.getSource().equals(statusLabel)) {
			if (e.getIndex() == -1) {
				statusLabel.destroy(this);
				statusLabel = null;
				partyList.setSelectable(true);
			}
		} else if (e.getSource().equals(config)) {
			config.setSelectable(false);
			config.destroy(this);
			menu.setSelectable(true);
		} else if (e.getSource().equals(saveLabel)) {
			saveLabel.setSelectable(false);
			saveLabel.destroy(this);
			if (e.getString() != null) {
				data.setFilename(e.getString());
				save();
			}
			menu.setSelectable(true);
		}
	}

	public void finish() {
		isFinished = true;
		curtain2 = Color.BLACK;
	}
}
