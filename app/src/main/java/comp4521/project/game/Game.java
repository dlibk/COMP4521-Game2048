package comp4521.project.game;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import comp4521.project.gamemap.component.Cell;
import comp4521.project.gamemap.component.FreezableCell;
import comp4521.project.gamemap.GameMap;
import comp4521.project.gamemap.position.Position;
import comp4521.project.utils.GameShouldStopException;
import comp4521.project.utils.ShouldNotReachException;

public class Game {
    public static final Supplier<Integer> classicSeed = () -> Math.random() < 0.6 ? 2 : 4;
    public static final Supplier<Integer> seedWithZero = () -> Math.random() < 0.1 ? 0 : classicSeed.get();
    private final GameMap gameMap;
    private Supplier<Integer> generator;
    private GameStopHandler gameStopHandler;
    private Mode mode;
    private GameEngine engine;

    private Game(@NonNull Mode mode, int length, Supplier<Integer> generator) {
        this.gameMap = new GameMap(length);
        this.generator = generator;
        this.mode = mode;
        switch (mode) {
            case CLASSIC: case ZERO:
                if (length == 6)
                    engine = freezingGameEngine;
                else
                    engine = classicGameEngine;
                break;
            case SPEED:
                engine = speedGameEngine;
                break;
            default:
                throw new ShouldNotReachException();
        }
    }

    public GameEngine engine() {
        return engine;
    }

    public void setGameStopHandler(@NonNull GameStopHandler gameStopHandler) {
        this.gameStopHandler = gameStopHandler;
    }

    public void switchMode(@NonNull Mode mode) {
        this.mode = mode;
        switch (mode) {
            case CLASSIC: case ZERO:
                if (gameMap.getLength() == 6)
                    engine = freezingGameEngine;
                else
                    engine = classicGameEngine;
                break;
            case SPEED:
                engine = speedGameEngine;
                break;
            default:
                throw new ShouldNotReachException();
        }
        generator = mode == Mode.ZERO ? seedWithZero : classicSeed;
        initialize();
    }

    public Mode getMode() {
        return mode;
    }

    public void initialize() {
        engine.pause();
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
        engine.pushAction(action, updateScore);
    }

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

    public final GameEngine freezingGameEngine = new GameEngine() {
        private boolean started = false;
        private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        private Position frozenPosition1, frozenPosition2;
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
                        gameMap.generateCell(generator);
                        if (ShouldStop())
                            throw new GameShouldStopException();
                    } catch (GameShouldStopException ignored) {
                        executor.shutdown();
                        gameStopHandler.onGameStop();
                    }
                }
            }

            if (!started) {
                started = true;
                if (executor.isShutdown())
                    executor = new ScheduledThreadPoolExecutor(2);
                executor.scheduleAtFixedRate(() -> {
                    synchronized (gameMap) {
                        if (frozenPosition1 != null) {
                            gameMap.getCell(frozenPosition1).unfreeze();
                        }

                        if (frozenPosition2 != null && !frozenPosition2.equals(frozenPosition1)) {
                            gameMap.getCell(frozenPosition2).unfreeze();
                        }

                        frozenPosition1 = gameMap.getAllPositions().get(
                                (int) (Math.random() * gameMap.getAllPositions().size())
                        );

                        gameMap.getCell(frozenPosition1).freeze();

                        frozenPosition2 = gameMap.getAllPositions().stream()
                                .filter(p -> !p.equals(frozenPosition1))
                                .collect(Collectors.toList())
                                .get((int) (Math.random() * (gameMap.getAllPositions().size() - 1)));

                        gameMap.getCell(frozenPosition2).freeze();
                    }
                }, 2, 4, TimeUnit.SECONDS);
            }
        }

        @Override
        public void pause() {
            executor.shutdown();
            started = false;
        }
    };

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
}
