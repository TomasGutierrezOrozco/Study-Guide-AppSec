# Log Poisoning (LFI a RCE)

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
El patron combina inyeccion en logs con inclusion o ejecucion posterior de esos mismos archivos.
Si la aplicacion escribe datos del atacante en un log y luego ese archivo puede incluirse como codigo o plantilla, un registro termina convertido en payload ejecutable.

## Como identificar casos similares
- Logs que almacenan headers, rutas o parametros sin neutralizacion.
- Funciones de inclusion que pueden apuntar a archivos de log.
- Aplicaciones donde los logs quedan dentro de rutas accesibles o interpretables.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `open('access.log','a').write(request.headers.get('User-Agent','')+'\n') return render_template_string(open(request.args['page']).read())`
En este ejemplo, lo vulnerable es registrar entrada controlada por el atacante en un archivo que mas tarde puede ser tratado como codigo o template. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca escritura de logs con datos no confiables y, ademas, alguna via para incluir o ejecutar esos mismos archivos.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { fs.appendFileSync('access.log',req.headers['user-agent']+'\n'); res.send(fs.readFileSync(req.query.page,'utf8')); }`
En este ejemplo, lo vulnerable es registrar entrada controlada por el atacante en un archivo que mas tarde puede ser tratado como codigo o template. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca escritura de logs con datos no confiables y, ademas, alguna via para incluir o ejecutar esos mismos archivos.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { logger.info(request.getHeader("User-Agent")); Files.readString(Path.of(request.getParameter("page"))); } }`
En este ejemplo, lo vulnerable es registrar entrada controlada por el atacante en un archivo que mas tarde puede ser tratado como codigo o template. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca escritura de logs con datos no confiables y, ademas, alguna via para incluir o ejecutar esos mismos archivos.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { os.WriteFile("access.log",[]byte(r.UserAgent()),0644) os.ReadFile(r.URL.Query().Get("page")) }`
En este ejemplo, lo vulnerable es registrar entrada controlada por el atacante en un archivo que mas tarde puede ser tratado como codigo o template. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca escritura de logs con datos no confiables y, ademas, alguna via para incluir o ejecutar esos mismos archivos.

### PHP (`php.php`)
Fragmento representativo: `file_put_contents('access.log', $_SERVER['HTTP_USER_AGENT'].PHP_EOL, FILE_APPEND); include($_GET['page']);`
En este ejemplo, lo vulnerable es registrar entrada controlada por el atacante en un archivo que mas tarde puede ser tratado como codigo o template. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca escritura de logs con datos no confiables y, ademas, alguna via para incluir o ejecutar esos mismos archivos.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { print $log $ENV{'HTTP_USER_AGENT'}; }`
En este ejemplo, lo vulnerable es registrar entrada controlada por el atacante en un archivo que mas tarde puede ser tratado como codigo o template. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca escritura de logs con datos no confiables y, ademas, alguna via para incluir o ejecutar esos mismos archivos.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin WriteLn(LogFile, Request.UserAgent); Response.Content := LoadFile(Request.QueryFields.Values['page']); end.`
En este ejemplo, lo vulnerable es registrar entrada controlada por el atacante en un archivo que mas tarde puede ser tratado como codigo o template. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca escritura de logs con datos no confiables y, ademas, alguna via para incluir o ejecutar esos mismos archivos.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) File.write('access.log', request.user_agent, mode: 'a') render plain: File.read(params[:page]) end`
En este ejemplo, lo vulnerable es registrar entrada controlada por el atacante en un archivo que mas tarde puede ser tratado como codigo o template. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca escritura de logs con datos no confiables y, ademas, alguna via para incluir o ejecutar esos mismos archivos.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { std::fs::write("access.log", ua)?; }`
En este ejemplo, lo vulnerable es registrar entrada controlada por el atacante en un archivo que mas tarde puede ser tratado como codigo o template. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca escritura de logs con datos no confiables y, ademas, alguna via para incluir o ejecutar esos mismos archivos.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { File.AppendAllText("access.log", Request.Headers["User-Agent"]); } }`
En este ejemplo, lo vulnerable es registrar entrada controlada por el atacante en un archivo que mas tarde puede ser tratado como codigo o template. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca escritura de logs con datos no confiables y, ademas, alguna via para incluir o ejecutar esos mismos archivos.
