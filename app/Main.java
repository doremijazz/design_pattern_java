package app;

import connection.DatabaseConnection;
import dao.ClientDAO;
import factory.AccountFactory;
import model.Account;
import model.Client;

import java.math.BigDecimal;
import java.sql.SQLException;

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
    }
}