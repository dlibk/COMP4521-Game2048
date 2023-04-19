package comp4521.project.tui;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import comp4521.project.game.Game;
import comp4521.project.game.action.Action;
import comp4521.project.game.action.ActionResult;
import comp4521.project.game.cell.Cell;
import comp4521.project.game.interact.InputEngine;
import comp4521.project.game.interact.RenderingEngine;
import comp4521.project.game.map.GameMap;
import comp4521.project.game.map.Position;

public class TerminalGame implements Game {
    GameMap gameMap;
    InputEngine inputEngine = new TerminalInputEngine(System.in);
    RenderingEngine renderingEngine = new TerminalRenderingEngine(System.out);

    public TerminalGame(@NotNull GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public boolean shouldStop() {
        if (!gameMap.getEmptyPositions().isEmpty()) {
            return false;
        }
        for (Position p: gameMap.getAllPositions()) {
            Cell cell = gameMap.getCell(p);
            for (Action a: List.of(Action.UP, Action.DOWN, Action.LEFT, Action.RIGHT)) {
                Position next = a.getNextPosition(p);
                if (gameMap.isPositionInMap(next) && gameMap.getCell(next).getValue() == cell.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void run() {
        renderingEngine.message("Game starting");
        while (!shouldStop()) {
            renderingEngine.render(gameMap);
            Action action = inputEngine.fetchAction();
            ActionResult actionResult = gameMap.processAction(action, x -> {}, x -> {});
            if (actionResult instanceof ActionResult.Success)
                gameMap.generateCell(() -> Math.random() < 0.6 ? 2 : 4);
        }
        renderingEngine.render(gameMap);
        renderingEngine.message("Game End");
    }
}
