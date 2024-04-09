<?php
    $con = mysqli_connect("localhost", "hs1288", "hs53102549!", "hs1288");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $markerTitle = $_POST["markerTitle"];

    $statement = mysqli_prepare($con, "INSERT INTO currentLocation VALUES (?,?)");
    mysqli_stmt_bind_param($statement, "ss", $userID, $markerTitle );
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;
    
    echo json_encode($response);



?>
