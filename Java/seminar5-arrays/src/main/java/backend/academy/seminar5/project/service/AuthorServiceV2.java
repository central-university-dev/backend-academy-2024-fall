package backend.academy.seminar5.project.service;

import java.util.ArrayList;
import java.util.List;

public class AuthorServiceV2 {
    private static final List<Author> AUTHORS_LIST = new ArrayList<>(List.of(
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
    ));

    public static void main(String[] args) {
        AuthorServiceV2 authorService = new AuthorServiceV2();
        System.out.println("add new author");
        authorService.addNewAuthor();

        System.out.println("remove last author");
        authorService.removeLastAuthor();
    }

    void addNewAuthor() {
//        AUTHORS_LIST.set(2,new Author(...)); ОШИБКА! в коллекции 2 элемента (0 и 1)

        Author author = new Author(11, "name", "bio");
        AUTHORS_LIST.add(author);

        printAuthors();
    }

    void removeLastAuthor() {
        AUTHORS_LIST.removeLast();
        printAuthors();
    }

    Author findAuthorByName(String name) {
        for (int i = 0; i < AUTHORS_LIST.size(); i++) {
            Author author = AUTHORS_LIST.get(i);
            if (author.getName().equals(name)) {
                return author;
            }
        }
        return null;
    }

    Author findAuthorByNameV2(String name) {
        for (Author author : AUTHORS_LIST) {
            if (author.getName().equals(name)) {
                return author;
            }
        }
        return null;
    }

    Author findAuthorByNameV3(String name) {
        AUTHORS_LIST.forEach(author -> {
            if (author.getName().equals(name)) {
                //! из forEach нельзя возвращать значение
                //return author;
                return;
            }
        });

        return null;
    }

    Author removeAuthorByName(String authorName) {
        //todo:
        return null;
    }

    private void printAuthors() {
        for (Author author : AUTHORS_LIST) {
            System.out.println(author);
        }
    }


}
