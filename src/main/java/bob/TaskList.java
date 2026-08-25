package bob;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages the tasks known to Bob.
 */
public class TaskList {
    private static final int MAX_TASKS = 100;
    private final ArrayList<Task> tasks;
    private int taskCount;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        this.taskCount = 0;
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param tasks Initial tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
        this.taskCount = tasks.size();
    }

    /**
     * Adds a task to the list.
     *
     * @param task Task to add.
     * @throws BobException If the task list is full.
     */
    public void addTask(Task task) throws BobException {
        if (taskCount >= MAX_TASKS) {
            throw new BobException("Task list is full :( delete tasks to add more!");
        }
        tasks.add(task);
        taskCount++;
    }

    /**
     * Marks the task at the displayed one-based index as completed.
     *
     * @param index One-based index of the task.
     * @throws BobException If the index does not identify a task.
     */
    public void markTask(int index) throws BobException {
        if (index < 1 || index > taskCount) {
            throw new BobException("Please enter a valid index.");
        }
        Task task = tasks.get(index - 1);
        task.markTask();

        System.out.println("        " + task);
        Bob.printLine();
    }

    /**
     * Marks the task at the displayed one-based index as incomplete.
     *
     * @param index One-based index of the task.
     * @throws BobException If the index does not identify a task.
     */
    public void unmarkTask(int index) throws BobException {
        if (index < 1 || index > taskCount) {
            throw new BobException("Please enter a valid index.");
        }
        Task task = tasks.get(index - 1);
        task.unmarkTask();

        System.out.println("        " + task);
        Bob.printLine();
    }

    public int getTaskCount() {
        return taskCount;
    }

    /**
     * Returns a copy of the tasks in this list.
     *
     * @return Copy of the current tasks.
     */
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Prints all tasks in their displayed order.
     */
    public void printTasks() {
        if (taskCount == 0) {
            System.out.println("        Your list is empty :(... Add more tasks!");
            Bob.printLine();
        } else {
            System.out.println("        Gotcha!! Here are your tasks:");
            for (int i = 1; i <= taskCount; i++) {
                System.out.println("        " + i + ". " + tasks.get(i - 1));
            }
            Bob.printLine();
        }
    }

    /**
     * Deletes the task at the displayed one-based index.
     *
     * @param index One-based index of the task.
     * @throws BobException If the index does not identify a task.
     */
    public void deleteTask(int index) throws BobException {
        if (index < 1 || index > taskCount) {
            throw new BobException("Please enter a valid index.");
        }
        tasks.remove(index - 1);
        taskCount--;
    }
}
