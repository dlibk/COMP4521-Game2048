package comp4521.project.game.map;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import comp4521.project.game.action.*;
import comp4521.project.game.cell.*;
import comp4521.project.utils.ShouldNotReachException;

public class GameMap {
    private final Cell[][] cells;
    protected int length;
    private final List<Position> positions;

    public GameMap(int length) {
        this.length = length;
        cells = new Cell[length][length];
        List<Position> temp = new ArrayList<>();
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < length; col++) {
                temp.add(new Position(row, col));
            }
        }
        positions = Collections.unmodifiableList(temp);
    }

    public int getLength() {
        return length;
    }

    public boolean isPositionInMap(@NotNull Position position) {
        return position.row() >= 0 && position.row() < length && position.col() >= 0 && position.col() < length;
    }

    public Cell getCell(@NotNull Position position) {
        if (!isPositionInMap(position)) {
            throw new IllegalArgumentException("position not in map");
        }
        return cells[position.row()][position.col()];
    }

    public void placeCell(@NotNull Position position, Cell cell) {
        if (!isPositionInMap(position)) {
            throw new IllegalArgumentException("position not in map");
        }
        cells[position.row()][position.col()] = cell;
    }

    public void generateCell(@NotNull Supplier<Integer> value) {
        List<Position> emptyPositions = getEmptyPositions();
        placeCell(emptyPositions.get(new Random().nextInt(emptyPositions.size())), new Cell(value.get()));
    }

    private void moveCell(@NotNull Move move) {
        Cell cell = getCell(move.getStart());
        assert cell != null;
        Cell target = getCell(move.getEnd());
        placeCell(move.getStart(), null);
        if (target == null) {
            placeCell(move.getEnd(), cell);
        } else {
            placeCell(move.getEnd(), Cell.collide(cell, target));
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
                return Move.of(position, target);
            }
            Cell nextCell = getCell(next);
            if (nextCell == null) {
                target = next;
            } else if (nextCell.isLocked() || nextCell.getValue() != cell.getValue()) {
                return Move.of(position, target);
            } else {
                return Move.of(position, next);
            }
        }
        return null;
    }

    public ActionResult processAction(Action action, Consumer<Move> onMove, Consumer<Cell> onCollide) {
        boolean succeed = false;
        List<Position> positions1 = new ArrayList<>(this.positions);

        positions1.sort((position, t1) -> {
            int row1 = position.row();
            int row2 = t1.row();
            int col1 = position.col();
            int col2 = t1.col();
            switch (action) {
                case LEFT:
                    return Integer.compare(col1, col2);
                case RIGHT:
                    return Integer.compare(col2, col1);
                case UP:
                    return Integer.compare(row1, row2);
                case DOWN:
                    return Integer.compare(row2, row1);
                default:
                    throw new ShouldNotReachException();
            }
        });

        for (Position p: positions1) {
            Move move = getMove(p, action);
            if (move != null) {
                succeed = true;
                onMove.accept(move);
                Cell newCell = getCell(move.getEnd());
                moveCell(move);
                assert getCell(move.getStart()) == null;
                if (newCell != null) {
                    onCollide.accept(newCell);
                    newCell.lock();
                }
            }
        }

        for (Position p: positions) {
            if (getCell(p) != null) {
                getCell(p).unlock();
            }
        }

        return succeed ? new ActionResult.Success(action) : new ActionResult.Failed(action,
                "No valid movement made.");
    }

    public List<Position> getAllPositions() {
        return positions;
    }

    public List<Position> getEmptyPositions() {
        return positions.stream().filter(x -> getCell(x) == null).collect(Collectors.toList());
    }
}
