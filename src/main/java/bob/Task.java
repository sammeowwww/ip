package bob;

/**
 * Represents a task and its completion status.
 */
public class Task {
    protected boolean isDone;
    protected final String description;

    /**
     * Creates an incomplete task with the specified description.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.isDone = false;
        this.description = description;
    }

    /**
     * Marks the task as done.
     */
    public void markTask() {
        this.isDone = true;
    }

    /**
     * Unmarks the task, the task is undone.
     */
    public void unmarkTask() {
        this.isDone = false;
    }

    /**
     * Returns whether the task is completed or not.
     *
     * @return True if the task is completed.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the task representation to be stored by {@code Storage}.
     *
     * @return Data of the task in the form of a string.
     */
    public String getDataString() {
        return "T | " + (isDone() ? "1" : "0") + " | " + this.description;
    }

    @Override
    public String toString() {
        String taskStatus = "[";
        if (this.isDone) {
            taskStatus += "X";
        }
        taskStatus += "] " + this.description;
        return taskStatus;
    }
}
