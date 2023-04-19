package comp4521.project.game.interact;

import org.jetbrains.annotations.NotNull;

import comp4521.project.game.map.GameMap;

public interface RenderingEngine {
    /**
     * Renders the game based on the current state.
     *
     * @param map The current game map.
     */
    void render(@NotNull GameMap map);

    /**
     * Display a message to the player.
     *
     * @param content The message
     */
    void message(@NotNull String content);
}
