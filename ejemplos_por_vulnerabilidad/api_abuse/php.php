<?php
// API Abuse
<?php
echo json_encode(range(1,intval($_GET['limit']??1000000)));
