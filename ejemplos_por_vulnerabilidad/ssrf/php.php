<?php
// Server-Side Request Forgery (SSRF)
echo file_get_contents($_GET['url']);
