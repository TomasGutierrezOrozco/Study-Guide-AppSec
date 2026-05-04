# GraphQL Introspection, Mutation and IDOR

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
El patron combina exposicion excesiva de capacidades GraphQL con falta de autorizacion por query, campo o recurso.
Aceptar consultas y mutaciones arbitrarias sin controles permite introspection, enumeracion de esquema y acceso a objetos por identificadores manipulables. El riesgo suele terminar en IDOR o abuso de consultas costosas.

## Como identificar casos similares
- Resolvers que confian en `id` sin verificar ownership.
- Introspection habilitada en produccion sin necesidad.
- Falta de limites de complejidad, profundidad o autorizacion por resolver.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `query=request.get_json()['query'] execute_graphql(query)`
En este ejemplo, lo vulnerable es aceptar la consulta GraphQL como si toda operacion pedida por el cliente fuera legitima. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca resolvers que ejecuten queries/mutations directamente desde la entrada sin checks por recurso, complejidad o contexto.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { graphql(schema,req.body.query); }`
En este ejemplo, lo vulnerable es aceptar la consulta GraphQL como si toda operacion pedida por el cliente fuera legitima. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca resolvers que ejecuten queries/mutations directamente desde la entrada sin checks por recurso, complejidad o contexto.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { graphql.execute(body); } }`
En este ejemplo, lo vulnerable es aceptar la consulta GraphQL como si toda operacion pedida por el cliente fuera legitima. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca resolvers que ejecuten queries/mutations directamente desde la entrada sin checks por recurso, complejidad o contexto.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { executeGraphQL(query) }`
En este ejemplo, lo vulnerable es aceptar la consulta GraphQL como si toda operacion pedida por el cliente fuera legitima. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca resolvers que ejecuten queries/mutations directamente desde la entrada sin checks por recurso, complejidad o contexto.

### PHP (`php.php`)
Fragmento representativo: `$query=file_get_contents('php://input'); executeGraphql($query);`
En este ejemplo, lo vulnerable es aceptar la consulta GraphQL como si toda operacion pedida por el cliente fuera legitima. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca resolvers que ejecuten queries/mutations directamente desde la entrada sin checks por recurso, complejidad o contexto.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { $schema->execute($query); }`
En este ejemplo, lo vulnerable es aceptar la consulta GraphQL como si toda operacion pedida por el cliente fuera legitima. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca resolvers que ejecuten queries/mutations directamente desde la entrada sin checks por recurso, complejidad o contexto.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Query := Request.Content; end.`
En este ejemplo, lo vulnerable es aceptar la consulta GraphQL como si toda operacion pedida por el cliente fuera legitima. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca resolvers que ejecuten queries/mutations directamente desde la entrada sin checks por recurso, complejidad o contexto.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) result = Schema.execute(params[:query]) end`
En este ejemplo, lo vulnerable es aceptar la consulta GraphQL como si toda operacion pedida por el cliente fuera legitima. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca resolvers que ejecuten queries/mutations directamente desde la entrada sin checks por recurso, complejidad o contexto.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let response = schema.execute(query).await; }`
En este ejemplo, lo vulnerable es aceptar la consulta GraphQL como si toda operacion pedida por el cliente fuera legitima. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca resolvers que ejecuten queries/mutations directamente desde la entrada sin checks por recurso, complejidad o contexto.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { var result = schema.Execute(_ => _.Query = query); } }`
En este ejemplo, lo vulnerable es aceptar la consulta GraphQL como si toda operacion pedida por el cliente fuera legitima. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca resolvers que ejecuten queries/mutations directamente desde la entrada sin checks por recurso, complejidad o contexto.
