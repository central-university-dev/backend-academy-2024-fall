package backend.academy.seminar1.solution.domain.user;

import backend.academy.seminar1.solution.domain.account.entity.Account;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private String name;
    private String id;
    private List<Account> accounts;

    public Client(String name, String id) {
        this.name = name;
        this.id = id;
        this.accounts = new ArrayList<>();
    }

    // Методы клиента
    public void addAccount(Account account) { /* Реализация метода */ }

    public void removeAccount(Account account) { /* Реализация метода */ }
}
