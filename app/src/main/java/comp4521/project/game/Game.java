package comp4521.project.game;

import comp4521.project.game.mode.Mode;

public interface Game extends Runnable {
    boolean shouldStop();

    void run();

    static void main(String[] args) {
        Game game = GameFactory.createTUIGame(Mode.CLASSIC, 4);
        game.run();
    }
}
