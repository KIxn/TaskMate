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
$sql = "INSERT INTO USERS (EMAIL,PSWD,PERM) VALUES (?,?,?)";

if($stmt = mysqli_prepare($link, $sql)){
    //SET PARAMETERs
    if(($email = trim($_POST['email']))&&($pswd = trim($_POST['pswd']))&&($perm = trim($_POST['perm']))){
        // Bind variables to the prepared statement as parameters
        mysqli_stmt_bind_param($stmt, "sss", $email,$pswd,$perm);

    //query db
    if(mysqli_stmt_execute($stmt)){
        //execute inserts
            echo "User Successfully Created; Please Remember Your Credentials";
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