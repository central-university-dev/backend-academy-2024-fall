package backend.academy.seminar5.api.list;

import backend.academy.seminar5.api.utils.ListUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Spliterator;

public class Seminar5_ArrayList {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        create();
        addElements();
        changeElements();
        get();
        search();
        remove();
        sort();
        iterators();
        others();
    }

    private static void create() {
        var list = new ArrayList<String>();
        int initialCapacity = 100;
        var listWithSize = new ArrayList<String>(initialCapacity);
        var listFromCollection = new ArrayList<>(List.of("1", "2", "3"));

        var list2 = List.of("1", "2", "3");
        var list3 = Collections.singletonList("1");

        System.out.println("list = " + list);
        System.out.println("listWithSize = " + listWithSize);
        System.out.println("listFromCollection = " + listFromCollection);
        System.out.println("list2 = " + list2);
        System.out.println("list3 = " + list3);
    }

    private static void addElements() {
        ArrayList<String> list = new ArrayList<>();
        //add
        boolean a = list.add("a");
        boolean b = list.add("b");
        boolean c = list.add("c");
        list.add(1, "c");

        list.addFirst("f");
        list.addLast("l");

        boolean d = list.addAll(List.of("d", "e", "f"));
        boolean e = list.addAll(0, List.of("g", "h", "i"));

        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("c = " + c);
        System.out.println("d = " + d);
        System.out.println("e = " + e);

        System.out.println("list.size() = " + list.size());
    }

    private static void changeElements() {
        ArrayList<String> list = new ArrayList<>();
        ListUtils.addMoreStrings(list, 100);

        list.reversed();
        list.set(0, "N");
        list.replaceAll(s -> s.toUpperCase());
        list.replaceAll(String::toUpperCase);
    }

    private static void get() {
        ArrayList<String> list = new ArrayList<>();
        //get
        String first = list.getFirst();
        String last = list.getLast();
        String getByIndex = list.get(10);

        System.out.println("first = " + first);
        System.out.println("last = " + last);
        System.out.println("getByIndex = " + getByIndex);
    }

    private static void search() {
        ArrayList<String> list = new ArrayList<>();
        ListUtils.addMoreStrings(list, 100);

        //finds
        int indexOf = list.indexOf("a");
        int lastIndexOf = list.lastIndexOf("a");

        boolean containsAll = list.containsAll(List.of("a", "b", "c", "d", "e", "f"));
        boolean contains = list.contains("a");

        list.clear();
        int size = list.size();

        System.out.println("indexOf = " + indexOf);
        System.out.println("lastIndexOf = " + lastIndexOf);
        System.out.println("containsAll = " + containsAll);
        System.out.println("contains = " + contains);
        System.out.println("size = " + size);
    }

    private static void remove() {
        ArrayList<String> list = new ArrayList<>();
        ListUtils.addMoreStrings(list, 100);

        String removedString = list.remove(1);
        boolean a = list.remove("a");
        boolean b = list.removeAll(List.of("a", "b", "c"));
        String removedFirst = list.removeFirst();
        String removedLast = list.removeLast();

        System.out.println("removedString = " + removedString);
        System.out.println("list = " + list);
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("removedFirst = " + removedFirst);
        System.out.println("removedLast = " + removedLast);
    }

    private static void iterators() {
        ArrayList<String> list = new ArrayList<>();
        ListUtils.addMoreStrings(list, 100);

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (RANDOM.nextBoolean()) {
                String el = iterator.next();
                System.out.println("el = " + el);
            } else {
                iterator.remove();
            }
        }

        ListIterator<String> stringListIterator = list.listIterator();
        while (stringListIterator.hasNext()) {
            System.out.println("stringListIterator.next() = " + stringListIterator.next());
        }

        Spliterator<String> spliterator = list.spliterator();
        spliterator.forEachRemaining(System.out::println);
        list.stream().toList();
        list.forEach(el -> System.out.println(el.toUpperCase()));
    }

    private static void sort() {
        ArrayList<String> list = new ArrayList<>();
        ListUtils.addMoreStrings(list, 100);

        List<String> slice = list.subList(0, 10);
        slice.sort(String::compareTo);
        List<String> reversed = slice.reversed();

        System.out.println("slice = " + slice);
        System.out.println("reversed = " + reversed);
    }

    private static void others() {
        ArrayList<String> list = new ArrayList<>();
        ListUtils.addMoreStrings(list, 100);

        //size
        list.ensureCapacity(999);
        list.trimToSize();

        System.out.println("list = " + list);
    }
}
