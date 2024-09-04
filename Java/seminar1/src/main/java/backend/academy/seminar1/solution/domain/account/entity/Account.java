package backend.academy.seminar1.solution.domain.account.entity;

public abstract class Account {
    protected String accountNumber;
    protected double balance;

    public Account(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    // Абстрактные методы счета
    public abstract void deposit(double amount);

    public abstract void withdraw(double amount);

    public abstract double getBalance();
}
