import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private static final int MAX = 100;
    private ArrayList<Task> list;
    private int count;

    public TaskList() {
        this.list = new ArrayList<>();
        this.count = 0;
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param tasks Initial tasks.
     */
    public TaskList(List<Task> tasks) {
        this.list = new ArrayList<>(tasks);
        this.count = tasks.size();
    }

    public void addTask(Task task) throws BobException{
        if (count >= MAX) {
            throw new BobException("Task list is full :( delete tasks to add more!");
        }
        list.add(task);
        count++;
    }

    public void markTask(int index) throws BobException {
        if (index < 0 || index > count) {
            throw new BobException("Please enter a valid index.");
        }
        Task task = list.get(index - 1);
        task.markTask();

        System.out.println("        " + task.toString());
        Bob.printLine();
    }

    public void unmarkTask(int index) throws BobException {
        if (index < 0 || index > count) {
            throw new BobException("Please enter a valid index.");
        }
        Task task = list.get(index - 1);
        task.unmarkTask();

        System.out.println("        " + task.toString());
        Bob.printLine();
    }

    public int getTaskCount() {
        return count;
    }

    /**
     * Returns a copy of the tasks in this list.
     *
     * @return Copy of the current tasks.
     */
    public List<Task> getTasks() {
        return new ArrayList<>(list);
    }

    public void printTasks() {
        if (count == 0) {
            System.out.println("        Your list is empty :(... Add more tasks!");
            Bob.printLine();
        } else {
            System.out.println("        Gotcha!! Here are your tasks:");
            for (int i = 1; i <= count; i++) {
                System.out.println("        " + i + ". " + list.get(i -1).toString());
            }
            Bob.printLine();
        }
    }

    public void deleteTask(int index) throws BobException {
        if (index < 0 || index > count) {
            throw new BobException("Please enter a valid index.");
        }
        list.remove(index - 1);
        count--;
    }
}
