package app;

import connection.DatabaseConnection;
import dao.AccountDAO;
import dao.ClientDAO;
import dao.OperationDAO;
import factory.AccountFactory;
import model.Account;
import model.Client;
import model.Operation;
import model.OperationType;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;


public class Main {

    public static void main(String[] args) throws SQLException {

        DatabaseConnection db1 = DatabaseConnection.getInstance();
        DatabaseConnection db2 = DatabaseConnection.getInstance();

        System.out.println("Singleton : " + (db1 == db2));

        System.out.println("Même connexion : "
            + (db1.getConnection() == db2.getConnection()));

        System.out.println("Connexion valide : "
            + (db1.getConnection() != null
            && db1.getConnection().isValid(5)));

        Client client = new Client(
            1, "Jean", "Dupont", "jean@example.fr"
        );

        Account account1 = AccountFactory.createAccount(
            "CURRENT",
            1,
            "000001",
            new BigDecimal("1000.00"),
            client,
            null
        );

        Account account2 = AccountFactory.createAccount(
            "SAVINGS",
            2,
            "000002",
            new BigDecimal("2500.00"),
            client,
            new BigDecimal("2.50")
        );

        System.out.println(account1.getClass().getSimpleName());
        System.out.println(account2.getClass().getSimpleName());

        ClientDAO clientDAO = new ClientDAO();

        // Test de findAll()
        for (Client client1 : clientDAO.findAll()) {
            System.out.println(client1.getFirst_name()
                + " " + client1.getLast_name());
        }

        // Test avec un client existant
        Client existingClient = clientDAO.findById(1);

        if (existingClient != null) {
            System.out.println(existingClient.getFirst_name());
        } else {
            System.out.println("Client not found");
        }

        // Test avec un client supposé inexistant
        System.out.println(clientDAO.findById(99999));

        Client client2 = clientDAO.findById(1);

        if (client2 != null) {

            client2.setFirst_name("Pierre");

            clientDAO.update(client2);

            Client updatedClient = clientDAO.findById(1);

            System.out.println(updatedClient.getFirst_name());
        }
        // Test de CRUD DAO pour Client
        // CREATE
        Client newClient = new Client(
            0,
            "Marie",
            "Martin",
            "test@example.fr"
        );

        int id = clientDAO.create(newClient);
        System.out.println("CREATE : " + id);
        // READ
        Client client3 = clientDAO.findById(id);
        System.out.println("READ : " + client3.getFirst_name());

        // UPDATE
        client3.setFirst_name("Sophie");
        clientDAO.update(client3);

        Client updatedClient = clientDAO.findById(id);
        System.out.println("UPDATE : " + updatedClient.getFirst_name());

        // DELETE
        clientDAO.delete(id);
        System.out.println("DELETE : " + (clientDAO.findById(id) == null));

        AccountDAO accountDAO = new AccountDAO();

        for (Account account : accountDAO.findAll()) {

            System.out.println(
                account.getClass().getSimpleName()
                + " : " + account.getBalance() + " €"
            );
        }

        // Test de findByClientId()
        int clientId = 1;
        List<Account> accounts = accountDAO.findByClientId(clientId);
        for (Account account : accounts) {
            System.out.println(
                account.getClass().getSimpleName()
                + " : " + account.getBalance() + " €"
            );
        }

        // Test de CRUD DAO pour Account
        // CREATE
        Account newAccount = AccountFactory.createAccount(
            "CURRENT",
            0,
            "000003",
            new BigDecimal("500.00"),
            client,
            null
        );
        int accountId = accountDAO.create(newAccount);
        System.out.println("CREATE : " + accountId);
        // READ
        Account account3 = accountDAO.findById(accountId);
        System.out.println("READ : " + account3.getBalance() + " €");
        // UPDATE
        account3.setBalance(new BigDecimal("750.00"));
        accountDAO.update(account3);
        // DELETE
        accountDAO.delete(accountId);
        System.out.println("DELETE : " + (accountDAO.findById(accountId) == null));     
        
        OperationDAO operationDAO = new OperationDAO();

        Account account = accountDAO.findById(1);

        if (account != null) {

            Operation operation = new Operation(
                0,
                new BigDecimal("100.00"),
                LocalDateTime.now(),
                OperationType.DEPOSIT,
                account
            );

            int id1 = operationDAO.create(operation);

            System.out.println("CREATE : " + id1);

            Operation savedOperation = operationDAO.findById(id1);

            System.out.println("AMOUNT : " + savedOperation.getAmount());
            System.out.println("TYPE : " + savedOperation.getType());

            for (Operation op : operationDAO.findByAccountId(1)) {
                System.out.println(
                    op.getDate() + " : "
                    + op.getType() + " : "
                    + op.getAmount() + " €"
                );
            }
        }
    }

    
}