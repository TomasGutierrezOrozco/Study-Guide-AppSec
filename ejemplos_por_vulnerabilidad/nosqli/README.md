# NoSQL Injection

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad ocurre cuando la aplicacion construye consultas NoSQL con datos del usuario sin imponer tipo, operador o estructura esperada.
Aunque no haya SQL, el motor sigue interpretando operadores y expresiones. Si el atacante puede inyectar objetos como `$ne` o `$gt`, altera la semantica de la consulta.

## Como identificar casos similares
- Uso directo de objetos JSON del request en filtros o busquedas.
- Aceptacion de operadores NoSQL enviados por el cliente.
- Transformacion de strings a documentos de consulta sin esquema estricto.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `mongo.db.users.find_one(request.get_json())`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario defina no solo el valor consultado sino tambien operadores y estructura de la query. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca filtros construidos a partir de objetos del cliente y uso directo de operadores del motor NoSQL.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { db.users.findOne(req.body); }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario defina no solo el valor consultado sino tambien operadores y estructura de la query. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca filtros construidos a partir de objetos del cliente y uso directo de operadores del motor NoSQL.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { collection.find(new Document(request.getParameterMap())); } }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario defina no solo el valor consultado sino tambien operadores y estructura de la query. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca filtros construidos a partir de objetos del cliente y uso directo de operadores del motor NoSQL.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { json.NewDecoder(r.Body).Decode(&filter) }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario defina no solo el valor consultado sino tambien operadores y estructura de la query. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca filtros construidos a partir de objetos del cliente y uso directo de operadores del motor NoSQL.

### PHP (`php.php`)
Fragmento representativo: `$collection->findOne(json_decode(file_get_contents('php://input'),true));`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario defina no solo el valor consultado sino tambien operadores y estructura de la query. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca filtros construidos a partir de objetos del cliente y uso directo de operadores del motor NoSQL.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { $collection->find_one(decode_json($body)); }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario defina no solo el valor consultado sino tambien operadores y estructura de la query. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca filtros construidos a partir de objetos del cliente y uso directo de operadores del motor NoSQL.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Filter := Request.Content; end.`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario defina no solo el valor consultado sino tambien operadores y estructura de la query. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca filtros construidos a partir de objetos del cliente y uso directo de operadores del motor NoSQL.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) User.where(params.permit!).first end`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario defina no solo el valor consultado sino tambien operadores y estructura de la query. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca filtros construidos a partir de objetos del cliente y uso directo de operadores del motor NoSQL.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let filter: serde_json::Value = serde_json::from_str(body)?; }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario defina no solo el valor consultado sino tambien operadores y estructura de la query. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca filtros construidos a partir de objetos del cliente y uso directo de operadores del motor NoSQL.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { collection.Find(BsonDocument.Parse(body)).FirstOrDefault(); } }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario defina no solo el valor consultado sino tambien operadores y estructura de la query. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca filtros construidos a partir de objetos del cliente y uso directo de operadores del motor NoSQL.
