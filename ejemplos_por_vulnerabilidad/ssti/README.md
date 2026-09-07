# Server-Side Template Injection (SSTI)

## Descripción General
Ocurre cuando una aplicación inserta datos de usuario directamente en la cadena de una plantilla antes de compilarla en el servidor (en lugar de pasar los datos como variables del contexto de la plantilla). Los motores de plantillas (Jinja2, Twig, ERB, FreeMarker) evalúan sintaxis embebida (ej. `{{7*7}}`), permitiendo acceder a clases del sistema y lograr Remote Code Execution (RCE).

## Patrones y Señales para Análisis SAST
- Uso de `render_template_string(input)` en Jinja2/Flask.
- Concatenación de variables en llamadas de renderizado de plantillas.
- Llamadas a `eval()` de motores de plantillas.

## Estrategia de Mitigación y Buenas Prácticas
- Nunca concatenar entradas de usuario en la definición de la plantilla.
- Pasar siempre los datos como variables de contexto al motor de plantillas: `render_template("page.html", name=user_input)`.
- Utilizar sandboxes restrictivos si es indispensable permitir que los usuarios diseñen plantillas.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Server-Side Template Injection (SSTI)
def demo():
    return render_template_string(request.args['tpl'])
```
- **Sink peligroso y causa raíz:** `render_template_string(request.args["tpl"])` en Flask/Jinja2.
- **Mecanismo de explotación y vector:** RCE mediante introspección de clases: `{{ cycler.__init__.__globals__.os.system("id") }}`.
- **Remediación idiomática:** Cargar archivos estáticos y pasar datos como parámetros: `render_template("file.html", tpl=val)`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Server-Side Template Injection (SSTI)
function demo(req, res) {
  res.send(ejs.render(req.query.tpl,{}));
  }
```
- **Sink peligroso y causa raíz:** Uso de plantillas dinámicas con `ejs.render(input)` o interpolación en Nunjucks.
- **Mecanismo de explotación y vector:** Ejecución de código arbitrario en el servidor Node.js.
- **Remediación idiomática:** Compilar archivos de plantilla fijos y pasar datos como objeto de contexto.

### 3. Java ([java.java](./java.java))
```java
// Server-Side Template Injection (SSTI)
public class Example {
  public void demo() throws Exception {
    engine.process(request.getParameter("tpl"), context, writer);
      }
}
```
- **Sink peligroso y causa raíz:** Evaluación de plantillas dinámicas en Velocity o FreeMarker con input crudo.
- **Mecanismo de explotación y vector:** RCE invocando métodos de clases Java en el contexto de ejecución.
- **Remediación idiomática:** Deshabilitar instanciación de clases y pasar variables por modelo estructurado.

### 4. Go ([go.go](./go.go))
```go
// Server-Side Template Injection (SSTI)
package main
func demo() {
  template.New("x").Parse(r.URL.Query().Get("tpl"))
  }
```
- **Sink peligroso y causa raíz:** `template.New("x").Parse(r.URL.Query().Get("tpl"))`.
- **Mecanismo de explotación y vector:** Fuga de datos en memoria y pánicos del servidor.
- **Remediación idiomática:** Parsear archivos estáticos con `template.ParseFiles` y ejecutar pasando estructuras de datos.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Server-Side Template Injection (SSTI)
eval('?>'.$_GET['tpl']);
```
- **Sink peligroso y causa raíz:** Uso de `eval("?>".$_GET["tpl"])` o plantillas Twig con código concatenado.
- **Mecanismo de explotación y vector:** RCE inmediato en el servidor.
- **Remediación idiomática:** Cargar plantillas fijas con `$twig->render("index.html", ["data" => $input])`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Server-Side Template Injection (SSTI)
public class Example {
  public void Demo() {
    return Razor.Parse(Request.Query["tpl"]);
      }
}
```
- **Sink peligroso y causa raíz:** Uso de RazorEngine compilando strings de plantillas recibidos del cliente.
- **Mecanismo de explotación y vector:** RCE mediante código C# en la plantilla.
- **Remediación idiomática:** Utilizar vistas Razor precompiladas pasando ViewModels.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Server-Side Template Injection (SSTI)
def demo(params)
  render inline: params[:tpl]
  end
```
- **Sink peligroso y causa raíz:** `ERB.new(params[:tpl]).result` con entrada de usuario.
- **Mecanismo de explotación y vector:** RCE ejecutando código Ruby embebido `<%= system("id") %>`.
- **Remediación idiomática:** Cargar plantillas desde archivos y pasar datos mediante bindings seguros.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Server-Side Template Injection (SSTI)
fn demo() {
  let tpl = params.get("tpl").unwrap();
  }
```
- **Sink peligroso y causa raíz:** Renderizado dinámico de plantillas en Tera/Handlebars con input concatenado.
- **Mecanismo de explotación y vector:** Fuga de variables del entorno.
- **Remediación idiomática:** Cargar archivos `.html` fijos y pasar structs de contexto.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Server-Side Template Injection (SSTI)
sub demo {
  $tt->process(\$tpl, \%vars);
  }
```
- **Sink peligroso y causa raíz:** Evaluación de plantillas con `Template-Toolkit` permitiendo directivas arbitrarias.
- **Mecanismo de explotación y vector:** Ejecución de código.
- **Remediación idiomática:** Configurar motores de plantillas en modo seguro sin directivas de ejecución.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Server-Side Template Injection (SSTI) }
program Example;
begin
  Template := Request.QueryFields.Values['tpl'];
  end.
```
- **Sink peligroso y causa raíz:** Procesamiento de plantillas con evaluación de expresiones.
- **Mecanismo de explotación y vector:** Comportamiento inesperado o RCE.
- **Remediación idiomática:** Separar definición de plantilla de datos.
