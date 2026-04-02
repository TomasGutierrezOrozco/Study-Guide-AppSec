# Session Puzzling / Fixation / Variable Overloading

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
El patron agrupa fallas de sesion donde identificadores o variables se mezclan de forma insegura entre autenticacion y logica de negocio.
La aplicacion reutiliza sesiones, acepta IDs fijados por el atacante o mezcla atributos de diferentes flujos en el mismo espacio de sesion. Eso facilita secuestro, confusion de identidad o escalamiento.

## Como identificar casos similares
- Sesiones que no rotan tras login o cambio de privilegios.
- Variables de sesion ambiguas para multiples propositos.
- IDs de sesion aceptados desde URL o fuentes no seguras.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `if 'sid' in request.args: session.sid=request.args['sid'] session.update(request.args)`
En este ejemplo, lo vulnerable es confiar en una sesion cuyo identificador o estado puede ser preparado, heredado o mezclado indebidamente por un atacante. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca fijacion de sesion, variables de sesion reutilizadas entre flujos y ausencia de regeneracion del identificador.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { req.session.id=req.query.sid;Object.assign(req.session,req.query); }`
En este ejemplo, lo vulnerable es confiar en una sesion cuyo identificador o estado puede ser preparado, heredado o mezclado indebidamente por un atacante. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca fijacion de sesion, variables de sesion reutilizadas entre flujos y ausencia de regeneracion del identificador.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { request.getSession(true).setAttribute("role",request.getParameter("role")); } }`
En este ejemplo, lo vulnerable es confiar en una sesion cuyo identificador o estado puede ser preparado, heredado o mezclado indebidamente por un atacante. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca fijacion de sesion, variables de sesion reutilizadas entre flujos y ausencia de regeneracion del identificador.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { session.ID=r.URL.Query().Get("sid") }`
En este ejemplo, lo vulnerable es confiar en una sesion cuyo identificador o estado puede ser preparado, heredado o mezclado indebidamente por un atacante. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca fijacion de sesion, variables de sesion reutilizadas entre flujos y ausencia de regeneracion del identificador.

### PHP (`php.php`)
Fragmento representativo: `if(isset($_GET['sid']))session_id($_GET['sid']);session_start();$_SESSION+=$_REQUEST;`
En este ejemplo, lo vulnerable es confiar en una sesion cuyo identificador o estado puede ser preparado, heredado o mezclado indebidamente por un atacante. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca fijacion de sesion, variables de sesion reutilizadas entre flujos y ausencia de regeneracion del identificador.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { session role => param('role'); }`
En este ejemplo, lo vulnerable es confiar en una sesion cuyo identificador o estado puede ser preparado, heredado o mezclado indebidamente por un atacante. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca fijacion de sesion, variables de sesion reutilizadas entre flujos y ausencia de regeneracion del identificador.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Session.ID := Request.QueryFields.Values['sid']; end.`
En este ejemplo, lo vulnerable es confiar en una sesion cuyo identificador o estado puede ser preparado, heredado o mezclado indebidamente por un atacante. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca fijacion de sesion, variables de sesion reutilizadas entre flujos y ausencia de regeneracion del identificador.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) session[:role] = params[:role] end`
En este ejemplo, lo vulnerable es confiar en una sesion cuyo identificador o estado puede ser preparado, heredado o mezclado indebidamente por un atacante. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca fijacion de sesion, variables de sesion reutilizadas entre flujos y ausencia de regeneracion del identificador.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { session.id = sid.to_string(); }`
En este ejemplo, lo vulnerable es confiar en una sesion cuyo identificador o estado puede ser preparado, heredado o mezclado indebidamente por un atacante. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca fijacion de sesion, variables de sesion reutilizadas entre flujos y ausencia de regeneracion del identificador.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { HttpContext.Session.SetString("role", Request.Query["role"]); } }`
En este ejemplo, lo vulnerable es confiar en una sesion cuyo identificador o estado puede ser preparado, heredado o mezclado indebidamente por un atacante. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca fijacion de sesion, variables de sesion reutilizadas entre flujos y ausencia de regeneracion del identificador.
