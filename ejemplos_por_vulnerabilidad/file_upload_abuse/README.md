# File Upload Abuse

## Descripción General
Se produce cuando una aplicación permite a los usuarios subir archivos pero no valida adecuadamente el nombre del archivo, su tipo real de contenido (MIME), su tamaño ni la ubicación de guardado. Permite subir webshells ejecutables a directorios web públicos, sobrescribir archivos del sistema mediante Path Traversal o agotar el espacio en disco.

## Patrones y Señales para Análisis SAST
- Uso directo del nombre provisto por el cliente (`filename`) al guardar el archivo sin sanitizar.
- Almacenamiento de archivos dentro del Document Root del servidor web con permisos de ejecución.
- Validación basada exclusivamente en la cabecera `Content-Type` enviada por el navegador.

## Estrategia de Mitigación y Buenas Prácticas
- Renombrar los archivos usando identificadores únicos generados en el servidor (ej. UUIDv4).
- Almacenar los archivos fuera del directorio raíz web (`webroot`) o en servicios de almacenamiento de objetos (S3, GCS).
- Validar el tipo real analizando los primeros bytes (Magic Bytes) y verificar extensiones contra una allowlist estricta.
- Deshabilitar permisos de ejecución en el directorio de almacenamiento.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# File Upload Abuse
request.files['file'].save('uploads/'+request.files['file'].filename)
```
- **Sink peligroso y causa raíz:** `request.files["file"].save("uploads/" + file.filename)` sin sanitización.
- **Mecanismo de explotación y vector:** Path traversal (`../../`) y sobrescritura de archivos o subida de scripts ejecutables.
- **Remediación idiomática:** Usar `werkzeug.utils.secure_filename` y generar nombres con `uuid.uuid4()`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// File Upload Abuse
function demo(req, res) {
  fs.writeFileSync('uploads/'+req.files.file.name,req.files.file.data);
  }
```
- **Sink peligroso y causa raíz:** `fs.writeFileSync("uploads/" + req.files.file.name, ...)` directo.
- **Mecanismo de explotación y vector:** Sobrescritura de archivos arbitrarios y ejecución de código si el directorio se sirve públicamente.
- **Remediación idiomática:** Usar `path.basename()` y generar un UUID seguro con `crypto.randomUUID()`.

### 3. Java ([java.java](./java.java))
```java
// File Upload Abuse
public class Example {
  public void demo() throws Exception {
    part.write("uploads/"+part.getSubmittedFileName());
      }
}
```
- **Sink peligroso y causa raíz:** `part.write("uploads/" + part.getSubmittedFileName())` con nombre del cliente.
- **Mecanismo de explotación y vector:** Path traversal hacia rutas de sistema.
- **Remediación idiomática:** Sanitizar con `Paths.get(name).getFileName().toString()` y verificar `startsWith(baseDir)`.

### 4. Go ([go.go](./go.go))
```go
// File Upload Abuse
package main
func demo() {
  f,_,_:=r.FormFile("file")
  _ = f
  }
```
- **Sink peligroso y causa raíz:** Guardado de archivo usando directamente el nombre de `r.FormFile`.
- **Mecanismo de explotación y vector:** Path traversal y almacenamiento de archivos en ubicaciones críticas.
- **Remediación idiomática:** Usar `filepath.Base()` o nombres aleatorios y limitar tamaño con `http.MaxBytesReader`.

### 5. PHP ([php.php](./php.php))
```php
<?php
// File Upload Abuse
move_uploaded_file($_FILES['f']['tmp_name'],'uploads/'.$_FILES['f']['name']);
```
- **Sink peligroso y causa raíz:** `move_uploaded_file($_FILES["f"]["tmp_name"], "uploads/" . $_FILES["f"]["name"])`.
- **Mecanismo de explotación y vector:** Subida directa de webshells PHP (`.php`) que se ejecutan directamente al visitarlas.
- **Remediación idiomática:** Renombrar a hash/UUID, validar extensión contra allowlist y guardar fuera de `public_html`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// File Upload Abuse
public class Example {
  public void Demo() {
    using var fs = File.Create("uploads/" + file.FileName);
      }
}
```
- **Sink peligroso y causa raíz:** `File.Create("uploads/" + file.FileName)` usando el nombre directo del cliente.
- **Mecanismo de explotación y vector:** Path traversal en sistemas de archivos locales.
- **Remediación idiomática:** Usar `Path.GetRandomFileName()` y almacenar metadatos en base de datos.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# File Upload Abuse
def demo(params)
  File.binwrite("uploads/#{params[:file].original_filename}", params[:file].read)
  end
```
- **Sink peligroso y causa raíz:** `File.binwrite("uploads/#{params[:file].original_filename}", ...)` directo.
- **Mecanismo de explotación y vector:** Path traversal y sobrescritura de código de la aplicación.
- **Remediación idiomática:** Sanitizar con `File.basename` y generar identificador con `SecureRandom.uuid`.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// File Upload Abuse
fn demo() {
  std::fs::write(format!("uploads/{}", filename), bytes)?;
  }
```
- **Sink peligroso y causa raíz:** `std::fs::write(format!("uploads/{}", filename), bytes)` sin validar.
- **Mecanismo de explotación y vector:** Escritura arbitraria de archivos en el sistema de archivos.
- **Remediación idiomática:** Usar `Path::new(filename).file_name()` o identificadores UUID con el crate `uuid`.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# File Upload Abuse
sub demo {
  open my $fh, '>', 'uploads/' . $filename;
  }
```
- **Sink peligroso y causa raíz:** Apertura de archivo con nombre controlado por el usuario.
- **Mecanismo de explotación y vector:** Path traversal o inyección de modos en llamadas de apertura.
- **Remediación idiomática:** Sanitizar con `File::Basename::basename` y forzar nombres aleatorios.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ File Upload Abuse }
program Example;
begin
  Upload.SaveToFile('uploads/' + Upload.FileName);
  end.
```
- **Sink peligroso y causa raíz:** `Upload.SaveToFile("uploads/" + Upload.FileName)` sin control.
- **Mecanismo de explotación y vector:** Sobrescritura de binarios o bibliotecas del servidor.
- **Remediación idiomática:** Usar `ExtractFileName()` y generar identificador aleatorio.
