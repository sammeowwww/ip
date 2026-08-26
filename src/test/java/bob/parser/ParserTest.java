package bob.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import bob.exception.BobException;

/**
 * Tests command parsing performed by {@link Parser}.
 */
class ParserTest {
    private final Parser parser = new Parser();

    /**
     * Tests that a command with an argument is separated correctly.
     *
     * @throws BobException If the valid command is unexpectedly rejected.
     */
    @Test
    void parse_commandWithArgument_commandWordAndArgumentReturned() throws BobException {
        ParsedCommand command = parser.parse("todo read book");

        assertEquals("todo", command.getCommandWord());
        assertEquals("read book", command.getArgument());
    }

    /**
     * Tests that a command without an argument produces an empty argument.
     *
     * @throws BobException If the valid command is unexpectedly rejected.
     */
    @Test
    void parse_commandWithoutArgument_emptyArgumentReturned() throws BobException {
        ParsedCommand command = parser.parse("list");

        assertEquals("list", command.getCommandWord());
        assertEquals("", command.getArgument());
    }

    /**
     * Tests that surrounding and separating whitespace is normalized.
     *
     * @throws BobException If the valid command is unexpectedly rejected.
     */
    @Test
    void parse_commandWithExtraWhitespace_whitespaceNormalized() throws BobException {
        ParsedCommand command = parser.parse("  deadline   return book /by 2026-09-01  ");

        assertEquals("deadline", command.getCommandWord());
        assertEquals("return book /by 2026-09-01", command.getArgument());
    }

    /**
     * Tests that blank user input is rejected.
     */
    @Test
    void parse_blankInput_exceptionThrown() {
        assertThrows(BobException.class, () -> parser.parse("   "));
    }
}
