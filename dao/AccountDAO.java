package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import java.sql.Types;

import connection.DatabaseConnection;
import factory.AccountFactory;
import model.Account;
import model.Client;
import model.CurrentAccount;
import model.SavingsAccount;

public class AccountDAO {

    private final Connection connection;
    private final ClientDAO clientDAO;

    public AccountDAO() {
        this.connection = DatabaseConnection
                .getInstance().getConnection();

        this.clientDAO = new ClientDAO();
    }

    public List<Account> findAll() {

        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account";

        try (PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                int clientId = resultSet.getInt("client_id");
                ClientDAO clientDAO = new ClientDAO();

                Client client = clientDAO.findById(clientId);

                Account account = AccountFactory.createAccount(
                    resultSet.getString("type"),
                    resultSet.getInt("id"),
                    resultSet.getString("account_number"),
                    resultSet.getBigDecimal("balance"),
                    client,
                    resultSet.getBigDecimal("interest_rate")
                );

                accounts.add(account);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                "Error retrieving accounts", e
            );
        }

        return accounts;
    }

    public Account findById(int id) {
        String sql = "SELECT * FROM account WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    int clientId = resultSet.getInt("client_id");
                    ClientDAO clientDAO = new ClientDAO();

                    Client client = clientDAO.findById(clientId);

                    return AccountFactory.createAccount(
                        resultSet.getString("type"),
                        resultSet.getInt("id"),
                        resultSet.getString("account_number"),
                        resultSet.getBigDecimal("balance"),
                        client,
                        resultSet.getBigDecimal("interest_rate")
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(
                "Error retrieving account with id: " + id, e
            );
        }
    }

    public int create(Account account) {

        String sql = "INSERT INTO account "
            + "(type, account_number, balance, client_id, interest_rate) "
            + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {

            if (account instanceof SavingsAccount savingsAccount) {

                statement.setString(1, "SAVINGS");
                statement.setBigDecimal(5, savingsAccount.getInterestRate());

            } else if (account instanceof CurrentAccount) {

                statement.setString(1, "CURRENT");
                statement.setNull(5, Types.DECIMAL);

            } else {
                throw new IllegalArgumentException("Invalid account type");
            }

            statement.setString(2, account.getAccountNumber());
            statement.setBigDecimal(3, account.getBalance());
            statement.setInt(4, account.getClient().getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(
                    "Creating account failed, no rows affected."
                );
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException(
                        "Creating account failed, no ID obtained."
                    );
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                "Error creating account", e
            );
        }
    }

    public void update(Account account) {

        String sql = "UPDATE account SET "
            + "type = ?, account_number = ?, balance = ?, "
            + "client_id = ?, interest_rate = ? "
            + "WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            if (account instanceof SavingsAccount savingsAccount) {

                statement.setString(1, "SAVINGS");
                statement.setBigDecimal(5, savingsAccount.getInterestRate());

            } else if (account instanceof CurrentAccount) {

                statement.setString(1, "CURRENT");
                statement.setNull(5, Types.DECIMAL);

            } else {
                throw new IllegalArgumentException("Invalid account type");
            }

            statement.setString(2, account.getAccountNumber());
            statement.setBigDecimal(3, account.getBalance());
            statement.setInt(4, account.getClient().getId());
            statement.setInt(6, account.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(
                    "Updating account failed, no rows affected."
                );
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                "Error updating account", e
            );
        }
    }

    public void delete(int id) {

        String sql = "DELETE FROM account WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(
                    "Deleting account failed, no rows affected."
                );
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                "Error deleting account with id: " + id, e
            );
        }
    }

    public List<Account> findByClientId(int clientId) {

        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account WHERE client_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Client client = clientDAO.findById(clientId);

                    Account account = AccountFactory.createAccount(
                        resultSet.getString("type"),
                        resultSet.getInt("id"),
                        resultSet.getString("account_number"),
                        resultSet.getBigDecimal("balance"),
                        client,
                        resultSet.getBigDecimal("interest_rate")
                    );

                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(
                "Error retrieving accounts for client with id: " + clientId, e
            );
        }

        return accounts;
    }
}
