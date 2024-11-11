package backend.academy.seminar10.refactor_tasks.todo_list.finish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final ArrayList<String> tasks = new ArrayList<>();

    public void addTask(String task) {
        tasks.add(task);
    }

    public void removeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
        } else {
            throw new IllegalArgumentException("Task index out of bounds");
        }
    }

    public List<String> getTasks() {
        return Collections.unmodifiableList(tasks);
    }
}
