<?php
function login($email, $password) {
  $conn=new mysqli("localhost", "root", "", "easyproject");
  $loginQuery="SELECT * FROM users WHERE email='" . $email . "' AND password='" . $password . "' AND isAdmin='0'";
  $result=mysqli_query($conn, $loginQuery);
  if (mysqli_num_rows($result) > 0) {
    $str="";
    foreach (mysqli_fetch_assoc($result) as $key => $value) {
      if ($key == "photo") {
        $path=new SplFileInfo($value);
        $str .= $key . ":" . $path->getRealPath() . ";";
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
  $conn=new mysqli("localhost", "root", "", "easyproject");
  $updateQuery="Select email from users where id='" . $id . "'";
  $result=mysqli_query($conn, $updateQuery);
  $row=mysqli_fetch_row($result)[0];
  
  if ($row == $email) { // if email is not changed
    $updateQuery="UPDATE users SET email='$email', password='$password', name='$name', address='$address', isAdmin='0' WHERE email='$email'";
    return (mysqli_query($conn, $updateQuery) ? "Updated Successfully" : "Updated Failed");
  } else { // if email is changed
    $emails=array();
    $getEmailsQuery="SELECT email FROM users";
    $result=mysqli_query($conn, $getEmailsQuery);
    while ($row1=mysqli_fetch_assoc($result)) {
      $emails[]=$row1['email'];
    }
    if (in_array($email, $emails)) { // if email is already exist
      return "Email is already taken!";
    } else { // if email is not exist
      $updateQuery="UPDATE users SET email='$email', password='$password', name='$name', address='$address', isAdmin='0' WHERE email='$row'";
      return (mysqli_query($conn, $updateQuery) ? "Updated Successfully" : "Updated Failed");
    }
  }
}

function updateImage($imagePath, $id, $photo) {
  $conn=new mysqli("localhost", "root", "", "easyproject");
  $updateQuery="Select photo from users where id='" . $id . "'";
  $result=mysqli_query($conn, $updateQuery);
  $row=mysqli_fetch_row($result)[0];
  unlink($row); // delete old photo
  file_put_contents($imagePath, base64_decode($photo)); // upload new photo
  $updateQuery="UPDATE users SET photo='$imagePath' WHERE id='$id'";
  return (mysqli_query($conn, $updateQuery) ? "Updated Successfully" : "Updated Failed");
}

switch ($_REQUEST['function']) {
  case "login":
    echo login($_REQUEST['email'], $_REQUEST['password']);
    break;
  case "updateInfo":
    echo updateInfo($_REQUEST['id'], $_REQUEST['email'], $_REQUEST['password'], $_REQUEST['name'], $_REQUEST['address']);
    break;
  case "updateImage":
    $imagePath='./Images/' . uniqid(rand(), true) . '.' . $_REQUEST['photoExtension'];
    echo updateImage($imagePath, $_REQUEST['id'], $_REQUEST['photo']);
    break;
  default:
    echo "Invalid Request";
}