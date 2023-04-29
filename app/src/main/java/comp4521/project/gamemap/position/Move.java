package comp4521.project.gamemap.position;

import androidx.annotation.NonNull;

public class Move {
    private final Position start;
    private final Position end;
    private Move(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    @NonNull
    public Position getStart() {
        return start;
    }

    @NonNull
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

    public static Move of(@NonNull Position start, @NonNull Position end) {
        if (start.equals(end)) {
            return null;
        }
        return new Move(start, end);
    }
}
