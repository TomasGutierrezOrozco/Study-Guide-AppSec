<?php
// Cross-Site Scripting (XSS)
<?php
$q=$_GET['q']??'';
echo "<h1>$q</h1>";
