package com.cootf.dubbo.component.ignite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author:ice
 * @Date: 2018/5/10 19:41
 */
public class IgniteTest {

  static {
    // Register JDBC driver
    try {
      Class.forName("org.apache.ignite.IgniteJdbcThinDriver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

  }

  public static void main1(String[] args) {
    try {
      // Register JDBC driver
      Class.forName("org.apache.ignite.IgniteJdbcThinDriver");
      // Open JDBC connection
      Connection conn = DriverManager.getConnection(
          "jdbc:ignite:thin://127.0.0.1/");
      Statement stmt = conn.createStatement();
      stmt.executeUpdate("CREATE TABLE City(" +
          " id LONG PRIMARY KEY, name VARCHAR) " +
          " WITH \"template=replicated\"");
      stmt.executeUpdate("CREATE TABLE Person (" +
          " id LONG, name VARCHAR, city_id LONG, " +
          " PRIMARY KEY (id, city_id)) " +
          " WITH \"backups=1, affinityKey=city_id\"");
      // Create an index on the City table
      stmt.executeUpdate("CREATE INDEX idx_city_name ON City (name)");
      // Create an index on the Person table
      stmt.executeUpdate("CREATE INDEX idx_person_name ON Person (name)");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    try {
      // Open JDBC connection
      Connection conn = DriverManager.getConnection(
          "jdbc:ignite:thin://127.0.0.1/");
      PreparedStatement pst = conn
          .prepareStatement("INSERT INTO City(id,name) values(?,?) ");
      pst.setLong(1, 1L);
      pst.setString(2, "ice");
      pst.executeUpdate();
      //
      pst.setLong(1, 2L);
      pst.setString(2, "Denver");
      pst.executeUpdate();
      //
      pst.setLong(1, 3L);
      pst.setString(2, "St. Petersburg");
      pst.executeUpdate();
      //
      pst = conn.prepareStatement("INSERT INTO Person (id, name, city_id) VALUES (?, ?, ?)");
      pst.setLong(1, 1L);
      pst.setString(2, "John Doe");
      pst.setLong(3, 3L);
      pst.executeUpdate();
      //
      pst.setLong(1, 2L);
      pst.setString(2, "Jane Roe");
      pst.setLong(3, 2L);
      pst.executeUpdate();
      //
      pst.setLong(1, 3L);
      pst.setString(2, "Mary Major");
      pst.setLong(3, 1L);
      pst.executeUpdate();
      //
      pst.setLong(1, 4L);
      pst.setString(2, "Richard Miles");
      pst.setLong(3, 2L);
      pst.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void main3(String[] args) {
    try {
      Connection conn = DriverManager.getConnection(
          "jdbc:ignite:thin://127.0.0.1/");
      Statement stmt = conn.createStatement();
      ResultSet rs =
          stmt.executeQuery("SELECT p.name, c.name " +
              " FROM person p, city c " +
              " WHERE p.city_id = c.id");

      System.out.println("Query results:");

      while (rs.next()) {
        System.out.println(">>>    " +
            rs.getString(1) +
            ", " +
            rs.getString(2));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
