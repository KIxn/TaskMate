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
$sql = "INSERT INTO GROUPS (GROUP_NAME,GROUP_DESC,ASS_ID) VALUES (?,?,?)";

if($stmt = mysqli_prepare($link, $sql)){
    //SET PARAMETERs
    if(($grp_name=trim($_POST["group_name"]))&&($grp_desc=trim($_POST["group_desc"]))&&($stud_id=trim($_POST["stud_id"]))&&($ass_id=trim($_POST["ass_id"]))){
        // Bind variables to the prepared statement as parameters
        mysqli_stmt_bind_param($stmt, "sss",$grp_name,$grp_desc,$ass_id);

    //query db
    if(mysqli_stmt_execute($stmt)){
        //execute inserts
        //insert into group_members
        $members_ins = "INSERT INTO GROUP_MEMBERS (GROUP_ID,STUD_ID) VALUES ((SELECT MAX(GROUP_ID) FROM GROUPS),'$stud_id')";
        if(mysqli_query($link,$members_ins)){
            echo "GROUP CREATED";
            mysqli_close($link); 
        }else{
            echo "Could not add to group";
        }
        
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