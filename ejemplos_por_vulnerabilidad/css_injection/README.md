# CSS Injection (CSSI)

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
El patron vulnerable consiste en insertar datos del usuario dentro de reglas CSS o atributos `style` sin validacion.
Aunque CSS no siempre ejecuta JavaScript, si puede alterar la interfaz, ocultar elementos, superponer controles, cargar recursos externos o apoyar ataques de UI redressing.

## Como identificar casos similares
- Interpolacion de entrada del usuario en `<style>`, `style=` o selectores.
- Temas personalizables sin lista de propiedades permitidas.
- Plantillas que construyen CSS a partir de texto arbitrario.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `return f'<style>{request.args.get("css","body{}")}</style>'`
En este ejemplo, lo vulnerable es tratar CSS como si fuera texto inocuo cuando en realidad modifica activamente el comportamiento visual de la pagina. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca entrada del usuario insertada en bloques CSS, atributos `style` o generadores de hojas de estilo.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { res.send(`<style>${req.query.css}</style>`); }`
En este ejemplo, lo vulnerable es tratar CSS como si fuera texto inocuo cuando en realidad modifica activamente el comportamiento visual de la pagina. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca entrada del usuario insertada en bloques CSS, atributos `style` o generadores de hojas de estilo.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { response.getWriter().write("<style>"+request.getParameter("css")+"</style>"); } }`
En este ejemplo, lo vulnerable es tratar CSS como si fuera texto inocuo cuando en realidad modifica activamente el comportamiento visual de la pagina. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca entrada del usuario insertada en bloques CSS, atributos `style` o generadores de hojas de estilo.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { fmt.Fprintf(w,"<style>%s</style>",r.URL.Query().Get("css")) }`
En este ejemplo, lo vulnerable es tratar CSS como si fuera texto inocuo cuando en realidad modifica activamente el comportamiento visual de la pagina. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca entrada del usuario insertada en bloques CSS, atributos `style` o generadores de hojas de estilo.

### PHP (`php.php`)
Fragmento representativo: `echo '<style>'.$_GET['css'].'</style>';`
En este ejemplo, lo vulnerable es tratar CSS como si fuera texto inocuo cuando en realidad modifica activamente el comportamiento visual de la pagina. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca entrada del usuario insertada en bloques CSS, atributos `style` o generadores de hojas de estilo.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { print '<style>' . param('css') . '</style>'; }`
En este ejemplo, lo vulnerable es tratar CSS como si fuera texto inocuo cuando en realidad modifica activamente el comportamiento visual de la pagina. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca entrada del usuario insertada en bloques CSS, atributos `style` o generadores de hojas de estilo.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Response.Content := '<style>' + Request.QueryFields.Values['css'] + '</style>'; end.`
En este ejemplo, lo vulnerable es tratar CSS como si fuera texto inocuo cuando en realidad modifica activamente el comportamiento visual de la pagina. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca entrada del usuario insertada en bloques CSS, atributos `style` o generadores de hojas de estilo.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) render html: "<style>#{params[:css]}</style>".html_safe end`
En este ejemplo, lo vulnerable es tratar CSS como si fuera texto inocuo cuando en realidad modifica activamente el comportamiento visual de la pagina. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca entrada del usuario insertada en bloques CSS, atributos `style` o generadores de hojas de estilo.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let html = format!("<style>{}</style>", css); }`
En este ejemplo, lo vulnerable es tratar CSS como si fuera texto inocuo cuando en realidad modifica activamente el comportamiento visual de la pagina. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca entrada del usuario insertada en bloques CSS, atributos `style` o generadores de hojas de estilo.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { Response.Write("<style>" + Request.Query["css"] + "</style>"); } }`
En este ejemplo, lo vulnerable es tratar CSS como si fuera texto inocuo cuando en realidad modifica activamente el comportamiento visual de la pagina. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca entrada del usuario insertada en bloques CSS, atributos `style` o generadores de hojas de estilo.
