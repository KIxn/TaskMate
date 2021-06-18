<?php
$username = "s2307935";
$password = "s2307935";
$database = "d2307935";
//connect to mysql
$link = mysqli_connect("127.0.0.1", $username, $password, $database);

// Check connection
if(!$link){
    die("ERROR: Could not connect. " . mysqli_connect_error());
}

//prepared sql
$sql = "DELETE FROM ANNOUNCEMENTS WHERE ANNOUNCE_ID=?";

if($stmt = mysqli_prepare($link, $sql)){
    //SET PARAMETERs
    if($ann_id = trim($_GET['ann_id'])){
        //clean grop tables
        // Bind variables to the prepared statement as parameters
        mysqli_stmt_bind_param($stmt, "s", $ann_id);

    //query db
    if(mysqli_stmt_execute($stmt)){ 
        //delete groups and group members corresponding to assignment
        //mysqli_query("");
        mysqli_close($link);
        echo "Assignment Successfully Deleted";
    } else{
        echo "ERROR 500 : Action could not be completed";
        }
    }else{
        echo "No Params";
    }
   
}else{
    echo "Could not prepare";
}
?>