package factory;

import java.math.BigDecimal;

import model.Account;
import model.Client;
import model.CurrentAccount;
import model.SavingsAccount;

public class AccountFactory {
    public static Account createAccount(       
            String accountType,
            int id,
            String accountNumber,
            BigDecimal balance,
            Client client,
            BigDecimal interestRate) {
        if (accountType.equalsIgnoreCase("current")) {
            return new CurrentAccount(id, accountNumber, balance, client);
        } else if (accountType.equalsIgnoreCase("savings")) {
            return new SavingsAccount(id, accountNumber, balance, client, interestRate);
        } else {
            throw new IllegalArgumentException("Invalid account type: " + accountType);
        }
    }
}