<?php

/*
 * Buat pendaftaran baru
 */

$response = array();

// cek form
if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['description'])) {
    
    $name = $_POST['name'];
    $email = $_POST['email'];
    $description = $_POST['description'];

    // include db connect
    require_once __DIR__ . '/db_connect.php';

    // konekin db
    $db = new DB_CONNECT();

    // insert ke db
    $result = mysql_query("INSERT INTO pendaftaran(name, email, description) VALUES('$name', '$email', '$description')");

    // cek data udah masuk belum
    if ($result) {
        // kalo sukses
        $response["success"] = 1;
        $response["message"] = "Pendaftaran anda berhasil";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // fkalo gagal
        $response["success"] = 0;
        $response["message"] = "Sistem mendeteksi kesalahan, silahkan coba lagi";
        
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Silahkan lengkapi aksi sebelum memulai permintaan anda";

    // echoing JSON response
    echo json_encode($response);
}
?>
