package bob;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Interprets user input and converts command arguments into application data.
 */
public class Parser {
    /**
     * Creates a parser for Bob's supported command formats.
     */
    public Parser() {
    }

    /**
     * Separates user input into its command word and argument.
     *
     * @param input User input to interpret.
     * @return Parsed command containing the command word and argument.
     * @throws BobException If the input is empty.
     */
    public ParsedCommand parse(String input) throws BobException {
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            throw new BobException("Enter a command! "
                    + "If you need a list of commands, please type 'help'. ");
        }

        String[] commandParts = trimmedInput.split("\\s+", 2);
        String commandWord = commandParts[0];
        String argument = commandParts.length > 1 ? commandParts[1] : "";
        return new ParsedCommand(commandWord, argument);
    }

    /**
     * Creates a task from a task-creation command.
     *
     * @param command Command containing the task type and details.
     * @return Task represented by the command.
     * @throws BobException If the task details are missing or invalid.
     */
    public Task parseTask(ParsedCommand command) throws BobException {
        switch (command.getCommandWord()) {
            case "todo":
                return parseToDo(command.getArgument());
            case "deadline":
                return parseDeadline(command.getArgument());
            case "event":
                return parseEvent(command.getArgument());
            default:
                throw new BobException("This command does not create a task.");
        }
    }

    /**
     * Converts the argument of an index-based command into an integer.
     *
     * @param command Command containing a task index.
     * @return Task index represented by the command argument.
     * @throws BobException If the argument is missing or is not an integer.
     */
    public int parseIndex(ParsedCommand command) throws BobException {
        try {
            return Integer.parseInt(command.getArgument());
        } catch (NumberFormatException exception) {
            throw new BobException("Use: " + command.getCommandWord() + " <task index>.");
        }
    }

    // Used to create a todo task.
    // Used Perplexity to help refine this code.
    private Task parseToDo(String argument) throws BobException {
        if (argument.isEmpty()) {
            throw new BobException("You need to enter a task name!!");
        }
        return new ToDo(argument);
    }

    // Used to create a deadline task.
    // Used Perplexity to help refine this code.
    private Task parseDeadline(String argument) throws BobException {
        if (argument.isEmpty()) {
            throw new BobException("You need to enter a task name!!");
        }

        String[] deadlineParts = argument.split("\\s+/by\\s+", 2);
        if (deadlineParts.length < 2
                || deadlineParts[0].trim().isEmpty()
                || deadlineParts[1].trim().isEmpty()) {
            throw new BobException("Use: deadline <description> /by <yyyy-MM-dd>.");
        }

        try {
            LocalDate deadline = LocalDate.parse(deadlineParts[1].trim());
            return new Deadline(deadlineParts[0].trim(), deadline);
        } catch (DateTimeParseException exception) {
            throw new BobException("Please enter the deadline date as yyyy-MM-dd.");
        }
    }

    // Used to create an event task.
    // Used Perplexity to help refine this code.
    private Task parseEvent(String argument) throws BobException {
        if (argument.isEmpty()) {
            throw new BobException("You need to enter a task name!!");
        }

        String[] fromParts = argument.split("\\s+/from\\s+", 2);
        if (fromParts.length < 2) {
            throw new BobException("Use: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.");
        }

        String[] toParts = fromParts[1].split("\\s+/to\\s+", 2);
        if (fromParts[0].trim().isEmpty()
                || toParts.length < 2
                || toParts[0].trim().isEmpty()
                || toParts[1].trim().isEmpty()) {
            throw new BobException("Use: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.");
        }

        try {
            LocalDate startDate = LocalDate.parse(toParts[0].trim());
            LocalDate endDate = LocalDate.parse(toParts[1].trim());
            return new Event(fromParts[0].trim(), startDate, endDate);
        } catch (DateTimeParseException exception) {
            throw new BobException("Please enter the event dates as yyyy-MM-dd.");
        }
    }
}
