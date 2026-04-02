# SQUID Proxy Enumeration and Exploitation

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
El patron vulnerable aparece cuando una aplicacion o infraestructura expone un proxy SQUID utilizable desde contextos no autorizados.
Un proxy abierto o mal restringido permite navegar a traves de la red de la organizacion, enumerar destinos internos o disfrazar el origen real de las peticiones.

## Como identificar casos similares
- Configuraciones de proxy sin ACL estrictas.
- Aplicaciones que enrutan trafico del usuario a traves de SQUID sin validar destino.
- Servicios internos accesibles solo desde la red del proxy.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `requests.get(request.args['url'], proxies={'http':'http://open-squid:3128'})`
En este ejemplo, lo vulnerable es ofrecer una capacidad de proxy hacia destinos que el cliente no deberia poder alcanzar. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca uso de proxies internos como salto generico y ACLs de SQUID demasiado permisivas.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { const proxy='http://open-squid:3128'; }`
En este ejemplo, lo vulnerable es ofrecer una capacidad de proxy hacia destinos que el cliente no deberia poder alcanzar. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca uso de proxies internos como salto generico y ACLs de SQUID demasiado permisivas.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { Proxy proxy=new Proxy(Proxy.Type.HTTP,new InetSocketAddress("open-squid",3128)); } }`
En este ejemplo, lo vulnerable es ofrecer una capacidad de proxy hacia destinos que el cliente no deberia poder alcanzar. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca uso de proxies internos como salto generico y ACLs de SQUID demasiado permisivas.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { proxyURL,_:=url.Parse("http://open-squid:3128") _ = proxyURL }`
En este ejemplo, lo vulnerable es ofrecer una capacidad de proxy hacia destinos que el cliente no deberia poder alcanzar. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca uso de proxies internos como salto generico y ACLs de SQUID demasiado permisivas.

### PHP (`php.php`)
Fragmento representativo: `$proxy='http://open-squid:3128';`
En este ejemplo, lo vulnerable es ofrecer una capacidad de proxy hacia destinos que el cliente no deberia poder alcanzar. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca uso de proxies internos como salto generico y ACLs de SQUID demasiado permisivas.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { $ENV{http_proxy} = 'http://open-squid:3128'; }`
En este ejemplo, lo vulnerable es ofrecer una capacidad de proxy hacia destinos que el cliente no deberia poder alcanzar. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca uso de proxies internos como salto generico y ACLs de SQUID demasiado permisivas.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin ProxyHost := 'open-squid'; ProxyPort := 3128; end.`
En este ejemplo, lo vulnerable es ofrecer una capacidad de proxy hacia destinos que el cliente no deberia poder alcanzar. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca uso de proxies internos como salto generico y ACLs de SQUID demasiado permisivas.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) proxy = Net::HTTP::Proxy('open-squid', 3128) end`
En este ejemplo, lo vulnerable es ofrecer una capacidad de proxy hacia destinos que el cliente no deberia poder alcanzar. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca uso de proxies internos como salto generico y ACLs de SQUID demasiado permisivas.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let proxy = reqwest::Proxy::http("http://open-squid:3128")?; }`
En este ejemplo, lo vulnerable es ofrecer una capacidad de proxy hacia destinos que el cliente no deberia poder alcanzar. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca uso de proxies internos como salto generico y ACLs de SQUID demasiado permisivas.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { var handler = new HttpClientHandler { Proxy = new WebProxy("http://open-squid:3128") }; } }`
En este ejemplo, lo vulnerable es ofrecer una capacidad de proxy hacia destinos que el cliente no deberia poder alcanzar. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca uso de proxies internos como salto generico y ACLs de SQUID demasiado permisivas.
