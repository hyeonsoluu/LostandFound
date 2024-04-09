<?php
//firstly, perform database connection

$conn=mysqli_connect('localhost','hs1288','hs53102549!','hs1288');

//then prepare query statement

$query=$conn->prepare("select userID, userName, userEmail, userPhone, userAddress from MEMBER");

//then execute the query

$query->execute();

//then bind the query from above

$query->bind_result($userID, $userName, $userEmail, $userPhone, $userAddress);

//after there, prepare your query results to loop within array

$contents=array();

//then, use for loop to iterate the incoming row/ assign rows to variable 'data'

while($query->fetch())
{
    $data=array();
    $data['userID']=$userID;
    $data['userName']=$userName;
    $data['userEmail']=$userEmail;
    $data['userPhone']=$userPhone;
    $data['userAddress']=$userAddress;

    //now push the array

    array_push($contents,$data);
}

//finally, send data as 'keys' using json object:  will be sent as string

echo json_encode($contents);

//--------------End-----you can contact me via whatsap +255675839840
?>
