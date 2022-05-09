package edu.najah.easyproject;

public class User {
  public String id;
  public String email;
  public String password;
  public String name;
  public String address;
  public String image;
  public String userType;
  
  public User(String id, String email, String password, String name, String address, String image, String userType) {
    this.id=id;
    this.email=email;
    this.password=password;
    this.name=name;
    this.address=address;
    this.image=image;
    this.userType=userType;
  }
  
  // setters and getters
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id=id;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email=email;
  }
  
  public String getPassword() {
    return password;
  }
  
  public void setPassword(String password) {
    this.password=password;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name=name;
  }
  
  public String getAddress() {
    return address;
  }
  
  public void setAddress(String address) {
    this.address=address;
  }
  
  public String getImage() {
    return image;
  }
  
  public void setImage(String image) {
    this.image=image;
  }
  
  public String getUserType() {
    return userType;
  }
}