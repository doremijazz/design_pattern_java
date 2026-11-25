package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}