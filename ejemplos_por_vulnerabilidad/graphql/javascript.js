// GraphQL Introspection, Mutation and IDOR
function demo(req, res) {
  graphql(schema,req.body.query);
  }
