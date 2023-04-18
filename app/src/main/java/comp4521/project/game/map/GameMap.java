package comp4521.project.game.map;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import comp4521.project.game.action.*;
import comp4521.project.game.cell.*;

public class GameMap {
    private final Cell[][] cells;
    protected int length;

    public GameMap(int length) {
        this.length = length;
        cells = new Cell[length][length];
    }

    private boolean isPositionInMap(@NotNull Position position) {
        return position.row() >= 0 && position.row() < length && position.col() >= 0 && position.col() < length;
    }

    private Cell getCell(@NotNull Position position) {
        return cells[position.row()][position.col()];
    }

    private void moveCell(@NotNull Move move) {
        Cell cell = getCell(move.getStart());
        assert cell != null;
        Cell target = getCell(move.getEnd());
        cells[move.getStart().row()][move.getEnd().col()] = null;
        if (target == null) {
            cells[move.getEnd().row()][move.getEnd().col()] = cell;
        } else {
            assert !target.isLocked();
            assert target.getValue() == cell.getValue();
            cells[move.getEnd().row()][move.getEnd().col()] = new Cell(2 * cell.getValue());
        }
    }

    private Move getMove(@NotNull Position position, @NotNull Action action) {
        Cell cell = getCell(position);
        if (cell == null) {
            return null;
        }
        Position target = position;
        while (isPositionInMap(target)) {
            Position next = action.getNextPosition(target);
            if (!isPositionInMap(next)) {
                return new Move(position, target);
            }
            Cell nextCell = getCell(next);
            if (nextCell == null) {
                target = next;
            } else if (nextCell.isLocked() || nextCell.getValue() != cell.getValue()) {
                return new Move(position, target);
            } else {
                return new Move(position, next);
            }
        }
        return null;
    }

    public void processAction(Action action, Consumer<Move> onMove, Consumer<Cell> onCollide) {
        switch (action) {
            case LEFT:
                for (int row = 0; row < length; row++) {
                    for (int col = 0; col < length; col++) {
                        Position cur = new Position(row, col);
                        Move move = getMove(cur, action);
                        if (!(move == null)) {
                            onMove.accept(move);
                            moveCell(move);
                            Cell newCell = getCell(move.getEnd());
                            if (newCell != null) {
                                onCollide.accept(newCell);
                                newCell.lock();
                            }
                        }
                    }
                }
                break;
            case RIGHT:
                break;
        }
    }
}
