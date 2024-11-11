package backend.academy.seminar10.refactor_tasks.todo_list.finish;

import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private final TaskManager taskManager;
    private final Scanner scanner;

    public UserInterface(TaskManager taskManager, InputStream inputStream) {
        this.taskManager = taskManager;
        this.scanner = new Scanner(inputStream);
    }

    public void start() {
        try {
            startDialog();
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void startDialog() {
        while (true) {
            printMenu();
            int choice = getUserChoice();
            if (choice == 1) {
                addTask();
            } else if (choice == 2) {
                removeTask();
            } else if (choice == 3) {
                viewTasks();
            } else if (choice == 4) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid option.\n");
            }
        }
    }

    private void printMenu() {
        System.out.println("1. Add Task");
        System.out.println("2. Remove Task");
        System.out.println("3. View Tasks");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");
    }

    private int getUserChoice() {
        return scanner.nextInt();
    }

    private void addTask() {
        System.out.print("Enter task description: ");
        scanner.nextLine(); // consume newline
        String task = scanner.nextLine();
        taskManager.addTask(task);
        System.out.println("Task added.\n");
    }

    private void removeTask() {
        System.out.print("Enter the index of the task to remove: ");
        int index = scanner.nextInt() - 1;
        taskManager.removeTask(index);
        System.out.println("Task removed.\n");
    }

    private void viewTasks() {
        List<String> tasks = taskManager.getTasks();
        System.out.println("Your tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        System.out.println();
    }
}
