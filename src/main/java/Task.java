public class Task {
    protected boolean done;
    protected String description;

    public Task(String description) {
        this.done = false;
        this.description = description;
    }

    /**
     * Marks the task as done.
     */
    public void markTask() {
        this.done = true;
    }

    /**
     * Unmarks the task, the task is undone.
     */
    public void unmarkTask() {
        this.done = false;
    }

    /**
     * Returns whether the task is completed or not.
     *
     * @return True if the task is completed.
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Converts the task to data string which would be stored in {@code Storage}.
     *
     * @return Data of the task in the form of a string
     */
    public String toDataString() {
        return "T | " + (isDone() ? "1" : "0") + " | " + this.description;
    }

    @Override
    public String toString() {
        String str = "[";
        if (this.done) {
            str += "X";
        }
        str += "] " +  this.description;
        return str;
    }
}
