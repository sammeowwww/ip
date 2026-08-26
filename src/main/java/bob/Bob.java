package bob;

import java.nio.file.Path;

/**
 * Runs Bob, a command-line task management assistant.
 */
public class Bob {
    private final Ui ui;
    private final Parser parser;
    private final Storage storage;
    private final TaskList taskList;

    /**
     * Creates Bob using the specified task data file.
     *
     * @param filePath Path of the task data file.
     */
    public Bob(Path filePath) {
        this.ui = new Ui();
        this.parser = new Parser();
        this.storage = new Storage(filePath);

        ui.showWelcome();

        TaskList loadedTaskList;
        try {
            loadedTaskList = new TaskList(storage.loadTasks());
        } catch (BobException exception) {
            ui.showLoadingError(exception.getMessage());
            loadedTaskList = new TaskList();
        }
        this.taskList = loadedTaskList;
    }

    /**
     * Runs the command loop until the user exits Bob.
     */
    public void run() {
        boolean shouldExit = false;
        while (!shouldExit) {
            try {
                ParsedCommand command = parser.parse(ui.readCommand());
                shouldExit = executeCommand(command);
            } catch (BobException exception) {
                ui.showError(exception.getMessage());
            }
        }
        ui.close();
    }

    private boolean executeCommand(ParsedCommand command) throws BobException {
        switch (command.getCommandWord()) {
            case "help" -> ui.showHelp();
            case "bye" -> {
                ui.showBye();
                return true;
            }
            case "list" -> ui.showTasks(taskList);
            case "unmark" -> {
                Task task = taskList.unmarkTask(parser.parseIndex(command));
                ui.showUnmarkedTask(task);
                storage.saveTasks(taskList.getTasks());
            }
            case "mark" -> {
                Task task = taskList.markTask(parser.parseIndex(command));
                ui.showMarkedTask(task);
                storage.saveTasks(taskList.getTasks());
            }
            case "todo", "deadline", "event" -> {
                Task task = parser.parseTask(command);
                taskList.addTask(task);
                ui.showAddedTask(taskList, task);
                storage.saveTasks(taskList.getTasks());
            }
            case "delete" -> {
                taskList.deleteTask(parser.parseIndex(command));
                ui.showDeletedTask(taskList);
                storage.saveTasks(taskList.getTasks());
            }
            default -> throw new BobException("Invalid command :(. If you need help, type 'help'!!\n"
                    + "        Bob will be on his wayyyy.");
        }
        return false;
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
