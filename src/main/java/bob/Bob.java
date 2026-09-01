package bob;

import java.nio.file.Path;
import java.util.List;

import bob.exception.BobException;
import bob.parser.ParsedCommand;
import bob.parser.Parser;
import bob.storage.Storage;
import bob.task.Task;
import bob.task.TaskList;
import bob.ui.Ui;

/**
 * Runs Bob, a task management assistant.
 */
public class Bob {
    private static final String WELCOME_MESSAGE =
            "Hey! I'm Bob, your friendly personal assistant.\nWhat can I do for you today?";

    private final Ui ui;
    private final Parser parser;
    private final Storage storage;
    private final TaskList taskList;
    private final String startupMessage;

    /**
     * Creates Bob using the specified task data file.
     *
     * @param filePath Path of the task data file.
     */
    public Bob(Path filePath) {
        this.ui = new Ui();
        this.parser = new Parser();
        this.storage = new Storage(filePath);

        TaskList loadedTaskList;
        String loadingMessage = WELCOME_MESSAGE;
        try {
            loadedTaskList = new TaskList(storage.loadTasks());
        } catch (BobException exception) {
            loadingMessage += "\n\n" + exception.getMessage()
                    + "\nStarting with an empty task list.";
            loadedTaskList = new TaskList();
        }
        this.taskList = loadedTaskList;
        this.startupMessage = loadingMessage;
    }

    /**
     * Runs the command-line loop until the user exits Bob.
     */
    public void run() {
        ui.showWelcome(startupMessage);

        boolean shouldExit = false;
        while (!shouldExit) {
            String input = ui.readCommand();
            ui.showResponse(getResponse(input));
            shouldExit = input.trim().equals("bye");
        }
        ui.close();
    }

    /**
     * Executes a user command and returns Bob's response.
     *
     * @param input User command to execute.
     * @return Response to display to the user.
     */
    public String getResponse(String input) {
        try {
            ParsedCommand command = parser.parse(input);
            return executeCommand(command);
        } catch (BobException exception) {
            return exception.getMessage();
        }
    }

    /**
     * Returns the message shown when Bob starts.
     *
     * @return Welcome message, including any loading warning.
     */
    public String getStartupMessage() {
        return startupMessage;
    }

    private String executeCommand(ParsedCommand command) throws BobException {
        switch (command.getCommandWord()) {
            case "help":
                return getHelpMessage();
            case "bye":
                return "Bye! See you later, alligator!";
            case "list":
                return getTaskListMessage();
            case "find":
                return getMatchingTasksMessage(parser.parseKeyword(command));
            case "unmark": {
                Task task = taskList.unmarkTask(parser.parseTaskNumber(command));
                storage.saveTasks(taskList.getTasks());
                return task + "\nI have marked the task as incomplete. Please complete it.";
            }
            case "mark": {
                Task task = taskList.markTask(parser.parseTaskNumber(command));
                storage.saveTasks(taskList.getTasks());
                return task + "\nI have marked the task as complete. You're good to go!";
            }
            case "todo":
            case "deadline":
            case "event": {
                Task task = parser.parseTask(command);
                taskList.addTask(task);
                storage.saveTasks(taskList.getTasks());
                return "Nice! I've added this task:\n" + task
                        + "\nYou now have " + taskList.getTaskCount() + " tasks.";
            }
            case "delete":
                taskList.deleteTask(parser.parseTaskNumber(command));
                storage.saveTasks(taskList.getTasks());
                return "I have deleted the task.\nYou now have "
                        + taskList.getTaskCount() + " tasks.";
            default:
                throw new BobException("Invalid command :( If you need help, type 'help'.");
        }
    }

    private String getHelpMessage() {
        return "Bob is to the rescue!!!\n"
                + "Here are the commands you can use:\n"
                + "1. todo <task>\n"
                + "2. deadline <task> /by <yyyy-MM-dd>\n"
                + "3. event <task> /from <yyyy-MM-dd> /to <yyyy-MM-dd>\n"
                + "4. list\n"
                + "5. mark <task index>\n"
                + "6. unmark <task index>\n"
                + "7. delete <task index>\n"
                + "8. find <keyword>\n"
                + "9. bye";
    }

    private String getTaskListMessage() {
        List<Task> tasks = taskList.getTasks();
        if (tasks.isEmpty()) {
            return "Your list is empty. Add a task to get started!";
        }
        return formatTasks("Here are your tasks:", tasks);
    }

    private String getMatchingTasksMessage(String keyword) {
        List<Task> matchingTasks = taskList.findTasks(keyword);
        if (matchingTasks.isEmpty()) {
            return "No matching tasks found.";
        }
        return formatTasks("Here are the matching tasks:", matchingTasks);
    }

    private String formatTasks(String heading, List<Task> tasks) {
        StringBuilder response = new StringBuilder(heading);
        for (int i = 0; i < tasks.size(); i++) {
            response.append(System.lineSeparator())
                    .append(i + 1)
                    .append(". ")
                    .append(tasks.get(i));
        }
        return response.toString();
    }

    /**
     * Starts Bob using the default task data file.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Bob(Path.of("data", "bob.txt")).run();
    }
}
