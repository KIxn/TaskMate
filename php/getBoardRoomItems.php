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
$sql = "SELECT GROUP_NAME,ASS_NAME,MARK FROM GROUPS,ASSIGNMENTS WHERE GROUPS.ASS_ID=ASSIGNMENTS.ASS_ID AND GROUPS.GROUP_ID IN (SELECT GROUP_ID FROM GROUP_MEMBERS WHERE STUD_ID=?)";

if($stmt = mysqli_prepare($link, $sql)){
    //SET PARAMETERs
    if($stud_id=trim($_GET["stud_id"])){
        // Bind variables to the prepared statement as parameters
        mysqli_stmt_bind_param($stmt, "s",$stud_id);
    //query db
        if(mysqli_stmt_execute($stmt)){
            //array to be returned
            $output=array();
            /*Select queries return a resultset*/
            //get result
            if($result = mysqli_stmt_get_result($stmt)){
                //loop through result object
                while($row = $result->fetch_assoc()){
                    $output[]=$row;
                }
            
                mysqli_close($link);

                echo json_encode($output);
            }else{
                echo "ERROR: Could not fetch results";
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