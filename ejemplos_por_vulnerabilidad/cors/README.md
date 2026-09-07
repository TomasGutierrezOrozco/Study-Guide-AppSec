# CORS Misconfiguration

## Descripción General
Cross-Origin Resource Sharing (CORS) permite a los navegadores compartir recursos entre diferentes orígenes. La vulnerabilidad aparece cuando el servidor refleja dinámicamente el encabezado `Origin` de la solicitud en `Access-Control-Allow-Origin` junto con `Access-Control-Allow-Credentials: true`, o cuando utiliza comodines (`*`) con endpoints que procesan datos privados mediante credenciales.

## Patrones y Señales para Análisis SAST
- Reflejo de `req.headers.origin` directamente en `Access-Control-Allow-Origin`.
- Presencia simultánea de `Access-Control-Allow-Origin: *` y `Access-Control-Allow-Credentials: true`.
- Regex de validación de origen débiles (ej. `origin.includes("example.com")` o `^https://example.com` sin escapar el punto).

## Estrategia de Mitigación y Buenas Prácticas
- Mantener una allowlist estricta de orígenes autorizados comparados con igualdad exacta.
- Nunca usar comodín `*` en endpoints que retornen datos sensibles o autenticados.
- No habilitar `Access-Control-Allow-Credentials: true` a menos que sea estrictamente necesario y solo para orígenes 100% de confianza.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# CORS Misconfiguration
resp.headers['Access-Control-Allow-Origin']='*';resp.headers['Access-Control-Allow-Credentials']='true'
```
- **Sink peligroso y causa raíz:** Cabeceras CORS con reflejo directo de `request.headers.get("Origin")`.
- **Mecanismo de explotación y vector:** Un sitio atacante puede leer respuestas privadas y datos de la sesión del usuario vía XMLHttpRequest/Fetch.
- **Remediación idiomática:** Usar `flask-cors` configurando `origins=["https://trusted.example.com"]` con orígenes explícitos.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// CORS Misconfiguration
function demo(req, res) {
  res.setHeader('Access-Control-Allow-Origin','*');res.setHeader('Access-Control-Allow-Credentials','true');
  }
```
- **Sink peligroso y causa raíz:** Configuración manual de cabeceras permitiendo `*` con `credentials: true`.
- **Mecanismo de explotación y vector:** Robo de datos sensibles de usuarios autenticados mediante peticiones cross-origin.
- **Remediación idiomática:** Usar middleware `cors` con allowlist: `cors({ origin: ["https://app.example.com"], credentials: true })`.

### 3. Java ([java.java](./java.java))
```java
// CORS Misconfiguration
public class Example {
  public void demo() throws Exception {
    response.setHeader("Access-Control-Allow-Origin","*");response.setHeader("Access-Control-Allow-Credentials","true");
      }
}
```
- **Sink peligroso y causa raíz:** Filtro Servlet o Spring Security que asigna el origen entrante sin verificar.
- **Mecanismo de explotación y vector:** Exfiltración de datos confidenciales por parte de dominios maliciosos.
- **Remediación idiomática:** Configurar `CorsConfiguration.setAllowedOrigins(List.of("https://app.example.com"))` en Spring Security.

### 4. Go ([go.go](./go.go))
```go
// CORS Misconfiguration
package main
func demo() {
  w.Header().Set("Access-Control-Allow-Origin","*")
  w.Header().Set("Access-Control-Allow-Credentials","true")
  }
```
- **Sink peligroso y causa raíz:** `w.Header().Set("Access-Control-Allow-Origin", "*")` con credenciales activadas.
- **Mecanismo de explotación y vector:** Violación del aislamiento del mismo origen en navegadores modernos.
- **Remediación idiomática:** Verificar el host contra un map seguro: `if allowedOrigins[origin] { w.Header().Set("Access-Control-Allow-Origin", origin) }`.

### 5. PHP ([php.php](./php.php))
```php
<?php
// CORS Misconfiguration
header('Access-Control-Allow-Origin:*');
header('Access-Control-Allow-Credentials:true');
```
- **Sink peligroso y causa raíz:** Uso de `header("Access-Control-Allow-Origin: " . $_SERVER["HTTP_ORIGIN"])`.
- **Mecanismo de explotación y vector:** Acceso total a APIs privadas desde cualquier sitio que visite la víctima.
- **Remediación idiomática:** Validar contra una lista cerrada: `if (in_array($origin, $trustedOrigins, true)) { header("Access-Control-Allow-Origin: " . $origin); }`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// CORS Misconfiguration
public class Example {
  public void Demo() {
    Response.Headers["Access-Control-Allow-Origin"] = "*";
      }
}
```
- **Sink peligroso y causa raíz:** Uso de `app.UseCors(builder => builder.AllowAnyOrigin().AllowCredentials())`.
- **Mecanismo de explotación y vector:** Exfiltración de información confidencial en APIs ASP.NET Core.
- **Remediación idiomática:** Usar `.WithOrigins("https://trusted.example.com")` con orígenes explícitos.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# CORS Misconfiguration
def demo(params)
  response.set_header('Access-Control-Allow-Origin', '*')
  end
```
- **Sink peligroso y causa raíz:** Configuración de Rack::Cors con `origins "*"` y credenciales activas.
- **Mecanismo de explotación y vector:** Extracción de datos del usuario mediante páginas maliciosas.
- **Remediación idiomática:** Configurar `origins "https://app.example.com"` en `config/initializers/cors.rb`.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// CORS Misconfiguration
fn demo() {
  headers.insert("Access-Control-Allow-Origin", "*");
  }
```
- **Sink peligroso y causa raíz:** Configuración del middleware CORS en Actix/Axum con `allow_any_origin()`.
- **Mecanismo de explotación y vector:** Exposición de recursos autenticados.
- **Remediación idiomática:** Usar `.allowed_origin("https://trusted.example.com")` explícito.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# CORS Misconfiguration
sub demo {
  header('Access-Control-Allow-Origin' => '*');
  }
```
- **Sink peligroso y causa raíz:** Emisión de cabeceras CORS basadas en input sin verificación.
- **Mecanismo de explotación y vector:** Robo de sesiones y datos JSON cross-site.
- **Remediación idiomática:** Validar estrictamente el origen antes de emitir la cabecera.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ CORS Misconfiguration }
program Example;
begin
  Response.SetCustomHeader('Access-Control-Allow-Origin', '*');
  end.
```
- **Sink peligroso y causa raíz:** Respuesta HTTP con cabeceras CORS permisivas.
- **Mecanismo de explotación y vector:** Filtración de respuestas hacia orígenes no confiables.
- **Remediación idiomática:** Restringir orígenes a los dominios oficiales de la solución.
