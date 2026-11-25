package app;

import connection.DatabaseConnection;
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
    }
}