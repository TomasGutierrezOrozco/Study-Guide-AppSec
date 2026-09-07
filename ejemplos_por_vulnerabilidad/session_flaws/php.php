<?php
// Session Puzzling / Fixation / Variable Overloading
if(isset($_GET['sid']))session_id($_GET['sid']);session_start();$_SESSION+=$_REQUEST;
