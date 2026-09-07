# Server-Side Request Forgery (SSRF)

## Descripción General
Permite a un atacante inducir al servidor a realizar solicitudes HTTP hacia destinos arbitrarios. Comúnmente se utiliza para acceder a endpoints de metadatos de proveedores cloud (ej. AWS/GCP `http://169.254.169.254`), escanear puertos en la red interna, interactuar con microservicios locales (`localhost`) o eludir firewalls perimetrales.

## Patrones y Señales para Análisis SAST
- Llamadas a clientes HTTP (`http.Get()`, `requests.get()`, `file_get_contents()`, `fetch()`) pasando URLs tomadas de query params o body.
- Falta de validación de esquema, host y resolución de IP final.

## Estrategia de Mitigación y Buenas Prácticas
- Implementar una allowlist estricta de dominios autorizados.
- Resolver el dominio por DNS y comprobar que la dirección IP resultante no sea privada, loopback o link-local (`127.0.0.0/8`, `10.0.0.0/8`, `172.16.0.0/12`, `192.168.0.0/16`, `169.254.0.0/16`).
- Deshabilitar el seguimiento automático de redirecciones HTTP.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Server-Side Request Forgery (SSRF)
def demo():
    return requests.get(request.args['url']).text
```
- **Sink peligroso y causa raíz:** `requests.get(request.args["url"])` sin verificar el host ni la IP.
- **Mecanismo de explotación y vector:** Robo de credenciales de metadatos cloud de AWS/GCP o interacción con paneles locales.
- **Remediación idiomática:** Validar esquema (`https`), resolver DNS y rechazar IPs privadas con el módulo `ipaddress`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Server-Side Request Forgery (SSRF)
function demo(req, res) {
  fetch(req.query.url).then(r=>r.text()).then(t=>res.send(t));
  }
```
- **Sink peligroso y causa raíz:** `fetch(req.query.url)` directo.
- **Mecanismo de explotación y vector:** Acceso a microservicios internos que no requieren autenticación en localhost.
- **Remediación idiomática:** Verificar la IP resuelta antes de conectar y deshabilitar redirecciones automáticas.

### 3. Java ([java.java](./java.java))
```java
// Server-Side Request Forgery (SSRF)
public class Example {
  public void demo() throws Exception {
    new URL(request.getParameter("url")).openConnection().getInputStream();
      }
}
```
- **Sink peligroso y causa raíz:** `new URL(target).openStream()` o `HttpURLConnection` sin restricciones.
- **Mecanismo de explotación y vector:** Escaneo de puertos internos y extracción de tokens de instancia.
- **Remediación idiomática:** Usar `HttpClient` configurando allowlist de hosts y bloqueando redirecciones hacia IPs privadas.

### 4. Go ([go.go](./go.go))
```go
// Server-Side Request Forgery (SSRF)
package main
func demo() {
  http.Get(r.URL.Query().Get("url"))
  }
```
- **Sink peligroso y causa raíz:** `http.Get(r.URL.Query().Get("url"))` sin validación.
- **Mecanismo de explotación y vector:** SSRF crítico hacia servicios internos.
- **Remediación idiomática:** Implementar `net.Dialer` personalizado con `Control` que bloquee conexiones a IPs locales/privadas.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Server-Side Request Forgery (SSRF)
echo file_get_contents($_GET['url']);
```
- **Sink peligroso y causa raíz:** `file_get_contents($_GET["url"])` o `curl_exec` con URL directa.
- **Mecanismo de explotación y vector:** Acceso a metadatos cloud y escaneo de intranet.
- **Remediación idiomática:** Validar con `parse_url()`, resolver DNS y verificar que la IP no sea privada con `filter_var(..., FILTER_FLAG_NO_PRIV_RANGE)`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Server-Side Request Forgery (SSRF)
public class Example {
  public void Demo() {
    var body = new HttpClient().GetStringAsync(url).Result;
      }
}
```
- **Sink peligroso y causa raíz:** `HttpClient.GetStringAsync(url)` con URL no controlada.
- **Mecanismo de explotación y vector:** Extracción de secretos cloud.
- **Remediación idiomática:** Validar host contra allowlist y comprobar que la IP no pertenezca a rangos locales.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Server-Side Request Forgery (SSRF)
def demo(params)
  render plain: Net::HTTP.get(URI(params[:url]))
  end
```
- **Sink peligroso y causa raíz:** `Net::HTTP.get(URI(params[:url]))`.
- **Mecanismo de explotación y vector:** Acceso a servicios internos y metadatos cloud.
- **Remediación idiomática:** Usar gemas de mitigación SSRF como `ssrf_filter`.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Server-Side Request Forgery (SSRF)
fn demo() {
  let body = reqwest::blocking::get(url)?.text()?;
  }
```
- **Sink peligroso y causa raíz:** `reqwest::get(&url)` con URL arbitraria.
- **Mecanismo de explotación y vector:** SSRF en la red interna.
- **Remediación idiomática:** Validar IP destino y deshabilitar redirecciones con `redirect::Policy::none()`.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Server-Side Request Forgery (SSRF)
sub demo {
  print HTTP::Tiny->new->get(param('url'))->{content};
  }
```
- **Sink peligroso y causa raíz:** `LWP::Simple::get($url)` con URL del cliente.
- **Mecanismo de explotación y vector:** Fuga de recursos internos.
- **Remediación idiomática:** Comprobar host e IP resuelta antes de iniciar el request.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Server-Side Request Forgery (SSRF) }
program Example;
begin
  Response.Content := TFPHTTPClient.SimpleGet(Request.QueryFields.Values['url']);
  end.
```
- **Sink peligroso y causa raíz:** Peticiones HTTP salientes basadas en input.
- **Mecanismo de explotación y vector:** SSRF contra endpoints de infraestructura.
- **Remediación idiomática:** Restringir a dominios conocidos.
