import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private LocalDate from;
    private LocalDate to;

    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toDataString() {
        return "E | " + (isDone() ? "1" : "0")
                + " | " + this.description
                + " | " + this.from
                + " | " + this.to;
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd yyyy");
        return "[E]" + super.toString()
                + " (from: " + from.format(dtf)
                + " to: " + to.format(dtf) + ")";
    }
}
