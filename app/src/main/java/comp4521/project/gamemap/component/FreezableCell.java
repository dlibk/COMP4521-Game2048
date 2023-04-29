package comp4521.project.gamemap.component;


public class FreezableCell extends Cell {
    private boolean frozen = false;

    @Override
    public boolean isLocked() {
        return frozen || super.isLocked();
    }

    @Override
    public boolean isEmpty() {
        return !frozen && super.isEmpty();
    }

    @Override
    public int getValue() {
        return frozen ? Integer.MAX_VALUE : super.getValue();
    }

    public synchronized void freeze() {
        frozen = true;
        if (card != null)
            card.freeze();
    }

    public synchronized void unfreeze() {
        frozen = false;
        if (card != null)
            card.unfreeze(getValue());
    }
}
