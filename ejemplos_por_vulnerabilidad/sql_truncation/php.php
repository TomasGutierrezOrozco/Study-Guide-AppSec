<?php
// SQL Truncation
<?php
$username=substr($_POST['username'],0,8);
createUser($username);
