package edu.najah.easyproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.Objects;

public class SearchController {
  public TableView<User> table;
  public TableColumn<User, Integer> idColumn;
  public TableColumn<User, String> emailColumn;
  public TableColumn<User, String> nameColumn;
  public TableColumn<User, String> addressColumn;
  public TableColumn<User, String> userTypeColumn;
  public Circle image;
  public Button cancelButton;
  public Label status;
  public Button saveButton;
  public TextField email;
  public TextField name;
  public TextField address;
  public TextField password;
  public Button editInfo;
  public Button deleteUserButton;
  public ComboBox userTypeComboBox;
  public Button nextButton;
  public Button prevButton;
  public ComboBox searchBy;
  public TextField searchField;
  String[] usersInfo;
  String[] res;
  String photoExtension;
  User[] users;
  String selectedId;
  String userTypeFlag = "";
  String searchByFlag = "";
  String searchFieldStr = "";
  String tempEmail = "";
  ObservableList<User> userList;
  int index;
  boolean flag = false;
  
  public void initialize() {
    res = new String[3];
    res[0] = "You have not selected any image";
    users = new User[5];
    setFieldsToDefaultValues();
    prevButton.setDisable(true);
    nextButton.setDisable(true);
    
    searchField.textProperty().addListener((obs, old, niu) -> {
      searchFieldStr = searchField.getText();
      try {
        getUsersFromDB();
        index = 0;
        refreshTable();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
    userTypeComboBox.setItems(FXCollections.observableArrayList("All", "Admins", "Employees"));
    searchBy.setItems(FXCollections.observableArrayList("ID", "Name"));
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));
    addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
    table.setRowFactory(tv -> {
      TableRow<User> row = new TableRow<>();
      row.setOnMouseClicked(event -> {
        if (row.getItem() != null && !Objects.equals(row.getItem().getId(), "")) {
          selectedId = row.getItem().getId();
          for (User user : users) {
            if (user != null && user.getId().equals(row.getItem().getId())) {
              this.image.setFill(new ImagePattern(new Image(user.getImage(), false)));
              this.email.setText(user.getEmail());
              this.name.setText(user.getName());
              this.address.setText(user.getAddress());
              this.password.setText(user.getPassword());
              if (user.getUserType().equals("Admin")) {
                this.editInfo.setDisable(true);
                this.deleteUserButton.setDisable(true);
                this.image.setDisable(true);
              } else {
                this.editInfo.setDisable(false);
                this.deleteUserButton.setDisable(false);
                this.image.setDisable(false);
              }
            }
          }
        } else {
          setFieldsToDefaultValues();
        }
      });
      return row;
    });
  }
  
  private void refreshTable() {
    for (int i = 0; i < 5; i++)
      users[i] = new User("", "", "", "", "", "", "");
    for (int i = index * 35, j = 0; i < Math.min(index * 35 + 35, usersInfo.length); i += 7, j++)
      users[j] = new User(this.usersInfo[i], this.usersInfo[i + 1], this.usersInfo[i + 2], this.usersInfo[i + 3], this.usersInfo[i + 4], this.usersInfo[i + 5], flag ? "" : (Objects.equals(this.usersInfo[i + 6], "1") ? "Admin" : "Employee"));
    userList = FXCollections.observableArrayList(users);
    table.setItems(userList);
    nextButton.setDisable(index * 5 + 5 >= usersInfo.length / 7);
    prevButton.setDisable(index == 0);
  }
  
  void setFieldsToDefaultValues() {
    this.image.setFill(new Color(0, 0, 0, 0));
    this.email.setText("");
    this.name.setText("");
    this.address.setText("");
    this.password.setText("");
    this.editInfo.setDisable(true);
    this.deleteUserButton.setDisable(true);
    this.image.setDisable(true);
  }
  
  private void getUsersFromDB() throws IOException {
    String[] params = {
            "function", "getUsersFromDB",
            "usersType", userTypeFlag.equals("Admins") ? "1" : userTypeFlag.equals("Employees") ? "0" : "all",
            "withSearch", searchFieldStr.equals("") ? "0" : "1",
            "searchBy", searchByFlag.equals("ID") ? "id" : searchByFlag.equals("Name") ? "name" : "none",
            "searchField", searchFieldStr.length() > 0 ? searchFieldStr : "none"
    };
    String info = Helper.prepareParameters(params);
    String response = Helper.connectToServer("AdminServer", info);
    String[] users = response.split(";");
    usersInfo = new String[7 * users.length];
    
    for (int i = 0; i < users.length; i++)
      if (users[i] != null && !users[i].equals("")) {
        System.arraycopy(users[i].split(","), 0, usersInfo, i * 7, 7);
        flag = false;
      } else {
        for (int j = 0; j < 7; j++)
          usersInfo[i * 7 + j] = "";
        flag = true;
      }
  }
  
  public void logout() throws IOException {
    Helper.changeWindow(Helper.getCurrentStage(), "AdminProfile", "Admin Profile", 489, 400);
  }
  
  public void chooseImage() {
    res = Helper.selectImage();
    if (res[0].equals("You have not selected any image")) {
      status.setText(res[0]);
    } else {
      status.setText("");
      photoExtension = res[0];
      this.image.setFill(new ImagePattern(new Image(res[1], false)));
    }
    cancelButton.setDisable(false);
    saveButton.setDisable(false);
  }
  
  public void cancel() {
    cancelButton.setDisable(true);
    saveButton.setDisable(true);
    for (User user : users)
      if (user.getId().equals(selectedId)) this.image.setFill(new ImagePattern(new Image(user.getImage(), false)));
  }
  
  public void save() throws IOException {
    cancelButton.setDisable(true);
    saveButton.setDisable(true);
    String[] params = {"function", "updateImage", "id", selectedId, "photoExtension", res[0], "photo", res[2]};
    String info = Helper.prepareParameters(params);
    String response = Helper.connectToServer("AdminServer", info);
    status.setText(response);
    index = 0;
    getUsersFromDB();
    refreshTable();
  }
  
  private void enableDisable(boolean b) {
    email.setDisable(b);
    password.setDisable(b);
    name.setDisable(b);
    address.setDisable(b);
  }
  
  public void deleteUser() throws IOException {
    String[] params = {"function", "deleteUser", "id", selectedId};
    String info = Helper.prepareParameters(params);
    String response = Helper.connectToServer("AdminServer", info);
    status.setText(response);
    refreshTable();
    index = 0;
    setFieldsToDefaultValues();
    getUsersFromDB();
    refreshTable();
  }
  
  public void updateInfo() throws IOException {
    if (editInfo.getText().equals("Edit Info")) {
      tempEmail = email.getText();
      editInfo.setText("Save Edits");
      enableDisable(false);
    } else {
      editInfo.setText("Edit Info");
      enableDisable(true);
      if (email.getText().isEmpty() || password.getText().isEmpty() || name.getText().isEmpty() || address.getText().isEmpty()) {
        status.setText("Please fill all the fields");
      } else {
        status.setText("");
        String[] params = {"function", "updateInfo", "email", email.getText(), "password", password.getText(), "name", name.getText(), "address", address.getText(), "id", selectedId};
        String info = Helper.prepareParameters(params);
        String response = Helper.connectToServer("EmployeeServer", info);
        status.setText(response);
        if (response.equals("Email is already taken!"))
          email.setText(tempEmail);
        getUsersFromDB();
        refreshTable();
      }
    }
  }
  
  public void prev() {
    index--;
    refreshTable();
  }
  
  public void next() {
    index++;
    refreshTable();
  }
  
  public void changeUserType() throws IOException {
    userTypeFlag = (String) userTypeComboBox.getSelectionModel().getSelectedItem();
    getUsersFromDB();
    index = 0;
    refreshTable();
  }
  
  public void changeSearchBy() throws IOException {
    searchByFlag = (String) searchBy.getSelectionModel().getSelectedItem();
    getUsersFromDB();
    index = 0;
    refreshTable();
  }
}