package backend.academy.seminar2.withResult.model;

import java.util.List;

public class Order {
    private final User user;
    private final List<Product> products;
    private final double totalPrice;

    public Order(User user, List<Product> products, double totalPrice) {
        this.user = user;
        this.products = products;
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        StringBuilder productNames = new StringBuilder();
        for (Product product : products) {
            productNames.append(product.getName()).append(", ");
        }
        // Удаляем последнее ", " для красивого вывода
        if (productNames.length() > 0) {
            productNames.setLength(productNames.length() - 2);
        }

        return "Order{" +
            "user=" + user.getName() +
            ", products=[" + productNames.toString() + "]" +
            ", totalPrice=" + totalPrice +
            '}';
    }
}
