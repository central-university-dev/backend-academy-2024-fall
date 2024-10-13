package backend.academy.seminar5.project.analytics.easy._2;

import java.util.Arrays;

public class StreamsArray {
    public static void main(String[] args) {
        int n = 1_000_000_000; // probably out of memory
        //int n = 100_000_000;

        int[] array = new int[n];
        Arrays.fill(array, 1);
        long start;

        start = System.currentTimeMillis();
        System.out.println("sumOfRandom(" + n + ") = " + sumOfRandom(n, array));
        System.out.println("time = " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        System.out.println("sumOfRandomParallel(" + n + ") = " + sumOfRandomParallel(n, array));
        System.out.println("time = " + (System.currentTimeMillis() - start));
    }

    private static int sumOfRandom(int n, int[] array) {
        return Arrays.stream(array)
            .skip(3)
//            .dropWhile(it -> it == 0)
            .distinct()
            .limit(n)
            .sum();
    }

    private static int sumOfRandomParallel(int n, int[] array) {
        return Arrays.stream(array)
            .parallel()
            .skip(3)
//            .dropWhile(it -> it == 0)  // OutOfMemoryError || heapspace
            //drop while делает выполнение однопоточным. -> sequential()
            .distinct() //значительно влияет на производительность
            .limit(n)
            .sum();
    }
}
