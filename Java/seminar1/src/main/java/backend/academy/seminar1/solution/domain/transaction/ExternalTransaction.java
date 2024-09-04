package backend.academy.seminar1.solution.domain.transaction;

import backend.academy.seminar1.solution.domain.account.entity.Account;

// Транзакция в другой банк
public class ExternalTransaction extends Transaction {
    private Account sourceAccount;
    private String externalBankAccountNumber;  // номер счета в другом банке

    public ExternalTransaction(
        String transactionId,
        double amount,
        Account sourceAccount,
        String externalBankAccountNumber
    ) {
        super(transactionId, amount);
        this.sourceAccount = sourceAccount;
        this.externalBankAccountNumber = externalBankAccountNumber;
    }

    @Override
    public void execute() {
        if (sourceAccount.getBalance() >= amount) {
            sourceAccount.withdraw(amount);
            // Внешние операции для перевода средств в другой банк
            System.out.println("External transfer to account " + externalBankAccountNumber + " executed successfully.");
        } else {
            System.out.println("Insufficient funds for external transfer.");
        }
    }
}
