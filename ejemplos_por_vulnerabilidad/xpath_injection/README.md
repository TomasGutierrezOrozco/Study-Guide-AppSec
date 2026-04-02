# XPath Injection

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad aparece cuando el codigo construye expresiones XPath con concatenacion de entrada no confiable.
Igual que en SQL, el dato del usuario pasa a formar parte de la consulta. Eso permite alterar predicados, navegar nodos no previstos o saltar comprobaciones logicas.

## Como identificar casos similares
- Construccion manual de expresiones XPath con variables del request.
- Consultas XML para login, busqueda o filtrado sin escape.
- Uso de comillas, operadores o funciones XPath provenientes del usuario.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `expr=f"//user[name='{request.args['user']}']"`
En este ejemplo, lo vulnerable es permitir que el atacante altere la estructura de la expresion XPath, no solo el valor consultado. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca concatenacion de strings para construir expresiones XPath antes de evaluarlas.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { const expr=`//user[name='${req.query.user}']`; }`
En este ejemplo, lo vulnerable es permitir que el atacante altere la estructura de la expresion XPath, no solo el valor consultado. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca concatenacion de strings para construir expresiones XPath antes de evaluarlas.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { String expr="//user[name='"+request.getParameter("user")+"']"; } }`
En este ejemplo, lo vulnerable es permitir que el atacante altere la estructura de la expresion XPath, no solo el valor consultado. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca concatenacion de strings para construir expresiones XPath antes de evaluarlas.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { }`
En este ejemplo, lo vulnerable es permitir que el atacante altere la estructura de la expresion XPath, no solo el valor consultado. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca concatenacion de strings para construir expresiones XPath antes de evaluarlas.

### PHP (`php.php`)
Fragmento representativo: `$expr="//user[name='".$_GET['user']."']";`
En este ejemplo, lo vulnerable es permitir que el atacante altere la estructura de la expresion XPath, no solo el valor consultado. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca concatenacion de strings para construir expresiones XPath antes de evaluarlas.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { $expr = "//user[name='" . param('user') . "']"; }`
En este ejemplo, lo vulnerable es permitir que el atacante altere la estructura de la expresion XPath, no solo el valor consultado. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca concatenacion de strings para construir expresiones XPath antes de evaluarlas.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Expr := '//user[name=''' + Request.QueryFields.Values['user'] + ''']'; end.`
En este ejemplo, lo vulnerable es permitir que el atacante altere la estructura de la expresion XPath, no solo el valor consultado. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca concatenacion de strings para construir expresiones XPath antes de evaluarlas.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) xpath = "//user[name='#{params[:user]}']" end`
En este ejemplo, lo vulnerable es permitir que el atacante altere la estructura de la expresion XPath, no solo el valor consultado. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca concatenacion de strings para construir expresiones XPath antes de evaluarlas.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let expr = format!("//user[name='{}']", user); }`
En este ejemplo, lo vulnerable es permitir que el atacante altere la estructura de la expresion XPath, no solo el valor consultado. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca concatenacion de strings para construir expresiones XPath antes de evaluarlas.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { var expr = "//user[name='" + Request.Query["user"] + "']"; } }`
En este ejemplo, lo vulnerable es permitir que el atacante altere la estructura de la expresion XPath, no solo el valor consultado. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca concatenacion de strings para construir expresiones XPath antes de evaluarlas.
