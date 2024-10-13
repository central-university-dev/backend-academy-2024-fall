package backend.academy.seminar5.api.set;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Predicate;

public class Seminar5_HashSet {
    public static void main(String[] args) {
        create();
        addElements();
        get();
        search();
        remove();
        sort();
        iterators();
        others();
    }

    private static void create() {
        var set = new HashSet<String>();
        set = new HashSet<>(List.of("a", "b", "c", "d", "e", "f", "g", "h"));

        Set<Integer> integers = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

    }

    private static void addElements() {
        var set = new HashSet<String>();
        boolean res = set.add("a");

        set.addAll(List.of("a", "b", "c", "d", "e", "f", "g", "h"));
    }

    private static void get() {
        //!
    }

    private static void search() {
        var set = new HashSet<String>();
        set.add("a");

        boolean contains = set.contains("a");
        boolean contains1 = set.contains("b");
    }

    private static void remove() {
        var set = new HashSet<String>();

        boolean res = set.remove("a");
        boolean res2 = set.removeIf(Predicate.isEqual("a"));
        boolean res3 = set.removeAll(List.of("a", "b", "c"));
    }

    private static void sort() {
        //!
    }

    private static void iterators() {
        var set = new HashSet<String>();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }

        Spliterator<String> spliterator = set.spliterator();
    }

    private static void others() {
        var set = new HashSet<String>();
        set.size();
        set.isEmpty();

        set.clear();
    }

}
