package bob.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import bob.exception.BobException;

/**
 * Tests task operations performed by {@link TaskList}.
 */
class TaskListTest {
    /**
     * Tests that a valid one-based index marks and returns the selected task.
     *
     * @throws BobException If the valid index is unexpectedly rejected.
     */
    @Test
    void markTask_validOneBasedIndex_selectedTaskMarkedAndReturned() throws BobException {
        Task firstTask = new ToDo("read book");
        Task secondTask = new ToDo("write report");
        TaskList taskList = new TaskList(List.of(firstTask, secondTask));

        Task markedTask = taskList.markTask(2);

        assertFalse(firstTask.isDone());
        assertTrue(secondTask.isDone());
        assertSame(secondTask, markedTask);
    }

    /**
     * Tests that an index below the valid range is rejected.
     */
    @Test
    void markTask_indexBelowValidRange_exceptionThrown() {
        TaskList taskList = new TaskList(List.of(new ToDo("read book")));

        assertThrows(BobException.class, () -> taskList.markTask(0));
    }

    /**
     * Tests that an index above the valid range is rejected.
     */
    @Test
    void markTask_indexAboveValidRange_exceptionThrown() {
        TaskList taskList = new TaskList(List.of(new ToDo("read book")));

        assertThrows(BobException.class, () -> taskList.markTask(2));
    }

    /**
     * Tests that deleting a valid index removes only the selected task.
     *
     * @throws BobException If the valid index is unexpectedly rejected.
     */
    @Test
    void deleteTask_validIndex_selectedTaskDeleted() throws BobException {
        Task task1 = new ToDo("eat food");
        Task task2 = new ToDo("eat food again");
        TaskList taskList = new TaskList(List.of(task1, task2));

        taskList.deleteTask(2);

        assertEquals(1, taskList.getTasks().size());
        assertSame(task1, taskList.getTasks().get(0));
    }
}
