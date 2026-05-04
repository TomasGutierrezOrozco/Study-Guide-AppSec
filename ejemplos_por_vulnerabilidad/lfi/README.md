# Local File Inclusion (LFI)

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad existe cuando el usuario controla una ruta de archivo local que la aplicacion abre, incluye o renderiza.
El nombre de archivo parece un dato inocuo, pero en realidad define que recurso del sistema se leera o ejecutara. Con traversal o rutas absolutas, el atacante sale del directorio esperado.

## Como identificar casos similares
- Parametros `file`, `page`, `template` o `path` usados en `open`, `include` o equivalentes.
- Ausencia de canonicalizacion y validacion de la ruta final.
- Soporte para `../`, rutas absolutas o extensiones controladas por el usuario.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `return open(request.args['file']).read()`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario decida que archivo del sistema se abre o se incluye. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca parametros de ruta usados en lectura, inclusion o render de archivos sin resolver y validar la ruta final.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { res.send(fs.readFileSync(req.query.file,'utf8')); }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario decida que archivo del sistema se abre o se incluye. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca parametros de ruta usados en lectura, inclusion o render de archivos sin resolver y validar la ruta final.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { Files.readString(Path.of(request.getParameter("file"))); } }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario decida que archivo del sistema se abre o se incluye. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca parametros de ruta usados en lectura, inclusion o render de archivos sin resolver y validar la ruta final.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { os.ReadFile(r.URL.Query().Get("file")) }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario decida que archivo del sistema se abre o se incluye. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca parametros de ruta usados en lectura, inclusion o render de archivos sin resolver y validar la ruta final.

### PHP (`php.php`)
Fragmento representativo: `include($_GET['file']);`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario decida que archivo del sistema se abre o se incluye. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca parametros de ruta usados en lectura, inclusion o render de archivos sin resolver y validar la ruta final.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { print do { local(@ARGV, $/) = $file; <> }; }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario decida que archivo del sistema se abre o se incluye. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca parametros de ruta usados en lectura, inclusion o render de archivos sin resolver y validar la ruta final.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Response.Content := TStringList.Create.Text; end.`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario decida que archivo del sistema se abre o se incluye. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca parametros de ruta usados en lectura, inclusion o render de archivos sin resolver y validar la ruta final.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) render plain: File.read(params[:file]) end`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario decida que archivo del sistema se abre o se incluye. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca parametros de ruta usados en lectura, inclusion o render de archivos sin resolver y validar la ruta final.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let body = std::fs::read_to_string(file)?; }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario decida que archivo del sistema se abre o se incluye. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca parametros de ruta usados en lectura, inclusion o render de archivos sin resolver y validar la ruta final.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { var body = File.ReadAllText(Request.Query["file"]); } }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario decida que archivo del sistema se abre o se incluye. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca parametros de ruta usados en lectura, inclusion o render de archivos sin resolver y validar la ruta final.
