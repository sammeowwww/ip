package bob.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import bob.exception.BobException;
import bob.task.Deadline;
import bob.task.Event;
import bob.task.Task;
import bob.task.ToDo;

/**
 * Interprets user input and converts command arguments into application data.
 */
public class Parser {
    /**
     * Contains the description and date text extracted from a deadline command.
     *
     * @param description Deadline description.
     * @param dueDateText Text representing the deadline date.
     */
    private record DeadlineDetails(String description, String dueDateText) {
    }

    /**
     * Contains the description and date texts extracted from an event command.
     *
     * @param description Event description.
     * @param startDateText Text representing the event start date.
     * @param endDateText Text representing the event end date.
     */
    private record EventDetails(String description, String startDateText, String endDateText) {
    }

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
        return switch (command.getCommandWord()) {
            case "todo" -> parseToDo(command.getArgument());
            case "deadline" -> parseDeadline(command.getArgument());
            case "event" -> parseEvent(command.getArgument());
            default -> throw new BobException("This command does not create a task.");
        };
    }

    /**
     * Converts the argument of a task-number command into an integer.
     *
     * @param command Command containing a user-visible task number.
     * @return Task number represented by the command argument.
     * @throws BobException If the argument is missing or is not an integer.
     */
    public int parseTaskNumber(ParsedCommand command) throws BobException {
        try {
            return Integer.parseInt(command.getArgument());
        } catch (NumberFormatException exception) {
            throw new BobException("Use: " + command.getCommandWord() + " <task index>.");
        }
    }

    /**
     * Returns the keyword supplied to a find command.
     *
     * @param command Find command containing the keyword.
     * @return Keyword supplied by the user.
     * @throws BobException If the keyword is missing.
     */
    public String parseKeyword(ParsedCommand command) throws BobException {
        String keyword = command.getArgument().trim();
        if (keyword.isEmpty()) {
            throw new BobException("Use: find <keyword>.");
        }
        return keyword;
    }

    // Used Perplexity to help refine this code.
    /**
     * Creates a to-do task from a command argument.
     *
     * @param argument Command argument containing the task description.
     * @return To-do task represented by the argument.
     * @throws BobException If the task description is missing.
     */
    private Task parseToDo(String argument) throws BobException {
        validateTaskDescriptionIsPresent(argument);
        return new ToDo(argument);
    }

    // Used Perplexity to help refine this code.
    /**
     * Creates a deadline task from a command argument.
     *
     * @param argument Command argument containing a description and due date.
     * @return Deadline task represented by the argument.
     * @throws BobException If the description or due date is missing or invalid.
     */
    private Task parseDeadline(String argument) throws BobException {
        DeadlineDetails deadlineDetails = parseDeadlineDetails(argument);
        LocalDate dueDate = parseDate(
                deadlineDetails.dueDateText(),
                "Please enter the deadline date as yyyy-MM-dd.");
        return new Deadline(deadlineDetails.description(), dueDate);
    }

    /**
     * Extracts the description and date text from a deadline argument.
     *
     * @param argument Deadline command argument.
     * @return Details extracted from the deadline argument.
     * @throws BobException If the deadline argument has an invalid format.
     */
    private DeadlineDetails parseDeadlineDetails(String argument) throws BobException {
        validateTaskDescriptionIsPresent(argument);
        String[] deadlineParts = argument.split("\\s+/by\\s+", 2);
        boolean hasDescriptionAndDeadline = deadlineParts.length == 2;
        if (!hasDescriptionAndDeadline) {
            throw new BobException("Use: deadline <description> /by <yyyy-MM-dd>.");
        }

        String description = deadlineParts[0].trim();
        String dueDateText = deadlineParts[1].trim();
        boolean isDescriptionMissing = description.isEmpty();
        boolean isDueDateMissing = dueDateText.isEmpty();
        if (isDescriptionMissing || isDueDateMissing) {
            throw new BobException("Use: deadline <description> /by <yyyy-MM-dd>.");
        }
        return new DeadlineDetails(description, dueDateText);
    }

    // Used Perplexity to help refine this code.
    /**
     * Creates an event task from a command argument.
     *
     * @param argument Command argument containing a description and date range.
     * @return Event task represented by the argument.
     * @throws BobException If the description or date range is missing or invalid.
     */
    private Task parseEvent(String argument) throws BobException {
        EventDetails eventDetails = parseEventDetails(argument);
        LocalDate startDate = parseDate(
                eventDetails.startDateText(),
                "Please enter the event dates as yyyy-MM-dd.");
        LocalDate endDate = parseDate(
                eventDetails.endDateText(),
                "Please enter the event dates as yyyy-MM-dd.");
        return new Event(eventDetails.description(), startDate, endDate);
    }

    /**
     * Extracts the description and date texts from an event argument.
     *
     * @param argument Event command argument.
     * @return Details extracted from the event argument.
     * @throws BobException If the event argument has an invalid format.
     */
    private EventDetails parseEventDetails(String argument) throws BobException {
        validateTaskDescriptionIsPresent(argument);
        String[] descriptionAndDateRangeParts = argument.split("\\s+/from\\s+", 2);
        if (descriptionAndDateRangeParts.length < 2) {
            throw new BobException("Use: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.");
        }

        String description = descriptionAndDateRangeParts[0].trim();
        String[] dateParts = descriptionAndDateRangeParts[1].split("\\s+/to\\s+", 2);
        boolean hasStartAndEndDates = dateParts.length == 2;
        if (!hasStartAndEndDates) {
            throw new BobException("Use: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.");
        }

        String startDateText = dateParts[0].trim();
        String endDateText = dateParts[1].trim();
        boolean isDescriptionMissing = description.isEmpty();
        boolean isStartDateMissing = startDateText.isEmpty();
        boolean isEndDateMissing = endDateText.isEmpty();
        if (isDescriptionMissing || isStartDateMissing || isEndDateMissing) {
            throw new BobException("Use: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.");
        }
        return new EventDetails(description, startDateText, endDateText);
    }

    /**
     * Validates that a task description was supplied.
     *
     * @param description Task description to validate.
     * @throws BobException If the task description is empty.
     */
    private void validateTaskDescriptionIsPresent(String description) throws BobException {
        if (description.isEmpty()) {
            throw new BobException("You need to enter a task name!!");
        }
    }

    /**
     * Converts ISO date text into a date.
     *
     * @param dateText Date text to convert.
     * @param invalidDateMessage Message to use if the date text is invalid.
     * @return Date represented by the text.
     * @throws BobException If the date text is not a valid ISO date.
     */
    private LocalDate parseDate(String dateText, String invalidDateMessage) throws BobException {
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException exception) {
            throw new BobException(invalidDateMessage);
        }
    }
}
