# AXFR Full Zone Transfer

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad existe cuando un servidor DNS permite transferencias de zona completas a clientes no autorizados.
AXFR expone el contenido entero de la zona: subdominios, hosts internos y otros metadatos utiles para reconocimiento. El problema aparece cuando la transferencia se permite a cualquiera o se automatiza sin validar al solicitante.

## Como identificar casos similares
- Configuraciones DNS con `allow-transfer` demasiado amplio.
- Herramientas o paneles que exponen datos de zona a cualquier usuario.
- Servicios que hacen AXFR sin validar origen o autenticacion entre maestro y secundario.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `subprocess.check_output(['dig','axfr',request.args['domain']])`
En este ejemplo, lo vulnerable es permitir o solicitar una transferencia de zona sin verificar si el origen esta autorizado para conocer toda la informacion DNS. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca codigo, scripts o configuraciones que permitan AXFR sin una allowlist estricta de servidores autorizados.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { exec(`dig axfr ${req.query.domain}`); }`
En este ejemplo, lo vulnerable es permitir o solicitar una transferencia de zona sin verificar si el origen esta autorizado para conocer toda la informacion DNS. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca codigo, scripts o configuraciones que permitan AXFR sin una allowlist estricta de servidores autorizados.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { new ProcessBuilder("dig","axfr",request.getParameter("domain")).start(); } }`
En este ejemplo, lo vulnerable es permitir o solicitar una transferencia de zona sin verificar si el origen esta autorizado para conocer toda la informacion DNS. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca codigo, scripts o configuraciones que permitan AXFR sin una allowlist estricta de servidores autorizados.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { exec.Command("dig","axfr",r.URL.Query().Get("domain")).Output() }`
En este ejemplo, lo vulnerable es permitir o solicitar una transferencia de zona sin verificar si el origen esta autorizado para conocer toda la informacion DNS. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca codigo, scripts o configuraciones que permitan AXFR sin una allowlist estricta de servidores autorizados.

### PHP (`php.php`)
Fragmento representativo: `shell_exec('dig axfr '.$_GET['domain']);`
En este ejemplo, lo vulnerable es permitir o solicitar una transferencia de zona sin verificar si el origen esta autorizado para conocer toda la informacion DNS. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca codigo, scripts o configuraciones que permitan AXFR sin una allowlist estricta de servidores autorizados.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { system('dig', 'axfr', param('domain')); }`
En este ejemplo, lo vulnerable es permitir o solicitar una transferencia de zona sin verificar si el origen esta autorizado para conocer toda la informacion DNS. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca codigo, scripts o configuraciones que permitan AXFR sin una allowlist estricta de servidores autorizados.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin RunCommand('dig', ['axfr', Request.QueryFields.Values['domain']], Output); end.`
En este ejemplo, lo vulnerable es permitir o solicitar una transferencia de zona sin verificar si el origen esta autorizado para conocer toda la informacion DNS. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca codigo, scripts o configuraciones que permitan AXFR sin una allowlist estricta de servidores autorizados.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) system("dig axfr #{params[:domain]}") end`
En este ejemplo, lo vulnerable es permitir o solicitar una transferencia de zona sin verificar si el origen esta autorizado para conocer toda la informacion DNS. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca codigo, scripts o configuraciones que permitan AXFR sin una allowlist estricta de servidores autorizados.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { Command::new("dig").arg("axfr").arg(domain).output()?; }`
En este ejemplo, lo vulnerable es permitir o solicitar una transferencia de zona sin verificar si el origen esta autorizado para conocer toda la informacion DNS. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca codigo, scripts o configuraciones que permitan AXFR sin una allowlist estricta de servidores autorizados.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { Process.Start("dig", "axfr " + Request.Query["domain"]); } }`
En este ejemplo, lo vulnerable es permitir o solicitar una transferencia de zona sin verificar si el origen esta autorizado para conocer toda la informacion DNS. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca codigo, scripts o configuraciones que permitan AXFR sin una allowlist estricta de servidores autorizados.
