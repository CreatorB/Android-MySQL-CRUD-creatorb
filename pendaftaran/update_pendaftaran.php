<?php

/*
 * syntax untuk update form pendaftaran
 */

// array JSON response
$response = array();

if (isset($_POST['pid']) && isset($_POST['name']) && isset($_POST['email']) && isset($_POST['description'])) {
    
    $pid = $_POST['pid'];
    $name = $_POST['name'];
    $email = $_POST['email'];
    $description = $_POST['description'];

    // include db connect
    require_once __DIR__ . '/db_connect.php';

    // konek ke db
    $db = new DB_CONNECT();

    // update pendaftaran by pendaftaran id (PID)
    $result = mysql_query("UPDATE pendaftaran SET name = '$name', email = '$email', description = '$description' WHERE pid = $pid");

    // cek data sudah masuk apa belum
    if ($result) {
        // kalo sukses
        $response["success"] = 1;
        $response["message"] = "Data anda berhasil di perbarui";
        
        // echo JSON response
        echo json_encode($response);
    } else {
        
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Mohon kelengkapan data anda";

    // echoJSON response
    echo json_encode($response);
}
?>
