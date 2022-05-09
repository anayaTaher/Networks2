package edu.najah.easyproject;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AdminLoginController {
  
  public static String[] adminInfo=new String[6];
  public PasswordField password;
  public TextField email;
  public Label status;
  
  public void login() throws IOException {
    if (email.getText().isEmpty() || password.getText().isEmpty()) {
      status.setText("Please fill all the fields");
    } else {
      String[] params={"function", "login", "email", email.getText(), "password", password.getText()};
      String info=Helper.prepareParameters(params);
      String response=Helper.connectToServer("AdminServer", info);
      if (response.equals("Email or Password is incorrect")) {
        status.setText(response);
      } else {
        adminInfo=Helper.convertResponseToArray(response);
        Helper.changeWindow(Helper.getCurrentStage(), "AdminProfile", "Admin Profile", 489, 400);
      }
    }
  }
  
  public void logout() throws IOException {
    Helper.changeWindow(Helper.getCurrentStage(), "MainPage", "Main Page", 569, 400);
  }
}
