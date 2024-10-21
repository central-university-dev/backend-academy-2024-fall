package backend.academy.seminar5.project.analytics.easy._2;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamsIntro {
    public static void main(String[] args) {
        someStream(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        someStream2();
        mergeStream();
    }

    private static void someStream(int[] array) {
        //todo: SHOW STREAM DEBUG HERE
        int res = Arrays.stream(array)
            .skip(10)
            .dropWhile(i -> i != 0)
            .takeWhile(i -> i > 0)
            .limit(10)
            .filter(it -> it % 2 == 0)
            .map(it -> it * 2)
            .peek(it -> System.out.println("it:" + it))
            //.sum
            //.min()
            //.max()
            .reduce((a, b) -> Integer.sum(a, b))
//            .reduce(Integer::sum)
            .orElse(0);
        System.out.println("res = " + res);
    }

    private static void someStream2() {
        IntSummaryStatistics intSummaryStatistics = IntStream.range(1, 100)
            .filter(it -> it % 2 == 0)
            .summaryStatistics();

        System.out.println("intSummaryStatistics = " + intSummaryStatistics);
        //intSummaryStatistics = IntSummaryStatistics{count=49, sum=2450, min=2, average=50.000000, max=98}
    }

    private static void mergeStream() {
        long count = Stream.concat(
                Arrays.stream("a b c d e f g h i j k l m n o p q r s t u v w x y z".split(" ")),
                Arrays.stream("а б в г д е ж з и к л м н о п р с т у ф х ц ч ш щ ъ ы ь э ю я".split(" "))
            )
            .count();

        System.out.println("всего букв = " + count);
    }

    private static void uniqueCharsCount() {
        String input = "example string";

        long uniqueCharactersCount = input.chars() // Создаём IntStream символов строки
            .mapToObj(c -> (char) c) // Преобразуем IntStream в Stream<Character>
            .distinct() // Оставляем только уникальные символы
            .count(); // Считаем количество уникальных символов

        System.out.println("Количество уникальных символов: " + uniqueCharactersCount);
    }
}
