<?php
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // 이미지를 받아옴
    $base64Image = $_POST['image'];

    // 게시글 정보를 받아옴
    $userID = $_POST['userID'];
    $category = $_POST['category'];
    $title = $_POST['title'];
    $Ddate = $_POST['Ddate'];
    $firstLocation = $_POST['firstLocation'];
    $detailLocation = $_POST['detailLocation'];
    $storage = $_POST['storage'];
    $detailContent = $_POST['detailContent'];

    // 이미지를 디코딩하여 저장
    $image = base64_decode($base64Image);

    // 이미지 유형 확인
    $finfo = new finfo(FILEINFO_MIME_TYPE);
    $mime_type = $finfo->buffer($image);
    $allowed_types = array('image/jpeg', 'image/png', 'image/gif');
    
    if (!in_array($mime_type, $allowed_types)) {
        die("Invalid image type. Allowed types: jpg, png, gif");
    }

    // 이미지를 서버에 저장
    $path = 'images/'.date("d-m-Y").'-'.time().'-'.rand(10000, 100000) . '.jpg';
    file_put_contents($path, $image);

    // 여기에서 받은 게시글 정보와 이미지 경로를 데이터베이스에 저장하거나 원하는 대로 처리

    // 여기에서 데이터베이스 작업을 수행할 수 있습니다.
    $servername = "localhost";
    $username = "hs1288";
    $password = "hs53102549!";
    $dbname = "hs1288";

    // 데이터베이스 연결 생성
    $conn = new mysqli($servername, $username, $password, $dbname);

    // 연결 확인
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    // 데이터베이스에 삽입할 쿼리 작성
    $stmt = $conn->prepare("INSERT INTO BOARDGET (userID, category, title, Ddate, firstLocation, detailLocation, storage, detailContent, path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("sssssssss", $userID, $category, $title, $Ddate, $firstLocation, $detailLocation, $storage, $detailContent, $path);

    // 쿼리 실행
    if ($stmt->execute()) {
        echo "success";
    } else {
        echo "Error: " . $stmt->error;
    }
    // 데이터베이스 연결 종료
    $stmt->close();
    $conn->close();
}
?>
