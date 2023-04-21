package comp4521.project.game.action;

import org.jetbrains.annotations.NotNull;

import comp4521.project.game.map.Position;
import comp4521.project.utils.ShouldNotReachException;

public enum Action {
    UP, DOWN, LEFT, RIGHT, NULL;
    public Position getNextPosition(@NotNull Position position) {
        switch (this) {
            case UP:
                return Position.of(position.row() - 1, position.col());
            case DOWN:
                return Position.of(position.row() + 1, position.col());
            case LEFT:
                return Position.of(position.row(), position.col() - 1);
            case RIGHT:
                return Position.of(position.row(), position.col() + 1);
            case NULL:
                return position;
            default:
                throw new ShouldNotReachException();
        }
    }
}
