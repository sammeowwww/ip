package bob.task;

import java.util.List;

import org.junit.jupiter.api.Test;

import bob.exception.BobException;

import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {
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

    @Test
    void markTask_indexBelowValidRange_exceptionThrown() {
        TaskList taskList = new TaskList(List.of(new ToDo("read book")));

        assertThrows(BobException.class, () -> taskList.markTask(0));
    }

    @Test
    void markTask_indexAboveValidRange_exceptionThrown() {
        TaskList taskList = new TaskList(List.of(new ToDo("read book")));

        assertThrows(BobException.class, () -> taskList.markTask(2));
    }

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
