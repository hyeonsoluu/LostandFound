<?php

$conn=mysqli_connect('localhost','hs1288','hs53102549!','hs1288');

$query=$conn->prepare("select userID, category, title, Ddate, firstLocation, detailLocation, detailContent, path from BOARDLOST");

$query->execute();

$query->bind_result($userID, $category, $title, $Ddate, $firstLocation, $detailLocation, $detailContent, $path);

$contents=array();

while($query->fetch())
{
    $data=array();
    // 이미지 URL을 웹 경로로 변환
    $data['imageUrl'] = 'http://hs1288.dothome.co.kr/' . $path;
    
    $data['userID']=$userID;
    $data['category']=$category;
    $data['title']=$title;
    $data['Ddate']=$Ddate;
    $data['firstLocation']=$firstLocation;
    $data['detailLocation']=$detailLocation;
    $data['detailContent']=$detailContent;

    array_push($contents,$data);
}

echo json_encode($contents);


?>
