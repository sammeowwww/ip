package bob;

import java.nio.file.Path;

/**
 * Runs Bob, a command-line task management assistant.
 */
public class Bob {
    /**
     * Starts Bob and processes commands entered through standard input.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Parser parser = new Parser();
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
                ParsedCommand command = parser.parse(ui.readCommand());

                switch (command.getCommandWord()) {
                    case "help" ->
                        ui.showHelp();
                    case "bye" -> {
                        ui.showBye();
                        ui.close();
                        return;
                    }
                    case "list" ->
                        ui.showTasks(taskList);
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
                    default ->
                        throw new BobException("Invalid command :(. If you need help, type 'help'!!\n"
                                + "        Bob will be on his wayyyy.");
                }
            } catch (BobException exception) {
                ui.showError(exception.getMessage());
            }
        }
    }
}
