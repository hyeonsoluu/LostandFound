<?php
    $con = mysqli_connect("localhost", "hs1288", "hs53102549!", "hs1288");
    mysqli_query($con,'SET NAMES utf8');

$adminID = $_POST["adminID"];
$adminPassword = $_POST["adminPassword"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM ADMIN WHERE adminID = ? AND adminPassword = ?");
    mysqli_stmt_bind_param($statement, "ss", $adminID, $adminPassword);
    mysqli_stmt_execute($statement);


    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $adminID, $adminPassword, $adminName, $adminEmail, $adminPhone, $adminConfirm);

    $response = array();
    $response["success"] = false;
 
    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["adminID"] = $adminID;
        $response["adminPassword"] = $adminPassword;
        $response["adminName"] = $adminName;
        $response["adminEmail"] = $adminrEmail;
        $response["adminPhone"] = $adminPhone;
        $response["adminConfirm"] = $adminConfirm;
        ;
    }

echo json_encode($response);



?>
