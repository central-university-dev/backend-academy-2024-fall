package backend.academy.seminar5.api.set;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.TreeSet;
import java.util.function.Predicate;

public class Seminar5_TreeSet {
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
        var set = new TreeSet<String>();
        set = new TreeSet<String>((String::compareTo));
        set = new TreeSet<>(List.of("a", "b", "c", "d", "e", "f", "g", "h"));

        Set<Integer> integers = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    private static void addElements() {
        var set = new TreeSet<String>();
        boolean res = set.add("a");

        set.addFirst("a");
        set.addLast("a");

        set.addAll(List.of("a", "b", "c", "d", "e", "f", "g", "h"));

        SortedSet<String> newSet = set.subSet("from", "to");
    }

    private static void get() {
        var set = new TreeSet<String>();

        String first = set.getFirst();
        String last = set.getLast();

        String pollFirst = set.pollFirst();
        String pollLast = set.pollLast();
    }

    private static void search() {
        var set = new TreeSet<String>();
        set.add("a");

        boolean contains = set.contains("a");
        boolean contains1 = set.contains("b");
    }

    private static void remove() {
        var set = new TreeSet<String>();

        boolean res = set.remove("a");
        boolean res2 = set.removeIf(Predicate.isEqual("a"));
        boolean res3 = set.removeAll(List.of("a", "b", "c"));

        String res4 = set.removeFirst();
        String res5 = set.removeLast();
    }

    private static void sort() {
        var set = new TreeSet<String>();

        NavigableSet<String> strings = set.descendingSet();
        Comparator<? super String> comparator = strings.comparator();
        Comparator<? super String> reversed = comparator.reversed();
    }

    private static void iterators() {
        var set = new TreeSet<String>();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }

        Iterator<String> descendingIterator = set.descendingIterator();

        Spliterator<String> spliterator = set.spliterator();
    }

    private static void others() {
        var set = new TreeSet<String>();
        set.size();
        set.isEmpty();

        set.clear();

        String res = set.ceiling("a");
        SortedSet<String> a = set.headSet("a");
        NavigableSet<String> strings = set.descendingSet();

    }

}
