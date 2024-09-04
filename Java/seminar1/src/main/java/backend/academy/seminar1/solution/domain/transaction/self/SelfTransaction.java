package backend.academy.seminar1.solution.domain.transaction.self;

import backend.academy.seminar1.solution.domain.account.entity.Account;
import backend.academy.seminar1.solution.domain.transaction.Transaction;

// Абстрактный класс для транзакций себя
public abstract class SelfTransaction extends Transaction {
    protected Account sourceAccount;
    protected Account destinationAccount;

    public SelfTransaction(String transactionId, double amount, Account sourceAccount, Account destinationAccount) {
        super(transactionId, amount);
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
    }
}

