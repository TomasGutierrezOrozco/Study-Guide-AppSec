<?php
$id = $_GET['id'];
$sql = "SELECT * FROM users WHERE id = " . $id;
$result = $db->query($sql);
echo json_encode($result->fetchAll());
