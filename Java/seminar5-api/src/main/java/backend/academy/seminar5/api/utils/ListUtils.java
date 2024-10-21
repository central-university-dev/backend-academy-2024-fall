package backend.academy.seminar5.api.utils;

import java.util.List;

public class ListUtils {
    public static void addMoreStrings(List<String> arrayList, int numOfElements) {
        for (int i = 0; i < numOfElements; i++) {
            arrayList.add(String.valueOf(i));
        }
    }
}
