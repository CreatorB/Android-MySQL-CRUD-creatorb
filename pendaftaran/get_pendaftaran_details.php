<?php

/*
 * kode untuk single pendaftaran
 */

$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';

// konekin ke db
$db = new DB_CONNECT();

// cek data
if (isset($_GET["pid"])) {
    $pid = $_GET['pid'];

    $result = mysql_query("SELECT *FROM pendaftaran WHERE pid = $pid");

    if (!empty($result)) {
        // cek kesediaan data
        if (mysql_num_rows($result) > 0) {

            $result = mysql_fetch_array($result);

            $pendaftaran = array();
            $pendaftaran["pid"] = $result["pid"];
            $pendaftaran["name"] = $result["name"];
            $pendaftaran["email"] = $result["email"];
            $pendaftaran["description"] = $result["description"];
            $pendaftaran["created_at"] = $result["created_at"];
            $pendaftaran["updated_at"] = $result["updated_at"];
            // maka
            $response["success"] = 1;

            // node
            $response["pendaftaran"] = array();

            array_push($response["pendaftaran"], $pendaftaran);

            // echoing JSON response
            echo json_encode($response);
        } else {
            // jika kosong
            $response["success"] = 0;
            $response["message"] = "Tidak ada data";

            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        $response["success"] = 0;
        $response["message"] = "Tidak ada data";

        echo json_encode($response);
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Silahkan lengkapi permintaan anda";

    echo json_encode($response);
}
?>
