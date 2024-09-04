package backend.academy.seminar1.solution.domain.transaction;

import backend.academy.seminar1.solution.domain.account.entity.Account;

// Транзакция между счетами одного клиента
public class InternalTransaction extends Transaction {
    private Account sourceAccount;
    private Account destinationAccount;

    public InternalTransaction(String transactionId, double amount, Account sourceAccount, Account destinationAccount) {
        super(transactionId, amount);
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
    }

    @Override
    public void execute() {
        if (sourceAccount.getBalance() >= amount) {
            sourceAccount.withdraw(amount);
            destinationAccount.deposit(amount);
            System.out.println("Internal transfer executed successfully.");
        } else {
            System.out.println("Insufficient funds for internal transfer.");
        }
    }
}
