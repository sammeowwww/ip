package bob;

/**
 * Represents a command word and its associated argument.
 */
public class ParsedCommand {
    private final String commandWord;
    private final String argument;

    /**
     * Creates a parsed command.
     *
     * @param commandWord Word identifying the command to execute.
     * @param argument Argument supplied with the command.
     */
    public ParsedCommand(String commandWord, String argument) {
        this.commandWord = commandWord;
        this.argument = argument;
    }

    public String getCommandWord() {
        return commandWord;
    }

    public String getArgument() {
        return argument;
    }
}
