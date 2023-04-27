package comp4521.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import comp4521.project.utils.*;

public class GameMap {
    final Cell[][] cells;
    private final int length;

    private final List<Position> allPositions;

    public GameMap(int length) {
        this.length = length;
        cells = new Cell[length][length];
        List<Position> temp = new ArrayList<>();
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < length; col++) {
                cells[row][col] = new Cell();
                temp.add(Position.of(row, col));
            }
        }
        allPositions = Collections.unmodifiableList(temp);
    }

    @NonNull
    public Cell getCell(@NonNull Position position) {
        assert contains(position);
        return cells[position.row()][position.col()];
    }

    public void placeCell(@NonNull Position position, int value) {
        assert contains(position);
        getCell(position).setValue(value);
    }

    public void generateCell(Supplier<Integer> generator) throws GameShouldStopException {
        var emptyPositions = getEmptyPositions();
        if (emptyPositions.isEmpty())
            throw new GameShouldStopException();

        var position = emptyPositions.get((int) (Math.random() * emptyPositions.size()));
        placeCell(position, generator.get());
        getCell(position).startAnimation(Card.generateAnimation);
    }

    public boolean processAction(@NonNull Action action) {
        boolean succeed = false;
        List<Position> positions = new ArrayList<>(this.allPositions);

        positions.sort((position, t1) -> {
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

        for (Position p: positions) {
            Move move = getMove(p, action);
            if (move != null) {
                succeed = true;
                moveCell(move);
                assert getCell(move.getStart()).isEmpty();
            }
        }

        for (Position p: positions) {
            getCell(p).unlock();
        }

        return succeed;
    }

    @Nullable
    private Move getMove(@NonNull Position position, @NonNull Action action) {
        Cell cell = getCell(position);
        if (cell.isEmpty() || cell.isLocked()) {
            return null;
        }
        Position target = position;
        while (contains(target)) {
            Position next = action.getNextPosition(target);
            if (!contains(next)) {
                return Move.of(position, target);
            }
            Cell nextCell = getCell(next);
            if (nextCell.isEmpty()) {
                target = next;
            } else if (nextCell.isLocked() || nextCell.getValue() != cell.getValue()) {
                return Move.of(position, target);
            } else {
                return Move.of(position, next);
            }
        }
        return null;
    }

    private void moveCell(@NonNull Move move) {
        Cell cell = getCell(move.getStart());
        var value = cell.getValue();
        assert !(cell.isEmpty() || cell.isLocked());
        Cell target = getCell(move.getEnd());
        placeCell(move.getStart(), -1);
        if (target.isEmpty()) {
            placeCell(move.getEnd(), value);
        } else {
            placeCell(move.getEnd(), value + target.getValue());
            target.startAnimation(Card.synthesisAnimation);
            target.lock();
        }
    }

    public List<Position> getAllPositions() {
        return allPositions;
    }

    public List<Position> getEmptyPositions() {
        return allPositions.stream().filter(p -> getCell(p).isEmpty()).collect(Collectors.toList());
    }

    private boolean contains(@NonNull Position position) {
        return position.row() >= 0 && position.col() >= 0
                && position.row() < length && position.col() < length;
    }

    public boolean isMovable(@NonNull Position position) {
        assert contains(position);
        var cell = getCell(position);
        if (cell.isEmpty())
            return true;
        for (Action action: List.of(Action.UP, Action.DOWN, Action.LEFT, Action.RIGHT)) {
            var next = action.getNextPosition(position);
            if (contains(next) && (getCell(next).isEmpty() || getCell(next).getValue() == cell.getValue())) {
                return true;
            }
        }
        return false;
    }

    public int getLength() {
        return length;
    }

    public void initialize() {
        for (Position p: allPositions) {
            getCell(p).setValue(-1);
        }
    }
}
