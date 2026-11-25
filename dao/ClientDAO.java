package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connection.DatabaseConnection;
import model.Client;

public class ClientDAO {

    private final Connection connection;

    public ClientDAO() {
        this.connection = DatabaseConnection
                .getInstance().getConnection();
    }

    public List<Client> findAll() {

        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";

        try (PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Client client = new Client(
                    resultSet.getInt("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email")
                );

                clients.add(client);
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                "Error retrieving clients", e
            );
        }

        return clients;
    }

    public Client findById(int id) {
        String sql = "SELECT * FROM client WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return new Client(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email")
                    );
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                "Error retrieving client with id: " + id, e
            );
        }
        return null;
    }

    public int create(Client client) {
        String sql = "INSERT INTO client (first_name, last_name, email) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, client.getFirst_name());
            statement.setString(2, client.getLast_name());
            statement.setString(3, client.getEmail());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating client failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating client failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error creating client", e);
        }
    }

    public void update(Client client) {
        String sql = "UPDATE client SET first_name = ?, last_name = ?, email = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, client.getFirst_name());
            statement.setString(2, client.getLast_name());
            statement.setString(3, client.getEmail());
            statement.setInt(4, client.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating client failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error updating client", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM client WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting client failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error deleting client", e);
        }
    }
}