<?php
// Server-Side Template Injection (SSTI)
eval('?>'.$_GET['tpl']);
