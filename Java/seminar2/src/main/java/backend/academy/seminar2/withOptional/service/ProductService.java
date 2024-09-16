package backend.academy.seminar2.withOptional.service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

import backend.academy.seminar2.withOptional.model.Product;
import backend.academy.seminar2.withOptional.utils.ItemNotFoundHandler;

public class ProductService {
    private final Map<Integer, Product> products = new HashMap<>();

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public Optional<Product> findProductById(int productId) {
        return Optional.ofNullable(products.get(productId));
    }

    public Optional<Product> findProductByName(String name) {
        return products.values().stream()
            .filter(product -> product.getName().equals(name))
            .findFirst();
    }

    public Optional<Integer> getProductStock(int productId) {
        return findProductById(productId).map(Product::getStockQuantity);
    }

    public Optional<Double> getProductPrice(int productId) {
        return findProductById(productId).map(Product::getPrice);
    }

    /**
     * Получение продукта с проверкой наличия, если нет - исключение
     */
    public Product getProductOrThrow(int productId) {
        return findProductById(productId)
            .orElseThrow(() -> new NoSuchElementException("Product with ID " + productId + " not found"));
    }

    /**
     * Получение цены товара или значения по умолчанию
     */
    public double getProductPriceOrDefault(int productId, ItemNotFoundHandler handler) {
        for (Product product : products.values()) {
            if (product.getId() == productId) {
                return product.getPrice();
            }
        }
        // Если товар не найден, вызываем обработчик с передачей списка товаров
        return handler.handle(products.values());
    }

    /**
     * Получение количества товара или значение по умолчанию, рассчитанное на основе других данных
     */
    public int getProductStockOrCalculate(int productId, Supplier<Integer> stockSupplier) {
        return findProductById(productId)
            .map(Product::getStockQuantity)
            .orElseGet(stockSupplier);
    }
}
