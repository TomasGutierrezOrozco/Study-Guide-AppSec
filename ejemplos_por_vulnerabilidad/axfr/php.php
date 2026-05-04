<?php
// AXFR Full Zone Transfer
<?php
shell_exec('dig axfr '.$_GET['domain']);
