package comp4521.project.game.interact;

import org.jetbrains.annotations.NotNull;

import comp4521.project.game.action.Action;

public interface InputEngine {
    @NotNull
    Action fetchAction();
}
