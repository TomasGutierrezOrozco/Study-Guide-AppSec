<?php
session_start();
$db = new PDO('sqlite:/tmp/ex3.db');
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$db->exec("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, username TEXT UNIQUE, email TEXT, passhash TEXT)");
if ((int) $db->query("SELECT COUNT(*) FROM users")->fetchColumn() === 0) {
    $db->exec("INSERT INTO users (username, email, passhash) VALUES ('admin','admin@example.com','0e123456')");
}

$path = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
if ($path === '/login' && $_SERVER['REQUEST_METHOD'] === 'POST') {
    $row = $db->query("SELECT username, passhash FROM users WHERE username = '" . ($_POST['user'] ?? '') . "'")->fetch(PDO::FETCH_ASSOC);
    echo ($row && md5($_POST['password'] ?? '') == $row['passhash']) ? 'ok' : 'invalid';
    exit;
}

if ($path === '/change-email' && $_SERVER['REQUEST_METHOD'] === 'POST') {
    $db->exec("UPDATE users SET email = " . $db->quote($_POST['email'] ?? '') . " WHERE username = 'admin'");
    echo 'updated';
    exit;
}

if ($path === '/register' && $_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = substr($_POST['username'] ?? '', 0, 8);
    $db->exec("INSERT INTO users (username, email, passhash) VALUES (" . $db->quote($username) . "," . $db->quote($_POST['email'] ?? '') . ",'0e1')");
    echo 'created';
    exit;
}
?>
<html><body>
<h1>Ejercicio3</h1>
<form action="/login" method="post">
  <input name="user" value="admin"><input name="password" value="test"><button>Login</button>
</form>
<form action="/change-email" method="post">
  <input name="email" value="new@example.com"><button>Change</button>
</form>
<form action="/register" method="post">
  <input name="username" value="adminAAAA"><input name="email" value="x@x"><button>Register</button>
</form>
</body></html>
