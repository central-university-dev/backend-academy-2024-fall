package backend.academy.seminar5.project.service;

import java.util.Arrays;

public class AuthorService {
    private static Author[] authorsArray = new Author[] {
        new Author(
            1L,
            "J.R.R. Tolkien",
            "British writer and academic"
        ),
        new Author(
            2L,
            "J.K. Rowling",
            "British author, philanthropist"
        )
    };

    public static void main(String[] args) {
        AuthorService authorService = new AuthorService();
        System.out.println("add new author");
        authorService.addNewAuthor();

        System.out.println("remove last author");
        authorService.removeLastAuthor();
    }

    void addNewAuthor() {
        //AUTHORS_LIST[2] = author; ОШИБКА! в коллекции 2 элемента (0 и 1)

        Author[] savedList = authorsArray;
        authorsArray = Arrays.copyOf(savedList, savedList.length + 1);

        Author author = new Author(11, "name", "bio");
        authorsArray[savedList.length] = author;

        printAuthors();
    }

    void removeLastAuthor() {
        authorsArray[authorsArray.length - 1] = null;
        Author[] savedList = authorsArray;
        authorsArray = Arrays.copyOf(savedList, savedList.length - 1);

        printAuthors();
    }

    Author removeAuthorByName(String authorName) {
        //todo:
        return null;
    }

    private void printAuthors() {
        for (Author author : authorsArray) {
            System.out.println(author);
        }
    }
}
