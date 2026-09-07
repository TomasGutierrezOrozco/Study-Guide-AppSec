<?php
// SQL Injection (SQLI)
$id=$_GET['id'];
$db->query("SELECT * FROM users WHERE id=$id");
