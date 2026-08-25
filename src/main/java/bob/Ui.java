package bob;

import java.util.List;
import java.util.Scanner;

/**
 * Handles interactions between Bob and the user.
 */
public class Ui {
    private final Scanner scanner;

    /**
     * Creates a user interface that reads from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return Trimmed command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Shows Bob's welcome message.
     */
    public void showWelcome() {
        printLine();
        printBanner();
        printLine();
        System.out.println("        Hey!! I'm Mr Bob, your friendly and personalised assistant.");
        System.out.println("        Anyways, what can i do for you today?");
        printLine();
    }

    /**
     * Shows confirmation that a task was added.
     *
     * @param taskList Task list containing the added task.
     * @param task Task that was added.
     */
    public void showAddedTask(TaskList taskList, Task task) {
        System.out.println("        Nice!! I've got your task and have added it into the list.");
        System.out.println("        " + task);
        System.out.println("        You have " + taskList.getTaskCount() + " tasks.");
        printLine();
    }

    /**
     * Shows Bob's farewell message.
     */
    public void showBye() {
        System.out.println("        Bye! See you later alligator!");
        printLine();
    }

    /**
     * Shows the commands supported by Bob.
     */
    public void showHelp() {
        System.out.println("        HEEELPPP is on the way!");
        System.out.println("        Here is a list of commands you'll need for this chatbot");
        System.out.println("        1. todo <task>\n"
                + "        2. deadline <task> /by <yyyy-MM-dd>\n"
                + "        3. event <task> /from <yyyy-MM-dd> /to <yyyy-MM-dd>\n"
                + "        4. list\n"
                + "        5. mark <task index>\n"
                + "        6. unmark <task index>\n"
                + "        7. delete <task index>\n"
                + "        8. bye");
        printLine();
    }

    /**
     * Shows all tasks in their displayed order.
     *
     * @param taskList Task list to display.
     */
    public void showTasks(TaskList taskList) {
        List<Task> tasks = taskList.getTasks();

        if (tasks.isEmpty()) {
            System.out.println("        Your list is empty :(... Add more tasks!");
        } else {
            System.out.println("        Gotcha!! Here are your tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("        " + (i + 1) + ". " + tasks.get(i));
            }
        }
        printLine();
    }

    /**
     * Shows confirmation that a task was marked as completed.
     *
     * @param task Task that was marked.
     */
    public void showMarkedTask(Task task) {
        System.out.println("        " + task);
        printLine();
        System.out.println("        I have marked the task. You're good to go!");
        printLine();
    }

    /**
     * Shows confirmation that a task was marked as incomplete.
     *
     * @param task Task that was unmarked.
     */
    public void showUnmarkedTask(Task task) {
        System.out.println("        " + task);
        printLine();
        System.out.println("        I have unmarked the task. Please complete it.");
        printLine();
    }

    /**
     * Shows confirmation that a task was deleted.
     *
     * @param taskList Task list after the deletion.
     */
    public void showDeletedTask(TaskList taskList) {
        System.out.println("        I have deleted the task. You're good to go!");
        System.out.println("        You have " + taskList.getTaskCount() + " tasks.");
        printLine();
    }

    /**
     * Shows an error message.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        System.out.println("        " + message);
        printLine();
    }

    /**
     * Shows a loading error and explains that Bob will use an empty task list.
     *
     * @param message Loading error message to display.
     */
    public void showLoadingError(String message) {
        System.out.println("        " + message);
        System.out.println("        Starting with an empty task list.");
        printLine();
    }

    /**
     * Closes the input scanner used by this user interface.
     */
    public void close() {
        scanner.close();
    }

    private void printLine() {
        System.out.println("        ___________________________________________________________________");
    }

    private void printBanner() {
        System.out.println("        ____        _     ");
        System.out.println("        |  _ \\      | |    ");
        System.out.println("        | |_) | ___ | |__  ");
        System.out.println("        |  _ < / _ \\| '_ \\ ");
        System.out.println("        | |_) | (_) | |_) |");
        System.out.println("        |____/ \\___/|_.__/ ");
    }
}
