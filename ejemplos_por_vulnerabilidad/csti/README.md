# Client-Side Template Injection (CSTI)

## Descripción General
Ocurre cuando frameworks de renderizado en el cliente (como AngularJS, Vue.js o motores de plantillas en frontend) procesan texto controlado por el usuario que contiene delimitadores de plantilla (como `{{7*7}}`). El motor evalúa la expresión en el contexto del navegador, permitiendo escapar de sandboxes y ejecutar código JavaScript arbitrario (XSS).

## Patrones y Señales para Análisis SAST
- Inserción de input del usuario en vistas HTML que luego son procesadas por AngularJS (`ng-app`), Vue o helpers `eval()`.
- Uso de delimitadores `{{ ... }}` sobre contenido no sanitizado.

## Estrategia de Mitigación y Buenas Prácticas
- Separar estrictamente los datos del cliente de las plantillas del framework.
- Utilizar directivas que deshabiliten la interpretación de plantillas (ej. `ng-non-bindable` en Angular o `v-pre` en Vue).
- Codificar adecuadamente los caracteres `{` y `}` antes de enviar datos al DOM.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Client-Side Template Injection (CSTI)
def demo():
    return f'<div>{{{{{request.args.get("expr", "7*7")}}}}}</div>'
```
- **Sink peligroso y causa raíz:** Renderizado de `{{expr}}` en el HTML servido al navegador.
- **Mecanismo de explotación y vector:** Ejecución de expresiones en frameworks SPA del cliente que desembocan en XSS.
- **Remediación idiomática:** Usar directivas de no-enlace o escapar las llaves en la salida HTML.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Client-Side Template Injection (CSTI)
function demo(req, res) {
  res.send(`<div>{{${req.query.expr}}}</div>`);
  }
```
- **Sink peligroso y causa raíz:** Inyección de expresiones dentro de `innerHTML` con reemplazo o `eval`.
- **Mecanismo de explotación y vector:** Ejecución remota de código en el navegador (XSS).
- **Remediación idiomática:** Usar `textContent` en lugar de `innerHTML` y evitar evaluar código dinámico.

### 3. Java ([java.java](./java.java))
```java
// Client-Side Template Injection (CSTI)
public class Example {
  public void demo() throws Exception {
    response.getWriter().write("<div>{{"+request.getParameter("expr")+"}}</div>");
      }
}
```
- **Sink peligroso y causa raíz:** Servlets que reflejan llaves dobles en páginas con AngularJS/Vue cargado.
- **Mecanismo de explotación y vector:** Escape del sandbox del framework frontend y ejecución de scripts maliciosos.
- **Remediación idiomática:** Codificar la salida con librerías como OWASP Java Encoder.

### 4. Go ([go.go](./go.go))
```go
// Client-Side Template Injection (CSTI)
package main
func demo() {
  fmt.Fprintf(w,`<div>{{%s}}</div>`,r.URL.Query().Get("expr"))
  }
```
- **Sink peligroso y causa raíz:** Inyección de delimitadores en plantillas HTML procesadas por el cliente.
- **Mecanismo de explotación y vector:** Cross-Site Scripting derivado de la evaluación del cliente.
- **Remediación idiomática:** Utilizar `html/template` nativo que contextualiza la salida y neutraliza delimitadores.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Client-Side Template Injection (CSTI)
echo '<div>{{'.$_GET['expr'].'}}</div>';
```
- **Sink peligroso y causa raíz:** Echo de parámetros con `{{...}}` en aplicaciones con bibliotecas JS reactivas.
- **Mecanismo de explotación y vector:** XSS reflected o stored al compilar la vista en el navegador.
- **Remediación idiomática:** Deshabilitar interpolación en bloques dinámicos o escapar llaves con `htmlspecialchars()`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Client-Side Template Injection (CSTI)
public class Example {
  public void Demo() {
    Response.Write("<div>{{" + Request.Query["expr"] + "}}</div>");
      }
}
```
- **Sink peligroso y causa raíz:** Reflejo de datos en vistas Razor que contienen componentes cliente reactivos.
- **Mecanismo de explotación y vector:** XSS en el navegador de la víctima.
- **Remediación idiomática:** Usar `@Html.Encode()` o directivas de protección de bindings frontend.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Client-Side Template Injection (CSTI)
def demo(params)
  render html: "<div>{{#{params[:expr]}}}</div>".html_safe
  end
```
- **Sink peligroso y causa raíz:** Inclusión de datos no confiables en contenedores con directivas JS.
- **Mecanismo de explotación y vector:** Inyección y ejecución de scripts en el cliente.
- **Remediación idiomática:** Usar helpers de escape seguros de Rails (`sanitize`, `h()`).

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Client-Side Template Injection (CSTI)
fn demo() {
  let body = format!("<div>{{{{{}}}}}</div>", expr);
  }
```
- **Sink peligroso y causa raíz:** Emisión de HTML con llaves de evaluación sin protección.
- **Mecanismo de explotación y vector:** XSS a través del compilador frontend.
- **Remediación idiomática:** Usar motores de plantillas seguros como `Tera` o `Askama` con autoescape.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Client-Side Template Injection (CSTI)
sub demo {
  print '<div>{{'.param('expr').'}}</div>';
  }
```
- **Sink peligroso y causa raíz:** Reflejo de expresiones de plantilla en vistas web.
- **Mecanismo de explotación y vector:** Ejecución de código en el contexto de la sesión del usuario.
- **Remediación idiomática:** Sanitizar caracteres delimitadores antes de emitir HTML.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Client-Side Template Injection (CSTI) }
program Example;
begin
  Response.Content := '<div>{{' + Request.QueryFields.Values['expr'] + '}}</div>';
  end.
```
- **Sink peligroso y causa raíz:** Generación de HTML con sintaxis de plantillas frontend.
- **Mecanismo de explotación y vector:** Compromiso de la cuenta del usuario en el navegador.
- **Remediación idiomática:** Tratar la entrada como texto plano estricto.
