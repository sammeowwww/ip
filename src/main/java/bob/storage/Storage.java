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

        if (taskFields.length < 3) {
            throw new BobException("Not enough fields.");
        }

        boolean isDone;
        if (taskFields[1].equals("1")) {
            isDone = true;
        } else if (taskFields[1].equals("0")) {
            isDone = false;
        } else {
            throw new BobException("Completion status must be 0 or 1.");
        }

        // Parse each type of task.
        Task task;
        try {
            switch (taskFields[0]) {
                case "T":
                    if (taskFields.length != 3) {
                        throw new BobException("A to-do task must have 3 fields.");
                    }
                    task = new ToDo(taskFields[2]);
                    break;
                case "D":
                    if (taskFields.length != 4) {
                        throw new BobException("A deadline must have 4 fields.");
                    }

                    LocalDate dueDate = LocalDate.parse(taskFields[3]);
                    task = new Deadline(taskFields[2], dueDate);
                    break;
                case "E":
                    if (taskFields.length != 5) {
                        throw new BobException("An event must have 5 fields.");
                    }

                    LocalDate startDate = LocalDate.parse(taskFields[3]);
                    LocalDate endDate = LocalDate.parse(taskFields[4]);
                    task = new Event(taskFields[2], startDate, endDate);
                    break;
                default:
                    throw new BobException("Unknown task type: " + taskFields[0]);
            }
        } catch (DateTimeParseException exception) {
            throw new BobException("Invalid date. Use yyyy-MM-dd.");
        }

        if (isDone) {
            task.markTask();
        }

        return task;
    }
}
