# Client-Side Template Injection (CSTI)

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad aparece cuando datos no confiables se insertan en plantillas evaluadas en el navegador.
El dato del usuario deja de ser solo contenido y pasa a ser parte de una expresion o plantilla activa. En frameworks client-side eso puede terminar en XSS o en manipulacion del DOM.

## Como identificar casos similares
- Compilacion dinamica de templates.
- Interpolacion de entrada del usuario en expresiones tipo `{{ }}`.
- Construccion de vistas o componentes a partir de strings arbitrarios.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `return f'<div>{{{{{request.args.get("expr","7*7")}}}}}</div>'`
En este ejemplo, lo vulnerable es que la entrada del usuario se interpreta como parte de la plantilla y no como dato ya escapado. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca render dinamico de templates, compilacion de cadenas o uso de HTML no confiable dentro del motor client-side.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { res.send(`<div>{{${req.query.expr}}}</div>`); }`
En este ejemplo, lo vulnerable es que la entrada del usuario se interpreta como parte de la plantilla y no como dato ya escapado. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca render dinamico de templates, compilacion de cadenas o uso de HTML no confiable dentro del motor client-side.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { response.getWriter().write("<div>{{"+request.getParameter("expr")+"}}</div>"); } }`
En este ejemplo, lo vulnerable es que la entrada del usuario se interpreta como parte de la plantilla y no como dato ya escapado. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca render dinamico de templates, compilacion de cadenas o uso de HTML no confiable dentro del motor client-side.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { fmt.Fprintf(w,`<div>{{%s}}</div>`,r.URL.Query().Get("expr")) }`
En este ejemplo, lo vulnerable es que la entrada del usuario se interpreta como parte de la plantilla y no como dato ya escapado. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca render dinamico de templates, compilacion de cadenas o uso de HTML no confiable dentro del motor client-side.

### PHP (`php.php`)
Fragmento representativo: `echo '<div>{{'.$_GET['expr'].'}}</div>';`
En este ejemplo, lo vulnerable es que la entrada del usuario se interpreta como parte de la plantilla y no como dato ya escapado. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca render dinamico de templates, compilacion de cadenas o uso de HTML no confiable dentro del motor client-side.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { print '<div>{{'.param('expr').'}}</div>'; }`
En este ejemplo, lo vulnerable es que la entrada del usuario se interpreta como parte de la plantilla y no como dato ya escapado. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca render dinamico de templates, compilacion de cadenas o uso de HTML no confiable dentro del motor client-side.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Response.Content := '<div>{{' + Request.QueryFields.Values['expr'] + '}}</div>'; end.`
En este ejemplo, lo vulnerable es que la entrada del usuario se interpreta como parte de la plantilla y no como dato ya escapado. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca render dinamico de templates, compilacion de cadenas o uso de HTML no confiable dentro del motor client-side.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) render html: "<div>{{#{params[:expr]}}}</div>".html_safe end`
En este ejemplo, lo vulnerable es que la entrada del usuario se interpreta como parte de la plantilla y no como dato ya escapado. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca render dinamico de templates, compilacion de cadenas o uso de HTML no confiable dentro del motor client-side.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let body = format!("<div>{{{{{}}}}}</div>", expr); }`
En este ejemplo, lo vulnerable es que la entrada del usuario se interpreta como parte de la plantilla y no como dato ya escapado. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca render dinamico de templates, compilacion de cadenas o uso de HTML no confiable dentro del motor client-side.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { Response.Write("<div>{{" + Request.Query["expr"] + "}}</div>"); } }`
En este ejemplo, lo vulnerable es que la entrada del usuario se interpreta como parte de la plantilla y no como dato ya escapado. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca render dinamico de templates, compilacion de cadenas o uso de HTML no confiable dentro del motor client-side.
