package backend.academy.seminar2.withResult.service;

import java.util.ArrayList;
import java.util.List;
import backend.academy.seminar2.withResult.model.Order;
import backend.academy.seminar2.withResult.model.Product;
import backend.academy.seminar2.withResult.model.User;
import backend.academy.seminar2.withResult.utils.Result;

public class OrderService {
    private final ProductService productService;

    public OrderService(ProductService productService) {
        this.productService = productService;
    }

    public Result<Order, String> createOrder(User user, List<Integer> productIds) {
        List<Product> availableProducts = new ArrayList<>();
        double totalPrice = 0.0;

        for (int productId : productIds) {
            // Используем orElseThrow для получения продукта или выброса исключения
            Product product = productService.getProductOrThrow(productId);

            // Проверяем наличие на складе с возвратом ошибки, если товара нет
            int stock = productService.getProductStockOrCalculate(productId, () -> 0);
            if (stock > 0) {
                availableProducts.add(product);
                totalPrice += product.getPrice();
            } else {
                return Result.failure("Product " + product.getName() + " is out of stock");
            }
        }

        // Создание заказа, если все продукты доступны
        Order order = new Order(user, availableProducts, totalPrice);
        return Result.success(order);
    }
}
