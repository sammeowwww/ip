public class TaskList {
    public static final int MAX = 100;
    private Task[] list;
    private int count;

    public TaskList() {
        this.list = new Task[100];
        this.count = 0;
    }

    public void addTask(Task task) {
        if (count >= MAX) {
            System.out.println("        Too many tasks, delete tasks to add more!");
            return;
        }
        list[count] = task;
        count++;
    }

    public void markTask(int index) throws BobException {
        if (index < 0 || index > count) {
            throw new BobException("Please enter a valid index.");
        }
        Task task = list[index - 1];
        task.markTask();

        System.out.println("        " + task.toString());
        Bob.printLine();
    }

    public void unmarkTask(int index) throws BobException {
        if (index < 0 || index > count) {
            throw new BobException("Please enter a valid index.");
        }
        Task task = list[index - 1];
        task.unmarkTask();

        System.out.println("        " + task.toString());
        Bob.printLine();
    }

    public int getTaskCount() {
        return count;
    }

    public void printTasks() {
        if (count == 0) {
            System.out.println("        Your list is empty");
            Bob.printLine();
        } else {
            for (int i = 1; i <= count; i++) {
                System.out.println("        " + i + ". " + list[i - 1].toString());
            }
            Bob.printLine();
        }
    }
}
