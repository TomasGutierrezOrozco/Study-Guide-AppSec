# LaTeX Injection

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad existe cuando texto controlado por el usuario se inserta en documentos LaTeX que luego son compilados.
LaTeX no es solo formato: tiene macros, inclusiones y, segun la configuracion, lectura de archivos o ejecucion de comandos. Si la entrada no se neutraliza, el atacante puede inyectar instrucciones activas.

## Como identificar casos similares
- Interpolacion directa de datos en plantillas `.tex`.
- Compilacion automatica de documentos editables por usuarios.
- Uso de macros potentes cerca de contenido no confiable.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `latex='\\input{'+request.args['name']+'}'`
En este ejemplo, lo vulnerable es pasar texto no confiable a un motor que interpreta comandos, macros y estructuras activas. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca entradas de usuario incrustadas en templates LaTeX sin escape o sin restricciones del compilador.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { const tex=`\\input{${req.query.name}}`; }`
En este ejemplo, lo vulnerable es pasar texto no confiable a un motor que interpreta comandos, macros y estructuras activas. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca entradas de usuario incrustadas en templates LaTeX sin escape o sin restricciones del compilador.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { String tex="\\input{"+request.getParameter("name")+"}"; } }`
En este ejemplo, lo vulnerable es pasar texto no confiable a un motor que interpreta comandos, macros y estructuras activas. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca entradas de usuario incrustadas en templates LaTeX sin escape o sin restricciones del compilador.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { latex:="\\input{"+r.URL.Query().Get("name")+"}" }`
En este ejemplo, lo vulnerable es pasar texto no confiable a un motor que interpreta comandos, macros y estructuras activas. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca entradas de usuario incrustadas en templates LaTeX sin escape o sin restricciones del compilador.

### PHP (`php.php`)
Fragmento representativo: `echo '\\input{'.$_GET['name'].'}';`
En este ejemplo, lo vulnerable es pasar texto no confiable a un motor que interpreta comandos, macros y estructuras activas. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca entradas de usuario incrustadas en templates LaTeX sin escape o sin restricciones del compilador.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { $tex = '\\input{' . param('name') . '}'; }`
En este ejemplo, lo vulnerable es pasar texto no confiable a un motor que interpreta comandos, macros y estructuras activas. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca entradas de usuario incrustadas en templates LaTeX sin escape o sin restricciones del compilador.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Latex := '\input{' + Request.QueryFields.Values['name'] + '}'; end.`
En este ejemplo, lo vulnerable es pasar texto no confiable a un motor que interpreta comandos, macros y estructuras activas. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca entradas de usuario incrustadas en templates LaTeX sin escape o sin restricciones del compilador.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) latex = "\\input{#{params[:name]}}" end`
En este ejemplo, lo vulnerable es pasar texto no confiable a un motor que interpreta comandos, macros y estructuras activas. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca entradas de usuario incrustadas en templates LaTeX sin escape o sin restricciones del compilador.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let latex = format!("\\input{{{}}}", name); }`
En este ejemplo, lo vulnerable es pasar texto no confiable a un motor que interpreta comandos, macros y estructuras activas. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca entradas de usuario incrustadas en templates LaTeX sin escape o sin restricciones del compilador.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { var tex = "\\input{" + Request.Query["name"] + "}"; } }`
En este ejemplo, lo vulnerable es pasar texto no confiable a un motor que interpreta comandos, macros y estructuras activas. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca entradas de usuario incrustadas en templates LaTeX sin escape o sin restricciones del compilador.
