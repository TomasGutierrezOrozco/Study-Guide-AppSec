<?php
// SQL Truncation
$username=substr($_POST['username'],0,8);
createUser($username);
