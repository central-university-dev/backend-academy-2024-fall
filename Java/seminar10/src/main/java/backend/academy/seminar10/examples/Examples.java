package backend.academy.seminar10.examples;

import lombok.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Examples {
}

// Дублирование 1
class PriceCalculatorBefore {
    public double calculatePriceWithTax(String product) {
        double total = 0.0;
        if (product.equals("apple")) {
            double price = 100;
            double tax = price * 0.1;
            total = price + tax;
        } else if (product.equals("banana")) {
            double price = 80;
            double tax = price * 0.1;
            total = price + tax;
        } else if (product.equals("orange")) {
            double price = 90;
            double tax = price * 0.1;
            total = price + tax;
        }
        return total;
    }
}

// Дублирование fixed
class PriceCalculatorAfter {
    private static final Map<String, Double> prices = new HashMap<>();

    static {
        prices.put("apple", 100.0);
        prices.put("banana", 80.0);
        prices.put("orange", 90.0);
    }

    public double calculatePriceWithTax(String product) {
        double price = prices.getOrDefault(product, 0.0);
        double tax = price * 0.1;
        return price + tax;
    }
}

// Глубокая вложенность
class DiscountCalculator {
    public double calculateDiscount(User user, List<Item> items) {
        if (user != null) {
            if (user.isActive()) {
                if (!items.isEmpty()) {
                    double total = 0;
                    for (Item item : items) {
                        if (item.isEligibleForDiscount()) {
                            if (item.getPrice() > 100) {
                                total += item.getPrice() * 0.1;
                            } else {
                                total += item.getPrice() * 0.05;
                            }
                        }
                    }
                    return total;
                } else {
                    System.out.println("Корзина пуста");
                }
            } else {
                System.out.println("Пользователь не активен");
            }
        } else {
            System.out.println("Пользователь не найден");
        }
        return 0;
    }
}

// метод с большим количеством аргументов
class OrderService {
    public void createOrder(String customerName, String customerEmail, String productName,
        int quantity, double price, String deliveryAddress, String deliveryDate) {
        // Логика создания заказа
        System.out.println("Order created for " + customerName);
    }
}





// Вспомогательные POJO
@Data
class User {
    private boolean active;
}

@Data
class Item {
    private boolean eligibleForDiscount;
    private double price;
}
