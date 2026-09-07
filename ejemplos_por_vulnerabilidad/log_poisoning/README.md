# Log Poisoning (LFI a RCE)

## Descripción General
Consiste en inyectar código ejecutable (comúnmente PHP como `<?php system($_GET["cmd"]); ?>`) en los registros del servidor (por ejemplo, enviándolo en el encabezado `User-Agent` o en la URL), y posteriormente utilizar una vulnerabilidad de Local File Inclusion (LFI) para incluir dicho archivo de log (`access.log`, `error.log`), logrando Remote Code Execution.

## Patrones y Señales para Análisis SAST
- Escritura de cabeceras HTTP crudas en archivos de log sin sanitizar.
- Presencia de inclusión dinámica de archivos (`include`, `render_template_string(open(log))`) capaz de alcanzar rutas de logs.

## Estrategia de Mitigación y Buenas Prácticas
- Corregir la vulnerabilidad primaria de LFI impidiendo la inclusión de archivos arbitrarios.
- Sanitizar o codificar los datos que se registran en los logs para neutralizar delimitadores de código.
- Almacenar los archivos de log en directorios inaccesibles para el usuario del servidor web.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Log Poisoning (LFI a RCE)
def demo():
    open('access.log', 'a').write(request.headers.get('User-Agent', '') + '\n')
    return render_template_string(open(request.args['page']).read())
```
- **Sink peligroso y causa raíz:** Registro de `User-Agent` en archivo y posterior carga con `render_template_string`.
- **Mecanismo de explotación y vector:** SSTI / RCE a través del archivo de log infectado.
- **Remediación idiomática:** No renderizar logs como plantillas y codificar caracteres de control en el logger.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Log Poisoning (LFI a RCE)
function demo(req, res) {
  fs.appendFileSync('access.log',req.headers['user-agent']+'\n');
  res.send(fs.readFileSync(req.query.page,'utf8'));
  }
```
- **Sink peligroso y causa raíz:** Escritura de encabezados en log y posterior lectura/evaluación.
- **Mecanismo de explotación y vector:** Ejecución de código en el servidor.
- **Remediación idiomática:** Sanitizar entradas en logs y no exponer archivos de log a endpoints de lectura.

### 3. Java ([java.java](./java.java))
```java
// Log Poisoning (LFI a RCE)
public class Example {
  public void demo() throws Exception {
    logger.info(request.getHeader("User-Agent"));
    Files.readString(Path.of(request.getParameter("page")));
      }
}
```
- **Sink peligroso y causa raíz:** Escritura de logs sin sanitizar combinada con inclusión de plantillas.
- **Mecanismo de explotación y vector:** RCE o DoS.
- **Remediación idiomática:** Usar patrones de logging seguros sin evaluación dinámica.

### 4. Go ([go.go](./go.go))
```go
// Log Poisoning (LFI a RCE)
package main
func demo() {
  os.WriteFile("access.log",[]byte(r.UserAgent()),0644)
  os.ReadFile(r.URL.Query().Get("page"))
  }
```
- **Sink peligroso y causa raíz:** Escritura de `r.UserAgent()` y lectura sin control.
- **Mecanismo de explotación y vector:** Corrupción de logs y potencial RCE en backends interpretados.
- **Remediación idiomática:** Validar entradas y mantener los logs en servicios dedicados fuera del contenedor web.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Log Poisoning (LFI a RCE)
file_put_contents('access.log', $_SERVER['HTTP_USER_AGENT'].PHP_EOL, FILE_APPEND);
include($_GET['page']);
```
- **Sink peligroso y causa raíz:** Escritura de `$_SERVER["HTTP_USER_AGENT"]` en `access.log` + `include("access.log")`.
- **Mecanismo de explotación y vector:** RCE inmediato: el motor PHP ejecuta el código inyectado en el log.
- **Remediación idiomática:** Eliminar el LFI usando allowlists de archivos y aislar los logs del webroot.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Log Poisoning (LFI a RCE)
public class Example {
  public void Demo() {
    File.AppendAllText("access.log", Request.Headers["User-Agent"]);
      }
}
```
- **Sink peligroso y causa raíz:** Escritura de encabezados de usuario en logs consumidos por evaluadores dinámicos.
- **Mecanismo de explotación y vector:** RCE secundario.
- **Remediación idiomática:** Utilizar librerías de logging estructurado (Serilog) sin interpretación dinámica.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Log Poisoning (LFI a RCE)
def demo(params)
  File.write('access.log', request.user_agent, mode: 'a')
  render plain: File.read(params[:page])
  end
```
- **Sink peligroso y causa raíz:** Inyección en logs de Rails combinada con renderizado dinámico de archivos.
- **Mecanismo de explotación y vector:** Ejecución remota de comandos.
- **Remediación idiomática:** Separar almacenamiento de logs y no permitir renderizado de archivos de log.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Log Poisoning (LFI a RCE)
fn demo() {
  std::fs::write("access.log", ua)?;
  }
```
- **Sink peligroso y causa raíz:** Registro de texto con secuencias maliciosas.
- **Mecanismo de explotación y vector:** Aunque Rust no evalúa código, puede usarse para contaminar logs en sistemas mixtos.
- **Remediación idiomática:** Sanitizar entradas de registro.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Log Poisoning (LFI a RCE)
sub demo {
  print $log $ENV{'HTTP_USER_AGENT'};
  }
```
- **Sink peligroso y causa raíz:** Registro de entradas no sanitizadas en logs procesados posteriormente.
- **Mecanismo de explotación y vector:** Ejecución de código arbitrario.
- **Remediación idiomática:** Sanitizar datos antes de registrarlos en disco.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Log Poisoning (LFI a RCE) }
program Example;
begin
  WriteLn(LogFile, Request.UserAgent);
  Response.Content := LoadFile(Request.QueryFields.Values['page']);
  end.
```
- **Sink peligroso y causa raíz:** Almacenamiento de strings de cabeceras en archivos de registro.
- **Mecanismo de explotación y vector:** Explotación secundaria vía inclusión de archivos.
- **Remediación idiomática:** Filtrar metacaracteres en el subsistema de logs.
