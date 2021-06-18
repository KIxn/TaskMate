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
$sql = "DELETE FROM ASSIGNMENTS WHERE ASS_NAME=?";

if($stmt = mysqli_prepare($link, $sql)){
    //SET PARAMETERs
    if($ASS_NAME = trim($_GET['ass_name'])){
        //delete groups
        $deleteGroupMembers = "DELETE FROM GROUP_MEMBERS WHERE GROUP_ID IN (SELECT GROUP_ID FROM GROUPS WHERE ASS_ID IN (SELECT ASS_ID FROM ASSIGNMENTS WHERE ASS_NAME='$ASS_NAME'))";
        mysqli_query($link,$deleteGroupMembers);
        $deleteGroups = "DELETE FROM GROUPS WHERE ASS_ID IN (SELECT ASS_ID FROM ASSIGNMENTS WHERE ASS_NAME='$ASS_NAME')";
        mysqli_query($link,$deleteGroups);
        //clean grop tables
        // Bind variables to the prepared statement as parameters
        mysqli_stmt_bind_param($stmt, "s", $ASS_NAME);

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