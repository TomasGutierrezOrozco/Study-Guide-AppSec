# SQUID Proxy Misconfiguration

## Descripción General
Ocurre cuando un servidor proxy HTTP (como Squid) está configurado como proxy abierto (`http_access allow all`) sin autenticación ni restricciones de destino. Un atacante puede utilizar el proxy para pivotar hacia la red interna de la empresa, saltarse firewalls, acceder a servicios de metadatos de la nube o encubrir su origen para actividades maliciosas.

## Patrones y Señales para Análisis SAST
- Archivos `squid.conf` con directivas `http_access allow all` y puertos expuestos públicamente (`3128`).
- Falta de ACLs de subredes origen o autenticación requerida.

## Estrategia de Mitigación y Buenas Prácticas
- Restringir el acceso en Squid a subredes IP autorizadas (`acl localnet src ...`).
- Exigir autenticación obligatoria para el uso del proxy.
- Bloquear peticiones hacia rangos de direcciones privadas internas y metadatos cloud (`169.254.169.254`, `127.0.0.1/8`, `10.0.0.0/8`).

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# SQUID Proxy Enumeration and Exploitation
requests.get(request.args['url'], proxies={'http':'http://open-squid:3128'})
```
- **Sink peligroso y causa raíz:** Configuración de clientes HTTP enviando tráfico a través de proxies abiertos.
- **Mecanismo de explotación y vector:** Acceso no autorizado a servicios internos usando el proxy como pivote.
- **Remediación idiomática:** Bloquear acceso al proxy y requerir autenticación mutua.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// SQUID Proxy Enumeration and Exploitation
function demo(req, res) {
  const proxy='http://open-squid:3128';
  }
```
- **Sink peligroso y causa raíz:** Uso de proxies HTTP sin validación de destino.
- **Mecanismo de explotación y vector:** Pivoting hacia infraestructura privada.
- **Remediación idiomática:** Configurar reglas de acceso estrictas en el servidor proxy.

### 3. Java ([java.java](./java.java))
```java
// SQUID Proxy Enumeration and Exploitation
public class Example {
  public void demo() throws Exception {
    Proxy proxy=new Proxy(Proxy.Type.HTTP,new InetSocketAddress("open-squid",3128));
      }
}
```
- **Sink peligroso y causa raíz:** Configuración de `System.setProperty("http.proxyHost", ...)` apuntando a proxy inseguro.
- **Mecanismo de explotación y vector:** Evasión de controles perimetrales.
- **Remediación idiomática:** Asegurar la configuración del proxy intermedio.

### 4. Go ([go.go](./go.go))
```go
// SQUID Proxy Enumeration and Exploitation
package main
func demo() {
  proxyURL,_:=url.Parse("http://open-squid:3128")
  _ = proxyURL
  }
```
- **Sink peligroso y causa raíz:** Rutas HTTP redirigidas a través de un proxy Squid desprotegido.
- **Mecanismo de explotación y vector:** Túnel hacia redes internas protegidas.
- **Remediación idiomática:** Restringir las listas de control de acceso (ACLs) en el proxy.

### 5. PHP ([php.php](./php.php))
```php
<?php
// SQUID Proxy Enumeration and Exploitation
$proxy='http://open-squid:3128';
```
- **Sink peligroso y causa raíz:** Peticiones cURL enrutadas a través de proxy Squid sin autenticación.
- **Mecanismo de explotación y vector:** SSRF y escaneo de puertos internos.
- **Remediación idiomática:** Restringir el acceso en `squid.conf` a IPs conocidas.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// SQUID Proxy Enumeration and Exploitation
public class Example {
  public void Demo() {
    var handler = new HttpClientHandler { Proxy = new WebProxy("http://open-squid:3128") };
      }
}
```
- **Sink peligroso y causa raíz:** Configuración de `WebProxy` hacia instancias Squid sin restricciones.
- **Mecanismo de explotación y vector:** Túnel a redes corporativas.
- **Remediación idiomática:** Asegurar que Squid deniegue peticiones por defecto (`http_access deny all`).

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# SQUID Proxy Enumeration and Exploitation
def demo(params)
  proxy = Net::HTTP::Proxy('open-squid', 3128)
  end
```
- **Sink peligroso y causa raíz:** Uso de proxies mal configurados en llamadas HTTP.
- **Mecanismo de explotación y vector:** Bypass perimetral.
- **Remediación idiomática:** Configurar ACLs estrictas en Squid.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// SQUID Proxy Enumeration and Exploitation
fn demo() {
  let proxy = reqwest::Proxy::http("http://open-squid:3128")?;
  }
```
- **Sink peligroso y causa raíz:** Clientes HTTP configurados con proxy permisivo.
- **Mecanismo de explotación y vector:** Pivoting.
- **Remediación idiomática:** Bloquear destinos RFC 1918 en el proxy.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# SQUID Proxy Enumeration and Exploitation
sub demo {
  $ENV{http_proxy} = 'http://open-squid:3128';
  }
```
- **Sink peligroso y causa raíz:** Enrutamiento de conexiones a través de proxies sin control.
- **Mecanismo de explotación y vector:** Acceso a redes no públicas.
- **Remediación idiomática:** Aplicar autenticación en el proxy.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ SQUID Proxy Enumeration and Exploitation }
program Example;
begin
  ProxyHost := 'open-squid'; ProxyPort := 3128;
  end.
```
- **Sink peligroso y causa raíz:** Configuración de red que delega tráfico a proxies abiertos.
- **Mecanismo de explotación y vector:** Evasión de firewall.
- **Remediación idiomática:** Cerrar el acceso público al proxy.
