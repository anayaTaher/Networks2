package edu.najah.easyproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
  public static Stage Window;
  
  public static void main(String[] args) {
    launch();
  }
  
  @Override
  public void start(Stage stage) throws IOException {
    Window=stage;
    Parent root=FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainPage.fxml")));
    Window.setTitle("EasyProject");
    Window.setScene(new Scene(root, 569, 400));
    Window.show();
    
  }
}