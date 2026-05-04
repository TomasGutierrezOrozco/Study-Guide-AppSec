# IDOR

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad existe cuando el cliente puede elegir el identificador de un recurso y el servidor no verifica si tiene permiso para accederlo.
El codigo confunde conocer un `id` con estar autorizado para usarlo. Cambiar un identificador en la URL, el body o la query alcanza para leer o modificar recursos de otro usuario.

## Como identificar casos similares
- Busquedas directas por identificador recibido desde request.
- Falta de filtros por usuario actual, tenant o rol.
- Operaciones tipo `find(id)` sin chequeo posterior de ownership.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `return db.execute('SELECT * FROM invoices WHERE id=?',(request.view_args['id'],)).fetchone()`
En este ejemplo, lo vulnerable es confiar en el identificador que provee el cliente sin ligar el recurso al usuario autenticado o a su contexto. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca consultas por ID y acciones sobre recursos donde el backend no compare ownership ni permisos despues de resolver el objeto.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { res.json(invoices[req.params.id]); }`
En este ejemplo, lo vulnerable es confiar en el identificador que provee el cliente sin ligar el recurso al usuario autenticado o a su contexto. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca consultas por ID y acciones sobre recursos donde el backend no compare ownership ni permisos despues de resolver el objeto.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { invoiceService.findById(Long.parseLong(request.getParameter("id"))); } }`
En este ejemplo, lo vulnerable es confiar en el identificador que provee el cliente sin ligar el recurso al usuario autenticado o a su contexto. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca consultas por ID y acciones sobre recursos donde el backend no compare ownership ni permisos despues de resolver el objeto.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { db.QueryRow("SELECT * FROM invoices WHERE id=?",r.URL.Query().Get("id")) }`
En este ejemplo, lo vulnerable es confiar en el identificador que provee el cliente sin ligar el recurso al usuario autenticado o a su contexto. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca consultas por ID y acciones sobre recursos donde el backend no compare ownership ni permisos despues de resolver el objeto.

### PHP (`php.php`)
Fragmento representativo: `echo json_encode(getInvoice($_GET['id']));`
En este ejemplo, lo vulnerable es confiar en el identificador que provee el cliente sin ligar el recurso al usuario autenticado o a su contexto. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca consultas por ID y acciones sobre recursos donde el backend no compare ownership ni permisos despues de resolver el objeto.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { print encode_json(get_invoice(param('id'))); }`
En este ejemplo, lo vulnerable es confiar en el identificador que provee el cliente sin ligar el recurso al usuario autenticado o a su contexto. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca consultas por ID y acciones sobre recursos donde el backend no compare ownership ni permisos despues de resolver el objeto.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Response.Content := GetInvoice(Request.QueryFields.Values['id']); end.`
En este ejemplo, lo vulnerable es confiar en el identificador que provee el cliente sin ligar el recurso al usuario autenticado o a su contexto. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca consultas por ID y acciones sobre recursos donde el backend no compare ownership ni permisos despues de resolver el objeto.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) render json: Invoice.find(params[:id]) end`
En este ejemplo, lo vulnerable es confiar en el identificador que provee el cliente sin ligar el recurso al usuario autenticado o a su contexto. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca consultas por ID y acciones sobre recursos donde el backend no compare ownership ni permisos despues de resolver el objeto.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let invoice = find_invoice(id); }`
En este ejemplo, lo vulnerable es confiar en el identificador que provee el cliente sin ligar el recurso al usuario autenticado o a su contexto. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca consultas por ID y acciones sobre recursos donde el backend no compare ownership ni permisos despues de resolver el objeto.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { return Json(invoices[int.Parse(Request.Query["id"])]); } }`
En este ejemplo, lo vulnerable es confiar en el identificador que provee el cliente sin ligar el recurso al usuario autenticado o a su contexto. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca consultas por ID y acciones sobre recursos donde el backend no compare ownership ni permisos despues de resolver el objeto.
