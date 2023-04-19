package comp4521.project.game.cell;

import org.jetbrains.annotations.NotNull;

public class Cell {
    private final int value;
    private boolean locked = false;
    public Cell(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public boolean isLocked() {
        return locked;
    }
    public void lock() {
        locked = true;
    }
    public void unlock() {
        locked = false;
    }
    @NotNull
    public static Cell collide(@NotNull Cell cell, @NotNull Cell other) {
        assert cell.value == other.value;
        assert !cell.isLocked();
        assert !other.isLocked();
        Cell newCell = new Cell(cell.value + other.value);
        newCell.lock();
        return newCell;
    }
}
