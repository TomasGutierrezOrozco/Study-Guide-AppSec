# File Upload Abuse

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad aparece cuando la aplicacion acepta archivos sin validar tipo real, contenido, ubicacion o permisos de ejecucion.
Subir un archivo no es el problema principal. El riesgo aparece cuando se confia solo en la extension o en `Content-Type`, se guarda dentro del webroot o se procesa con herramientas inseguras.

## Como identificar casos similares
- Validaciones basadas solo en nombre o MIME enviado por el cliente.
- Archivos guardados en rutas publicas o predecibles.
- Falta de limites de tamano, renombrado seguro o allowlists de formatos.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `request.files['file'].save('uploads/'+request.files['file'].filename)`
En este ejemplo, lo vulnerable es aceptar y persistir contenido que el atacante controla sin limitar tipo, destino ni forma de uso posterior. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca endpoints que escriban archivos con nombres o rutas controladas por el usuario o que validen solo extension/MIME.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { fs.writeFileSync('uploads/'+req.files.file.name,req.files.file.data); }`
En este ejemplo, lo vulnerable es aceptar y persistir contenido que el atacante controla sin limitar tipo, destino ni forma de uso posterior. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca endpoints que escriban archivos con nombres o rutas controladas por el usuario o que validen solo extension/MIME.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { part.write("uploads/"+part.getSubmittedFileName()); } }`
En este ejemplo, lo vulnerable es aceptar y persistir contenido que el atacante controla sin limitar tipo, destino ni forma de uso posterior. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca endpoints que escriban archivos con nombres o rutas controladas por el usuario o que validen solo extension/MIME.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { f,_,_:=r.FormFile("file") _ = f }`
En este ejemplo, lo vulnerable es aceptar y persistir contenido que el atacante controla sin limitar tipo, destino ni forma de uso posterior. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca endpoints que escriban archivos con nombres o rutas controladas por el usuario o que validen solo extension/MIME.

### PHP (`php.php`)
Fragmento representativo: `move_uploaded_file($_FILES['f']['tmp_name'],'uploads/'.$_FILES['f']['name']);`
En este ejemplo, lo vulnerable es aceptar y persistir contenido que el atacante controla sin limitar tipo, destino ni forma de uso posterior. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca endpoints que escriban archivos con nombres o rutas controladas por el usuario o que validen solo extension/MIME.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { open my $fh, '>', 'uploads/' . $filename; }`
En este ejemplo, lo vulnerable es aceptar y persistir contenido que el atacante controla sin limitar tipo, destino ni forma de uso posterior. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca endpoints que escriban archivos con nombres o rutas controladas por el usuario o que validen solo extension/MIME.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Upload.SaveToFile('uploads/' + Upload.FileName); end.`
En este ejemplo, lo vulnerable es aceptar y persistir contenido que el atacante controla sin limitar tipo, destino ni forma de uso posterior. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca endpoints que escriban archivos con nombres o rutas controladas por el usuario o que validen solo extension/MIME.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) File.binwrite("uploads/#{params[:file].original_filename}", params[:file].read) end`
En este ejemplo, lo vulnerable es aceptar y persistir contenido que el atacante controla sin limitar tipo, destino ni forma de uso posterior. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca endpoints que escriban archivos con nombres o rutas controladas por el usuario o que validen solo extension/MIME.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { std::fs::write(format!("uploads/{}", filename), bytes)?; }`
En este ejemplo, lo vulnerable es aceptar y persistir contenido que el atacante controla sin limitar tipo, destino ni forma de uso posterior. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca endpoints que escriban archivos con nombres o rutas controladas por el usuario o que validen solo extension/MIME.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { using var fs = File.Create("uploads/" + file.FileName); } }`
En este ejemplo, lo vulnerable es aceptar y persistir contenido que el atacante controla sin limitar tipo, destino ni forma de uso posterior. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca endpoints que escriban archivos con nombres o rutas controladas por el usuario o que validen solo extension/MIME.
