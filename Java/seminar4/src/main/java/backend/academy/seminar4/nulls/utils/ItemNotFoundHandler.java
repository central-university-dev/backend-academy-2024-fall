package backend.academy.seminar4.nulls.utils;

import backend.academy.seminar4.optional.model.Product;
import java.util.Collection;

@FunctionalInterface
public interface ItemNotFoundHandler {
    double handle(Collection<Product> items);
}
