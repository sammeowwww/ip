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
            String userCommand = ui.readCommand();
            ui.showResponse(executeUserCommand(userCommand));
            shouldExit = userCommand.trim().equals("bye");
        }
        ui.close();
    }

    /**
     * Executes a user command and returns Bob's response.
     *
     * @param userCommand User command to execute.
     * @return Response to display to the user.
     */
    public String executeUserCommand(String userCommand) {
        try {
            ParsedCommand parsedCommand = parser.parse(userCommand);
            return executeParsedCommand(parsedCommand);
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

    /**
     * Executes a parsed command and returns the response for the user.
     *
     * @param parsedCommand Parsed command to execute.
     * @return Response produced by executing the command.
     * @throws BobException If the command or its task operation is invalid.
     */
    private String executeParsedCommand(ParsedCommand parsedCommand) throws BobException {
        return switch (parsedCommand.getCommandWord()) {
            case "help" -> getHelpMessage();
            case "bye" -> "Bye! See you later, alligator!";
            case "list" -> getTaskListMessage();
            case "find" -> getMatchingTasksMessage(parser.parseKeyword(parsedCommand));
            case "unmark" -> {
                Task task = taskList.unmarkTask(parser.parseTaskNumber(parsedCommand));
                storage.saveTasks(taskList.getTasks());
                yield task + "\nI have marked the task as incomplete. Please complete it.";
            }
            case "mark" -> {
                Task task = taskList.markTask(parser.parseTaskNumber(parsedCommand));
                storage.saveTasks(taskList.getTasks());
                yield task + "\nI have marked the task as complete. You're good to go!";
            }
            case "todo", "deadline", "event" -> {
                Task task = parser.parseTask(parsedCommand);
                taskList.addTask(task);
                storage.saveTasks(taskList.getTasks());
                yield "Nice! I've added this task:\n" + task
                        + "\nYou now have " + taskList.getTaskCount() + " tasks.";
            }
            case "delete" -> {
                taskList.deleteTask(parser.parseTaskNumber(parsedCommand));
                storage.saveTasks(taskList.getTasks());
                yield "I have deleted the task.\nYou now have "
                        + taskList.getTaskCount() + " tasks.";
            }
            default -> throw new BobException("Invalid command :( If you need help, type 'help'.");
        };
    }

    /**
     * Returns instructions for every command supported by Bob.
     *
     * @return Help message containing the supported command formats.
     */
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

    /**
     * Returns a display message containing every task in the task list.
     *
     * @return Task-list message, or an explanation if the list is empty.
     */
    private String getTaskListMessage() {
        List<Task> tasks = taskList.getTasks();
        if (tasks.isEmpty()) {
            return "Your list is empty. Add a task to get started!";
        }
        return formatTasks("Here are your tasks:", tasks);
    }

    /**
     * Returns a display message containing tasks that match a keyword.
     *
     * @param keyword Keyword to find in task descriptions.
     * @return Matching-task message, or an explanation if no tasks match.
     */
    private String getMatchingTasksMessage(String keyword) {
        List<Task> matchingTasks = taskList.findTasks(keyword);
        if (matchingTasks.isEmpty()) {
            return "No matching tasks found.";
        }
        return formatTasks("Here are the matching tasks:", matchingTasks);
    }

    /**
     * Formats a heading and ordered collection of tasks as a display message.
     *
     * @param heading Heading to place before the tasks.
     * @param tasks Tasks to include in their displayed order.
     * @return Formatted message containing the heading and numbered tasks.
     */
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
