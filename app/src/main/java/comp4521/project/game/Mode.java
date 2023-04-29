package comp4521.project.game;

import androidx.annotation.NonNull;

public enum Mode {
    CLASSIC, SPEED, ZERO;
    public static Mode of(@NonNull String name) {
        switch (name.toLowerCase()) {
            case "classic":
                return Mode.CLASSIC;
            case "speed":
                return Mode.SPEED;
            case "zero":
                return Mode.ZERO;
            default:
                return null;
        }
    }
}
