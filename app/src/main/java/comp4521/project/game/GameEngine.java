package comp4521.project.game;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

public interface GameEngine {
    void pushAction(@NonNull Action action, @NonNull Consumer<Integer> updateScore);
    default void pause() {}
}
