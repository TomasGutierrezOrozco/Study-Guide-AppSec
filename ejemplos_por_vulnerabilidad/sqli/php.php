<?php
// SQL Injection (SQLI)
<?php
$id=$_GET['id'];
$db->query("SELECT * FROM users WHERE id=$id");
