# SQL Truncation

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad existe cuando la aplicacion y la base de datos interpretan de forma distinta la longitud o el contenido significativo de un valor.
La aplicacion valida un dato completo, pero la base lo recorta al insertarlo o compararlo. Esa diferencia puede causar colisiones, bypasses o confusion de identidad.

## Como identificar casos similares
- Campos cortos en base de datos y validacion mas laxa en la aplicacion.
- Comparaciones o unicidad sobre valores que luego se truncan.
- Normalizacion inconsistente de espacios, NUL o multibyte.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `username=request.form['username'][:8] create_user(username)`
En este ejemplo, lo vulnerable es depender de una validacion previa que no coincide con la forma en que la base persistira o comparara el dato. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca diferencias entre validacion de longitud/formato en aplicacion y definicion real del campo en la base.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { const username=req.body.username.slice(0,8); }`
En este ejemplo, lo vulnerable es depender de una validacion previa que no coincide con la forma en que la base persistira o comparara el dato. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca diferencias entre validacion de longitud/formato en aplicacion y definicion real del campo en la base.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { String username=request.getParameter("username").substring(0,8); } }`
En este ejemplo, lo vulnerable es depender de una validacion previa que no coincide con la forma en que la base persistira o comparara el dato. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca diferencias entre validacion de longitud/formato en aplicacion y definicion real del campo en la base.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { username:=r.FormValue("username")[:8] }`
En este ejemplo, lo vulnerable es depender de una validacion previa que no coincide con la forma en que la base persistira o comparara el dato. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca diferencias entre validacion de longitud/formato en aplicacion y definicion real del campo en la base.

### PHP (`php.php`)
Fragmento representativo: `$username=substr($_POST['username'],0,8); createUser($username);`
En este ejemplo, lo vulnerable es depender de una validacion previa que no coincide con la forma en que la base persistira o comparara el dato. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca diferencias entre validacion de longitud/formato en aplicacion y definicion real del campo en la base.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { $username = substr(param('username'), 0, 8); }`
En este ejemplo, lo vulnerable es depender de una validacion previa que no coincide con la forma en que la base persistira o comparara el dato. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca diferencias entre validacion de longitud/formato en aplicacion y definicion real del campo en la base.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Username := Copy(Request.ContentFields.Values['username'], 1, 8); end.`
En este ejemplo, lo vulnerable es depender de una validacion previa que no coincide con la forma en que la base persistira o comparara el dato. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca diferencias entre validacion de longitud/formato en aplicacion y definicion real del campo en la base.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) username = params[:username][0,8] end`
En este ejemplo, lo vulnerable es depender de una validacion previa que no coincide con la forma en que la base persistira o comparara el dato. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca diferencias entre validacion de longitud/formato en aplicacion y definicion real del campo en la base.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let username = &username[..8]; }`
En este ejemplo, lo vulnerable es depender de una validacion previa que no coincide con la forma en que la base persistira o comparara el dato. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca diferencias entre validacion de longitud/formato en aplicacion y definicion real del campo en la base.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { var username = (Request.Form["username"] ?? "").Substring(0, 8); } }`
En este ejemplo, lo vulnerable es depender de una validacion previa que no coincide con la forma en que la base persistira o comparara el dato. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca diferencias entre validacion de longitud/formato en aplicacion y definicion real del campo en la base.
