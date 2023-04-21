package comp4521.project;

import comp4521.project.game.map.GameMap;
import comp4521.project.game.mode.Mode;
import comp4521.project.utils.ShouldNotReachException;

public class GameFactory {
    public static Game createGame(Mode mode, int length) {
        GameMap gameMap = new GameMap(length);
        switch (mode) {
            case CLASSIC:
                gameMap.generateCell(Game.classicSeed);
                gameMap.generateCell(Game.classicSeed);
                return new ClassicGame(gameMap, Game.classicSeed);
            case ZERO:
                gameMap.generateCell(Game.classicSeed);
                gameMap.generateCell(Game.seedWithZero);
                return new ClassicGame(gameMap, Game.seedWithZero);
            default:
                throw new ShouldNotReachException();
        }
    }
}
