# Server-Side Template Injection (SSTI)

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad aparece cuando entrada del usuario se interpreta dentro de una plantilla renderizada en el servidor.
El usuario no solo controla contenido, sino parte de la logica del motor de plantillas. En motores expresivos, eso puede terminar en lectura de secretos o RCE.

## Como identificar casos similares
- Construccion de templates mediante concatenacion.
- Funciones que hacen `render(user_input)` o compilan cadenas arbitrarias.
- Falta de separacion entre plantilla fija y contenido escapado.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `return render_template_string(request.args['tpl'])`
En este ejemplo, lo vulnerable es ejecutar el motor de templates sobre una cadena que ya contiene entrada no confiable como parte de la plantilla. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca compilacion o render de templates creados desde strings del usuario en lugar de plantillas predefinidas.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { res.send(ejs.render(req.query.tpl,{})); }`
En este ejemplo, lo vulnerable es ejecutar el motor de templates sobre una cadena que ya contiene entrada no confiable como parte de la plantilla. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca compilacion o render de templates creados desde strings del usuario en lugar de plantillas predefinidas.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { engine.process(request.getParameter("tpl"), context, writer); } }`
En este ejemplo, lo vulnerable es ejecutar el motor de templates sobre una cadena que ya contiene entrada no confiable como parte de la plantilla. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca compilacion o render de templates creados desde strings del usuario en lugar de plantillas predefinidas.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { template.New("x").Parse(r.URL.Query().Get("tpl")) }`
En este ejemplo, lo vulnerable es ejecutar el motor de templates sobre una cadena que ya contiene entrada no confiable como parte de la plantilla. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca compilacion o render de templates creados desde strings del usuario en lugar de plantillas predefinidas.

### PHP (`php.php`)
Fragmento representativo: `eval('?>'.$_GET['tpl']);`
En este ejemplo, lo vulnerable es ejecutar el motor de templates sobre una cadena que ya contiene entrada no confiable como parte de la plantilla. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca compilacion o render de templates creados desde strings del usuario en lugar de plantillas predefinidas.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { $tt->process(\$tpl, \%vars); }`
En este ejemplo, lo vulnerable es ejecutar el motor de templates sobre una cadena que ya contiene entrada no confiable como parte de la plantilla. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca compilacion o render de templates creados desde strings del usuario en lugar de plantillas predefinidas.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Template := Request.QueryFields.Values['tpl']; end.`
En este ejemplo, lo vulnerable es ejecutar el motor de templates sobre una cadena que ya contiene entrada no confiable como parte de la plantilla. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca compilacion o render de templates creados desde strings del usuario en lugar de plantillas predefinidas.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) render inline: params[:tpl] end`
En este ejemplo, lo vulnerable es ejecutar el motor de templates sobre una cadena que ya contiene entrada no confiable como parte de la plantilla. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca compilacion o render de templates creados desde strings del usuario en lugar de plantillas predefinidas.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let tpl = params.get("tpl").unwrap(); }`
En este ejemplo, lo vulnerable es ejecutar el motor de templates sobre una cadena que ya contiene entrada no confiable como parte de la plantilla. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca compilacion o render de templates creados desde strings del usuario en lugar de plantillas predefinidas.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { return Razor.Parse(Request.Query["tpl"]); } }`
En este ejemplo, lo vulnerable es ejecutar el motor de templates sobre una cadena que ya contiene entrada no confiable como parte de la plantilla. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca compilacion o render de templates creados desde strings del usuario en lugar de plantillas predefinidas.
