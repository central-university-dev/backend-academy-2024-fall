package backend.academy.seminar1.solution.domain.account.entity;

// Класс для сберегательного счета
public class SavingsAccount extends Account {

    public SavingsAccount(String accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public void deposit(double amount) { /* Реализация метода */ }

    @Override
    public void withdraw(double amount) { /* Реализация метода */ }

    @Override
    public double getBalance() {
        return 0;
    }
}
