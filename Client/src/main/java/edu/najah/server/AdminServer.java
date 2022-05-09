package edu.najah.server;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.UUID;

import static java.nio.file.Files.delete;

public class AdminServer {
  public static String login(String email, String password) {
    try {
      String result = "";
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/easyproject", "root", "");
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email='" + email + "' AND password='" + password + "' AND isAdmin='1'");
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        result += "id:" + resultSet.getString("id") + ";";
        result += "email:" + resultSet.getString("email") + ";";
        result += "password:" + resultSet.getString("password") + ";";
        result += "name:" + resultSet.getString("name") + ";";
        result += "address:" + resultSet.getString("address") + ";";
        result += "photo:C:/xampp/htdocs/Server" + resultSet.getString("photo").substring(1) + ";";
        result += "isAdmin:" + resultSet.getString("isAdmin") + ";";
        return result;
      }
      return "Email or Password is incorrect";
    } catch (Exception e) {
      e.printStackTrace();
      return "Login Failed";
    }
  }
  
  public static String updateInfo(String id, String email, String password, String name, String address) {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/easyproject", "root", "");
      PreparedStatement preparedStatement = connection.prepareStatement("Select email from users where id='" + id + "'");
      ResultSet resultSet = preparedStatement.executeQuery();
      
      // check if email is not changed
      if (resultSet.next() && resultSet.getString("email").equals(email)) {
        preparedStatement = connection.prepareStatement("UPDATE users SET password='" + password + "', name='" + name + "', address='" + address + "' WHERE id='" + id + "'");
        return preparedStatement.executeUpdate() > 0 ? "Updated Successfully" : "Updated Failed";
      }
      
      // check if email is changed
      preparedStatement = connection.prepareStatement("Select email from users where email='" + email + "'");
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return "Email is already taken!";
      }
      // if email is not exist
      preparedStatement = connection.prepareStatement("UPDATE users SET email='" + email + "', password='" + password + "', name='" + name + "', address='" + address + "' WHERE id='" + id + "'");
      return preparedStatement.executeUpdate() > 0 ? "Updated Successfully" : "Updated Failed";
    } catch (Exception e) {
      e.printStackTrace();
      return "Updated Failed";
    }
  }
  
  public static String updateImage(String photoExtension, String id, String photo) {
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    try {
      String imagePath = "./Images/" + UUID.randomUUID() + "." + photoExtension;
      Class.forName("com.mysql.cj.jdbc.Driver");
      connection = DriverManager.getConnection("jdbc:mysql://localhost/easyproject", "root", "");
      preparedStatement = connection.prepareStatement("Select photo from users where id='" + id + "'");
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next())
        delete(Path.of("C:/xampp/htdocs/Server" + resultSet.getString("photo").substring(1))); // unlink the old photo
      byte[] decodedImage = Base64.getDecoder().decode(photo);
      FileOutputStream fileOutputStream = new FileOutputStream("C:/xampp/htdocs/Server" + imagePath.substring(1));
      fileOutputStream.write(decodedImage);
      fileOutputStream.close();
      preparedStatement = connection.prepareStatement("UPDATE users SET photo='" + imagePath + "' WHERE id='" + id + "'");
      return preparedStatement.executeUpdate() > 0 ? "Updated Successfully" : "Updated Failed";
    } catch (Exception e) {
      e.printStackTrace();
      return "Login Failed";
    }
  }
  
  public static String addNewUser(String email, String password, String name, String address, String id, String photo, String photoExtension, String isAdmin) {
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    try {
      String imagePath = "./Images/" + UUID.randomUUID() + "." + photoExtension;
      Class.forName("com.mysql.cj.jdbc.Driver");
      connection = DriverManager.getConnection("jdbc:mysql://localhost/easyproject", "root", "");
      
      // if id is given, check if the id is already exist
      if (!id.equals("default")) {
        preparedStatement = connection.prepareStatement("Select id from users where id='" + id + "'");
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) return "ID is already taken!";
      }
      
      // check if email is already exist
      preparedStatement = connection.prepareStatement("Select email from users where email='" + email + "'");
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) return "Email is already taken!";
      
      byte[] decodedImage = Base64.getDecoder().decode(photo);
      FileOutputStream fileOutputStream = new FileOutputStream("C:/xampp/htdocs/Server" + imagePath.substring(1));
      fileOutputStream.write(decodedImage);
      fileOutputStream.close();
      
      // insert new user
      if (!id.equals("default"))
        preparedStatement = connection.prepareStatement("INSERT INTO users (email, password, name, address, photo, isAdmin, id) VALUES (?, ?, ?, ?, ?, ?, ?)");
      else
        preparedStatement = connection.prepareStatement("INSERT INTO users (email, password, name, address, photo, isAdmin) VALUES (?, ?, ?, ?, ?, ?)");
      preparedStatement.setString(1, email);
      preparedStatement.setString(2, password);
      preparedStatement.setString(3, name);
      preparedStatement.setString(4, address);
      preparedStatement.setString(5, imagePath);
      preparedStatement.setString(6, isAdmin);
      if (!id.equals("default")) preparedStatement.setString(7, id);
      return preparedStatement.executeUpdate() > 0 ? "Added Successfully" : "Added Failed";
    } catch (Exception e) {
      e.printStackTrace();
      return "Added Failed";
    }
  }
  
  public static String getUsersFromDB(String usersType, String withSearch, String searchBy, String searchField) {
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      connection = DriverManager.getConnection("jdbc:mysql://localhost/easyproject", "root", "");
      
      if (withSearch.equals("1") && !searchBy.equals("none")) {
        if (searchBy.equals("id")) {
          if (usersType.equals("all"))
            preparedStatement = connection.prepareStatement("Select * from users where id like '%" + searchField + "%'");
          else
            preparedStatement = connection.prepareStatement("Select * from users where id like '%" + searchField + "%' and isAdmin='" + usersType + "'");
        } else {
          if (usersType.equals("all"))
            preparedStatement = connection.prepareStatement("Select * from users where name like '%" + searchField + "%'");
          else
            preparedStatement = connection.prepareStatement("Select * from users where name like '%" + searchField + "%' and isAdmin='" + usersType + "'");
        }
      } else {
        if (usersType.equals("all")) preparedStatement = connection.prepareStatement("Select * from users");
        else preparedStatement = connection.prepareStatement("Select * from users where isAdmin='" + usersType + "'");
      }
      
      resultSet = preparedStatement.executeQuery();
      StringBuilder users = new StringBuilder();
      while (resultSet.next())
        users.append(resultSet.getString("id")).append(",").append(resultSet.getString("email")).append(",").append(resultSet.getString("password")).append(",").append(resultSet.getString("name")).append(",").append(resultSet.getString("address")).append(",").append("C:/xampp/htdocs/Server").append(resultSet.getString("photo").substring(1)).append(",").append(resultSet.getString("isAdmin")).append(";");
      return users.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return "Query Failed";
    }
  }
  
  public static String deleteUser(String id) {
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      connection = DriverManager.getConnection("jdbc:mysql://localhost/easyproject", "root", "");
      preparedStatement = connection.prepareStatement("Delete from users where id='" + id + "'");
      return preparedStatement.executeUpdate() > 0 ? "Deleted Successfully" : "Deleted Failed";
    } catch (Exception e) {
      e.printStackTrace();
      return "Query Failed";
    }
  }
}