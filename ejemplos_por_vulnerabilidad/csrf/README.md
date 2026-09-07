# Cross-Site Request Forgery (CSRF)

## Descripción General
Ocurre cuando una aplicación web permite que peticiones secundarias disparadas desde un sitio externo ejecuten acciones sensibles en nombre de un usuario autenticado. El navegador adjunta automáticamente las cookies de sesión a la solicitud cross-site, y el backend procesa la acción sin validar la intención real del usuario.

## Patrones y Señales para Análisis SAST
- Endpoints que mutan estado (`POST`, `PUT`, `DELETE`) sin solicitar ni verificar tokens anti-CSRF.
- Cookies de sesión configuradas sin atributo `SameSite` o con `SameSite=None`.
- Ausencia de validación de encabezados `Origin` o `Referer` en peticiones de cambio de estado.

## Estrategia de Mitigación y Buenas Prácticas
- Implementar tokens anti-CSRF únicos por sesión o por formulario (patrón Synchronizer Token o Double Submit Cookie).
- Configurar cookies con `SameSite=Lax` o `SameSite=Strict`.
- Verificar que el encabezado `Origin` coincida exactamente con el dominio de la aplicación en todas las mutaciones.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Cross-Site Request Forgery (CSRF)
if request.method=='POST': change_email(request.form['email'])
```
- **Sink peligroso y causa raíz:** Procesamiento de POST autenticado solo por `session["user_id"]`.
- **Mecanismo de explotación y vector:** Cambio de correo, contraseña o compras no autorizadas inducidas por un enlace malicioso.
- **Remediación idiomática:** Usar `Flask-WTF` con `CSRFProtect(app)` y validar tokens en cada formulario y API.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Cross-Site Request Forgery (CSRF)
function demo(req, res) {
  app.post('/email',(req,res)=>changeEmail(req.body.email));
  }
```
- **Sink peligroso y causa raíz:** Rutas `app.post()` en Express sin middleware de protección CSRF.
- **Mecanismo de explotación y vector:** Ejecución de acciones privilegiadas cuando el usuario visita un sitio externo.
- **Remediación idiomática:** Usar `csurf` o implementar validación estricta de encabezados `Origin` y tokens en cabeceras `X-CSRF-Token`.

### 3. Java ([java.java](./java.java))
```java
// Cross-Site Request Forgery (CSRF)
public class Example {
  public void demo() throws Exception {
    if(request.getMethod().equals("POST")){changeEmail(request.getParameter("email"));}
      }
}
```
- **Sink peligroso y causa raíz:** Deshabilitación explícita de CSRF en Spring Security (`csrf().disable()`).
- **Mecanismo de explotación y vector:** Vulnerabilidad ante formularios HTML ocultos que envían POST automático.
- **Remediación idiomática:** Mantener activado el filtro CSRF por defecto en Spring Security y enviar `_csrf` en formularios/cabeceras.

### 4. Go ([go.go](./go.go))
```go
// Cross-Site Request Forgery (CSRF)
package main
func demo() {
  if r.Method==http.MethodPost { changeEmail(r.FormValue("email")) }
  }
```
- **Sink peligroso y causa raíz:** Handlers HTTP que leen formularios y modifican datos sin token de verificación.
- **Mecanismo de explotación y vector:** Mutación de datos del usuario mediante peticiones cross-origin.
- **Remediación idiomática:** Integrar middleware como `gorilla/csrf` que inyecta y valida tokens criptográficos.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Cross-Site Request Forgery (CSRF)
if($_SERVER['REQUEST_METHOD']==='POST'){changeEmail($_POST['email']);}
```
- **Sink peligroso y causa raíz:** Scripts que procesan `$_POST` confiando únicamente en `session_start()`.
- **Mecanismo de explotación y vector:** Ataques de cambio de credenciales mediante payloads `<form action=...><script>submit()`.
- **Remediación idiomática:** Generar `$_SESSION["csrf_token"] = bin2hex(random_bytes(32))` y validar con `hash_equals()`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Cross-Site Request Forgery (CSRF)
public class Example {
  public void Demo() {
    if (Request.Method == "POST") ChangeEmail(Request.Form["email"]);
      }
}
```
- **Sink peligroso y causa raíz:** Acciones en ASP.NET Core MVC sin el atributo `[ValidateAntiForgeryToken]`.
- **Mecanismo de explotación y vector:** Ejecución forzada de peticiones sensibles.
- **Remediación idiomática:** Aplicar `[ValidateAntiForgeryToken]` en controladores o activar el filtro global de auto-validación.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Cross-Site Request Forgery (CSRF)
def demo(params)
  change_email(params[:email])
  end
```
- **Sink peligroso y causa raíz:** Uso de `skip_before_action :verify_authenticity_token` en Rails.
- **Mecanismo de explotación y vector:** Bypass deliberado de la protección nativa de Rails permitiendo ataques CSRF.
- **Remediación idiomática:** Mantener `protect_from_forgery with: :exception` activo en todos los controladores sensibles.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Cross-Site Request Forgery (CSRF)
fn demo() {
  change_email(email);
  }
```
- **Sink peligroso y causa raíz:** Endpoints mutables en Axum/Actix sin verificación de cabeceras de origen o tokens.
- **Mecanismo de explotación y vector:** Ejecución de acciones cross-site.
- **Remediación idiomática:** Validar tokens anti-CSRF en el estado de la sesión y aplicar `SameSite::Strict` en cookies.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Cross-Site Request Forgery (CSRF)
sub demo {
  change_email(param('email')) if request_method() eq 'POST';
  }
```
- **Sink peligroso y causa raíz:** Acciones sensibles ejecutadas al recibir parámetros POST sin token.
- **Mecanismo de explotación y vector:** Pérdida de control de la cuenta del usuario.
- **Remediación idiomática:** Implementar verificación de tokens de sesión aleatorios y validar cabecera `Origin`.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Cross-Site Request Forgery (CSRF) }
program Example;
begin
  if Request.Method = 'POST' then ChangeEmail(Request.ContentFields.Values['email']);
  end.
```
- **Sink peligroso y causa raíz:** Recepción de peticiones de modificación sin comprobación de origen.
- **Mecanismo de explotación y vector:** Ejecución no deseada de operaciones críticas.
- **Remediación idiomática:** Añadir validación de token sincronizador en cada solicitud de mutación.
