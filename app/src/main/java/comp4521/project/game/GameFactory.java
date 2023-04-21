package comp4521.project.game;

import org.jetbrains.annotations.NotNull;

import comp4521.project.game.cell.Cell;
import comp4521.project.game.map.GameMap;
import comp4521.project.game.map.Position;
import comp4521.project.game.mode.Mode;
import comp4521.project.tui.TerminalGame;
import comp4521.project.utils.ShouldNotReachException;

public class GameFactory {
    @NotNull
    public static Game createTUIGame(@NotNull Mode mode, int length) {
        assert length >= 2;
        switch (mode) {
            case CLASSIC:
                GameMap gameMap = new GameMap(length);
                Position first = gameMap.getAllPositions().get((int) (Math.random() * length * length));
                Position second = gameMap.getEmptyPositions().get((int) (Math.random() * (length * length - 1)));
//                gameMap.placeCell(first, new Cell(Math.random() < 0.6 ? 2 : 4));
//                gameMap.placeCell(second, new Cell(Math.random() < 0.6 ? 2 : 4));
                return new TerminalGame(gameMap);
            default:
                throw new ShouldNotReachException();
        }
    }
}
