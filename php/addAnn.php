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
$sql = "INSERT INTO ANNOUNCEMENTS (CREATOR_ID,TOPIC,ANNOUNCE_DESC,ANNOUNCE_DATETIME) VALUES (?,?,?,?)";

if($stmt = mysqli_prepare($link, $sql)){
    //SET PARAMETERs
    if(($creator_id=trim($_POST["creator_id"]))&&($topic=trim($_POST["topic"]))&&($desc=trim($_POST["desc"]))&&($date=trim($_POST["date"]))){
        // Bind variables to the prepared statement as parameters
        mysqli_stmt_bind_param($stmt, "ssss",$creator_id,$topic,$desc,$date);

    //query db
    if(mysqli_stmt_execute($stmt)){
        //execute inserts
            echo "Announcement Successfully Created";
            mysqli_close($link);
    } else{
        echo "ERROR: Could not execute query: $sql. " . mysqli_error($link);
        }
    }else{
        echo "No Params";
    }
   
}else{
    echo "Could not prepare";
}
?>