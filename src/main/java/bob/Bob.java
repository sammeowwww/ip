package bob;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Runs Bob, a command-line task management assistant.
 */
public class Bob {
    static void printLine() {
        System.out.println("        ___________________________________________________________________");
    }

    private static void printBanner() {
        System.out.println("        ____        _     ");
        System.out.println("        |  _ \\      | |    ");
        System.out.println("        | |_) | ___ | |__  ");
        System.out.println("        |  _ < / _ \\| '_ \\ ");
        System.out.println("        | |_) | (_) | |_) |");
        System.out.println("        |____/ \\___/|_.__/ ");
    }

    private static void printWelcome() {
        printLine();
        printBanner();
        printLine();
        System.out.println("        Hey!! I'm Mr Bob, your friendly and personalised assistant.");
        System.out.println("        Anyways, what can i do for you today?");
        printLine();
    }

    private static void printAddedTask(TaskList taskList, Task task) {
        System.out.println("        Nice!! I've got your task and have added it into the list.");
        System.out.println("        " + task);
        System.out.println("        You have " + taskList.getTaskCount() + " tasks.");
        printLine();
    }

    private static void printBye() {
        System.out.println("        Bye! See you later alligator!");
    }

    private static void printHelp() {
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

    // Used Perplexity to help refine this code.
    private static void addToDo(TaskList taskList, String argument) throws BobException {
        if (argument.isEmpty()) {
            throw new BobException("You need to enter a task name!!");
        }

        Task task = new ToDo(argument);
        taskList.addTask(task);
        printAddedTask(taskList, task);
    }

    // Used Perplexity to help refine this code.
    private static void addDeadline(TaskList taskList, String argument) throws BobException {
        if (argument.isEmpty()) {
            throw new BobException("You need to enter a task name!!");
        }

        String[] deadlineParts = argument.split("\\s+/by\\s+", 2);

        if (deadlineParts.length < 2
                || deadlineParts[0].trim().isEmpty()
                || deadlineParts[1].trim().isEmpty()) {
            throw new BobException("Use: deadline <description> /by <yyyy-MM-dd>.");
        }

        try {
            LocalDate deadline = LocalDate.parse(deadlineParts[1].trim());
            Task task = new Deadline(deadlineParts[0].trim(), deadline);
            taskList.addTask(task);
            printAddedTask(taskList, task);
        } catch (DateTimeParseException exception) {
            throw new BobException("Please enter the deadline date as yyyy-MM-dd.");
        }
    }

    // Used Perplexity to help refine this code.
    private static void addEvent(TaskList taskList, String argument) throws BobException {
        if (argument.isEmpty()) {
            throw new BobException("You need to enter a task name!!");
        }

        String[] fromParts = argument.split("\\s+/from\\s+", 2);

        if (fromParts.length < 2) {
            throw new BobException("Use: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.");
        }

        String[] toParts = fromParts[1].split("\\s+/to\\s+", 2);

        if (fromParts[0].trim().isEmpty()
                || toParts.length < 2
                || toParts[0].trim().isEmpty()
                || toParts[1].trim().isEmpty()) {
            throw new BobException("Use: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.");
        }

        try {
            LocalDate startDate = LocalDate.parse(toParts[0].trim());
            LocalDate endDate = LocalDate.parse(toParts[1].trim());
            Task task = new Event(fromParts[0].trim(), startDate, endDate);
            taskList.addTask(task);
            printAddedTask(taskList, task);
        } catch (DateTimeParseException exception) {
            throw new BobException("Please enter the event dates as yyyy-MM-dd.");
        }
    }

    private static void unmarkTask(TaskList taskList, String argument) throws BobException {
        if (argument.isEmpty()) {
            throw new BobException("Use: unmark <task index>.");
        }

        try {
            int index = Integer.parseInt(argument);
            taskList.unmarkTask(index);
            System.out.println("        I have unmarked the task. Please complete it.");
            printLine();
        } catch (NumberFormatException exception) {
            throw new BobException("Use: unmark <task index>.");
        }
    }

    private static void markTask(TaskList taskList, String argument) throws BobException {
        if (argument.isEmpty()) {
            throw new BobException("Use: mark <task index>.");
        }

        try {
            int index = Integer.parseInt(argument);
            taskList.markTask(index);
            System.out.println("        I have marked the task. You're good to go!");
            printLine();
        } catch (NumberFormatException exception) {
            throw new BobException("Use: mark <task index>.");
        }
    }

    private static void deleteTask(TaskList taskList, String argument) throws BobException {
        if (argument.isEmpty()) {
            throw new BobException("Use: delete <task index>.");
        }

        try {
            int index = Integer.parseInt(argument);
            taskList.deleteTask(index);
            System.out.println("        I have deleted the task. You're good to go!");
            System.out.println("        You have " + taskList.getTaskCount() + " tasks.");
            printLine();
        } catch (NumberFormatException exception) {
            throw new BobException("Use: delete <task index>.");
        }
    }

    /**
     * Starts Bob and processes commands entered through standard input.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Storage storage = new Storage(Path.of("data", "bob.txt"));
        TaskList taskList;

        // Print the welcome message.
        printWelcome();

        try {
            taskList = new TaskList(storage.loadTasks());
        } catch (BobException exception) {
            System.out.println("        " + exception.getMessage());
            System.out.println("        Starting with an empty task list.");
            printLine();
            taskList = new TaskList();
        }

        while (true) {
            try {
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    throw new BobException("Enter a command! "
                            + "If you need a list of commands, please type 'help'. ");
                }

                String[] parts = input.split("\\s+", 2);
                String command = parts[0];
                String argument = parts.length > 1 ? parts[1] : "";

                switch (command) {
                    case "help":
                        printHelp();
                        break;

                    case "bye":
                        scanner.close();
                        printBye();
                        printLine();
                        return;

                    case "list":
                        taskList.printTasks();
                        break;

                    case "unmark":
                        unmarkTask(taskList, argument);
                        storage.saveTasks(taskList.getTasks());
                        break;

                    case "mark":
                        markTask(taskList, argument);
                        storage.saveTasks(taskList.getTasks());
                        break;

                    case "todo":
                        addToDo(taskList, argument);
                        storage.saveTasks(taskList.getTasks());
                        break;

                    case "deadline":
                        addDeadline(taskList, argument);
                        storage.saveTasks(taskList.getTasks());
                        break;

                    case "event":
                        addEvent(taskList, argument);
                        storage.saveTasks(taskList.getTasks());
                        break;

                    case "delete":
                        deleteTask(taskList, argument);
                        storage.saveTasks(taskList.getTasks());
                        break;

                    default:
                        throw new BobException("Invalid command :(. If you need help, type 'help'!!\n"
                                + "        Bob will be on his wayyyy.");
                }
            } catch (BobException exception) {
                System.out.println("        " + exception.getMessage());
                printLine();
            }
        }
    }
}
