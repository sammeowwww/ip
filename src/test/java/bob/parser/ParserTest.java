package bob.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import bob.exception.BobException;

class ParserTest {
    private final Parser parser = new Parser();

    @Test
    void parse_commandWithArgument_commandWordAndArgumentReturned() throws BobException {
        ParsedCommand command = parser.parse("todo read book");

        assertEquals("todo", command.getCommandWord());
        assertEquals("read book", command.getArgument());
    }

    @Test
    void parse_commandWithoutArgument_emptyArgumentReturned() throws BobException {
        ParsedCommand command = parser.parse("list");

        assertEquals("list", command.getCommandWord());
        assertEquals("", command.getArgument());
    }

    @Test
    void parse_commandWithExtraWhitespace_whitespaceNormalized() throws BobException {
        ParsedCommand command = parser.parse("  deadline   return book /by 2026-09-01  ");

        assertEquals("deadline", command.getCommandWord());
        assertEquals("return book /by 2026-09-01", command.getArgument());
    }

    @Test
    void parse_blankInput_exceptionThrown() {
        assertThrows(BobException.class, () -> parser.parse("   "));
    }
}
