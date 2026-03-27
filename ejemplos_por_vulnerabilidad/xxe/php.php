<?php
// XML External Entity Injection (XXE)
<?php
$dom=new DOMDocument();
$dom->loadXML(file_get_contents('php://input'), LIBXML_NOENT);
