package edu.najah.easyproject;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class AdminProfileController {
  public TextField email;
  public Label nameLabel;
  public TextField name;
  public Button editInfo;
  public Circle image;
  public Label status;
  public TextField address;
  public TextField password;
  public Button saveButton;
  public Button cancelButton;
  public Label id;
  String photoExtension;
  String[] res;
  
  public void initialize() {
    this.id.setText(AdminLoginController.adminInfo[0]);
    this.email.setText(AdminLoginController.adminInfo[1]);
    this.password.setText(AdminLoginController.adminInfo[2]);
    this.name.setText(AdminLoginController.adminInfo[3]);
    this.nameLabel.setText(AdminLoginController.adminInfo[3]);
    this.address.setText(AdminLoginController.adminInfo[4]);
    this.image.setFill(new ImagePattern(new Image(AdminLoginController.adminInfo[5].split(",isAdmin")[0], false)));
  }
  
  public void logout() throws IOException {
    Helper.changeWindow(Helper.getCurrentStage(), "MainPage", "Main Page", 569, 400);
  }
  
  public void updateInfo() throws IOException {
    if (editInfo.getText().equals("Edit Info")) {
      editInfo.setText("Save Edits");
      enableDisable(false);
    } else {
      editInfo.setText("Edit Info");
      enableDisable(true);
      if (email.getText().isEmpty() || password.getText().isEmpty() || name.getText().isEmpty() || address.getText().isEmpty() || id.getText().isEmpty()) {
        status.setText("Please fill all the fields");
      } else {
        status.setText("");
        String[] params = {"function", "updateInfo", "email", email.getText(), "password", password.getText(), "name", name.getText(), "address", address.getText(), "id", id.getText()};
        String info = Helper.prepareParameters(params);
        String response = Helper.connectToServer("AdminServer", info);
        status.setText(response);
        if (response.equals("Updated Successfully")) {
          this.nameLabel.setText(name.getText());
        }
      }
    }
  }
  
  private void enableDisable(boolean b) {
    email.setDisable(b);
    password.setDisable(b);
    name.setDisable(b);
    address.setDisable(b);
  }
  
  public void chooseImage() {
    res = Helper.selectImage();
    if (res[0].equals("You have not selected any image")) {
      status.setText(res[0]);
    } else {
      cancelButton.setDisable(false);
      saveButton.setDisable(false);
      status.setText("");
      photoExtension = res[0];
      this.image.setFill(new ImagePattern(new Image(res[1], false)));
    }
  }
  
  public void cancel() {
    this.image.setFill(new ImagePattern(new Image(AdminLoginController.adminInfo[5], false)));
    cancelButton.setDisable(true);
    saveButton.setDisable(true);
  }
  
  public void save() throws IOException {
    cancelButton.setDisable(true);
    saveButton.setDisable(true);
    String[] params = {"function", "updateImage", "id", id.getText(), "photoExtension", res[0], "photo", res[2]};
    String info = Helper.prepareParameters(params);
    String response = Helper.connectToServer("AdminServer", info);
    status.setText(response);
  }
  
  public void goToAddNewUserPage() throws IOException {
    Helper.changeWindow(Helper.getCurrentStage(), "AddNewUser", "Add New User", 489, 388);
  }
  
  public void goToEmployeesPage() throws IOException {
    Helper.changeWindow(Helper.getCurrentStage(), "Search", "Employees Information", 476, 388);
  }
}