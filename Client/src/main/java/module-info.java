module edu.najah.easyproject {
  requires javafx.controls;
  requires javafx.base;
  requires javafx.fxml;
  requires java.sql;
  requires mysql.connector.java;
  
  
  opens edu.najah.easyproject to javafx.fxml, javafx.base;
  exports edu.najah.easyproject;
}