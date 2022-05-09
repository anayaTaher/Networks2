package edu.najah.easyproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.Stack;

public class Helper {
  
  public static Stage Window1;
  
  public static void changeWindow(Stage currentWindow, String windowName, String windowTitle, int width, int height) throws IOException {
    Stage primaryStage = new Stage();
    Window1 = primaryStage;
    Parent root = FXMLLoader.load(Objects.requireNonNull(Helper.class.getResource(windowName + ".fxml")));
    primaryStage.setTitle(windowTitle);
    primaryStage.setScene(new Scene(root, width, height));
    primaryStage.show();
    currentWindow.close();
  }
  
  public static String addParameter(String info, String name, String value) {
    if (name == null || value == null || name.length() == 0 || value.length() == 0) return info;
    info += name + "=" + URLEncoder.encode(value, StandardCharsets.US_ASCII) + "&";
    return info;
  }
  
  public static String prepareParameters(String[] arr) {
    String info = "";
    for (int i = 0; i < arr.length; i++) info = addParameter(info, arr[i], arr[++i]);
    return info;
  }
  
  public static String connectToServer(String serverName, String info) throws IOException {
    URL url = new URL("http://localhost/Server/" + serverName + ".php");
//    URL url = new URL("http://localhost:8080/EasyProject/" + serverName + ".jsp");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setDoOutput(true);
    connection.setDoInput(true);
    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    connection.setUseCaches(false);
    BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
    outputStream.write(info.getBytes());
    outputStream.flush();
    outputStream.close();
    
    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    StringBuilder response = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) response.append(line);
    reader.close();
    return response.toString();
  }
  
  public static Stage getCurrentStage() {
    return (Stage) Stage.getWindows().stream().filter(Window::isShowing).iterator().next();
  }
  
  public static String[] convertResponseToArray(String response) {
    String[] temp = response.split(";");
    Stack<String> stack = new Stack<>();
    for (String s : temp) stack.push(s.startsWith("photo") ? s.split("photo:")[1] : s.split(":")[1]);
    String[] arr = new String[stack.size()];
    stack.toArray(arr);
    return arr;
  }
  
  public static String encodeFileToBase64Binary(File file) throws Exception {
    byte[] bytes = new byte[(int) file.length()];
    new FileInputStream(file).read(bytes);
    return Base64.getEncoder().encodeToString(bytes);
  }
  
  public static String[] selectImage() {
    String[] result = new String[3];
    try {
      FileChooser imageChooser = new FileChooser();
      imageChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
      File selectedImage = imageChooser.showOpenDialog(null);
      result[0] = selectedImage.getName().split("\\.")[1]; // extension
      File file = new File(selectedImage.getAbsolutePath());
      result[1] = selectedImage.getAbsolutePath(); // path
      result[2] = encodeFileToBase64Binary(file); // image
    } catch (Exception e) {
      result[0] = "You have not selected any image";
    }
    return result;
  }
}