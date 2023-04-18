package comp4521.project.game.map;

import org.jetbrains.annotations.NotNull;

public class Move {
    private final Position start;
    private final Position end;
    public Move(@NotNull Position start, @NotNull Position end) {
        this.start = start;
        this.end = end;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }
}
