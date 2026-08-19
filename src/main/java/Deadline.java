import java.util.Date;

public class Deadline extends Task{
    private String deadline;

    public Deadline(String description, String deadline){
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        // description
        String str = "[D][";
        if (this.done) {
            str += "X";
        }
        str += "] " +  this.description;

        //deadline
        str += "(by: " + this.deadline + ")";
        return str;
    }
}
