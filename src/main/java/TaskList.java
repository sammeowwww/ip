import java.util.ArrayList;

public class TaskList {
    public static final int MAX = 100;
    private ArrayList<Task> list;
    private int count;

    public TaskList() {
        this.list = new ArrayList<>();
        this.count = 0;
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
