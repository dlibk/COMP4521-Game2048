package comp4521.project;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Handler;
import java.util.stream.Collectors;

import comp4521.project.utils.GameShouldStopException;
import comp4521.project.utils.ShouldNotReachException;

public class Game {
    interface GameEngine {
        void pushAction(@NonNull Action action, @NonNull Consumer<Integer> updateScore);
    }
    public static final Supplier<Integer> classicSeed = () -> Math.random() < 0.6 ? 2 : 4;
    public static final Supplier<Integer> seedWithZero = () -> Math.random() < 0.1 ? 0 : classicSeed.get();
    private final GameMap gameMap;
    private Supplier<Integer> generator;

    private GameEngine gameEngine;

    private Game(@NonNull Mode mode, int length, Supplier<Integer> generator) {
        this.gameMap = new GameMap(length);
        this.generator = generator;
        setGameEngine(mode);
    }

    public void setGameEngine(@NonNull Mode mode) {
        switch (mode) {
            case CLASSIC: case ZERO:
                gameEngine = classicGameEngine;
                break;
            default:
                throw new ShouldNotReachException();
        }
    }

    public void switchMode(@NonNull Mode mode) {
        switch (mode) {
            case CLASSIC:
                gameEngine = classicGameEngine;
                generator = classicSeed;
                break;
            case ZERO:
                gameEngine = classicGameEngine;
                generator = seedWithZero;
                break;
            case SPEED:
                gameEngine = speedGameEngine;
                generator = classicSeed;
                break;
            default:
                throw new ShouldNotReachException();
        }
        initialize();
    }

    public void initialize() {
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

    public void pushAction(@NonNull Action action, @NonNull Consumer<Integer> updateScore) throws GameShouldStopException {
        gameEngine.pushAction(action, updateScore);
        if (shouldStop())
            throw new GameShouldStopException();
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

    @NonNull
    public static Game createGame(@NonNull Mode mode, int length) {
        switch (mode) {
            case CLASSIC:
                return new Game(Mode.CLASSIC, length, classicSeed);
            case ZERO:
                return new Game(Mode.CLASSIC, length, seedWithZero);
            case SPEED:
                return new Game(Mode.SPEED, length, classicSeed);
            default:
                throw new ShouldNotReachException();
        }
    }



    public final GameEngine classicGameEngine = new GameEngine() {
        @Override
        public void pushAction(@NonNull Action action, @NonNull Consumer<Integer> updateScore) {
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
    };

    public final GameEngine speedGameEngine = new GameEngine() {
        private final Timer timer = new Timer();
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
            }

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        gameMap.generateCell(generator);
                    } catch (GameShouldStopException ignored) {
                        timer.cancel();
                        started = false;
                    }
                }
            }, 5000, 5000);
        }
    };



    @Deprecated
    public void render() {
        for (int row = 0; row < gameMap.getLength(); row++) {
            for (int col = 0; col < gameMap.getLength(); col++) {
                var value = gameMap.getCell(Position.of(row, col)).getValue();
                if (value < 0)
                    System.out.print(".\t");
                else
                    System.out.print(value + "\t");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Game game = Game.createGame(Mode.CLASSIC, 2);
        game.initialize();
        game.render();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Action: ");
            String cmd = scanner.nextLine();
            try {
                switch (cmd) {
                    case "exit":
                        return;
                    case "w":
                        game.pushAction(Action.UP, (score) -> {});
                        break;
                    case "a":
                        game.pushAction(Action.LEFT, (score) -> {});
                        break;
                    case "s":
                        game.pushAction(Action.DOWN, (score) -> {});
                        break;
                    case "d":
                        game.pushAction(Action.RIGHT, (score) -> {});
                        break;
                }
                game.render();
            } catch (GameShouldStopException ignored) {
                game.render();
                System.out.println("Game Stop");
                return;
            }
        }
    }
}
