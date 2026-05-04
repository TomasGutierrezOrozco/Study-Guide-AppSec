<?php
$db = new PDO('sqlite:/tmp/ex1.db');
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db->exec("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, username TEXT UNIQUE, email TEXT)");
if ((int) $db->query("SELECT COUNT(*) FROM users")->fetchColumn() === 0) {
    $db->exec("INSERT INTO users (username, email) VALUES ('admin','admin@example.com'), ('alice','alice@example.com')");
}

$path = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);

if ($path === '/user') {
    $id = $_GET['id'] ?? '1';
    header('Content-Type: application/json');
    echo json_encode($db->query("SELECT id, username, email FROM users WHERE id = " . $id)->fetchAll(PDO::FETCH_ASSOC));
    exit;
}

if ($path === '/search') {
    $q = $_GET['q'] ?? '';
    echo "<h1>Resultados: $q</h1>";
    exit;
}

if ($path === '/redirect') {
    header('Location: ' . ($_GET['next'] ?? '/'));
    exit;
}
?>
<html><body>
<h1>Ejercicio1</h1>
<ul>
  <li>/user?id=1</li>
  <li>/search?q=test</li>
  <li>/redirect?next=https://example.com</li>
</ul>
</body></html>
