package game.status;

import java.awt.Dimension;

import utility.Gallery;

public enum EnemyStatusList {
	// @formatter:off
	MOCHIRI(new CharacterStatus(Gallery.mochiri.image(), new Dimension(100, 100), "モチリ", 1, 10, 0, 3, 1, 5, 5)),
	MEME(new CharacterStatus(Gallery.meme.image(), new Dimension(100, 100), "メメ", 2, 12, 3, 5, 2, 9, 6)),
	HATCHI(new CharacterStatus(Gallery.hatchi.image(), new Dimension(80, 80), "ハッチ", 3, 23, 8, 8, 3, 18, 11)),
	BAKEO(new CharacterStatus(Gallery.bakeo.image(), new Dimension(100, 100), "バケオ", 5, 35, 5, 14, 4, 15, 15)),
	DOMOCHIRI(new CharacterStatus(Gallery.domochiri.image(), new Dimension(100, 100), "ドモチリ", 10, 60, 20, 22, 8, 31, 30)),
	NYORONRI(new CharacterStatus(Gallery.nyoronri.image(), new Dimension(130, 130), "ニョロンリ", 8, 45, 30, 20, 5, 40, 26)),
	BIKURINKO(new CharacterStatus(Gallery.bikurinko.image(), new Dimension(100, 100), "ビクリンコ", 18, 100, 26, 40, 14, 42, 55)),
	HITODESTAR(new CharacterStatus(Gallery.hitodestar.image(), new Dimension(150, 150), "ヒトデスター", 23, 80, 40, 51, 12, 77, 88)),
	DODONNNU(new CharacterStatus(Gallery.dodonnnu.image(), new Dimension(180, 180), "ドドンヌ", 15, 150, 0, 30, 20, 18, 50)),
	BOSS(new StrongEnemyStatus(Gallery.boss.image(), new Dimension(200, 240), "魔王", 30, 500, 80, 95, 40, 75, 1000)),
	;
	// @formatter:on

	private final CharacterStatus status;

	EnemyStatusList(CharacterStatus status) {
		this.status = status;
	}

	public CharacterStatus getStatus() {
		return status.clone();
	}
}
