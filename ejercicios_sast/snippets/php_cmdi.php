<?php
$host = $_POST['host'];
echo shell_exec("ping -c 1 " . $host);
