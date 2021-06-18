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
$sql = "INSERT INTO ASSIGNMENTS (CREATOR_ID,ASS_NAME,ASS_DESC,DUE_DATETIME) VALUES (?,?,?,?)";

if($stmt = mysqli_prepare($link, $sql)){
    //SET PARAMETERs
    if(($ass_name = trim($_POST['ass_name']))&&($ass_desc = trim($_POST['ass_desc']))&&($creator = trim($_POST['creator_id']))&&($due_datetime= trim($_POST['due_datetime']))){
        // Bind variables to the prepared statement as parameters
        mysqli_stmt_bind_param($stmt, "ssss", $creator,$ass_name,$ass_desc,$due_datetime);

    //query db
    if(mysqli_stmt_execute($stmt)){
        //execute inserts
            echo "Assignment Successfully Created";
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