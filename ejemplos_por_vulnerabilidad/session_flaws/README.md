# Session Puzzling / Fixation / Variable Overloading

## Descripción General
Agrupa vulnerabilidades en la gestión del estado de sesión: Session Fixation (el servidor acepta un session ID provisto por el usuario sin renovarlo al autenticarse), Session Puzzling (reutilización de la misma variable de sesión en múltiples contextos distintos) y Variable Overloading (inyectar parámetros de la petición directamente en el almacenamiento de sesión global).

## Patrones y Señales para Análisis SAST
- Asignación manual de session ID: `session_id($_GET["sid"])`.
- Fusión directa de entradas en la sesión: `$_SESSION += $_REQUEST`.
- Falta de regeneración de sesión tras login (`session_regenerate_id`).

## Estrategia de Mitigación y Buenas Prácticas
- Regenerar siempre el ID de sesión inmediatamente después de un cambio de nivel de autenticación.
- Rechazar identificadores de sesión provenientes de parámetros URL o GET.
- No almacenar masivamente variables de la petición dentro del objeto de sesión.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Session Puzzling / Fixation / Variable Overloading
if 'sid' in request.args: session.sid=request.args['sid']
session.update(request.args)
```
- **Sink peligroso y causa raíz:** Aceptar `sid` por URL o hacer `session.update(request.form)`.
- **Mecanismo de explotación y vector:** Fijación de sesión y escalamiento de privilegios sobrescribiendo atributos como `role`.
- **Remediación idiomática:** Regenerar sesión tras autenticación y asignar variables individualmente.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Session Puzzling / Fixation / Variable Overloading
function demo(req, res) {
  req.session.id=req.query.sid;Object.assign(req.session,req.query);
  }
```
- **Sink peligroso y causa raíz:** Modificación de `req.session` fusionando `req.body`.
- **Mecanismo de explotación y vector:** Secuestro de sesión o escalamiento a roles administrativos.
- **Remediación idiomática:** Usar `req.session.regenerate()` tras login y tipar los datos de sesión.

### 3. Java ([java.java](./java.java))
```java
// Session Puzzling / Fixation / Variable Overloading
public class Example {
  public void demo() throws Exception {
    request.getSession(true).setAttribute("role",request.getParameter("role"));
      }
}
```
- **Sink peligroso y causa raíz:** Uso de `request.getSession()` sin invalidar la sesión anterior tras autenticación.
- **Mecanismo de explotación y vector:** Session Fixation permitiendo secuestro de cuentas.
- **Remediación idiomática:** Configurar `sessionManagement().sessionFixation().migrateSession()` en Spring Security.

### 4. Go ([go.go](./go.go))
```go
// Session Puzzling / Fixation / Variable Overloading
package main
func demo() {
  session.ID=r.URL.Query().Get("sid")
  }
```
- **Sink peligroso y causa raíz:** Aceptación de tokens de sesión por query params sin renovación.
- **Mecanismo de explotación y vector:** Hijacking de sesiones de usuario.
- **Remediación idiomática:** Regenerar el identificador de sesión y emitir cookies con `HttpOnly` y `Secure`.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Session Puzzling / Fixation / Variable Overloading
if(isset($_GET['sid']))session_id($_GET['sid']);session_start();$_SESSION+=$_REQUEST;
```
- **Sink peligroso y causa raíz:** `session_id($_GET["sid"]); session_start(); $_SESSION += $_REQUEST;`.
- **Mecanismo de explotación y vector:** Un atacante fija el ID de sesión de la víctima o se otorga rol de administrador.
- **Remediación idiomática:** Activar `session.use_strict_mode = 1`, llamar a `session_regenerate_id(true)` y no fusionar arrays.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Session Puzzling / Fixation / Variable Overloading
public class Example {
  public void Demo() {
    HttpContext.Session.SetString("role", Request.Query["role"]);
      }
}
```
- **Sink peligroso y causa raíz:** Uso de identificadores de sesión estáticos o expuestos en URLs.
- **Mecanismo de explotación y vector:** Secuestro de sesión.
- **Remediación idiomática:** Usar los middlewares estándar de autenticación por cookies de ASP.NET Core con renovación automática.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Session Puzzling / Fixation / Variable Overloading
def demo(params)
  session[:role] = params[:role]
  end
```
- **Sink peligroso y causa raíz:** Fusión de `params` en el hash `session` sin filtrar.
- **Mecanismo de explotación y vector:** Manipulación de variables de autorización.
- **Remediación idiomática:** Usar `reset_session` tras login y asignar valores explícitamente.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Session Puzzling / Fixation / Variable Overloading
fn demo() {
  session.id = sid.to_string();
  }
```
- **Sink peligroso y causa raíz:** Gestión de sesión permeable a parámetros de entrada.
- **Mecanismo de explotación y vector:** Fijación de sesión.
- **Remediación idiomática:** Regenerar el identificador de sesión al autenticar.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Session Puzzling / Fixation / Variable Overloading
sub demo {
  $session{role} = param('role');
}
```
- **Sink peligroso y causa raíz:** Asignación directa de parámetros a variables de sesión.
- **Mecanismo de explotación y vector:** Corrupción de estado de sesión.
- **Remediación idiomática:** Filtrar estrictamente las claves permitidas en sesión.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Session Puzzling / Fixation / Variable Overloading }
program Example;
begin
  Session.ID := Request.QueryFields.Values['sid'];
  end.
```
- **Sink peligroso y causa raíz:** Adopción de identificadores de sesión enviados por el cliente.
- **Mecanismo de explotación y vector:** Fijación de sesión.
- **Remediación idiomática:** Generar identificadores criptográficos en el servidor.
