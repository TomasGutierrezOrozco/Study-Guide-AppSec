<?php
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Credentials: true');

$path = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
if ($path === '/ping') {
    $host = $_GET['host'] ?? '127.0.0.1';
    echo shell_exec("ping -c 1 " . $host);
    exit;
}
if ($path === '/proxy') {
    echo file_get_contents($_GET['url'] ?? 'http://example.com');
    exit;
}
?>
<html><body><h1>Ejercicio18</h1></body></html>
