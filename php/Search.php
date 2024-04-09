<?php
// 데이터베이스 연결 설정
$host = "localhost";
$username = "hs1288";
$password = "hs53102549!";
$database = "hs1288";

$connection = new mysqli($host, $username, $password, $database);
if ($connection->connect_error) {
    die("Connection failed: " . $connection->connect_error);
}

// 클라이언트에서 검색어를 받아오기
$query = $_GET["query"];

// MySQL 데이터베이스에서 정보를 검색
$sql = "SELECT * FROM BOARD WHERE title LIKE '%$query%'";
$result = $connection->query($sql);

if ($result->num_rows > 0) {
    $response = array();
    while ($row = $result->fetch_assoc()) {
        $response[] = $row;
    }
    echo json_encode($response);
} else {
    echo "No results found.";
}

$connection->close();
?>
