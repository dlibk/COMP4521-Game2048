package comp4521.project.gui;

import comp4521.project.game.Game;
import comp4521.project.game.map.GameMap;

public class GUIGame implements Game {
    GameMap gameMap;

    @Override
    public boolean shouldStop() {
        return false;
    }

    @Override
    public void run() {

    }
}
