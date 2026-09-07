# AXFR Full Zone Transfer

## Descripción General
La transferencia de zona DNS (AXFR) permite replicar todos los registros de una zona entre servidores primarios y secundarios. Si un servidor DNS permite AXFR a cualquier IP no autorizada, un atacante puede descargar el mapa completo de la red interna (subdominios, IPs privadas, registros TXT con credenciales).

## Patrones y Señales para Análisis SAST
- Configuraciones DNS con `allow-transfer { any; };` o sin control de IPs autorizadas.
- Invocación directa de comandos `dig axfr` pasando dominios controlados por el usuario sin validación.

## Estrategia de Mitigación y Buenas Prácticas
- Restringir las transferencias de zona únicamente a las IPs de los servidores secundarios legítimos (`allow-transfer { secondary_ip; };`).
- Utilizar autenticación con claves TSIG (Transaction Signature) para todas las operaciones de transferencia de zona.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# AXFR Full Zone Transfer
subprocess.check_output(['dig','axfr',request.args['domain']])
```
- **Sink peligroso y causa raíz:** Ejecución de `dig axfr` con `domain` no filtrado.
- **Mecanismo de explotación y vector:** Fuga total de registros DNS o inyección de parámetros si se usa shell.
- **Remediación idiomática:** Restringir a allowlist y ejecutar resolución con librerías nativas como `dnspython` sin invocar shell.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// AXFR Full Zone Transfer
function demo(req, res) {
  exec(`dig axfr ${req.query.domain}`);
  }
```
- **Sink peligroso y causa raíz:** Invocación de binarios DNS externos con parámetros del cliente.
- **Mecanismo de explotación y vector:** Exposición de registros DNS internos o command injection.
- **Remediación idiomática:** Usar el módulo nativo `dns.promises.resolve()` para registros puntuales y nunca invocar AXFR hacia clientes.

### 3. Java ([java.java](./java.java))
```java
// AXFR Full Zone Transfer
public class Example {
  public void demo() throws Exception {
    new ProcessBuilder("dig","axfr",request.getParameter("domain")).start();
      }
}
```
- **Sink peligroso y causa raíz:** Invocación de procesos del sistema para ejecutar consultas de zona.
- **Mecanismo de explotación y vector:** Exposición de infraestructura interna al cliente web.
- **Remediación idiomática:** Usar `dnsjava` para consultas específicas y bloquear AXFR a nivel de configuración en el servidor BIND/PowerDNS.

### 4. Go ([go.go](./go.go))
```go
// AXFR Full Zone Transfer
package main
func demo() {
  exec.Command("dig","axfr",r.URL.Query().Get("domain")).Output()
  }
```
- **Sink peligroso y causa raíz:** `exec.Command("dig", "axfr", domain)` expuesto.
- **Mecanismo de explotación y vector:** Fuga de subdominios y mapeo completo de arquitectura interna.
- **Remediación idiomática:** Utilizar el paquete `net` nativo para resolver registros individuales (`LookupHost`, `LookupMX`).

### 5. PHP ([php.php](./php.php))
```php
<?php
// AXFR Full Zone Transfer
shell_exec('dig axfr '.$_GET['domain']);
```
- **Sink peligroso y causa raíz:** Uso de `shell_exec("dig axfr " . $domain)`.
- **Mecanismo de explotación y vector:** Fuga de topología de red e inyección de comandos si el host no se valida.
- **Remediación idiomática:** Usar `dns_get_record()` para consultas específicas y verificar que los nameservers tengan `allow-transfer` restringido.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// AXFR Full Zone Transfer
public class Example {
  public void Demo() {
    Process.Start("dig", "axfr " + Request.Query["domain"]);
      }
}
```
- **Sink peligroso y causa raíz:** Invocación de procesos para consultas de zona.
- **Mecanismo de explotación y vector:** Exposición de registros internos y subdominios.
- **Remediación idiomática:** Usar `Dns.GetHostEntry` en .NET y restringir transferencias en Active Directory DNS / BIND.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# AXFR Full Zone Transfer
def demo(params)
  system("dig axfr #{params[:domain]}")
  end
```
- **Sink peligroso y causa raíz:** Invocación de utilidades de shell para volcado DNS.
- **Mecanismo de explotación y vector:** Descarga de zonas privadas de la organización.
- **Remediación idiomática:** Utilizar la gema `resolv` estándar y consultar únicamente registros públicos necesarios.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// AXFR Full Zone Transfer
fn demo() {
  Command::new("dig").arg("axfr").arg(domain).output()?;
  }
```
- **Sink peligroso y causa raíz:** Llamada de procesos externos con `Command::new("dig")`.
- **Mecanismo de explotación y vector:** Fuga de topología interna.
- **Remediación idiomática:** Usar el crate `trust-dns-resolver` con consultas específicas `LookupIp`.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# AXFR Full Zone Transfer
sub demo {
  system('dig', 'axfr', param('domain'));
  }
```
- **Sink peligroso y causa raíz:** Uso de `backticks` o `system` con `dig axfr`.
- **Mecanismo de explotación y vector:** Mapeo total de la zona y ejecución de comandos si hay metacaracteres.
- **Remediación idiomática:** Usar el módulo `Net::DNS` para consultas autorizadas y restringir transferencias en el servidor.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ AXFR Full Zone Transfer }
program Example;
begin
  RunCommand('dig', ['axfr', Request.QueryFields.Values['domain']], Output);
  end.
```
- **Sink peligroso y causa raíz:** Llamada de procesos externos para solicitar volcados de zona.
- **Mecanismo de explotación y vector:** Exposición de registros DNS confidenciales.
- **Remediación idiomática:** Configurar el servidor DNS para rechazar transferencias no autorizadas.
