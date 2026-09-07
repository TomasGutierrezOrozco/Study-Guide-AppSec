<?php
// Prototype Pollution
$config=array_merge($config,json_decode(file_get_contents('php://input'),true));
