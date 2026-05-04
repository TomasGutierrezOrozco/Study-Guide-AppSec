# Remote File Inclusion (RFI)

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad aparece cuando la aplicacion permite incluir o cargar codigo o plantillas desde una URL controlada por el usuario.
A diferencia de LFI, aqui el recurso puede venir de otro servidor. Eso entrega al atacante control sobre el contenido que la aplicacion descargara e interpretara.

## Como identificar casos similares
- Parametros URL usados en `include`, `require` o carga de plantillas.
- Funciones que descargan contenido remoto y luego lo evaluan o ejecutan.
- Falta de allowlist de hosts, rutas o firmas del recurso.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `return requests.get(request.args['url']).text`
En este ejemplo, lo vulnerable es tratar contenido remoto controlado por el atacante como si fuera parte confiable de la aplicacion. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca includes remotos, carga de templates desde URL y cualquier download seguido de evaluacion o interpretacion activa.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { fetch(req.query.url).then(r=>r.text()).then(t=>res.send(t)); }`
En este ejemplo, lo vulnerable es tratar contenido remoto controlado por el atacante como si fuera parte confiable de la aplicacion. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca includes remotos, carga de templates desde URL y cualquier download seguido de evaluacion o interpretacion activa.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { new URL(request.getParameter("url")).openStream(); } }`
En este ejemplo, lo vulnerable es tratar contenido remoto controlado por el atacante como si fuera parte confiable de la aplicacion. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca includes remotos, carga de templates desde URL y cualquier download seguido de evaluacion o interpretacion activa.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { http.Get(r.URL.Query().Get("url")) }`
En este ejemplo, lo vulnerable es tratar contenido remoto controlado por el atacante como si fuera parte confiable de la aplicacion. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca includes remotos, carga de templates desde URL y cualquier download seguido de evaluacion o interpretacion activa.

### PHP (`php.php`)
Fragmento representativo: `include($_GET['url']);`
En este ejemplo, lo vulnerable es tratar contenido remoto controlado por el atacante como si fuera parte confiable de la aplicacion. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca includes remotos, carga de templates desde URL y cualquier download seguido de evaluacion o interpretacion activa.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { print HTTP::Tiny->new->get($url)->{content}; }`
En este ejemplo, lo vulnerable es tratar contenido remoto controlado por el atacante como si fuera parte confiable de la aplicacion. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca includes remotos, carga de templates desde URL y cualquier download seguido de evaluacion o interpretacion activa.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Response.Content := TFPHTTPClient.SimpleGet(Request.QueryFields.Values['url']); end.`
En este ejemplo, lo vulnerable es tratar contenido remoto controlado por el atacante como si fuera parte confiable de la aplicacion. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca includes remotos, carga de templates desde URL y cualquier download seguido de evaluacion o interpretacion activa.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) render plain: URI.open(params[:url]).read end`
En este ejemplo, lo vulnerable es tratar contenido remoto controlado por el atacante como si fuera parte confiable de la aplicacion. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca includes remotos, carga de templates desde URL y cualquier download seguido de evaluacion o interpretacion activa.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let body = reqwest::blocking::get(url)?.text()?; }`
En este ejemplo, lo vulnerable es tratar contenido remoto controlado por el atacante como si fuera parte confiable de la aplicacion. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca includes remotos, carga de templates desde URL y cualquier download seguido de evaluacion o interpretacion activa.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { var body = new HttpClient().GetStringAsync(url).Result; } }`
En este ejemplo, lo vulnerable es tratar contenido remoto controlado por el atacante como si fuera parte confiable de la aplicacion. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca includes remotos, carga de templates desde URL y cualquier download seguido de evaluacion o interpretacion activa.
