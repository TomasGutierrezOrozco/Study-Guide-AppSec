# SQL Injection (SQLI)

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad aparece cuando la aplicacion construye sentencias SQL mezclando codigo de consulta con datos controlados por el usuario.
El motor SQL interpreta ese dato como parte de la sentencia, no como un valor aislado. El atacante puede alterar filtros, unir tablas, cambiar el orden o ejecutar operaciones no previstas.

## Como identificar casos similares
- Concatenacion o interpolacion dentro de `SELECT`, `INSERT`, `UPDATE` o `DELETE`.
- Uso de consultas raw sin placeholders.
- Parametros de `WHERE`, `ORDER BY`, columnas o tablas construidos desde input.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `query=f"SELECT * FROM users WHERE id={request.args['id']}" conn.execute(query)`
En este ejemplo, lo vulnerable es insertar datos del usuario dentro de la propia estructura SQL en lugar de enviarlos como parametros. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca concatenacion, interpolacion o formateo de strings antes de llamar a `execute`, `query` o APIs equivalentes.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { const sql=`SELECT * FROM users WHERE id=${req.query.id}`; }`
En este ejemplo, lo vulnerable es insertar datos del usuario dentro de la propia estructura SQL en lugar de enviarlos como parametros. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca concatenacion, interpolacion o formateo de strings antes de llamar a `execute`, `query` o APIs equivalentes.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { String sql="SELECT * FROM users WHERE id="+request.getParameter("id"); } }`
En este ejemplo, lo vulnerable es insertar datos del usuario dentro de la propia estructura SQL en lugar de enviarlos como parametros. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca concatenacion, interpolacion o formateo de strings antes de llamar a `execute`, `query` o APIs equivalentes.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { query:="SELECT * FROM users WHERE id="+r.URL.Query().Get("id") }`
En este ejemplo, lo vulnerable es insertar datos del usuario dentro de la propia estructura SQL en lugar de enviarlos como parametros. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca concatenacion, interpolacion o formateo de strings antes de llamar a `execute`, `query` o APIs equivalentes.

### PHP (`php.php`)
Fragmento representativo: `$id=$_GET['id']; $db->query("SELECT * FROM users WHERE id=$id");`
En este ejemplo, lo vulnerable es insertar datos del usuario dentro de la propia estructura SQL en lugar de enviarlos como parametros. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca concatenacion, interpolacion o formateo de strings antes de llamar a `execute`, `query` o APIs equivalentes.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { $sql = "SELECT * FROM users WHERE id = $id"; }`
En este ejemplo, lo vulnerable es insertar datos del usuario dentro de la propia estructura SQL en lugar de enviarlos como parametros. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca concatenacion, interpolacion o formateo de strings antes de llamar a `execute`, `query` o APIs equivalentes.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Query.SQL.Text := 'SELECT * FROM users WHERE id = ' + ParamStr(1); end.`
En este ejemplo, lo vulnerable es insertar datos del usuario dentro de la propia estructura SQL en lugar de enviarlos como parametros. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca concatenacion, interpolacion o formateo de strings antes de llamar a `execute`, `query` o APIs equivalentes.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) sql = "SELECT * FROM users WHERE id = #{params[:id]}" end`
En este ejemplo, lo vulnerable es insertar datos del usuario dentro de la propia estructura SQL en lugar de enviarlos como parametros. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca concatenacion, interpolacion o formateo de strings antes de llamar a `execute`, `query` o APIs equivalentes.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let sql = format!("SELECT * FROM users WHERE id = {}", id); }`
En este ejemplo, lo vulnerable es insertar datos del usuario dentro de la propia estructura SQL en lugar de enviarlos como parametros. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca concatenacion, interpolacion o formateo de strings antes de llamar a `execute`, `query` o APIs equivalentes.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { var sql = "SELECT * FROM users WHERE id = " + id; } }`
En este ejemplo, lo vulnerable es insertar datos del usuario dentro de la propia estructura SQL en lugar de enviarlos como parametros. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca concatenacion, interpolacion o formateo de strings antes de llamar a `execute`, `query` o APIs equivalentes.
