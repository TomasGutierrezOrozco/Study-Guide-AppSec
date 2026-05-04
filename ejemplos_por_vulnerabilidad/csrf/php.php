<?php
// Cross-Site Request Forgery (CSRF)
<?php
if($_SERVER['REQUEST_METHOD']==='POST'){changeEmail($_POST['email']);}
