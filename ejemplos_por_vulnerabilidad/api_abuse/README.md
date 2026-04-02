# API Abuse

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
El patron vulnerable aparece cuando la API expone acciones sensibles sin controles de autorizacion, limites de uso o validaciones de negocio suficientes.
El backend acepta la operacion tal como llega y asume que el cliente usara el flujo correcto. Eso permite scraping, automatizacion abusiva, fuerza bruta o consumo de acciones de alto impacto fuera de contexto.

## Como identificar casos similares
- Endpoints potentes sin rate limiting, cuotas ni alertas.
- Acciones criticas protegidas solo por la UI o por conocer la ruta.
- Reglas de negocio aplicadas en frontend y no en servidor.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `return {'items':list(range(int(request.args.get('limit','1000000'))))}`
En este ejemplo, lo vulnerable es que el ejemplo deja que el cliente controle una accion o un flujo que deberia estar restringido por autorizacion, cuota o contexto de negocio. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca endpoints que acepten la operacion completa desde el cliente y no verifiquen permisos, frecuencia ni reglas de negocio en el servidor.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { res.json({items:[...Array(Number(req.query.limit||1000000)).keys()]}); }`
En este ejemplo, lo vulnerable es que el ejemplo deja que el cliente controle una accion o un flujo que deberia estar restringido por autorizacion, cuota o contexto de negocio. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca endpoints que acepten la operacion completa desde el cliente y no verifiquen permisos, frecuencia ni reglas de negocio en el servidor.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { int limit=Integer.parseInt(request.getParameter("limit")); } }`
En este ejemplo, lo vulnerable es que el ejemplo deja que el cliente controle una accion o un flujo que deberia estar restringido por autorizacion, cuota o contexto de negocio. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca endpoints que acepten la operacion completa desde el cliente y no verifiquen permisos, frecuencia ni reglas de negocio en el servidor.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { limit,_:=strconv.Atoi(r.URL.Query().Get("limit")) }`
En este ejemplo, lo vulnerable es que el ejemplo deja que el cliente controle una accion o un flujo que deberia estar restringido por autorizacion, cuota o contexto de negocio. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca endpoints que acepten la operacion completa desde el cliente y no verifiquen permisos, frecuencia ni reglas de negocio en el servidor.

### PHP (`php.php`)
Fragmento representativo: `echo json_encode(range(1,intval($_GET['limit']??1000000)));`
En este ejemplo, lo vulnerable es que el ejemplo deja que el cliente controle una accion o un flujo que deberia estar restringido por autorizacion, cuota o contexto de negocio. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca endpoints que acepten la operacion completa desde el cliente y no verifiquen permisos, frecuencia ni reglas de negocio en el servidor.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { @items = (1..(param('limit') || 1000000)); }`
En este ejemplo, lo vulnerable es que el ejemplo deja que el cliente controle una accion o un flujo que deberia estar restringido por autorizacion, cuota o contexto de negocio. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca endpoints que acepten la operacion completa desde el cliente y no verifiquen permisos, frecuencia ni reglas de negocio en el servidor.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Limit := StrToIntDef(Request.QueryFields.Values['limit'], 1000000); end.`
En este ejemplo, lo vulnerable es que el ejemplo deja que el cliente controle una accion o un flujo que deberia estar restringido por autorizacion, cuota o contexto de negocio. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca endpoints que acepten la operacion completa desde el cliente y no verifiquen permisos, frecuencia ni reglas de negocio en el servidor.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) render json: (1..params.fetch(:limit, 1_000_000).to_i).to_a end`
En este ejemplo, lo vulnerable es que el ejemplo deja que el cliente controle una accion o un flujo que deberia estar restringido por autorizacion, cuota o contexto de negocio. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca endpoints que acepten la operacion completa desde el cliente y no verifiquen permisos, frecuencia ni reglas de negocio en el servidor.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let limit: usize = limit.parse().unwrap_or(1_000_000); }`
En este ejemplo, lo vulnerable es que el ejemplo deja que el cliente controle una accion o un flujo que deberia estar restringido por autorizacion, cuota o contexto de negocio. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca endpoints que acepten la operacion completa desde el cliente y no verifiquen permisos, frecuencia ni reglas de negocio en el servidor.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { var limit = int.Parse(Request.Query["limit"] ?? "1000000"); } }`
En este ejemplo, lo vulnerable es que el ejemplo deja que el cliente controle una accion o un flujo que deberia estar restringido por autorizacion, cuota o contexto de negocio. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca endpoints que acepten la operacion completa desde el cliente y no verifiquen permisos, frecuencia ni reglas de negocio en el servidor.
