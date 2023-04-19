package comp4521.project.tui;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

import comp4521.project.game.action.Action;
import comp4521.project.game.interact.InputEngine;
import comp4521.project.utils.ShouldNotReachException;


/**
 * An input engine that fetches actions from terminal input.
 */
public class TerminalInputEngine implements InputEngine {

    /**
     * The {@link Scanner} for reading input from the terminal.
     */
    private final Scanner terminalScanner;

    /**
     * @param terminalStream The stream to read terminal inputs.
     */
    public TerminalInputEngine(InputStream terminalStream) {
        this.terminalScanner = new Scanner(terminalStream);
    }

    private static final Pattern MOVE_REGEX = Pattern.compile("^(?<action>[WASDwasdRrHJKLhjklUu])$");

    /**
     * Fetch an action from user in terminal to process.
     *
     * @return the user action.
     */
    @Override
    public @NotNull Action fetchAction() {
        System.out.print("Please enter your move: ");
        final var inputLine = terminalScanner.nextLine();
        final var moveMatcher = MOVE_REGEX.matcher(inputLine);
        if (moveMatcher.find()) {
            final var moveCommand = Objects.requireNonNull(moveMatcher.group("action")).toUpperCase();

            switch (moveCommand) {
                case "W":
                    return Action.UP;
                case "A":
                    return Action.LEFT;
                case "S":
                    return Action.DOWN;
                case "D":
                    return Action.RIGHT;
                default:
                    throw new ShouldNotReachException();
            }
        } else {
            throw new ShouldNotReachException();
        }
    }
}
