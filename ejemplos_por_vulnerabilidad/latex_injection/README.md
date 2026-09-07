# LaTeX Injection

## Descripción General
Ocurre cuando una aplicación genera documentos PDF o imágenes a partir de plantillas LaTeX concatenando texto no confiable del usuario. Comandos como `\input{/etc/passwd}` permiten leer archivos locales del servidor, y directivas como `\write18` permiten ejecutar comandos en el sistema operativo.

## Patrones y Señales para Análisis SAST
- Concatenación de variables en código fuente `.tex` o llamadas a `pdflatex`.
- Ausencia de deshabilitación de comandos shell (`-no-shell-escape`).

## Estrategia de Mitigación y Buenas Prácticas
- Escapar todos los caracteres especiales de LaTeX (`\`, `{`, `}`, `$`, `&`, `%`, `#`, `_`, `^`, `~`).
- Ejecutar el compilador LaTeX en un sandbox aislado con banderas de seguridad: `pdflatex -no-shell-escape`.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# LaTeX Injection
latex='\\input{'+request.args['name']+'}'
```
- **Sink peligroso y causa raíz:** Concatenación de `request.args["name"]` dentro de comandos `\input{}`.
- **Mecanismo de explotación y vector:** Lectura de archivos del servidor y RCE si shell-escape está activo.
- **Remediación idiomática:** Sanitizar caracteres especiales o usar plantillas con motor seguro y compilación aislada.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// LaTeX Injection
function demo(req, res) {
  const tex=`\\input{${req.query.name}}`;
  }
```
- **Sink peligroso y causa raíz:** Construcción de cadenas LaTeX con parámetros de query.
- **Mecanismo de explotación y vector:** Exfiltración de archivos confidenciales del host.
- **Remediación idiomática:** Escapar metacaracteres de LaTeX antes de compilar el documento.

### 3. Java ([java.java](./java.java))
```java
// LaTeX Injection
public class Example {
  public void demo() throws Exception {
    String tex="\\input{"+request.getParameter("name")+"}";
      }
}
```
- **Sink peligroso y causa raíz:** Interpolación de cadenas en documentos LaTeX procesados por binarios del sistema.
- **Mecanismo de explotación y vector:** Lectura arbitraria de ficheros en el backend.
- **Remediación idiomática:** Aplicar filtros de reemplazo estricto sobre caracteres reservados de LaTeX.

### 4. Go ([go.go](./go.go))
```go
// LaTeX Injection
package main
func demo() {
  latex:="\\input{"+r.URL.Query().Get("name")+"}"
  }
```
- **Sink peligroso y causa raíz:** Formateo de documentos `.tex` con datos crudos del usuario.
- **Mecanismo de explotación y vector:** Compromiso de la confidencialidad de archivos del servidor.
- **Remediación idiomática:** Restringir la entrada a caracteres estrictamente alfanuméricos.

### 5. PHP ([php.php](./php.php))
```php
<?php
// LaTeX Injection
echo '\\input{'.$_GET['name'].'}';
```
- **Sink peligroso y causa raíz:** `echo "\input{" . $_GET["name"] . "}";` en scripts que generan reportes.
- **Mecanismo de explotación y vector:** Inclusión de archivos locales en el PDF resultante.
- **Remediación idiomática:** Escapar backslashes y llaves con funciones de sanitización.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// LaTeX Injection
public class Example {
  public void Demo() {
    var tex = "\\input{" + Request.Query["name"] + "}";
      }
}
```
- **Sink peligroso y causa raíz:** Generación de reportes PDF concatenando código LaTeX.
- **Mecanismo de explotación y vector:** Fuga de información confidencial.
- **Remediación idiomática:** Reemplazar por generadores de PDF seguros que no dependan de motores LaTeX.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# LaTeX Injection
def demo(params)
  latex = "\\input{#{params[:name]}}"
  end
```
- **Sink peligroso y causa raíz:** Interpolación en cadenas procesadas por `pdflatex`.
- **Mecanismo de explotación y vector:** Fuga de secretos y archivos de configuración.
- **Remediación idiomática:** Utilizar librerías de escape de LaTeX como `latex-escaper`.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// LaTeX Injection
fn demo() {
  let latex = format!("\\input{{{}}}", name);
  }
```
- **Sink peligroso y causa raíz:** Construcción de fuentes LaTeX con parámetros del cliente.
- **Mecanismo de explotación y vector:** Lectura de archivos locales en la compilación.
- **Remediación idiomática:** Sanitizar metacaracteres reservados.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# LaTeX Injection
sub demo {
  $tex = '\\input{' . param('name') . '}';
  }
```
- **Sink peligroso y causa raíz:** Inserción de texto en documentos LaTeX.
- **Mecanismo de explotación y vector:** Lectura de `/etc/passwd` o ejecución de comandos.
- **Remediación idiomática:** Filtrar toda directiva que comience con backslash.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ LaTeX Injection }
program Example;
begin
  Latex := '\input{' + Request.QueryFields.Values['name'] + '}';
  end.
```
- **Sink peligroso y causa raíz:** Generación de plantillas LaTeX con texto directo.
- **Mecanismo de explotación y vector:** Lectura no autorizada de archivos del host.
- **Remediación idiomática:** Sanitizar la entrada antes del formateo.
