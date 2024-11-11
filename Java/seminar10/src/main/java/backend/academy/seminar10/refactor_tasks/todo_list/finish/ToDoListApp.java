package backend.academy.seminar10.refactor_tasks.todo_list.finish;

public class ToDoListApp {
    public static void main(String[] args) {
        TaskManager taskManager = new InMemoryTaskManager();
        UserInterface ui = new UserInterface(taskManager, System.in);
        ui.start();
    }
}
