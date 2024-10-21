//package backend.academy.seminar5.project.analytics.hard;
//
//import backend.academy.seminar5.project.db.LibraryDb;
//import backend.academy.seminar5.project.db.entities.Book;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Supplier;
//import java.util.stream.Gatherer;
//
//public class StreamGather {
//    public static void main(String[] args) {
//        var list = LibraryDb.BOOKS.values().stream()
//            .map(Book::getTitle)
//            .gather(gatherer(2))
//            .toList();
//        System.out.println(list);
//
//    }
//
//    private static <T> Gatherer<T, List<T>, List<T>> gatherer(int limit) {
//        Supplier<List<T>> initializer = ArrayList::new;
//        Gatherer.Integrator<List<T>, T, List<T>> integrator = ((state, element, downstream) -> {
//            state.add(element);
//            if (state.size() == limit) {
//                var group = List.copyOf(state);
//                downstream.push(group);
//                state.clear();
//            }
//
//            return true;
//        });
//        return Gatherer.ofSequential(initializer, integrator);
//    }
//}
