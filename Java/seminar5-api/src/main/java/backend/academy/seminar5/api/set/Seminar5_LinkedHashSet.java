package backend.academy.seminar5.api.set;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Predicate;

public class Seminar5_LinkedHashSet {
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
        var set = new LinkedHashSet<String>();
        set = new LinkedHashSet<>(List.of("a", "b", "c", "d", "e", "f", "g", "h"));

        Set<Integer> integers = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    private static void addElements() {
        var set = new LinkedHashSet<String>();
        boolean res = set.add("a");

        set.addFirst("a");
        set.addLast("a");

        set.addAll(List.of("a", "b", "c", "d", "e", "f", "g", "h"));

    }

    private static void get() {
        var set = new LinkedHashSet<String>();

        String first = set.getFirst();
        String last = set.getLast();

    }

    private static void search() {
        var set = new LinkedHashSet<String>();
        set.add("a");

        boolean contains = set.contains("a");
        boolean contains1 = set.contains("b");
    }

    private static void remove() {
        var set = new LinkedHashSet<String>();

        boolean res = set.remove("a");
        boolean res2 = set.removeIf(Predicate.isEqual("a"));
        boolean res3 = set.removeAll(List.of("a", "b", "c"));

        String res4 = set.removeFirst();
        String res5 = set.removeLast();
    }

    private static void sort() {
        var set = new LinkedHashSet<String>();
    }

    private static void iterators() {
        var set = new LinkedHashSet<String>();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }

        Spliterator<String> spliterator = set.spliterator();
    }

    private static void others() {
        var set = new LinkedHashSet<String>();
        set.size();
        set.isEmpty();

        set.clear();
    }

}
