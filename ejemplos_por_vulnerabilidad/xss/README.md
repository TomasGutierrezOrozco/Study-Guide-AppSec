# Cross-Site Scripting (XSS)

## Descripción General
Ocurre cuando una aplicación incluye datos de usuario no confiables en una página web sin el escape contextual adecuado (HTML body, atributos, JavaScript, URLs). Esto permite a un atacante ejecutar scripts arbitrarios en el navegador de la víctima para robar cookies de sesión, tokens de autenticación, registrar pulsaciones de teclas o desfigurar la página.

## Patrones y Señales para Análisis SAST
- Interpolación de parámetros en respuestas HTML (`res.send("<h1>" + q + "</h1>")`, `echo $q`, `render_template_string`).
- Uso de APIs inseguras en el frontend (`innerHTML`, `document.write()`, `v-html`).

## Estrategia de Mitigación y Buenas Prácticas
- Aplicar escape contextual estricto (HTML Entity Encoding, Attribute Encoding, JavaScript Encoding) antes de renderizar.
- Utilizar frameworks modernos con autoescape por defecto (React, Angular, Jinja2, Blade).
- Configurar Content Security Policy (CSP) robusta restringiendo la ejecución de scripts inline (`script-src 'self'`).
- Marcar cookies sensibles con la bandera `HttpOnly` para evitar su lectura mediante JavaScript.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Cross-Site Scripting (XSS)
def demo():
    return f"<h1>{request.args.get('q', '')}</h1>"
```
- **Sink peligroso y causa raíz:** `f"<h1>{request.args.get('q', '')}</h1>"` devuelto en respuesta HTTP.
- **Mecanismo de explotación y vector:** Ejecución de JavaScript inyectando `<script>alert(document.cookie)</script>`.
- **Remediación idiomática:** Usar motores con autoescape como Jinja2 (`render_template`) o escapar con `html.escape()`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Cross-Site Scripting (XSS)
function demo(req, res) {
  res.send(`<h1>${req.query.q}</h1>`);
  }
```
- **Sink peligroso y causa raíz:** `res.send("<h1>" + req.query.q + "</h1>")` en Node.js.
- **Mecanismo de explotación y vector:** Robo de sesiones y acciones no autorizadas en el navegador de la víctima.
- **Remediación idiomática:** Escapar caracteres HTML (`&`, `<`, `>`, `"`, `'`) o usar plantillas con autoescape.

### 3. Java ([java.java](./java.java))
```java
// Cross-Site Scripting (XSS)
public class Example {
  public void demo() throws Exception {
    response.getWriter().write("<h1>"+request.getParameter("q")+"</h1>");
      }
}
```
- **Sink peligroso y causa raíz:** `response.getWriter().write("<h1>" + request.getParameter("q") + "</h1>")`.
- **Mecanismo de explotación y vector:** Reflected XSS.
- **Remediación idiomática:** Usar `OWASP Java Encoder`: `Encode.forHtml(q)` antes de emitir en respuesta.

### 4. Go ([go.go](./go.go))
```go
// Cross-Site Scripting (XSS)
package main
func demo() {
  fmt.Fprintf(w,"<h1>%s</h1>",r.URL.Query().Get("q"))
  }
```
- **Sink peligroso y causa raíz:** `fmt.Fprintf(w, "<h1>%s</h1>", r.URL.Query().Get("q"))`.
- **Mecanismo de explotación y vector:** Ejecución de scripts maliciosos.
- **Remediación idiomática:** Usar el paquete `html/template` nativo que contextualiza y escapa automáticamente.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Cross-Site Scripting (XSS)
$q=$_GET['q']??'';
echo "<h1>$q</h1>";
```
- **Sink peligroso y causa raíz:** `echo "<h1>" . $_GET["q"] . "</h1>";` directo.
- **Mecanismo de explotación y vector:** Reflected XSS clásico explotable vía enlaces maliciosos.
- **Remediación idiomática:** Escapar siempre con `htmlspecialchars($q, ENT_QUOTES, "UTF-8")`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Cross-Site Scripting (XSS)
public class Example {
  public void Demo() {
    Response.Write("<h1>" + Request.Query["q"] + "</h1>");
      }
}
```
- **Sink peligroso y causa raíz:** `Response.WriteAsync("<h1>" + Request.Query["q"] + "</h1>")`.
- **Mecanismo de explotación y vector:** Reflected XSS.
- **Remediación idiomática:** Usar vistas Razor con codificación automática o `HtmlEncoder.Default.Encode()`.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Cross-Site Scripting (XSS)
def demo(params)
  render html: "<h1>#{params[:q]}</h1>".html_safe
  end
```
- **Sink peligroso y causa raíz:** Uso de `raw` o `html_safe` sobre parámetros en vistas Rails.
- **Mecanismo de explotación y vector:** Bypass de la protección nativa de autoescape de Rails.
- **Remediación idiomática:** Evitar `html_safe` sobre entradas del usuario; permitir que Rails aplique escape automático.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Cross-Site Scripting (XSS)
fn demo() {
  format!("<h1>{}</h1>", q);
  }
```
- **Sink peligroso y causa raíz:** Formateo de strings HTML con `format!("<h1>{}</h1>", q)`.
- **Mecanismo de explotación y vector:** XSS.
- **Remediación idiomática:** Usar motores de plantillas como `Tera` o `Askama` con autoescape por defecto.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Cross-Site Scripting (XSS)
sub demo {
  print "<h1>$q</h1>";
  }
```
- **Sink peligroso y causa raíz:** `print "<h1>" . param("q") . "</h1>";` en scripts CGI.
- **Mecanismo de explotación y vector:** Compromiso de la sesión del usuario.
- **Remediación idiomática:** Usar módulos de escape como `HTML::Entities::encode_entities`.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Cross-Site Scripting (XSS) }
program Example;
begin
  Response.Content := '<h1>' + Request.QueryFields.Values['q'] + '</h1>';
  end.
```
- **Sink peligroso y causa raíz:** Emisión de parámetros en la respuesta sin codificar.
- **Mecanismo de explotación y vector:** XSS reflejado.
- **Remediación idiomática:** Reemplazar caracteres `<` y `>` por entidades HTML.
