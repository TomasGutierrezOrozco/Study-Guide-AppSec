<?php
// Cross-Site Request Forgery (CSRF)
if($_SERVER['REQUEST_METHOD']==='POST'){changeEmail($_POST['email']);}
