import java.util.Date;

public class Deadline extends Task{
    private String deadline;

    public Deadline(String description, String deadline){
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String toDataString() {
        return "D | " + (isDone() ? "1" : "0")
                + " | " + this.description
                + " | " + this.deadline;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString()
                + " (by: " + deadline + ")";
    }
}
