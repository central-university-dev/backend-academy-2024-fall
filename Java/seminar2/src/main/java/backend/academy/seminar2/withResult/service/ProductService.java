package backend.academy.seminar2.withResult.service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import backend.academy.seminar2.withResult.utils.MyOptional;
import backend.academy.seminar2.withResult.model.Product;

public class ProductService {
    private final Map<Integer, Product> products = new HashMap<>();

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public MyOptional<Product> findProductById(int productId) {
        return MyOptional.ofNullable(products.get(productId));
    }

    public MyOptional<Integer> getProductStock(int productId) {
        return findProductById(productId).map(Product::getStockQuantity);
    }

    public MyOptional<Double> getProductPrice(int productId) {
        return findProductById(productId).map(Product::getPrice);
    }

    public Product getProductOrThrow(int productId) {
        return findProductById(productId)
            .orElseThrow(() -> new NoSuchElementException("Product with ID " + productId + " not found"));
    }

    public double getProductPriceOrDefault(int productId, double defaultPrice) {
        return findProductById(productId)
            .map(Product::getPrice)
            .orElse(defaultPrice);
    }

    public int getProductStockOrCalculate(int productId, Supplier<Integer> stockSupplier) {
        return findProductById(productId)
            .map(Product::getStockQuantity)
            .orElseGet(stockSupplier);
    }
}
