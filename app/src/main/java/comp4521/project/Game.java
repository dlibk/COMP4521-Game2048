package comp4521.project;

import androidx.annotation.NonNull;

import java.util.Scanner;
import java.util.function.Supplier;

import comp4521.project.utils.GameShouldStopException;
import comp4521.project.utils.ShouldNotReachException;

public abstract class Game {
    public static final Supplier<Integer> classicSeed = () -> Math.random() < 0.6 ? 2 : 4;
    public static final Supplier<Integer> seedWithZero = () -> Math.random() < 0.1 ? 0 : classicSeed.get();
    protected final GameMap gameMap;
    protected final Supplier<Integer> generator;

    protected Game(int length, Supplier<Integer> generator) {
        this.gameMap = new GameMap(length);
        this.generator = generator;
    }

    public void initialize() {
        gameMap.generateCell(generator);
        gameMap.generateCell(generator);
    }

    public abstract void pushAction(@NonNull Action action);

    @NonNull
    public static Game createGame(@NonNull Mode mode, int length) {
        switch (mode) {
            case CLASSIC:
                return new ClassicGame(length, classicSeed);
            case ZERO:
                return new ClassicGame(length, seedWithZero);
            default:
                throw new ShouldNotReachException();
        }
    }

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
                        game.pushAction(Action.UP);
                        break;
                    case "a":
                        game.pushAction(Action.LEFT);
                        break;
                    case "s":
                        game.pushAction(Action.DOWN);
                        break;
                    case "d":
                        game.pushAction(Action.RIGHT);
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
