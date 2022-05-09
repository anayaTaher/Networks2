package edu.najah.easyproject;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.Objects;

public class AddNewUserController {
  public Circle image;
  public Label status;
  public TextField email;
  public TextField name;
  public TextField address;
  public TextField password;
  public ToggleGroup userType;
  public TextField id;
  public RadioButton admin;
  public RadioButton employee;
  String photoExtension;
  String[] res;
  boolean isAdmin;
  
  public void initialize() {
    res=new String[3];
    res[0]="You have not selected any image";
  }
  
  public void logout() throws IOException {
    Helper.changeWindow(
            Helper.getCurrentStage(),
            "AdminProfile",
            "Admin Profile",
            489,
            400
    );
  }
  
  public void chooseImage() {
    res=Helper.selectImage();
    if (res[0].equals("You have not selected any image")) {
      status.setText(res[0]);
    } else {
      status.setText("");
      photoExtension=res[0];
      this.image.setFill(new ImagePattern(new Image(res[1], false)));
    }
  }
  
  public void addNewUser() throws IOException {
    if (email.getText().isEmpty() || name.getText().isEmpty() || address.getText().isEmpty() || password.getText().isEmpty()) {
      status.setText("Please Fill All The Fields");
    }
    if (res[0].equals("You have not selected any image")) {
      status.setText(res[0]);
    } else if (userType.getSelectedToggle() == null) {
      status.setText("Please Select User Type");
    } else {
      status.setText("");
      isAdmin=Objects.equals(((RadioButton) userType.getSelectedToggle()).getText(), "Admin");
      String[] params={
              "function", "addNewUser",
              "id", id.getText().equals("") ? "default" : id.getText(),
              "email", email.getText(),
              "password", password.getText(),
              "name", name.getText(),
              "address", address.getText(),
              "isAdmin", isAdmin ? "1" : "0",
              "photoExtension", photoExtension,
              "photo", res[2]
      };
      String info=Helper.prepareParameters(params);
      String response=Helper.connectToServer("AdminServer", info);
      status.setText(response);
    }
  }
}