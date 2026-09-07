# WebDAV Misconfiguration and Abuse

## Descripción General
Aparece cuando un servidor web tiene habilitadas extensiones WebDAV con métodos HTTP peligrosos (como `PUT`, `DELETE`, `MOVE`, `PROPFIND`) sin autenticación estricta ni restricciones de directorio. Permite a atacantes no autenticados subir scripts maliciosos (webshells) directamente mediante peticiones HTTP `PUT`.

## Patrones y Señales para Análisis SAST
- Manejadores HTTP que procesan `PUT` guardando el cuerpo directamente en la ruta solicitada.
- Configuraciones de servidor web con WebDAV activo en directorios de publicación.

## Estrategia de Mitigación y Buenas Prácticas
- Deshabilitar extensiones y métodos WebDAV si no son estrictamente requeridos.
- Si WebDAV es necesario, exigir autenticación robusta y restringir los métodos autorizados.
- Impedir la escritura en directorios que permitan la ejecución de scripts del lado del servidor.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# WebDAV Enumeration and Exploitation
if request.method=='PUT': open(request.path.lstrip('/'),'wb').write(request.data)
```
- **Sink peligroso y causa raíz:** `request.method == "PUT"` escribe el contenido en el filesystem directamente.
- **Mecanismo de explotación y vector:** Subida y ejecución arbitraria de archivos.
- **Remediación idiomática:** Deshabilitar métodos HTTP no requeridos y exigir autenticación estricta.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// WebDAV Enumeration and Exploitation
function demo(req, res) {
  if(req.method==='PUT')fs.writeFileSync(req.path,req.body);
  }
```
- **Sink peligroso y causa raíz:** Manejador Express `app.put("*")` guardando archivos con nombres de la URL.
- **Mecanismo de explotación y vector:** Sobrescritura de recursos de la aplicación.
- **Remediación idiomática:** Rechazar métodos HTTP de escritura arbitraria.

### 3. Java ([java.java](./java.java))
```java
// WebDAV Enumeration and Exploitation
public class Example {
  public void demo() throws Exception {
    if(request.getMethod().equals("PUT")) Files.write(Path.of(request.getRequestURI()), request.getInputStream().readAllBytes());
      }
}
```
- **Sink peligroso y causa raíz:** Configuración de servlets WebDAV en Tomcat con `readonly=false` sin autenticación.
- **Mecanismo de explotación y vector:** RCE mediante subida de archivos `.jsp` con `PUT`.
- **Remediación idiomática:** Configurar `readonly=true` en el WebDAV servlet de Tomcat.

### 4. Go ([go.go](./go.go))
```go
// WebDAV Enumeration and Exploitation
package main
func demo() {
  if r.Method=="PUT" { os.WriteFile(r.URL.Path[1:],body,0644) }
  }
```
- **Sink peligroso y causa raíz:** Servidor HTTP procesando método `PUT` escribiendo en `r.URL.Path`.
- **Mecanismo de explotación y vector:** Subida no autorizada de archivos.
- **Remediación idiomática:** Restringir métodos a GET/POST y aplicar control de acceso.

### 5. PHP ([php.php](./php.php))
```php
<?php
// WebDAV Enumeration and Exploitation
if($_SERVER['REQUEST_METHOD']==='PUT'){file_put_contents($_SERVER['REQUEST_URI'],file_get_contents('php://input'));}
```
- **Sink peligroso y causa raíz:** Script PHP que procesa `PUT` y escribe en `$SERVER["REQUEST_URI"]`.
- **Mecanismo de explotación y vector:** Subida de webshells PHP ejecutables en el servidor.
- **Remediación idiomática:** Desactivar WebDAV en Apache/Nginx y requerir autenticación.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// WebDAV Enumeration and Exploitation
public class Example {
  public void Demo() {
    if (Request.Method == "PUT") File.WriteAllBytes(Request.Path, body);
      }
}
```
- **Sink peligroso y causa raíz:** Módulo WebDAV habilitado en IIS en carpetas con permisos de ejecución.
- **Mecanismo de explotación y vector:** Subida de archivos `.aspx` y ejecución de código.
- **Remediación idiomática:** Desinstalar o deshabilitar el módulo WebDAV de IIS.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# WebDAV Enumeration and Exploitation
def demo(params)
  File.binwrite(request.path, request.body.read) if request.put?
  end
```
- **Sink peligroso y causa raíz:** Controladores que admiten subida vía `PUT` en rutas públicas.
- **Mecanismo de explotación y vector:** Carga de archivos no autorizada.
- **Remediación idiomática:** Deshabilitar rutas WebDAV abiertas.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// WebDAV Enumeration and Exploitation
fn demo() {
  std::fs::write(path, body)?;
  }
```
- **Sink peligroso y causa raíz:** Servidores Actix/Axum con rutas `PUT` sin middleware de autenticación.
- **Mecanismo de explotación y vector:** Sobrescritura de archivos.
- **Remediación idiomática:** Exigir autenticación y validar destinos de escritura.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# WebDAV Enumeration and Exploitation
sub demo {
  if (request_method() eq 'PUT') { ... }
  }
```
- **Sink peligroso y causa raíz:** Manejador de métodos `PUT` y `MOVE` sin credenciales.
- **Mecanismo de explotación y vector:** Modificación y eliminación de archivos del sitio.
- **Remediación idiomática:** Deshabilitar soporte para WebDAV no autorizado.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ WebDAV Enumeration and Exploitation }
program Example;
begin
  if Request.Method = 'PUT' then SaveFile(Request.PathInfo, Request.Content);
  end.
```
- **Sink peligroso y causa raíz:** Implementación de endpoints WebDAV sin control de acceso.
- **Mecanismo de explotación y vector:** Escritura arbitraria en el servidor.
- **Remediación idiomática:** Restringir métodos permitidos en el despachador HTTP.
