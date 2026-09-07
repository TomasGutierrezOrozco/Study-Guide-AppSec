<?php
// Log Poisoning (LFI a RCE)
file_put_contents('access.log', $_SERVER['HTTP_USER_AGENT'].PHP_EOL, FILE_APPEND);
include($_GET['page']);
