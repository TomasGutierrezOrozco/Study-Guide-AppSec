# Server-Side Request Forgery (SSRF)

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad ocurre cuando el servidor realiza peticiones a URLs o destinos controlados por el usuario.
El backend tiene visibilidad de red distinta a la del atacante. Si este puede elegir el destino, usa al servidor como proxy hacia servicios internos, metadata cloud o puertos inaccesibles externamente.

## Como identificar casos similares
- Parametros `url`, `callback`, `image` o `webhook` enviados a clientes HTTP.
- Falta de allowlist de hosts o protocolos.
- Soporte para redirecciones, DNS rebinding o esquemas alternativos sin control.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `return requests.get(request.args['url']).text`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario determine a que host o servicio interno se conectara el servidor. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca llamadas HTTP cuyo destino salga de parametros, body, webhooks o URLs suministradas por el cliente.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { fetch(req.query.url).then(r=>r.text()).then(t=>res.send(t)); }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario determine a que host o servicio interno se conectara el servidor. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca llamadas HTTP cuyo destino salga de parametros, body, webhooks o URLs suministradas por el cliente.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { new URL(request.getParameter("url")).openConnection().getInputStream(); } }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario determine a que host o servicio interno se conectara el servidor. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca llamadas HTTP cuyo destino salga de parametros, body, webhooks o URLs suministradas por el cliente.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { http.Get(r.URL.Query().Get("url")) }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario determine a que host o servicio interno se conectara el servidor. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca llamadas HTTP cuyo destino salga de parametros, body, webhooks o URLs suministradas por el cliente.

### PHP (`php.php`)
Fragmento representativo: `echo file_get_contents($_GET['url']);`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario determine a que host o servicio interno se conectara el servidor. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca llamadas HTTP cuyo destino salga de parametros, body, webhooks o URLs suministradas por el cliente.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { print HTTP::Tiny->new->get(param('url'))->{content}; }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario determine a que host o servicio interno se conectara el servidor. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca llamadas HTTP cuyo destino salga de parametros, body, webhooks o URLs suministradas por el cliente.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Response.Content := TFPHTTPClient.SimpleGet(Request.QueryFields.Values['url']); end.`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario determine a que host o servicio interno se conectara el servidor. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca llamadas HTTP cuyo destino salga de parametros, body, webhooks o URLs suministradas por el cliente.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) render plain: Net::HTTP.get(URI(params[:url])) end`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario determine a que host o servicio interno se conectara el servidor. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca llamadas HTTP cuyo destino salga de parametros, body, webhooks o URLs suministradas por el cliente.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let body = reqwest::blocking::get(url)?.text()?; }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario determine a que host o servicio interno se conectara el servidor. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca llamadas HTTP cuyo destino salga de parametros, body, webhooks o URLs suministradas por el cliente.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { var body = new HttpClient().GetStringAsync(url).Result; } }`
En este ejemplo, lo vulnerable es permitir que la entrada del usuario determine a que host o servicio interno se conectara el servidor. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca llamadas HTTP cuyo destino salga de parametros, body, webhooks o URLs suministradas por el cliente.
