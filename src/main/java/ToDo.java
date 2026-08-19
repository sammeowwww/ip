public class ToDo extends Task {
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        String str = "[T][";
        if (this.done) {
            str += "X";
        }
        str += "] " +  this.description;
        return str;
    }
}
