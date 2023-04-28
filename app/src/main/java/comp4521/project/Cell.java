package comp4521.project;

import android.app.Activity;
import android.view.animation.Animation;

import androidx.annotation.NonNull;

public class Cell {
    private int value = -1;
    private boolean locked = false;

    private Card card;
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        if (value < 0)
            unlock();
        if (card != null)
            card.setValue(value);
    }

    public void startAnimation(@NonNull Animation animation) {
        if (card != null)
            ((Activity) card.getContext()).runOnUiThread(() -> card.startAnimation(animation));
    }

    public boolean isLocked() {
        return locked;
    }

    public void unlock() {
        locked = false;
    }

    public void lock() {
        if (value >= 0)
            locked = true;
    }

    public boolean isEmpty() {
        return value < 0;
    }

    public void bindCard(@NonNull Card card) {
        this.card = card;
    }
}
