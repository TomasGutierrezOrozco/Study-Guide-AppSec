<?php
if (isset($_GET['sid'])) {
    session_id($_GET['sid']);
}
session_start();

$profiles = [
    1 => ['user' => 'alice', 'plan' => 'basic'],
    2 => ['user' => 'bob', 'plan' => 'premium'],
];

$path = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
if ($path === '/change-plan' && $_SERVER['REQUEST_METHOD'] === 'POST') {
    $_SESSION['plan'] = $_POST['plan'] ?? 'basic';
    echo 'updated';
    exit;
}
if ($path === '/profile') {
    header('Content-Type: application/json');
    echo json_encode($profiles[(int) ($_GET['id'] ?? 1)] ?? []);
    exit;
}
?>
<html><body>
<h1>Ejercicio25</h1>
<p>Plan actual: <?php echo $_SESSION['plan'] ?? 'basic'; ?></p>
<form action="/change-plan" method="post">
  <input name="plan" value="premium"><button>Change</button>
</form>
</body></html>
