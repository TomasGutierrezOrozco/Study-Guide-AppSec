<?php
// JWT Enumeration and Exploitation
<?php
$payload=json_decode(base64_decode(explode('.',$_SERVER['HTTP_AUTHORIZATION'])[1]),true);
