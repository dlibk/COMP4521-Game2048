package comp4521.project;

import androidx.annotation.NonNull;

import java.util.function.Supplier;

import comp4521.project.game.action.Action;
import comp4521.project.game.action.ActionResult;
import comp4521.project.game.map.GameMap;
import comp4521.project.utils.GameShouldStopException;

public class ClassicGame extends Game {
    public ClassicGame(@NonNull GameMap map, @NonNull Supplier<Integer> generator) {
        super(map, generator);
    }

    @Override
    public void pushAction(@NonNull Action action) throws GameShouldStopException {
        synchronized (gameMap) {
            ActionResult actionResult = gameMap.processAction(action);
            if (actionResult instanceof ActionResult.Success) {
                gameMap.generateCell(generator);
                onMapUpdate.accept(gameMap);
            }
        }
    }
}
