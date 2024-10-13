package backend.academy.seminar5.api.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Seminar5_HashMap {
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
        var map = new HashMap<String, String>();
        map = new HashMap<>(map);

        var map2 = Map.of("k", "v", "k", "v");

    }

    private static void addElements() {
        var map = new HashMap<String, String>();
        String put = map.put("k", "v");
        map.putAll(map);
        String s = map.putIfAbsent("k", "v");
    }

    private static void changeElements() {
        var map = new HashMap<String, String>();

        String put = map.put("k", "v");
        String replace = map.replace("was", "new");
        map.replaceAll((k1, k2) -> k1 + k2);

    }

    private static void get() {
        var map = new HashMap<String, String>();

        String get = map.get("a");
        String getOrDefault = map.getOrDefault("a", "default");

    }

    private static void search() {
        var map = new HashMap<String, String>();

        boolean b = map.containsKey("k");
        boolean b1 = map.containsValue("v");

        Set<String> strings = map.keySet();
        Collection<String> values = map.values();

        for (Map.Entry<String, String> mapEntry : map.entrySet()) {
            String key = mapEntry.getKey();
            String value = mapEntry.getValue();
        }

        map.forEach((k, v) -> {
            System.out.println(k);
            System.out.println(v);
        });

    }

    private static void remove() {
        var map = new HashMap<String, String>();

        String remove = map.remove("k");
    }

    private static void sort() {
        //!
    }

    private static void iterators() {
        var map = new HashMap<String, String>();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
    }

    private static void others() {
        var map = new HashMap<String, String>();
        map.size();
        map.isEmpty();

        map.clear();
    }

}
