package edu.najah.easyproject;

import java.io.IOException;

public class MainPageController {
  
  public void LoginAdmin() throws IOException {
    Helper.changeWindow(Helper.getCurrentStage(), "AdminLogin", "Admin Login", 310, 412);
  }
  
  public void LoginEmployee() throws IOException {
    Helper.changeWindow(Helper.getCurrentStage(), "EmployeeLogin", "Employee Login", 310, 400);
  }
}