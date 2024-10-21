package backend.academy.seminar5.project.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class LibrarySearchServiceTest {

    @Test
    void getTopCategoriesWithPopularBooks() {
        //filters
        String authorNamePattern = "J.*";
        int topCategoriesCount = 2;
        int limitInCategory = 1;

        //logic
        var libraryAnalyticsService = new LibraryAnalyticsService();
        var res = libraryAnalyticsService.getTopCategoriesWithPopularBooks(
            authorNamePattern,
            topCategoriesCount,
            limitInCategory
        );

        //print
        res.forEach((category, books) -> {
            System.out.println("Категория: " + category);
            books.forEach(book -> System.out.printf("ID: %d, Название: %s%n", book.getId(), book.getTitle()));
            System.out.println();
        });
    }

    @Test
    void debugThisStream() {
        List<Integer> processedNumbers = IntStream.range(1, 100)
            .boxed()
            .filter(n -> n % 2 == 0 && n % 3 == 0 && n % 5 == 0)
            .map(n -> n + 1)
            .peek(n -> System.out.println("After squaring: " + n))
            .filter(n -> n > 50)
            .peek(n -> System.out.println("After filtering > 50: " + n))
            .toList();

        System.out.println("Final Result: " + processedNumbers);
    }

    @Test
    void debugThisStream2() {
        String result = IntStream.range(1, 100)
            .filter(n -> n % 2 == 0)
            .peek(n -> System.out.println("Filtered even: " + n))
            .mapToObj(n -> "Number: " + n)
            .peek(s -> System.out.println("Mapped to string: " + s))
            .flatMap(s -> IntStream.range(0, s.length()).mapToObj(i -> s.substring(0, i + 1)))
            .peek(sub -> System.out.println("Generated substring: " + sub))
            .collect(Collectors.groupingBy(String::length))

            .entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .peek(entry -> System.out.println("Grouped by length: " + entry))
            .flatMap(entry -> entry.getValue().stream())
            .distinct()
            .limit(50)
            .collect(Collectors.joining(", "));

        System.out.println("result = " + result);

    }
}
