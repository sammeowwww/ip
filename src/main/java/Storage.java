import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

    //used codex
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

    //used codex
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
                    tasks.add(parseTask(lines.get(i)));
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


    //used codex
    /**
     * Converts a line from the data file into a task.
     *
     * @param line Line to convert.
     * @return Task represented by the line.
     * @throws BobException If the line does not follow the expected format.
     */
    private Task parseTask(String line) throws BobException {
        String[] fields = line.split(" \\| ", -1);

        if (fields.length < 3) {
            throw new BobException("Not enough fields.");
        }

        boolean isDone;
        if (fields[1].equals("1")) {
            isDone = true;
        } else if (fields[1].equals("0")) {
            isDone = false;
        } else {
            throw new BobException("Completion status must be 0 or 1.");
        }

        Task task;
        switch (fields[0]) {
            case "T":
                if (fields.length != 3) {
                    throw new BobException("A to-do task must have 3 fields.");
                }
                task = new ToDo(fields[2]);
                break;
            case "D":
                if (fields.length != 4) {
                    throw new BobException("A deadline must have 4 fields.");
                }
                task = new Deadline(fields[2], fields[3]);
                break;
            case "E":
                if (fields.length != 5) {
                    throw new BobException("An event must have 5 fields.");
                }
                task = new Event(fields[2], fields[3], fields[4]);
                break;
            default:
                throw new BobException("Unknown task type: " + fields[0]);
        }

        if (isDone) {
            task.markTask();
        }

        return task;
    }
}
