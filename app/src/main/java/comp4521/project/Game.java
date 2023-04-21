package comp4521.project;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import comp4521.project.game.action.Action;
import comp4521.project.game.map.GameMap;
import comp4521.project.utils.GameShouldStopException;

public abstract class Game {
    public final static Supplier<Integer> classicSeed = () -> Math.random() < 0.6 ? 2 : 4;
    public final static Supplier<Integer> seedWithZero = () -> Math.random() < 0.1 ? 0 : classicSeed.get();

    protected final GameMap gameMap;

    protected Supplier<Integer> generator;

    protected Consumer<GameMap> onMapUpdate;

    public Game(@NonNull GameMap map, @NonNull Supplier<Integer> generator) {
        this.gameMap = map;
        this.generator = generator;
    }

    public abstract void pushAction(@NonNull Action action) throws GameShouldStopException;

    public void setOnMapUpdate(Consumer<GameMap> onMapUpdate) {
        this.onMapUpdate = onMapUpdate;
    }

    public List<Integer> getFlattenMap() {
        var len = gameMap.getLength();
        List<Integer> map = new ArrayList<>();
        for (int row = 0; row < len; row++) {
            for (int col = 0; col < len; col++) {
                var cell = gameMap.getCell(row, col);
                map.add(cell == null ? -1 : cell.getValue());
            }
        }
        return map;
    }

    public void updateMap() {
        onMapUpdate.accept(gameMap);
    }
 }
