package backend.academy.dto;

import java.util.Objects;

public class PaymentRequest {
    private String cardNumber;
    private double amount;

    public PaymentRequest() { }
    public PaymentRequest(String cardNumber, double amount) {
        this.cardNumber = cardNumber;
        this.amount = amount;
    }

    public String getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentRequest that = (PaymentRequest) o;
        return Double.compare(amount, that.amount) == 0 && Objects.equals(cardNumber, that.cardNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, amount);
    }

    @Override public String toString() {
        return "PaymentRequest{" +
            "cardNumber='" + cardNumber + '\'' +
            ", amount=" + amount +
            '}';
    }
}
