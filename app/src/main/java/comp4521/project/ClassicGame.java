package comp4521.project;

import androidx.annotation.NonNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

import comp4521.project.utils.GameShouldStopException;

public class ClassicGame extends Game {

    public ClassicGame(int length, Supplier<Integer> generator) {
        super(length, generator);
    }
    @Override
    public void pushAction(@NonNull Action action, @NonNull Consumer<Integer> updateScore) throws GameShouldStopException {
        synchronized (gameMap) {
            var score = gameMap.processAction(action);
            if (score >= 0) {
                updateScore.accept(score);
                gameMap.generateCell(generator);
            }
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
