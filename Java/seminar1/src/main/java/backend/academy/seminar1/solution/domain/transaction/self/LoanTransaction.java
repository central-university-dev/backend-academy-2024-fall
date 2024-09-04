package backend.academy.seminar1.solution.domain.transaction.self;

import backend.academy.seminar1.solution.domain.account.entity.Account;

public class LoanTransaction extends SelfTransaction {
    public LoanTransaction(String transactionId, double amount, Account bankAccount, Account clientAccount) {
        super(transactionId, amount, bankAccount, clientAccount); // Источник - банк, получатель - клиент
    }

    @Override
    public void execute() {
        // В реальной ситуации будет дополнительная логика для проверки кредита и договора
        destinationAccount.deposit(amount);
        System.out.println("Loan granted and funds credited to the client's account.");
    }
}
