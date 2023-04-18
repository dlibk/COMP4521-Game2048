package comp4521.project.game.action;

import org.jetbrains.annotations.NotNull;

import comp4521.project.game.map.Position;

public enum Action {
    UP, DOWN, LEFT, RIGHT;
    public Position getNextPosition(@NotNull Position position) {
        switch (this) {
            case UP:
                return new Position(position.row() - 1, position.col());
            case DOWN:
                return new Position(position.row() + 1, position.col());
            case LEFT:
                return new Position(position.row(), position.col() - 1);
            case RIGHT:
                return new Position(position.row(), position.col() + 1);
            default:
                throw new RuntimeException();
        }
    }
}
