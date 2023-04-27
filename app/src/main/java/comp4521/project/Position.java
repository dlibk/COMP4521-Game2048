package comp4521.project;

import androidx.annotation.NonNull;

public class Position {

    private final int row;
    private final int col;
    public static final int MAX_MAP_LENGTH = 6;

    private static final Position[][] cache =
            new Position[MAX_MAP_LENGTH][MAX_MAP_LENGTH];

    @NonNull
    public static Position of(int row, int col) throws AssertionError {
        if (row >= 0 && row < MAX_MAP_LENGTH && col >= 0 && col < MAX_MAP_LENGTH) {
            if (cache[row][col] == null)
                cache[row][col] = new Position(row, col);
            return cache[row][col];
        } else {
            return new Position(row, col);
        }
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

    @NonNull
    @Override
    public String toString() {
        return "Position(" + row + ", " + col + ")";
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }
}
