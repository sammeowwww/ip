package bob.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
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

    /**
     * Tests that partial, case-insensitive matches are returned in task order.
     */
    @Test
    void findTasks_matchingDescriptions_matchingTasksReturnedInOrder() {
        Task firstTask = new ToDo("Read Book");
        Task secondTask = new ToDo("buy bookmark");
        Task nonMatchingTask = new ToDo("write report");
        TaskList taskList = new TaskList(List.of(firstTask, secondTask, nonMatchingTask));

        List<Task> matchingTasks = taskList.findTasks("BOOK");

        assertEquals(List.of(firstTask, secondTask), matchingTasks);
    }

    /**
     * Tests that no matches produces an empty result.
     */
    @Test
    void findTasks_noMatchingDescription_emptyListReturned() {
        TaskList taskList = new TaskList(List.of(new ToDo("write report")));

        assertTrue(taskList.findTasks("book").isEmpty());
    }

    /**
     * Tests that searching an empty task list produces an empty result.
     */
    @Test
    void findTasks_emptyTaskList_emptyListReturned() {
        TaskList taskList = new TaskList();

        assertTrue(taskList.findTasks("book").isEmpty());
    }

    /**
     * Tests that task dates are excluded from keyword matching.
     */
    @Test
    void findTasks_keywordOnlyInTaskDate_emptyListReturned() {
        Task deadline = new Deadline("return notes", LocalDate.of(2026, 9, 1));
        TaskList taskList = new TaskList(List.of(deadline));

        assertTrue(taskList.findTasks("2026").isEmpty());
    }
}
