public class Event extends Task {
    private String from;
    private String to;

    public Event(String from, String to) {
        super("event");
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        // description
        String str = "[E][";
        if (this.done) {
            str += "X";
        }
        str += "] " +  this.description;

        // timeline
        str += "(from: " + this.from + " to: " + this.to + ")";
        return str;
    }
}
