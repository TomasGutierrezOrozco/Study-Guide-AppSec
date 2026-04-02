# CORS Misconfiguration

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
El problema aparece cuando el servidor permite origenes inseguros o refleja `Origin` sin validacion.
Si una respuesta sensible queda disponible para origenes no confiables, una pagina del atacante puede leer datos autenticados desde el navegador de la victima. El riesgo aumenta cuando tambien se permiten credenciales.

## Como identificar casos similares
- `Access-Control-Allow-Origin: *` en respuestas sensibles.
- Reflexion directa del header `Origin`.
- Allowlists basadas en prefijos, subcadenas o regex debiles.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `resp.headers['Access-Control-Allow-Origin']='*';resp.headers['Access-Control-Allow-Credentials']='true'`
En este ejemplo, lo vulnerable es que el servidor le da confianza cross-origin a un origen que no deberia poder leer la respuesta. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca codigo que calcule headers CORS desde `Origin` o que permita cualquier origen para recursos autenticados o sensibles.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { res.setHeader('Access-Control-Allow-Origin','*');res.setHeader('Access-Control-Allow-Credentials','true'); }`
En este ejemplo, lo vulnerable es que el servidor le da confianza cross-origin a un origen que no deberia poder leer la respuesta. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca codigo que calcule headers CORS desde `Origin` o que permita cualquier origen para recursos autenticados o sensibles.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { response.setHeader("Access-Control-Allow-Origin","*");response.setHeader("Access-Control-Allow-Credentials","true");...`
En este ejemplo, lo vulnerable es que el servidor le da confianza cross-origin a un origen que no deberia poder leer la respuesta. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca codigo que calcule headers CORS desde `Origin` o que permita cualquier origen para recursos autenticados o sensibles.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { w.Header().Set("Access-Control-Allow-Origin","*") w.Header().Set("Access-Control-Allow-Credentials","true") }`
En este ejemplo, lo vulnerable es que el servidor le da confianza cross-origin a un origen que no deberia poder leer la respuesta. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca codigo que calcule headers CORS desde `Origin` o que permita cualquier origen para recursos autenticados o sensibles.

### PHP (`php.php`)
Fragmento representativo: `header('Access-Control-Allow-Origin:*'); header('Access-Control-Allow-Credentials:true');`
En este ejemplo, lo vulnerable es que el servidor le da confianza cross-origin a un origen que no deberia poder leer la respuesta. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca codigo que calcule headers CORS desde `Origin` o que permita cualquier origen para recursos autenticados o sensibles.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { header('Access-Control-Allow-Origin' => '*'); }`
En este ejemplo, lo vulnerable es que el servidor le da confianza cross-origin a un origen que no deberia poder leer la respuesta. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca codigo que calcule headers CORS desde `Origin` o que permita cualquier origen para recursos autenticados o sensibles.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Response.SetCustomHeader('Access-Control-Allow-Origin', '*'); end.`
En este ejemplo, lo vulnerable es que el servidor le da confianza cross-origin a un origen que no deberia poder leer la respuesta. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca codigo que calcule headers CORS desde `Origin` o que permita cualquier origen para recursos autenticados o sensibles.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) response.set_header('Access-Control-Allow-Origin', '*') end`
En este ejemplo, lo vulnerable es que el servidor le da confianza cross-origin a un origen que no deberia poder leer la respuesta. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca codigo que calcule headers CORS desde `Origin` o que permita cualquier origen para recursos autenticados o sensibles.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { headers.insert("Access-Control-Allow-Origin", "*"); }`
En este ejemplo, lo vulnerable es que el servidor le da confianza cross-origin a un origen que no deberia poder leer la respuesta. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca codigo que calcule headers CORS desde `Origin` o que permita cualquier origen para recursos autenticados o sensibles.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { Response.Headers["Access-Control-Allow-Origin"] = "*"; } }`
En este ejemplo, lo vulnerable es que el servidor le da confianza cross-origin a un origen que no deberia poder leer la respuesta. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca codigo que calcule headers CORS desde `Origin` o que permita cualquier origen para recursos autenticados o sensibles.
