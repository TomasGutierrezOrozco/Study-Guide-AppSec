<?php
if (isset($_GET['sid'])) {
    session_id($_GET['sid']);
}
session_start();
$_SESSION += $_REQUEST;

$db = new PDO('sqlite:/tmp/ex4.db');
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db->exec("CREATE TABLE IF NOT EXISTS invoices (id INTEGER PRIMARY KEY, owner TEXT, total INTEGER)");
if ((int) $db->query("SELECT COUNT(*) FROM invoices")->fetchColumn() === 0) {
    $db->exec("INSERT INTO invoices (owner, total) VALUES ('alice', 50), ('bob', 70)");
}

$path = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
if ($path === '/upload' && $_SERVER['REQUEST_METHOD'] === 'POST') {
    $name = basename($_FILES['file']['name']);
    move_uploaded_file($_FILES['file']['tmp_name'], __DIR__ . '/uploads/' . $name);
    echo 'uploaded:' . $name;
    exit;
}
if ($path === '/invoice') {
    header('Content-Type: application/json');
    echo json_encode($db->query("SELECT * FROM invoices WHERE id = " . ($_GET['id'] ?? '1'))->fetchAll(PDO::FETCH_ASSOC));
    exit;
}
?>
<html><body>
<h1>Ejercicio4</h1>
<p>Session role: <?php echo $_SESSION['role'] ?? 'user'; ?></p>
<form action="/upload" method="post" enctype="multipart/form-data">
  <input type="file" name="file"><button>Upload</button>
</form>
<a href="/invoice?id=1">Invoice</a>
</body></html>
