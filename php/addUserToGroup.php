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
$sql = "INSERT INTO GROUP_MEMBERS (GROUP_ID,STUD_ID) VALUES (?,?)";

if($stmt = mysqli_prepare($link, $sql)){
    //SET PARAMETERs
    if(($grp_id=trim($_POST["group_id"])) && ($stud_id=trim($_POST["stud_id"]))){
        // Bind variables to the prepared statement as parameters
        mysqli_stmt_bind_param($stmt, "ss",$grp_id,$stud_id);

    //query db
    if(mysqli_stmt_execute($stmt)){
        echo "Added You to Group, Please re-open Assignment Overview";
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