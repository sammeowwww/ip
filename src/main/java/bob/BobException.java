package bob;

/**
 * Represents an error caused by an invalid command, task operation, or data file.
 */
public class BobException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a Bob-specific exception with an explanatory message.
     *
     * @param message Explanation of the error.
     */
    public BobException(String message) {
        super(message);
    }
}
