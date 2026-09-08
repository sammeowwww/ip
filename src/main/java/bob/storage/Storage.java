package bob.storage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import bob.exception.BobException;
import bob.task.Deadline;
import bob.task.Event;
import bob.task.Task;
import bob.task.ToDo;

/**
 * Loads tasks from and saves tasks to a data file.
 */
public class Storage {
    private static final int TASK_TYPE_FIELD_INDEX = 0;
    private static final int COMPLETION_STATUS_FIELD_INDEX = 1;
    private static final int DESCRIPTION_FIELD_INDEX = 2;
    private static final int DEADLINE_DATE_FIELD_INDEX = 3;
    private static final int EVENT_START_DATE_FIELD_INDEX = 3;
    private static final int EVENT_END_DATE_FIELD_INDEX = 4;
    private static final int MINIMUM_FIELD_COUNT = 3;
    private static final int TODO_FIELD_COUNT = 3;
    private static final int DEADLINE_FIELD_COUNT = 4;
    private static final int EVENT_FIELD_COUNT = 5;

    private final Path filePath;

    /**
     * Creates storage that uses the specified data file.
     *
     * @param filePath Path of the data file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    // Used Codex to help write this method.
    /**
     * Saves tasks to the data file.
     *
     * @param tasks Tasks to save.
     * @throws BobException If the tasks cannot be saved.
     */
    public void saveTasks(List<Task> tasks) throws BobException {
        try {
            Path parentDirectory = filePath.getParent();

            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(
                    filePath, StandardCharsets.UTF_8)) {
                for (Task task : tasks) {
                    writer.write(task.toDataString());
                    writer.newLine();
                }
            }
        } catch (IOException exception) {
            throw new BobException("Unable to save tasks :( " + exception.getMessage());
        }
    }

    // Used Codex to help write this method.
    /**
     * Loads tasks from the data file.
     *
     * @return Tasks reconstructed from the data file.
     * @throws BobException If the tasks cannot be loaded.
     */
    public List<Task> loadTasks() throws BobException {
        List<Task> tasks = new ArrayList<>();

        if (Files.notExists(filePath)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

            for (int i = 0; i < lines.size(); i++) {
                try {
                    tasks.add(parseTaskFromDataLine(lines.get(i)));
                } catch (BobException exception) {
                    throw new BobException("Invalid data on line " + (i + 1)
                            + ": " + exception.getMessage());
                }
            }
        } catch (IOException exception) {
            throw new BobException("Unable to load tasks :( " + exception.getMessage());
        }

        return tasks;
    }

    // Used Codex to help write this method.
    /**
     * Converts a line from the data file into a task.
     *
     * @param dataLine Data-file line to convert.
     * @return Task represented by the data-file line.
     * @throws BobException If the data-file line does not follow the expected format.
     */
    private Task parseTaskFromDataLine(String dataLine) throws BobException {
        String[] taskFields = dataLine.split(" \\| ", -1);
        validateMinimumFieldCount(taskFields);

        boolean isDone = parseCompletionStatus(taskFields[COMPLETION_STATUS_FIELD_INDEX]);
        Task task = createTaskFromFields(taskFields);
        if (isDone) {
            task.markTask();
        }

        return task;
    }

    /**
     * Validates that a data-file line contains the common task fields.
     *
     * @param taskFields Fields extracted from a data-file line.
     * @throws BobException If the line does not contain all common fields.
     */
    private void validateMinimumFieldCount(String[] taskFields) throws BobException {
        if (taskFields.length < MINIMUM_FIELD_COUNT) {
            throw new BobException("Not enough fields.");
        }
    }

    /**
     * Converts a stored completion marker into its boolean representation.
     *
     * @param completionStatus Stored completion marker.
     * @return True if the task is completed.
     * @throws BobException If the completion marker is invalid.
     */
    private boolean parseCompletionStatus(String completionStatus) throws BobException {
        return switch (completionStatus) {
            case "1" -> true;
            case "0" -> false;
            default -> throw new BobException("Completion status must be 0 or 1.");
        };
    }

    /**
     * Creates the task represented by a collection of stored fields.
     *
     * @param taskFields Fields extracted from a data-file line.
     * @return Task represented by the fields.
     * @throws BobException If the task type, field count, or date is invalid.
     */
    private Task createTaskFromFields(String[] taskFields) throws BobException {
        try {
            return switch (taskFields[TASK_TYPE_FIELD_INDEX]) {
                case "T" -> createToDoFromFields(taskFields);
                case "D" -> createDeadlineFromFields(taskFields);
                case "E" -> createEventFromFields(taskFields);
                default -> throw new BobException(
                        "Unknown task type: " + taskFields[TASK_TYPE_FIELD_INDEX]);
            };
        } catch (DateTimeParseException exception) {
            throw new BobException("Invalid date. Use yyyy-MM-dd.");
        }
    }

    /**
     * Creates a to-do task from stored fields.
     *
     * @param taskFields Fields extracted from a data-file line.
     * @return To-do task represented by the fields.
     * @throws BobException If the number of fields is invalid.
     */
    private Task createToDoFromFields(String[] taskFields) throws BobException {
        if (taskFields.length != TODO_FIELD_COUNT) {
            throw new BobException("A to-do task must have 3 fields.");
        }
        return new ToDo(taskFields[DESCRIPTION_FIELD_INDEX]);
    }

    /**
     * Creates a deadline task from stored fields.
     *
     * @param taskFields Fields extracted from a data-file line.
     * @return Deadline task represented by the fields.
     * @throws BobException If the number of fields is invalid.
     */
    private Task createDeadlineFromFields(String[] taskFields) throws BobException {
        if (taskFields.length != DEADLINE_FIELD_COUNT) {
            throw new BobException("A deadline must have 4 fields.");
        }
        LocalDate dueDate = LocalDate.parse(taskFields[DEADLINE_DATE_FIELD_INDEX]);
        return new Deadline(taskFields[DESCRIPTION_FIELD_INDEX], dueDate);
    }

    /**
     * Creates an event task from stored fields.
     *
     * @param taskFields Fields extracted from a data-file line.
     * @return Event task represented by the fields.
     * @throws BobException If the number of fields is invalid.
     */
    private Task createEventFromFields(String[] taskFields) throws BobException {
        if (taskFields.length != EVENT_FIELD_COUNT) {
            throw new BobException("An event must have 5 fields.");
        }
        LocalDate startDate = LocalDate.parse(taskFields[EVENT_START_DATE_FIELD_INDEX]);
        LocalDate endDate = LocalDate.parse(taskFields[EVENT_END_DATE_FIELD_INDEX]);
        return new Event(taskFields[DESCRIPTION_FIELD_INDEX], startDate, endDate);
    }
}
