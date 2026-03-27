<?php
// Server-Side Request Forgery (SSRF)
<?php
echo file_get_contents($_GET['url']);
