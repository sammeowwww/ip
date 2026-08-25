package bob;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Runs Bob, a command-line task management assistant.
 */
public class Bob {
    // Used Perplexity to help refine this code.
    private static void addToDo(TaskList taskList, Ui ui, String argument) throws BobException {
        if (argument.isEmpty()) {
            throw new BobException("You need to enter a task name!!");
        }

        Task task = new ToDo(argument);
        taskList.addTask(task);
        ui.showAddedTask(taskList, task);
    }

    // Used Perplexity to help refine this code.
    private static void addDeadline(TaskList taskList, Ui ui, String argument) throws BobException {
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
            ui.showAddedTask(taskList, task);
        } catch (DateTimeParseException exception) {
            throw new BobException("Please enter the deadline date as yyyy-MM-dd.");
        }
    }

    // Used Perplexity to help refine this code.
    private static void addEvent(TaskList taskList, Ui ui, String argument) throws BobException {
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
            ui.showAddedTask(taskList, task);
        } catch (DateTimeParseException exception) {
            throw new BobException("Please enter the event dates as yyyy-MM-dd.");
        }
    }

    private static void unmarkTask(TaskList taskList, Ui ui, String argument) throws BobException {
        if (argument.isEmpty()) {
            throw new BobException("Use: unmark <task index>.");
        }

        try {
            int index = Integer.parseInt(argument);
            Task task = taskList.unmarkTask(index);
            ui.showUnmarkedTask(task);
        } catch (NumberFormatException exception) {
            throw new BobException("Use: unmark <task index>.");
        }
    }

    private static void markTask(TaskList taskList, Ui ui, String argument) throws BobException {
        if (argument.isEmpty()) {
            throw new BobException("Use: mark <task index>.");
        }

        try {
            int index = Integer.parseInt(argument);
            Task task = taskList.markTask(index);
            ui.showMarkedTask(task);
        } catch (NumberFormatException exception) {
            throw new BobException("Use: mark <task index>.");
        }
    }

    private static void deleteTask(TaskList taskList, Ui ui, String argument) throws BobException {
        if (argument.isEmpty()) {
            throw new BobException("Use: delete <task index>.");
        }

        try {
            int index = Integer.parseInt(argument);
            taskList.deleteTask(index);
            ui.showDeletedTask(taskList);
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
        Ui ui = new Ui();
        Storage storage = new Storage(Path.of("data", "bob.txt"));
        TaskList taskList;

        ui.showWelcome();

        try {
            taskList = new TaskList(storage.loadTasks());
        } catch (BobException exception) {
            ui.showLoadingError(exception.getMessage());
            taskList = new TaskList();
        }

        while (true) {
            try {
                String input = ui.readCommand();

                if (input.isEmpty()) {
                    throw new BobException("Enter a command! "
                            + "If you need a list of commands, please type 'help'. ");
                }

                String[] parts = input.split("\\s+", 2);
                String command = parts[0];
                String argument = parts.length > 1 ? parts[1] : "";

                switch (command) {
                    case "help":
                        ui.showHelp();
                        break;

                    case "bye":
                        ui.showBye();
                        ui.close();
                        return;

                    case "list":
                        ui.showTasks(taskList);
                        break;

                    case "unmark":
                        unmarkTask(taskList, ui, argument);
                        storage.saveTasks(taskList.getTasks());
                        break;

                    case "mark":
                        markTask(taskList, ui, argument);
                        storage.saveTasks(taskList.getTasks());
                        break;

                    case "todo":
                        addToDo(taskList, ui, argument);
                        storage.saveTasks(taskList.getTasks());
                        break;

                    case "deadline":
                        addDeadline(taskList, ui, argument);
                        storage.saveTasks(taskList.getTasks());
                        break;

                    case "event":
                        addEvent(taskList, ui, argument);
                        storage.saveTasks(taskList.getTasks());
                        break;

                    case "delete":
                        deleteTask(taskList, ui, argument);
                        storage.saveTasks(taskList.getTasks());
                        break;

                    default:
                        throw new BobException("Invalid command :(. If you need help, type 'help'!!\n"
                                + "        Bob will be on his wayyyy.");
                }
            } catch (BobException exception) {
                ui.showError(exception.getMessage());
            }
        }
    }
}
