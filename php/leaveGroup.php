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
$sql = "DELETE FROM GROUP_MEMBERS WHERE GROUP_ID=? AND STUD_ID=?";

if($stmt = mysqli_prepare($link, $sql)){
    //SET PARAMETERs
    if(($group_id = trim($_GET['group_id'])) && ($stud_id = trim($_GET['stud_id']))){
        // Bind variables to the prepared statement as parameters
        mysqli_stmt_bind_param($stmt, "ss",$group_id,$stud_id);

    //query db
    if(mysqli_stmt_execute($stmt)){
        //delete group if group is empty
        mysqli_query($link,"DELETE FROM GROUPS WHERE GROUP_ID='$group_id' AND (SELECT COUNT(STUD_ID) FROM GROUP_MEMBERS WHERE GROUP_ID='$group_id')=0");
        mysqli_close($link);
        echo "Group membership adjusted, re-open Assignment Overview if needed";
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