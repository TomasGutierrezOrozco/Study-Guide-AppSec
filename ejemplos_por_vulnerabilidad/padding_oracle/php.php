<?php
// Padding Oracle
if(!openssl_decrypt($_GET['token'],'AES-128-CBC',$key,0,$iv)){echo 'bad padding';}
