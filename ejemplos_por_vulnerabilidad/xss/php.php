<?php
// Cross-Site Scripting (XSS)
$q=$_GET['q']??'';
echo "<h1>$q</h1>";
