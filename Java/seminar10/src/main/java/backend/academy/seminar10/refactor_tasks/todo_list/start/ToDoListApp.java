package backend.academy.seminar10.refactor_tasks.todo_list.start;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Этот проект представляет собой простую консольную программу для управления списком задач (To-Do List).
 * Проект предоставляет базовый функционал для добавления, удаления и отображения задач
 */
public class ToDoListApp {
    private static ArrayList<String> tasks = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("1. Add Task");
            System.out.println("2. Remove Task");
            System.out.println("3. View Tasks");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (choice == 1) {
                System.out.print("Enter task description: ");
                String task = scanner.nextLine();
                tasks.add(task);
                System.out.println("Task added.\n");
            } else if (choice == 2) {
                System.out.println("Enter the index of the task to remove: ");
                int index = scanner.nextInt();
                if (index >= 0 && index < tasks.size()) {
                    tasks.remove(index);
                    System.out.println("Task removed.\n");
                } else {
                    System.out.println("Invalid index.\n");
                }
                scanner.nextLine(); // consume newline
            } else if (choice == 3) {
                System.out.println("Your tasks:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + ". " + tasks.get(i));
                }
                System.out.println();
            } else if (choice == 4) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid option.\n");
            }
        }
    }
}


/*

Проблемы кода:

- Глобальные переменные: Поля tasks и scanner статичны, что не очень хорошо с точки зрения дизайна.

- Метод main слишком большой: Основная логика программы находится в методе main,
 что делает его сложным для понимания и тестирования.

- Отсутствие разделения ответственности: Код не соблюдает принцип единственной ответственности (SRP),
 так как управление задачами, ввод данных и логика приложения смешаны.

- Плохая обработка ошибок: Код предполагает, что пользователь всегда вводит корректные данные.




Идеи для рефакторинга
- Создать отдельный класс для управления задачами.
- Создать отдельный класс для пользовательского интерфейса.
- Ввести обработку исключений для некорректного ввода.
- Разделить логику программы на методы для повышения читаемости.
- Сделать код более тестируемым, убрав статические зависимости.

Создайте класс TaskManager для управления задачами.
Создайте класс UserInterface для обработки пользовательского ввода и вывода.
Перенесите логику управления в отдельные методы.

 */
