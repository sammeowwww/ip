package bob;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import bob.task.Deadline;

/**
 * Tests user commands executed by {@link Bob}.
 */
class BobTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    void executeUserCommand_validEdit_taskUpdatedAndSaved() throws IOException {
        Path dataFile = temporaryDirectory.resolve("bob.txt");
        Bob bob = new Bob(dataFile);
        bob.executeUserCommand("deadline return book /by 2026-09-10");

        String response = bob.executeUserCommand("edit 1 by 2026-09-20");

        Deadline expectedDeadline = new Deadline("return book", LocalDate.of(2026, 9, 20));
        assertEquals("Nice! I've updated this task:\n" + expectedDeadline, response);
        assertEquals(
                List.of("D | 0 | return book | 2026-09-20"),
                Files.readAllLines(dataFile));
    }
}
