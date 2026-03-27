<?php
// Server-Side Template Injection (SSTI)
<?php
eval('?>'.$_GET['tpl']);
