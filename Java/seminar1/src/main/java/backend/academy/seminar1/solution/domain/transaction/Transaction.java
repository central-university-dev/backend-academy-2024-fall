package backend.academy.seminar1.solution.domain.transaction;

// Абстрактный класс для представления транзакции
public abstract class Transaction {
    protected String transactionId;
    protected double amount;

    public Transaction(String transactionId, double amount) {
        this.transactionId = transactionId;
        this.amount = amount;
    }

    // Абстрактный метод для выполнения транзакции
    public abstract void execute();

    // Общие методы, например, для получения информации о транзакции
    public String getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }
}
