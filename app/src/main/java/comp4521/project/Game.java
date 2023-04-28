package comp4521.project;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import comp4521.project.utils.GameShouldStopException;
import comp4521.project.utils.ShouldNotReachException;

public class Game {
    interface GameEngine {
        void pushAction(@NonNull Action action, @NonNull Consumer<Integer> updateScore);
        default void pause() {}
    }

    interface GameStopHandler {
        void onGameStop();
    }
    public static final Supplier<Integer> classicSeed = () -> Math.random() < 0.6 ? 2 : 4;
    public static final Supplier<Integer> seedWithZero = () -> Math.random() < 0.1 ? 0 : classicSeed.get();
    private final GameMap gameMap;
    private Supplier<Integer> generator;
    private GameEngine gameEngine;
    private GameStopHandler gameStopHandler;
    private Mode mode;

    private Game(@NonNull Mode mode, int length, Supplier<Integer> generator) {
        this.gameMap = new GameMap(length);
        this.generator = generator;
        this.mode = mode;
        setGameEngine(mode);
    }

    public void setGameEngine(@NonNull Mode mode) {
        switch (mode) {
            case CLASSIC: case ZERO:
                gameEngine = classicGameEngine;
                break;
            case SPEED:
                gameEngine = speedGameEngine;
                break;
            default:
                throw new ShouldNotReachException();
        }
    }

    public void setGameStopHandler(@NonNull GameStopHandler gameStopHandler) {
        this.gameStopHandler = gameStopHandler;
    }

    public void switchMode(@NonNull Mode mode) {
        this.mode = mode;
        setGameEngine(mode);
        generator = mode == Mode.ZERO ? seedWithZero : classicSeed;
        initialize();
    }

    public Mode getMode() {
        return mode;
    }

    public void initialize() {
        speedGameEngine.pause();
        synchronized (gameMap) {
            gameMap.initialize();
            gameMap.generateCell(generator);
            gameMap.generateCell(generator);
        }
    }

    public List<Cell> getCellList() {
        return gameMap.getAllPositions().stream()
                .map(gameMap::getCell)
                .collect(Collectors.toList());
    }

    public void pushAction(@NonNull Action action, @NonNull Consumer<Integer> updateScore) {
        gameEngine.pushAction(action, updateScore);
    }

    @NonNull
    public static Game of(int length) {
        switch (length) {
            case 4:
                return game4;
            case 5:
                return game5;
            case 6:
                return game6;
            default:
                throw new ShouldNotReachException();
        }
    }

    private static final Game game4 = new Game(Mode.CLASSIC, 4, classicSeed);
    private static final Game game5 = new Game(Mode.CLASSIC, 5, classicSeed);
    private static final Game game6 = new Game(Mode.CLASSIC, 6, classicSeed);


    public final GameEngine classicGameEngine = new GameEngine() {
        private boolean ShouldStop() {
            if (!gameMap.getEmptyPositions().isEmpty())
                return false;
            for (Position p: gameMap.getAllPositions()) {
                if (gameMap.isMovable(p))
                    return false;
            }
            return true;
        }
        @Override
        public void pushAction(@NonNull Action action, @NonNull Consumer<Integer> updateScore) {
            synchronized (gameMap) {
                var score = gameMap.processAction(action);
                if (score >= 0) {
                    updateScore.accept(score);
                    try {
                        gameMap.generateCell(generator);
                        if (ShouldStop())
                            throw new GameShouldStopException();
                    } catch (GameShouldStopException ignored) {
                        gameStopHandler.onGameStop();
                    }
                }
            }
        }
    };

    public final GameEngine speedGameEngine = new GameEngine() {
        private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        private boolean started = false;
        @Override
        public void pushAction(@NonNull Action action, @NonNull Consumer<Integer> updateScore) {
            synchronized (gameMap) {
                var score = gameMap.processAction(action);
                if (score >= 0) {
                    updateScore.accept(score);
                }
            }

            if (!started) {
                started = true;
                if (executor.isShutdown())
                    executor = new ScheduledThreadPoolExecutor(2);
                executor.scheduleAtFixedRate(() -> {
                    synchronized (gameMap) {
                        try {
                            gameMap.generateCell(generator);
                        } catch (GameShouldStopException ignored) {
                            executor.shutdown();
                            gameStopHandler.onGameStop();
                            started = false;
                        }
                    }
                }, 0, 2, TimeUnit.SECONDS);
            }
        }

        @Override
        public void pause() {
            executor.shutdown();
            started = false;
        }
    };
}
