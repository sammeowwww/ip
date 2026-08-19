public class Task {
    protected boolean done;
    protected String description;

    public Task(String description) {
        this.done = false;
        this.description = description;
    }

    public void markTask() {
        this.done = true;
    }

    public void unmarkTask() {
        this.done = false;
    }

    public String getDescription() {
        return description;
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
