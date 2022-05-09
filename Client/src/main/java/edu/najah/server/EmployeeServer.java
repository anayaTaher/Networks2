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

public class EmployeeServer {
  public static String login(String email, String password) {
    try {
      String result = "";
      Class.forName("com.mysql.cj.jdbc.Driver");
      Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/easyproject", "root", "");
      PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email='" + email + "' AND password='" + password + "' AND isAdmin='0'");
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
}