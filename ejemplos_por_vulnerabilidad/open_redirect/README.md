# Open Redirect

## Descripción General
Ocurre cuando una aplicación redirige a los usuarios hacia una URL provista en un parámetro de la solicitud (como `?next=https://evil.com`) sin verificar que el destino pertenezca al mismo dominio o a una lista de orígenes de confianza. Se explota en ataques de phishing, robo de tokens OAuth y bypass de filtros de seguridad.

## Patrones y Señales para Análisis SAST
- Llamadas a `redirect(request.args["next"])`, `res.redirect(req.query.url)`, `header("Location: " . $url)`.
- Ausencia de verificación del dominio de destino.

## Estrategia de Mitigación y Buenas Prácticas
- Permitir únicamente rutas relativas que inicien con `/` (evitando `//` que indica cambio de protocolo y host).
- Si se requiere redirigir a dominios externos, validar contra una allowlist estricta de dominios autorizados.
- Mostrar una página intermedia de confirmación antes de redirigir a un sitio externo.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Open Redirect
def demo():
    return redirect(request.args['next'])
```
- **Sink peligroso y causa raíz:** `return redirect(request.args["next"])` sin validar.
- **Mecanismo de explotación y vector:** Phishing creíble utilizando la reputación del dominio legítimo.
- **Remediación idiomática:** Validar que sea relativo: `url_has_allowed_host_and_scheme(target, {request.host})`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Open Redirect
function demo(req, res) {
  res.redirect(req.query.next);
  }
```
- **Sink peligroso y causa raíz:** `res.redirect(req.query.next)` directo.
- **Mecanismo de explotación y vector:** Redirección no autorizada a sitios fraudulentos.
- **Remediación idiomática:** Comprobar que comience con `/` y no con `//`: `if (next.startsWith("/") && !next.startsWith("//"))`.

### 3. Java ([java.java](./java.java))
```java
// Open Redirect
public class Example {
  public void demo() throws Exception {
    response.sendRedirect(request.getParameter("next"));
      }
}
```
- **Sink peligroso y causa raíz:** `response.sendRedirect(request.getParameter("next"))`.
- **Mecanismo de explotación y vector:** Ataques de ingeniería social y robo de credenciales.
- **Remediación idiomática:** Validar contra una allowlist de URLs o forzar rutas relativas del contexto.

### 4. Go ([go.go](./go.go))
```go
// Open Redirect
package main
func demo() {
  http.Redirect(w,r,r.URL.Query().Get("next"),302)
  }
```
- **Sink peligroso y causa raíz:** `http.Redirect(w, r, r.URL.Query().Get("next"), 302)`.
- **Mecanismo de explotación y vector:** Desvío de usuarios a páginas de phishing.
- **Remediación idiomática:** Analizar con `url.Parse` y verificar que `u.Hostname() == ""` o coincida con el dominio del sitio.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Open Redirect
header('Location: '.$_GET['next']);
```
- **Sink peligroso y causa raíz:** `header("Location: " . $_GET["next"])` sin comprobación.
- **Mecanismo de explotación y vector:** Redirección abierta hacia destinos de ataque.
- **Remediación idiomática:** Validar con `filter_var` y comprobar que el host pertenezca a la organización.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Open Redirect
public class Example {
  public void Demo() {
    return Redirect(Request.Query["next"]);
      }
}
```
- **Sink peligroso y causa raíz:** `return Redirect(returnUrl)` en ASP.NET Core.
- **Mecanismo de explotación y vector:** Vulnerabilidad ante redirecciones externas.
- **Remediación idiomática:** Usar `LocalRedirect(returnUrl)` que rechaza automáticamente URLs externas.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Open Redirect
def demo(params)
  redirect_to params[:next], allow_other_host: true
  end
```
- **Sink peligroso y causa raíz:** `redirect_to params[:next]` en controladores Rails.
- **Mecanismo de explotación y vector:** Ataques de Open Redirect.
- **Remediación idiomática:** Usar `redirect_to params[:next], allow_other_host: false` en Rails.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Open Redirect
fn demo() {
  redirect(next);
  }
```
- **Sink peligroso y causa raíz:** Respuestas con código 302 y cabecera `Location` arbitraria.
- **Mecanismo de explotación y vector:** Phishing.
- **Remediación idiomática:** Validar la URL de destino contra una lista permitida.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Open Redirect
sub demo {
  redirect param('next');
  }
```
- **Sink peligroso y causa raíz:** Emisión de cabecera `Location` con URL del usuario.
- **Mecanismo de explotación y vector:** Phishing y bypass de políticas de seguridad.
- **Remediación idiomática:** Restringir a rutas relativas seguras.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Open Redirect }
program Example;
begin
  Response.Code := 302; Response.Location := Request.QueryFields.Values['next'];
  end.
```
- **Sink peligroso y causa raíz:** Redirección HTTP 302 con parámetro de cliente.
- **Mecanismo de explotación y vector:** Redirección no controlada.
- **Remediación idiomática:** Validar el host antes de enviar la cabecera.
