<?php
// GraphQL Introspection, Mutation and IDOR
<?php
$query=file_get_contents('php://input');
executeGraphql($query);
