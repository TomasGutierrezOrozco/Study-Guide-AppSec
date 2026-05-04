# ShellShock

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
El patron vulnerable aparece cuando datos de entorno controlables por el atacante llegan a Bash u otro shell con parsing inseguro.
Si un servicio pasa headers, variables CGI u otros datos del usuario a un shell vulnerable, el interprete puede ejecutar comandos adicionales durante la evaluacion de variables.

## Como identificar casos similares
- Aplicaciones CGI o wrappers que exportan headers a variables de entorno.
- Invocaciones a shell con datos del usuario en variables o comandos.
- Dependencia de Bash en rutas expuestas por red.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `os.system('echo $HTTP_USER_AGENT')`
En este ejemplo, lo vulnerable es dejar que datos del atacante alcancen el entorno o la linea de comandos de un shell interpretable. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca invocaciones a shell, exportacion de variables desde input y dependencias CGI/Bash en rutas expuestas.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { exec('bash -c "echo $HTTP_USER_AGENT"'); }`
En este ejemplo, lo vulnerable es dejar que datos del atacante alcancen el entorno o la linea de comandos de un shell interpretable. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca invocaciones a shell, exportacion de variables desde input y dependencias CGI/Bash en rutas expuestas.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { new ProcessBuilder("bash","-c","echo $HTTP_USER_AGENT").start(); } }`
En este ejemplo, lo vulnerable es dejar que datos del atacante alcancen el entorno o la linea de comandos de un shell interpretable. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca invocaciones a shell, exportacion de variables desde input y dependencias CGI/Bash en rutas expuestas.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { exec.Command("bash","-c","echo $HTTP_USER_AGENT").Run() }`
En este ejemplo, lo vulnerable es dejar que datos del atacante alcancen el entorno o la linea de comandos de un shell interpretable. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca invocaciones a shell, exportacion de variables desde input y dependencias CGI/Bash en rutas expuestas.

### PHP (`php.php`)
Fragmento representativo: `echo shell_exec('env');`
En este ejemplo, lo vulnerable es dejar que datos del atacante alcancen el entorno o la linea de comandos de un shell interpretable. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca invocaciones a shell, exportacion de variables desde input y dependencias CGI/Bash en rutas expuestas.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { system('bash', '-c', 'echo $HTTP_USER_AGENT'); }`
En este ejemplo, lo vulnerable es dejar que datos del atacante alcancen el entorno o la linea de comandos de un shell interpretable. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca invocaciones a shell, exportacion de variables desde input y dependencias CGI/Bash en rutas expuestas.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin RunCommand('/bin/bash', ['-c', 'echo $HTTP_USER_AGENT'], Output); end.`
En este ejemplo, lo vulnerable es dejar que datos del atacante alcancen el entorno o la linea de comandos de un shell interpretable. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca invocaciones a shell, exportacion de variables desde input y dependencias CGI/Bash en rutas expuestas.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) system('bash -c "echo $HTTP_USER_AGENT"') end`
En este ejemplo, lo vulnerable es dejar que datos del atacante alcancen el entorno o la linea de comandos de un shell interpretable. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca invocaciones a shell, exportacion de variables desde input y dependencias CGI/Bash en rutas expuestas.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { Command::new("bash").arg("-c").arg("echo $HTTP_USER_AGENT").output()?; }`
En este ejemplo, lo vulnerable es dejar que datos del atacante alcancen el entorno o la linea de comandos de un shell interpretable. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca invocaciones a shell, exportacion de variables desde input y dependencias CGI/Bash en rutas expuestas.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { Process.Start("bash", "-c \"echo $HTTP_USER_AGENT\""); } }`
En este ejemplo, lo vulnerable es dejar que datos del atacante alcancen el entorno o la linea de comandos de un shell interpretable. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca invocaciones a shell, exportacion de variables desde input y dependencias CGI/Bash en rutas expuestas.
