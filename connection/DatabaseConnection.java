package connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DatabaseConnection {
    private static DatabaseConnection instance;
   private String url = "jdbc:mariadb://127.0.0.1:3306/tp_bank?sslMode=trust";
   private String user = "abinet002";
   private String password = "";
   private Connection connection;

   private DatabaseConnection() {
      try {
        this.connection = DriverManager.getConnection(
        this.url, this.user, this.password);
      } catch (SQLException e) {
         e.printStackTrace();
      }

   }

   public static synchronized DatabaseConnection getInstance() {
      if (instance == null) {
         instance = new DatabaseConnection();
      }

      return instance;
   }
   
   public Connection getConnection() {
      return this.connection;
   }


   public void executeRequest(String sql) {
      System.out.println("Exécution sur [" + this.url + "] : " + sql);
   }
}