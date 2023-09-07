package game;

import java.awt.Point;

import game.background.Background;

public record RecordOfMap(Background background, Point next, Direction direction) {

}
