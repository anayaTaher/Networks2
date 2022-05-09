<?php
function login($email, $password) {
  $conn = new mysqli("localhost", "root", "", "easyproject");
  $loginQuery = "SELECT * FROM users WHERE email='" . $email . "' AND password='" . $password . "' AND isAdmin='1'";
  $result = mysqli_query($conn, $loginQuery);
  if (mysqli_num_rows($result) > 0) {
    $str = "";
    foreach (mysqli_fetch_assoc($result) as $key => $value) {
      if ($key == "photo") {
        $path = new SplFileInfo($value);
        $str .= $key . ":" . $path->getRealPath() . ",";
      } else {
        $str .= $key . ":" . $value . ";";
      }
    }
    return $str;
  } else {
    return "Email or Password is incorrect";
  }
}

function updateInfo($id, $email, $password, $name, $address) {
  $conn = new mysqli("localhost", "root", "", "easyproject");
  $updateQuery = "Select email from users where id='" . $id . "'";
  $result = mysqli_query($conn, $updateQuery);
  $row = mysqli_fetch_row($result)[0];
  
  if ($row == $email) { // if email is not changed
    $updateQuery = "UPDATE users SET password='" . $password . "', name='" . $name . "', address='" . $address . "' WHERE id='" . $id . "'";
    return (mysqli_query($conn, $updateQuery) ? "Updated Successfully" : "Updated Failed");
  }
  // check if email is changed
  $updateQuery = "SELECT * FROM users WHERE email='" . $email . "'";
  $result = mysqli_query($conn, $updateQuery);
  if (mysqli_num_rows($result) > 0) {
    return "Email is already taken!";
  }
  $updateQuery = "UPDATE users SET email='" . $email . "', password='" . $password . "', name='" . $name . "', address='" . $address . "' WHERE id='" . $id . "'";
  return (mysqli_query($conn, $updateQuery) ? "Updated Successfully" : "Updated Failed");
}

function updateImage($imagePath, $id, $photo) {
  $conn = new mysqli("localhost", "root", "", "easyproject");
  $updateQuery = "Select photo from users where id='" . $id . "'";
  $result = mysqli_query($conn, $updateQuery);
  $row = mysqli_fetch_row($result)[0];
  unlink($row);
  file_put_contents($imagePath, base64_decode($photo));
  $updateQuery = "UPDATE users SET photo='$imagePath' WHERE id='$id'";
  return (mysqli_query($conn, $updateQuery) ? "Updated Successfully" : "Updated Failed");
}

function addNewUser($email, $password, $name, $address, $id, $photo, $isAdmin, $imagePath) {
  $conn = new mysqli("localhost", "root", "", "easyproject");
  
  if ($id != "default") { // if id is given, check if it is already exist
    $ids = array();
    $getIdsQuery = "SELECT id FROM users";
    $result = mysqli_query($conn, $getIdsQuery);
    while ($row1 = mysqli_fetch_assoc($result)) {
      $ids[] = $row1['id'];
    }
    if (in_array($id, $ids)) { // if id is already exist
      return "ID is already taken!";
    }
  }
  
  // check if email is already exist
  $emails = array();
  $getEmailsQuery = "SELECT email FROM users";
  $result = mysqli_query($conn, $getEmailsQuery);
  while ($row1 = mysqli_fetch_assoc($result)) {
    $emails[] = $row1['email'];
  }
  if (in_array($email, $emails)) { // if email is already exist
    return "This email is already taken!";
  }
  
  file_put_contents($imagePath, base64_decode($photo));
  if ($id != "default")
    $insertQuery = "INSERT INTO users (email, password, name, address, id, photo, isAdmin) VALUES ('$email', '$password', '$name', '$address', '$id', '$imagePath', '$isAdmin')";
  else
    $insertQuery = "INSERT INTO users (email, password, name, address, photo, isAdmin) VALUES ('$email', '$password', '$name', '$address', '$imagePath', '$isAdmin')";
  return (mysqli_query($conn, $insertQuery) ? "Added Successfully" : "Added Failed");
}

function getUsersFromDB($usersType, $withSearch, $searchBy, $searchField) {
  $conn = new mysqli("localhost", "root", "", "easyproject");
  if ($withSearch == "1" && $searchBy != "none") {
    if ($searchBy == "id") {
      if ($usersType == "all") {
        $getUsersQuery = "SELECT * FROM users WHERE id like '%$searchField%'";
      } else {
        $getUsersQuery = "SELECT * FROM users WHERE id like '%$searchField%' AND isAdmin='$usersType'";
      }
    } else {
      if ($usersType == "all")
        $getUsersQuery = "SELECT * FROM users WHERE name like '%$searchField%'";
      else
        $getUsersQuery = "SELECT * FROM users WHERE name like '%$searchField%' AND isAdmin='$usersType'";
    }
  } else {
    if ($usersType == "all")
      $getUsersQuery = "SELECT * FROM users";
    else
      $getUsersQuery = "SELECT * FROM users WHERE isAdmin='$usersType'";
  }
  
  $result = mysqli_query($conn, $getUsersQuery);
  $users = array();
  while ($row = mysqli_fetch_assoc($result)) $users[] = $row;
  $result = '';
  foreach ($users as $user) {
    $user["photo"] = realpath($user["photo"]);
    $result .= implode(',', $user) . ";";
  }
  return $result;
}

function deleteUser($id) {
  $conn = new mysqli("localhost", "root", "", "easyproject");
  $deleteQuery = "DELETE FROM users WHERE id='$id'";
  return (mysqli_query($conn, $deleteQuery) ? "Deleted Successfully" : "Deleted Failed");
}

switch ($_REQUEST['function']) {
  case 'login':
    echo login($_REQUEST['email'], $_REQUEST['password']);
    break;
  case 'updateInfo':
    echo updateInfo($_REQUEST['id'], $_REQUEST['email'], $_REQUEST['password'], $_REQUEST['name'], $_REQUEST['address']);
    break;
  case 'updateImage':
    $imagePath = './Images/' . uniqid(rand(), true) . '.' . $_REQUEST['photoExtension'];
    echo updateImage($imagePath, $_REQUEST['id'], $_REQUEST['photo']);
    break;
  case 'addNewUser':
    $imagePath = './Images/' . uniqid(rand(), true) . '.' . $_REQUEST['photoExtension'];
    echo addNewUser($_REQUEST['email'], $_REQUEST['password'], $_REQUEST['name'], $_REQUEST['address'], $_REQUEST['id'], $_REQUEST['photo'], $_REQUEST['isAdmin'], $imagePath);
    break;
  case 'getUsersFromDB':
    echo getUsersFromDB($_REQUEST['usersType'], $_REQUEST['withSearch'], $_REQUEST['searchBy'], $_REQUEST['searchField']);
    break;
  case 'deleteUser':
    echo deleteUser($_REQUEST['id']);
    break;
  default:
    echo "Invalid Request";
}