package game.background;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import game.BGMList;
import game.obj.Object;
import game.status.CharacterStatus;
import game.status.EnemyStatusList;
import utility.Gallery;

public enum Background {
	// @formatter:off
	MATAMA("マタマ村",
			new Gallery[] { Gallery.matama_front },
			new Gallery[] { Gallery.matama_collider },
			new Gallery[] { Gallery.matama_back },
			new Rectangle(60, 76, 1302, 884),
			new EnemyStatusList[] { }),
	WASHO("ワショー平原",
			new Gallery[] { Gallery.washouheigen_front },
			new Gallery[] { Gallery.washouheigen_collider },
			new Gallery[] { Gallery.washouheigen_back },
			new Rectangle(139, 46, 764, 805),
			new EnemyStatusList[] { EnemyStatusList.MOCHIRI,
									EnemyStatusList.MEME,
									EnemyStatusList.HATCHI }),
	MITSUKUBI1("ミツクビ山 - パート1",
			new Gallery[] { Gallery.mitsukubi1_front },
			new Gallery[] { Gallery.mitsukubi1_collider },
			new Gallery[] { Gallery.mitsukubi1_back },
			new Rectangle(57, 31, 1111, 831),
			new EnemyStatusList[] { EnemyStatusList.MOCHIRI,
									EnemyStatusList.HATCHI,
									EnemyStatusList.BAKEO }),
	MITSUKUBI2("ミツクビ山 - パート2",
			new Gallery[] {},
			new Gallery[] { Gallery.mitsukubi2_collider },
			new Gallery[] { Gallery.mitsukubi2_backforest, Gallery.mitsukubi2_back },
			new Rectangle(96, 95, 1139, 674),
			new EnemyStatusList[] { EnemyStatusList.BAKEO,
									EnemyStatusList.DOMOCHIRI,
									EnemyStatusList.NYORONRI }),
	MITSUKUBI3("ミツクビ山 - パート3",
			new Gallery[] { Gallery.mitsukubi3_front },
			new Gallery[] { Gallery.mitsukubi3_collider },
			new Gallery[] { Gallery.mitsukubi3_backtree, Gallery.mitsukubi3_back },
			new Rectangle(77, 46, 933, 1557),
			new EnemyStatusList[] { EnemyStatusList.DOMOCHIRI,
									EnemyStatusList.NYORONRI,
									EnemyStatusList.DODONNNU }),
	BITTER("ビッター渓谷",
			new Gallery[] { Gallery.bittervalley_front },
			new Gallery[] { Gallery.bittervalley_collider },
			new Gallery[] { Gallery.bittervalley_back },
			new Rectangle(100, 137, 1057, 795),
			new EnemyStatusList[] { EnemyStatusList.DOMOCHIRI,
									EnemyStatusList.BIKURINKO,
									EnemyStatusList.DODONNNU,
									EnemyStatusList.HITODESTAR }),
	NYUIN("城塞都市ニューイン",
			new Gallery[] { Gallery.nyuin_front },
			new Gallery[] { Gallery.nyuin_collider },
			new Gallery[] { Gallery.nyuin_back },
			new Rectangle(25, 75, 1449, 873),
			new EnemyStatusList[] { }),
	;
	// @formatter:on

	private static final Random random = new Random();

	private final Gallery front[];
	private final Gallery collider[];
	private final Gallery back[];
	private BufferedImage frontLayers[] = null;
	private BufferedImage backLayers[] = null;
	private BackgroundColliderColor status[][][] = null;
	private final Rectangle rec;
	private Dimension size = null;
	private final String name;
	private final EnemyStatusList list[];

	Background(String name, Gallery front[], Gallery collider[], Gallery back[], Rectangle rec,
			EnemyStatusList list[]) {
		this.name = name;
		this.front = front;
		this.collider = collider;
		this.back = back;
		this.rec = rec;
		this.list = list;
	}

	public void load() {
		if (frontLayers == null) {
			setFront();
		}
		if (status == null) {
			setCollider();
		}
		if (backLayers == null) {
			setBack();
		}
	}

	public BGMList getBGM() {
		if (this == MATAMA)
			return BGMList.CITY;
		else
			return BGMList.FIELD;
	}

	public CharacterStatus[] getEnemies() {
		CharacterStatus enemy[];
		if (this == WASHO || this == MITSUKUBI1)
			enemy = new CharacterStatus[getRandomLength(2) + 1];
		else if (this == BITTER)
			enemy = new CharacterStatus[getRandomLength(3) + 2];
		else
			enemy = new CharacterStatus[getRandomLength(3) + 1];
		for (int i = 0; i < enemy.length; i++) {
			enemy[i] = list[getRandomNum()].getStatus().getRandomStatus();
		}
		return enemy;
	}

	private int getRandomLength(int x) {
		double num = random.nextDouble();
		for (int i = 0; i < x; i++) {
			if (num < (double) (i + 1) / (x * (x + 1) / 2))
				return (x - i - 1);
			num -= (double) (i + 1) / ((double) x * (x + 1) / 2);
		}
		return 0;
	}

	private int getRandomNum() {
		double num = random.nextDouble();
		if (this != BITTER) {
			if (num < 0.45) {
				return 0;
			} else if (num < 0.8) {
				return 1;
			} else {
				return 2;
			}
		} else {
			if (num < 0.4) {
				return 0;
			} else if (num < 0.7) {
				return 1;
			} else if (num < 0.9) {
				return 2;
			} else {
				return 3;
			}
		}
	}

	public String getName() {
		return name;
	}

	public BufferedImage[] getFront() {
		if (frontLayers == null) {
			setFront();
		}
		return frontLayers;
	}

	public BufferedImage[] getBack() {
		if (backLayers == null) {
			setBack();
		}
		return backLayers;
	}

	public Rectangle getCamera() {
		return new Rectangle(rec);
	}

	public Dimension getSize() {
		if (size == null) {
			size = new Dimension(back[0].image().getWidth(), back[0].image().getHeight());
		}
		return size;
	}

	private void setFront() {
		int length = front.length;
		this.frontLayers = new BufferedImage[length];
		for (int i = 0; i < length; i++) {
			this.frontLayers[i] = front[i].image();
		}
	}

	private void setCollider() {
		int length = collider.length;
		getSize();
		status = new BackgroundColliderColor[length][size.width][size.height];
		for (int k = 0; k <length; k++) {
			BufferedImage b = collider[k].image();
			for (int i = 0; i < size.width; i++) {
				for (int j = 0; j < size.height; j++) {
					status[k][i][j] = BackgroundColliderColor.getStatus(new Color(b.getRGB(i, j)));
				}
			}
			collider[k].free();
		}
	}

	private void setBack() {
		int length = back.length;
		getSize();
		this.backLayers = new BufferedImage[length];
		for (int i = 0; i < length; i++) {
			if (i != length - 1)
				this.backLayers[i] = Gallery.filter(back[i].image());
			else
				this.backLayers[i] = back[i].image();
			back[i].free();
		}
	}

	public ArrayList<BackgroundColliderColor> getColorOnCollider(Rectangle r) {
		Rectangle range = new Rectangle(r.x, r.y, r.width, r.height);
		ArrayList<BackgroundColliderColor> out = new ArrayList<>();
		for (int k = 0; k < status.length; k++) {
			for (int i = Math.max(range.x, 0); i < range.x + range.width && i < size.width; i++) {
				for (int j = Math.max(range.y, 0); j < range.y + range.height && j < size.height; j++) {
					if (!out.contains(status[k][i][j])) {
						out.add(status[k][i][j]);
					}
				}
			}
		}
		if (!out.contains(BackgroundColliderColor.WALL) && (range.x < 0 || range.y < 0
				|| range.x + range.width > size.width || range.y + range.height > size.height)) {
			out.add(BackgroundColliderColor.WALL);
		}
		return out;
	}

	public Dimension getClosestMoveDistance(Object target, ArrayList<Object> object) {

		Rectangle range = target.getColliderOnMap();

		getSize();
		boolean passable[][] = new boolean[range.width * 3][range.height * 3];
		for (int i = range.x - range.width; i < range.x + range.width * 2; i++) {
			for (int j = range.y - range.height; j < range.y + range.height * 2; j++) {
				if (i < 0 || i >= size.width || j < 0 || j >= size.height) {
					passable[i - range.x + range.width][j - range.y + range.height] = false;
					continue;
				}
				passable[i - range.x + range.width][j - range.y + range.height] = true;
				for (Object o : object) {
					if (!o.isPassable() && !o.equals(target) && o.getBeforeCollider().contains(i, j)) {
						passable[i - range.x + range.width][j - range.y + range.height] = false;
						break;
					}
				}
				for (int k = 0; k <status.length; k++) {
					if (!status[k][i][j].isPassable()) {
						passable[i - range.x + range.width][j - range.y + range.height] = false;
						break;
					}
				}
			}
		}

		boolean flag = true;
		for (int i = range.width; i < range.width * 2; i++) {
			for (int j = range.height; j < range.height * 2; j++) {
				flag &= passable[i][j];
			}
		}
		if (flag) {
			return new Dimension(0, 0);
		}

		//Dimension ans[] = new Dimension[4];
		Dimension out = new Dimension(range.width + 1, range.height + 1);
		//int limit = range.height;
		//int limitlast = 0;
		int longest[][] = new int[range.width * 3][range.height * 2];

		for (int i = 0; i < range.width * 3; i++) {
			for (int j = 0; j < range.height * 2; j++) {
				if (!passable[i][j + range.height]) {
					longest[i][j] = 0;
				} else if (j == 0) {
					longest[i][j] = 1;
				} else {
					longest[i][j] = longest[i][j - 1] + 1;
				}
			}
		}
		for (int i = 0; i <= range.width; i++) {
			if (i > getDistance(out)) {
				break;
			}
			for (int j = 0; j <= range.height; j++) {
				for (int k = 0; longest[i + range.width + k][j + range.height - 1] >= range.height; k++) {
					if (k + 1 >= range.width) {
						out = minDistance(out, new Dimension(i, j));
						break;
					}
				}
			}
		}

		for (int i = 0; i >= -range.width; i--) {
			if (Math.abs(i) > getDistance(out)) {
				break;
			}
			for (int j = 0; j <= range.height; j++) {
				for (int k = 0; longest[i + range.width * 2 - 1 - k][j + range.height - 1] >= range.height; k++) {
					if (k + 1 >= range.width) {
						out = minDistance(out, new Dimension(i, j));
						break;
					}
				}
			}
		}

		for (int i = 0; i < range.width * 3; i++) {
			for (int j = range.height * 2 - 1; j >= 0; j--) {
				if (!passable[i][j]) {
					longest[i][j] = 0;
				} else if (j == range.height * 2 - 1) {
					longest[i][j] = 1;
				} else {
					longest[i][j] = longest[i][j + 1] + 1;
				}
			}
		}
		for (int i = 0; i <= range.width; i++) {
			if (i > getDistance(out)) {
				break;
			}
			for (int j = 0; j >= -range.height; j--) {
				for (int k = 0; longest[i + range.width + k][j + range.height] >= range.height; k++) {
					if (k + 1 >= range.width) {
						out = minDistance(out, new Dimension(i, j));
						break;
					}
				}
			}
		}

		for (int i = 0; i >= -range.width; i--) {
			if (Math.abs(i) > getDistance(out)) {
				break;
			}
			for (int j = 0; j >= -range.height; j--) {
				for (int k = 0; longest[i + range.width * 2 - 1 - k][j + range.height] >= range.height; k++) {
					if (k + 1 >= range.width) {
						out = minDistance(out, new Dimension(i, j));
						break;
					}
				}
			}
		}

		if (out.width != range.width + 1) {
			return out;
		} else {
			return new Dimension(0, 0);
		}

		/*	過去のアルゴリズム
						for (int i = range.x - range.width > 0 ? range.x - range.width : 0; i < range.x + range.width
								&& i < size.width; i++, limitlast--) {
							if (Math.abs(limit - range.height) > getDistance(tmp) && limitlast >= range.x + range.width - i) {
								break;
							}
							if (limitlast == 0) {
								limit = range.height;
							}
							for (int j = range.y - range.height > 0 ? range.y - range.height : 0; j < range.y + limit
									&& j < size.height; j++) {
								for (int k = 0; k < getCollider().length; k++) {
									if (!status[k][i][j].isPassable()) {
										limitlast = range.width;
										limit = j - range.y;
										break;
									}
								}
							}
							tmp = minDistance(tmp, new Dimension(i - range.x - range.width + 1, limit - range.height));
						}
						if (tmp.width > range.width) {
							int width, height;
							width = Math.min(size.width - range.x - range.width, 0);
							if (width == 0) {
								width = Math.max(-range.x - range.width, 0);
							}
							height = Math.min(size.height - range.y - range.height, 0);
							if (height == 0) {
								width = Math.max(-range.y - range.height, 0);
							}
							return new Dimension(width, height);
						}
				ans[0] = tmp;
				if (!containsWall(new Rectangle(range.x + tmp.width, range.y + tmp.height, -tmp.width, range.height))
						&& !containsWall(new Rectangle(range.x, range.y + tmp.height, range.width + tmp.width, -tmp.height))) {
					out = minDistance(out, tmp);
				}
				//System.out.println(out);
		
				limit = -1;
				limitlast = 0;
				tmp = new Dimension(range.width * 2 + 1, range.height * 2 + 1);
				for (int i = range.x - range.width > 0 ? range.x - range.width : 0; i < range.x + range.width
						&& i < size.width; i++, limitlast--) {
					if ((Math.abs(limit + 1) > getDistance(tmp) || Math.abs(limit + 1) > getDistance(out))
							&& limitlast >= range.x + range.width - i) {
						break;
					}
					if (limitlast == 0) {
						limit = -1;
					}
					for (int j = (j = range.y + range.height * 2 - 1) < size.height ? j : size.height; j > range.y + limit
							&& j >= 0; j--) {
						for (int k = 0; k < getCollider().length; k++) {
							if (!status[k][i][j].isPassable()) {
								limitlast = range.width;
								limit = j - range.y;
								break;
							}
						}
					}
					tmp = minDistance(tmp, new Dimension(i - range.x - range.width + 1, limit + 1));
				}
				ans[1] = tmp;
				if (!containsWall(new Rectangle(range.x + tmp.width, range.y + tmp.height, -tmp.width, range.height))
						&& !containsWall(new Rectangle(range.x, range.y + range.height, range.width + tmp.width, tmp.height))) {
					out = minDistance(out, tmp);
				}
				//System.out.println(out);
		
				limit = range.height;
				limitlast = 0;
				tmp = new Dimension(range.width + 1, range.height + 1);
				for (int i = (i = range.x + range.width * 2 - 1) < size.width ? i : size.width; i >= range.x
						&& i >= 0; i--, limitlast--) {
					if ((Math.abs(limit - range.height) > getDistance(tmp)
							|| Math.abs(limit - range.height) > getDistance(out)) && limitlast > i - range.x) {
						break;
					}
					if (limitlast == 0) {
						limit = range.height;
					}
					for (int j = (j = range.y - range.height) > 0 ? j : 0; j < range.y + limit && j < size.height; j++) {
						for (int k = 0; k < getCollider().length; k++) {
							if (!status[k][i][j].isPassable()) {
								limit = j - range.y;
								limitlast = range.width;
								break;
							}
						}
					}
					tmp = minDistance(tmp, new Dimension(i - range.x, limit - range.height));
				}
				ans[2] = tmp;
				if (!containsWall(new Rectangle(range.x + range.width, range.y + tmp.height, tmp.width, range.height))
						&& !containsWall(new Rectangle(range.x + tmp.width, range.y + tmp.height, range.width - tmp.width,
								-tmp.height))) {
					out = minDistance(out, tmp);
				}
				//System.out.println(out);
		
				limit = -1;
				limitlast = 0;
				tmp = new Dimension(range.width + 1, range.height + 1);
				for (int i = (i = range.x + range.width * 2 - 1) < size.width ? i : size.width; i >= range.x
						&& i >= 0; i--, limitlast--) {
					if ((Math.abs(limit + 1) > getDistance(tmp) || Math.abs(limit + 1) > getDistance(out))
							&& limitlast > i - range.x) {
						break;
					}
					if (limitlast == 0) {
						limit = -1;
					}
					for (int j = (j = range.y + range.height * 2 - 1) < size.height ? j : size.height; j > range.y + limit
							&& j >= 0; j--) {
						for (int k = 0; k < getCollider().length; k++) {
							if (!status[k][i][j].isPassable()) {
								limit = j - range.y;
								limitlast = range.width;
								break;
							}
						}
					}
					tmp = minDistance(tmp, new Dimension(i - range.x, limit + 1));
				}
				ans[3] = tmp;
				if (!containsWall(new Rectangle(range.x + range.width, range.y + tmp.height, tmp.width, range.height))
						&& !containsWall(new Rectangle(range.x + tmp.width, range.y + range.height, range.width - tmp.width,
								tmp.height))) {
					out = minDistance(out, tmp);
				}
				//System.out.println(out);
		
				if (out.width != range.width * 2 + 1) {
					return out;
				}
				//System.exit(0);
				//return ans[random.nextInt(4)];
		*/
	}
	/*
		private boolean containsWall(Rectangle range) {
			if (range.x < 0 || range.y < 0 || range.x + range.width > rec.width || range.y + range.height > rec.height) {
				return true;
			}
			for (int i = range.x; i < range.x + range.width; i++) {
				for (int j = range.y; j < range.y + range.height; j++) {
					for (int k = 0; k < getCollider().length; k++) {
						if (!status[k][i - rec.x][j - rec.y].isPassable()) {
							return true;
						}
					}
				}
			}
			return false;
		}
	*/

	private int getDistance(Dimension d) {
		return Math.abs(d.width) + Math.abs(d.height);
	}

	private Dimension minDistance(Dimension p1, Dimension p2) {
		int i = getDistance(p1) - getDistance(p2);
		if (i < 0) {
			return p1;
		} else if (i == 0) {
			return random.nextInt(2) == 0 ? p1 : p2;
		} else {
			return p2;
		}
	}
}
