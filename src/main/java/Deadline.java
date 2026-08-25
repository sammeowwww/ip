import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Deadline extends Task{
    private LocalDate deadline;

    public Deadline(String description, LocalDate deadline){
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String toDataString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "D | " + (isDone() ? "1" : "0")
                + " | " + this.description
                + " | " + deadline.format(dtf);
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd yyyy");
        return "[D]" + super.toString()
                + " (by: " + deadline.format(dtf) + ")";
    }
}
