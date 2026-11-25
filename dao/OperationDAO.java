package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

import connection.DatabaseConnection;
import model.Account;
import model.Operation;
import model.OperationType;

public class OperationDAO {

    private final Connection connection;
    private final AccountDAO accountDAO;

    public OperationDAO() {
        this.connection = DatabaseConnection
                .getInstance().getConnection();
        this.accountDAO = new AccountDAO();
    }

    public List<Operation> findAll() {

        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT * FROM operation";

        try (PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Account account = accountDAO.findById(
                    resultSet.getInt("account_id")
                );

                Operation operation = new Operation(
                    resultSet.getInt("id"),
                    resultSet.getBigDecimal("amount"),
                    resultSet.getTimestamp("date").toLocalDateTime(),
                    OperationType.valueOf(resultSet.getString("type")),
                    account
                );

                operations.add(operation);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                "Error retrieving operations", e
            );
        }

        return operations;
    }

    public Operation findById(int id) {
        String sql = "SELECT * FROM operation WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    Account account = accountDAO.findById(
                        resultSet.getInt("account_id")
                    );

                    return new Operation(
                        resultSet.getInt("id"),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getTimestamp("date").toLocalDateTime(),
                        OperationType.valueOf(resultSet.getString("type")),
                        account
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(
                "Error retrieving operation with id: " + id, e
            );
        }
    }

    public List<Operation> findByAccountId(int accountId) {
        String sql = "SELECT * FROM operation WHERE account_id = ?";

        List<Operation> operations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Account account = accountDAO.findById(
                        resultSet.getInt("account_id")
                    );

                    Operation operation = new Operation(
                        resultSet.getInt("id"),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getTimestamp("date").toLocalDateTime(),
                        OperationType.valueOf(resultSet.getString("type")),
                        account
                    );

                    operations.add(operation);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(
                "Error retrieving operations for account with id: " + accountId, e
            );
        }

        return operations;
    }

    public int create(Operation operation) {
        String sql = "INSERT INTO operation (amount, date, type, account_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setBigDecimal(1, operation.getAmount());
            statement.setTimestamp(2, java.sql.Timestamp.valueOf(operation.getDate()));
            statement.setString(3, operation.getType().name());
            statement.setInt(4, operation.getAccount().getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating operation failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating operation failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(
                "Error creating operation", e
            );
        }
    }
}