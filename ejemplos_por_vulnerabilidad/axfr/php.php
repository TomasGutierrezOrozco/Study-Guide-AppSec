<?php
// AXFR Full Zone Transfer
shell_exec('dig axfr '.$_GET['domain']);
