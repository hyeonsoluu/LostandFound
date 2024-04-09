<?php
    $con = mysqli_connect("localhost", "hs1288", "hs53102549!", "hs1288");
    mysqli_query($con,'SET NAMES utf8');

    $adminID = $_POST["adminID"];
    $adminPassword = $_POST["adminPassword"];
    $adminName = $_POST["adminName"];
    $adminEmail = $_POST["adminEmail"];
    $adminPhone = $_POST["adminPhone"];
    $adminConfirm = $_POST["adminConfirm"];

    $statement = mysqli_prepare($con, "INSERT INTO ADMIN VALUES (?,?,?,?,?,?)");
    mysqli_stmt_bind_param($statement, "ssssss", $adminID, $adminPassword, $adminName, $adminEmail, $adminPhone, $adminConfirm);
    mysqli_stmt_execute($statement);


    $response = array();
    $response["success"] = true;
 
   
    echo json_encode($response);



?>
