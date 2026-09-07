<?php
// Client-Side Template Injection (CSTI)
echo '<div>{{'.$_GET['expr'].'}}</div>';
