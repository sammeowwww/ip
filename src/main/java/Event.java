public class Event extends Task {
    private String from;
    private String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toDataString() {
        return " E | " + (isDone() ? "1" : "0")
                + " | " + this.description
                + " | " + this.from
                + " | " + this.to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + this.from
                + " to: " + this.to + ")";
    }
}
