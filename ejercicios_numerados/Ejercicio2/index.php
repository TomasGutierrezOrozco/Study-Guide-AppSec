<?php
$ua = $_SERVER['HTTP_USER_AGENT'] ?? 'unknown';
file_put_contents(__DIR__ . '/logs/access.log', $ua . PHP_EOL, FILE_APPEND);

$path = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
if ($path === '/include') {
    include($_GET['page'] ?? 'home.php');
    exit;
}
?>
<html><body>
<h1>Ejercicio2</h1>
<ul>
  <li>/include?page=home.php</li>
</ul>
</body></html>
