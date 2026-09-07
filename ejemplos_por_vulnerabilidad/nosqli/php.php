<?php
// NoSQL Injection
$collection->findOne(json_decode(file_get_contents('php://input'),true));
