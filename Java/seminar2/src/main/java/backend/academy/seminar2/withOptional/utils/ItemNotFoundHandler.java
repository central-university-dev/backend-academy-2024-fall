package backend.academy.seminar2.withOptional.utils;

import backend.academy.seminar2.withOptional.model.Product;
import java.util.Collection;

@FunctionalInterface
public interface ItemNotFoundHandler {
    double handle(Collection<Product> items);
}
