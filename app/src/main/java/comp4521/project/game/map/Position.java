package comp4521.project.game.map;

import org.jetbrains.annotations.NotNull;

public class Position {

    private final int row;
    private final int col;
    public static final int MAX_MAP_LENGTH = 6;

    private static final Position[][] cache =
            new Position[Position.MAX_MAP_LENGTH + 2][Position.MAX_MAP_LENGTH + 2];

    @NotNull
    public static Position of(int row, int col) {
        int x = row + 1;
        int y = col + 1;
        if (cache[x][y] == null) {
            cache[x][y] = new Position(row, col);
        }
        return cache[x][y];
    }

    private Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Position) {
            Position p = (Position) o;
            return row == p.row && col == p.col;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 17 + 31 * row + col;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }
}
