package comp4521.project;

import androidx.annotation.NonNull;

import java.util.function.Supplier;

import comp4521.project.utils.GameShouldStopException;

public class ClassicGame extends Game {

    public ClassicGame(int length, Supplier<Integer> generator) {
        super(length, generator);
    }
    @Override
    public void pushAction(@NonNull Action action) throws GameShouldStopException {
        synchronized (gameMap) {
            if (gameMap.processAction(action))
                gameMap.generateCell(generator);
            if (shouldStop())
                throw new GameShouldStopException();
        }
    }

    public boolean shouldStop() {
        if (!gameMap.getEmptyPositions().isEmpty())
            return false;

        for (Position p: gameMap.getAllPositions()) {
            if (gameMap.isMovable(p))
                return false;
        }
        return true;
    }
}
