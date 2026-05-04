# XML External Entity Injection (XXE)

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad aparece cuando un parser XML procesa entidades externas definidas por el atacante.
Las entidades pueden apuntar a archivos locales, recursos internos o payloads expansivos. Si el parser las resuelve, el atacante obtiene lectura de archivos, SSRF o denegacion de servicio.

## Como identificar casos similares
- Parsers XML con DTD y entidades externas habilitadas.
- Procesamiento de XML subido por usuarios o recibido desde terceros sin configuracion segura.
- Uso de librerias antiguas o defaults inseguros.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `root=etree.fromstring(request.data, parser=etree.XMLParser(resolve_entities=True))`
En este ejemplo, lo vulnerable es permitir que el parser interprete definiciones externas provenientes del XML controlado por el atacante. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca parseo XML con DTD/entidades externas habilitadas o sin hardening del parser.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { parser.parse(req.body,{processEntities:true}); }`
En este ejemplo, lo vulnerable es permitir que el parser interprete definiciones externas provenientes del XML controlado por el atacante. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca parseo XML con DTD/entidades externas habilitadas o sin hardening del parser.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(request.getInputStream()); } }`
En este ejemplo, lo vulnerable es permitir que el parser interprete definiciones externas provenientes del XML controlado por el atacante. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca parseo XML con DTD/entidades externas habilitadas o sin hardening del parser.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { decoder:=xml.NewDecoder(r.Body) }`
En este ejemplo, lo vulnerable es permitir que el parser interprete definiciones externas provenientes del XML controlado por el atacante. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca parseo XML con DTD/entidades externas habilitadas o sin hardening del parser.

### PHP (`php.php`)
Fragmento representativo: `$dom=new DOMDocument(); $dom->loadXML(file_get_contents('php://input'), LIBXML_NOENT);`
En este ejemplo, lo vulnerable es permitir que el parser interprete definiciones externas provenientes del XML controlado por el atacante. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca parseo XML con DTD/entidades externas habilitadas o sin hardening del parser.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { $parser->parse_string($xml); }`
En este ejemplo, lo vulnerable es permitir que el parser interprete definiciones externas provenientes del XML controlado por el atacante. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca parseo XML con DTD/entidades externas habilitadas o sin hardening del parser.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin XMLDoc.LoadFromXML(Request.Content); end.`
En este ejemplo, lo vulnerable es permitir que el parser interprete definiciones externas provenientes del XML controlado por el atacante. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca parseo XML con DTD/entidades externas habilitadas o sin hardening del parser.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) doc = Nokogiri::XML(request.body.read) { |c| c.noent } end`
En este ejemplo, lo vulnerable es permitir que el parser interprete definiciones externas provenientes del XML controlado por el atacante. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca parseo XML con DTD/entidades externas habilitadas o sin hardening del parser.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { }`
En este ejemplo, lo vulnerable es permitir que el parser interprete definiciones externas provenientes del XML controlado por el atacante. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca parseo XML con DTD/entidades externas habilitadas o sin hardening del parser.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { var doc = new XmlDocument(); doc.LoadXml(body); } }`
En este ejemplo, lo vulnerable es permitir que el parser interprete definiciones externas provenientes del XML controlado por el atacante. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca parseo XML con DTD/entidades externas habilitadas o sin hardening del parser.
