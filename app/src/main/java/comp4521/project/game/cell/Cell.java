package comp4521.project.game.cell;

public class Cell {
    private int value = 0;
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
}
