package backend.academy.seminar1.solution;

import backend.academy.seminar1.solution.domain.account.entity.Account;
import backend.academy.seminar1.solution.domain.transaction.Transaction;
import backend.academy.seminar1.solution.domain.user.Client;
import java.util.ArrayList;
import java.util.List;

public class BankService {
    private String name;
    private List<Client> clients;
    private List<Account> accounts;

    public BankService(String name) {
        this.name = name;
        this.clients = new ArrayList<>();
        this.accounts = new ArrayList<>();
    }

    public void addClient(Client client) { /* Реализация метода */ }

    public void removeClient(Client client) { /* Реализация метода */ }

    public void performTransaction(Transaction transaction) { /* Реализация метода */ }
}
