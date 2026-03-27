<?php
$db = new PDO('sqlite:/tmp/study.db');
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db->exec("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, email TEXT)");
$count = $db->query("SELECT COUNT(*) FROM users")->fetchColumn();
if ((int)$count === 0) {
    $db->exec("INSERT INTO users (email) VALUES ('alice@example.com'), ('bob@example.com'), ('admin@example.com')");
}

$path = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);

if ($path === '/ping') {
    $ip = $_GET['ip'] ?? '127.0.0.1';
    header('Content-Type: text/plain');
    echo shell_exec("ping -c 1 " . $ip);
    exit;
}

if ($path === '/user') {
    $id = $_GET['id'] ?? '1';
    $sql = "SELECT id, email FROM users WHERE id = " . $id;
    $rows = $db->query($sql)->fetchAll(PDO::FETCH_ASSOC);
    header('Content-Type: application/json');
    echo json_encode($rows);
    exit;
}
?>
<html>
  <body>
    <h1>PHP Lab</h1>
    <ul>
      <li><a href="/ping?ip=127.0.0.1">/ping?ip=127.0.0.1</a></li>
      <li><a href="/user?id=1">/user?id=1</a></li>
    </ul>
  </body>
</html>
