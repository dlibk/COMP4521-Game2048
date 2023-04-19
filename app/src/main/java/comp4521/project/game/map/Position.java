package comp4521.project.game.map;

public class Position {

    private final int row;
    private final int col;

    public Position(int row, int col) {
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
