<?php
    $con = mysqli_connect("localhost", "hs1288", "hs53102549!", "hs1288");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $userPassword = $_POST["userPassword"];
    $userName = $_POST["userName"];
    $userEmail = $_POST["userEmail"];
    $userPhone = $_POST["userPhone"];
    $userAddress = $_POST["userAddress"];

    $statement = mysqli_prepare($con, "INSERT INTO MEMBER VALUES (?,?,?,?,?,?)");
    mysqli_stmt_bind_param($statement, "ssssss", $userID, $userPassword, $userName, $userEmail, $userPhone, $userAddress);
    mysqli_stmt_execute($statement);


    $response = array();
    $response["success"] = true;
 
   
    echo json_encode($response);



?>
