# Local File Inclusion (LFI)

## Descripción General
Permite a un atacante incluir y leer (o en ciertos entornos ejecutar) archivos del sistema de archivos local manipulando rutas provistas en parámetros HTTP. Utiliza secuencias de escape como `../` o rutas absolutas hacia archivos sensibles (`/etc/passwd`, variables de entorno, código fuente).

## Patrones y Señales para Análisis SAST
- Paso de entradas de usuario a funciones de lectura o inclusión (`open()`, `include()`, `fs.readFile()`, `os.ReadFile()`).
- Ausencia de canonicalización de rutas y verificación del directorio base.

## Estrategia de Mitigación y Buenas Prácticas
- Usar una allowlist estricta de nombres de archivos válidos en lugar de rutas dinámicas.
- Canonicalizar la ruta (`realpath`, `Path.normalize()`) y comprobar que comience con el directorio base permitido.
- Almacenar archivos con nombres generados por la aplicación en carpetas aisladas.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Local File Inclusion (LFI)
def demo():
    return open(request.args['file']).read()
```
- **Sink peligroso y causa raíz:** `open(request.args["file"]).read()` sin validar la ruta.
- **Mecanismo de explotación y vector:** Lectura de archivos del sistema como `/etc/passwd` o código confidencial.
- **Remediación idiomática:** Usar `Path(base).resolve()` y verificar `target.is_relative_to(base)`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Local File Inclusion (LFI)
function demo(req, res) {
  res.send(fs.readFileSync(req.query.file,'utf8'));
  }
```
- **Sink peligroso y causa raíz:** `fs.readFileSync("templates/" + req.query.file)` con concatenación.
- **Mecanismo de explotación y vector:** Fuga de secretos de entorno y archivos de configuración.
- **Remediación idiomática:** Usar `path.resolve` y verificar `targetPath.startsWith(allowedBasePath)`.

### 3. Java ([java.java](./java.java))
```java
// Local File Inclusion (LFI)
public class Example {
  public void demo() throws Exception {
    Files.readString(Path.of(request.getParameter("file")));
      }
}
```
- **Sink peligroso y causa raíz:** `Files.readString(Path.of("/docs").resolve(filename))` sin canonicalizar.
- **Mecanismo de explotación y vector:** Lectura arbitraria del sistema de archivos del host.
- **Remediación idiomática:** Usar `target.toRealPath()` y comprobar `target.startsWith(baseDir)`.

### 4. Go ([go.go](./go.go))
```go
// Local File Inclusion (LFI)
package main
func demo() {
  os.ReadFile(r.URL.Query().Get("file"))
  }
```
- **Sink peligroso y causa raíz:** `os.ReadFile("/data/" + name)` sin verificación de ruta base.
- **Mecanismo de explotación y vector:** Extracción de archivos confidenciales del contenedor/host.
- **Remediación idiomática:** Usar `filepath.Clean` y verificar con `filepath.Rel` que no inicie con `..`.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Local File Inclusion (LFI)
include($_GET['file']);
```
- **Sink peligroso y causa raíz:** `include($_GET["file"])` directo.
- **Mecanismo de explotación y vector:** LFI con ejecución de código PHP si el archivo contiene código ejecutable.
- **Remediación idiomática:** Usar allowlist de nombres permitidos: `if (!in_array($f, $allowed)) die(); include $f;`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Local File Inclusion (LFI)
public class Example {
  public void Demo() {
    var body = File.ReadAllText(Request.Query["file"]);
      }
}
```
- **Sink peligroso y causa raíz:** `File.ReadAllText(Path.Combine(basePath, userInput))` sin verificación de prefijo.
- **Mecanismo de explotación y vector:** Bypass de carpeta base mediante rutas absolutas o traversal.
- **Remediación idiomática:** Verificar con `Path.GetFullPath` que la ruta inicie con `baseDir`.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Local File Inclusion (LFI)
def demo(params)
  render plain: File.read(params[:file])
  end
```
- **Sink peligroso y causa raíz:** `File.read("pages/#{params[:page]}")` sin normalizar.
- **Mecanismo de explotación y vector:** Lectura de credenciales y archivos de base de datos.
- **Remediación idiomática:** Usar `Pathname.new(path).cleanpath` y verificar pertenencia al directorio base.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Local File Inclusion (LFI)
fn demo() {
  let body = std::fs::read_to_string(file)?;
  }
```
- **Sink peligroso y causa raíz:** `std::fs::read_to_string(format!("/srv/{}", file))` sin comprobación.
- **Mecanismo de explotación y vector:** Lectura de archivos confidenciales en el host.
- **Remediación idiomática:** Usar `canonicalize()` y verificar que el path comience con la base esperada.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Local File Inclusion (LFI)
sub demo {
  print do { local(@ARGV, $/) = $file; <> };
  }
```
- **Sink peligroso y causa raíz:** `open my $fh, "<", $path` donde `$path` viene de parámetro.
- **Mecanismo de explotación y vector:** Lectura de archivos arbitrarios del servidor.
- **Remediación idiomática:** Validar la ruta y restringir a nombres de archivo predefinidos.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Local File Inclusion (LFI) }
program Example;
begin
  Response.Content := TStringList.Create.Text;
  end.
```
- **Sink peligroso y causa raíz:** Lectura de archivos con nombres tomados de la solicitud.
- **Mecanismo de explotación y vector:** Exposición de archivos del sistema operativo.
- **Remediación idiomática:** Verificar que la ruta resuelta permanezca en la carpeta autorizada.
