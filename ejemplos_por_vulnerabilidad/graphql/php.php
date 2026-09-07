<?php
// GraphQL Introspection, Mutation and IDOR
$query=file_get_contents('php://input');
executeGraphql($query);
