package comp4521.project.game.map;

import org.jetbrains.annotations.NotNull;

public class Move {
    private final Position start;
    private final Position end;
    private Move(Position start, Position end) {

        this.start = start;
        this.end = end;
    }

    @NotNull
    public Position getStart() {
        return start;
    }

    @NotNull
    public Position getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Move) {
            var m = (Move) o;
            return start.equals(m.start) && end.equals(m.end);
        }
        return false;
    }

    public static Move of(@NotNull Position start, @NotNull Position end) {
        if (start.equals(end)) {
            return null;
        }
        return new Move(start, end);
    }
}
