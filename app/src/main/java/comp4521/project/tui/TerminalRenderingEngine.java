package comp4521.project.tui;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;

import comp4521.project.game.interact.RenderingEngine;
import comp4521.project.game.map.GameMap;
import comp4521.project.game.map.Position;

/**
 * A rendering engine that prints to the terminal.
 */
public class TerminalRenderingEngine implements RenderingEngine {

    private final PrintStream outputSteam;

    /**
     * @param outputSteam The {@link PrintStream} to write the output to.
     */
    public TerminalRenderingEngine(PrintStream outputSteam) {
        this.outputSteam = outputSteam;
    }

    @Override
    public void render(@NotNull GameMap map) {
        final var builder = new StringBuilder();
        for (int y = 0; y < map.getLength(); y++) {
            for (int x = 0; x < map.getLength(); x++) {
                final var cell = map.getCell(new Position(y, x));
                if (cell == null) {
                    builder.append('.');
                } else {
                    builder.append(cell.getValue());
                }
                builder.append('\t');
            }
            builder.append('\n');
        }
        outputSteam.print(builder);
    }

    @Override
    public void message(@NotNull String content) {
        outputSteam.println(content);
    }
}
