# Cross-Site Scripting (XSS)

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad existe cuando datos controlados por el usuario se insertan en una respuesta HTML, JS o CSS sin el escape contextual adecuado.
El navegador interpreta ese contenido como parte de la pagina. Si el dato cae en HTML, atributos, JavaScript inline, URLs o CSS sin proteccion, el atacante ejecuta codigo en el navegador de la victima.

## Como identificar casos similares
- Uso de `innerHTML`, `res.send` o templates con entrada del usuario.
- Falta de encoding contextual en HTML, atributos, JS o URLs.
- Datos reflejados o almacenados que luego se renderizan en el cliente.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `return f"<h1>{request.args.get('q','')}</h1>"`
En este ejemplo, lo vulnerable es convertir entrada del usuario en markup o script interpretable por el navegador. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca salida HTML/JS/CSS construida con datos del usuario sin escape contextual o con APIs inseguras.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { res.send(`<h1>${req.query.q}</h1>`); }`
En este ejemplo, lo vulnerable es convertir entrada del usuario en markup o script interpretable por el navegador. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca salida HTML/JS/CSS construida con datos del usuario sin escape contextual o con APIs inseguras.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { response.getWriter().write("<h1>"+request.getParameter("q")+"</h1>"); } }`
En este ejemplo, lo vulnerable es convertir entrada del usuario en markup o script interpretable por el navegador. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca salida HTML/JS/CSS construida con datos del usuario sin escape contextual o con APIs inseguras.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { fmt.Fprintf(w,"<h1>%s</h1>",r.URL.Query().Get("q")) }`
En este ejemplo, lo vulnerable es convertir entrada del usuario en markup o script interpretable por el navegador. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca salida HTML/JS/CSS construida con datos del usuario sin escape contextual o con APIs inseguras.

### PHP (`php.php`)
Fragmento representativo: `$q=$_GET['q']??''; echo "<h1>$q</h1>";`
En este ejemplo, lo vulnerable es convertir entrada del usuario en markup o script interpretable por el navegador. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca salida HTML/JS/CSS construida con datos del usuario sin escape contextual o con APIs inseguras.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { print "<h1>$q</h1>"; }`
En este ejemplo, lo vulnerable es convertir entrada del usuario en markup o script interpretable por el navegador. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca salida HTML/JS/CSS construida con datos del usuario sin escape contextual o con APIs inseguras.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Response.Content := '<h1>' + Request.QueryFields.Values['q'] + '</h1>'; end.`
En este ejemplo, lo vulnerable es convertir entrada del usuario en markup o script interpretable por el navegador. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca salida HTML/JS/CSS construida con datos del usuario sin escape contextual o con APIs inseguras.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) render html: "<h1>#{params[:q]}</h1>".html_safe end`
En este ejemplo, lo vulnerable es convertir entrada del usuario en markup o script interpretable por el navegador. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca salida HTML/JS/CSS construida con datos del usuario sin escape contextual o con APIs inseguras.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { format!("<h1>{}</h1>", q); }`
En este ejemplo, lo vulnerable es convertir entrada del usuario en markup o script interpretable por el navegador. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca salida HTML/JS/CSS construida con datos del usuario sin escape contextual o con APIs inseguras.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { Response.Write("<h1>" + Request.Query["q"] + "</h1>"); } }`
En este ejemplo, lo vulnerable es convertir entrada del usuario en markup o script interpretable por el navegador. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca salida HTML/JS/CSS construida con datos del usuario sin escape contextual o con APIs inseguras.
