package bob;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that must be completed by a specified deadline.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DATE_FORMAT_DATA = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATE_FORMAT_DISPLAY =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private final LocalDate deadline;

    /**
     * Creates a deadline task.
     *
     * @param description Description of the task.
     * @param deadline Deadline by which the task should be completed.
     */
    public Deadline(String description, LocalDate deadline) {
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String getDataString() {
        return "D | " + (isDone() ? "1" : "0")
                + " | " + this.description
                + " | " + deadline.format(DATE_FORMAT_DATA);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + deadline.format(DATE_FORMAT_DISPLAY) + ")";
    }
}
