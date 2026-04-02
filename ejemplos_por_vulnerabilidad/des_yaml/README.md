# YAML Deserialization - Python

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
Aunque el titulo menciona Python, el patron es parsear YAML no confiable con loaders que permiten construir objetos o tipos complejos.
YAML soporta tags, referencias y estructuras avanzadas. Si el parser acepta esas capacidades sobre datos del usuario, un atacante puede forzar comportamientos inesperados o peligrosos durante la carga.

## Como identificar casos similares
- Uso de `yaml.load` sin loader seguro, o equivalentes permisivos.
- Procesamiento de YAML subido por usuarios o recibido desde red sin validacion.
- Aceptacion de tags o tipos personalizados no controlados.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `yaml.load(request.data, Loader=yaml.Loader)`
En este ejemplo, lo vulnerable es parsear YAML con capacidades de construccion rica sobre una fuente no confiable. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca loaders YAML inseguros, deserializacion de objetos y aceptacion de tags o tipos definidos por el usuario.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { yaml.load(req.body); }`
En este ejemplo, lo vulnerable es parsear YAML con capacidades de construccion rica sobre una fuente no confiable. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca loaders YAML inseguros, deserializacion de objetos y aceptacion de tags o tipos definidos por el usuario.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { new org.yaml.snakeyaml.Yaml().load(body); } }`
En este ejemplo, lo vulnerable es parsear YAML con capacidades de construccion rica sobre una fuente no confiable. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca loaders YAML inseguros, deserializacion de objetos y aceptacion de tags o tipos definidos por el usuario.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { yaml.Unmarshal(body,&obj) }`
En este ejemplo, lo vulnerable es parsear YAML con capacidades de construccion rica sobre una fuente no confiable. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca loaders YAML inseguros, deserializacion de objetos y aceptacion de tags o tipos definidos por el usuario.

### PHP (`php.php`)
Fragmento representativo: `YAML Deserialization - Python equivalente en PHP: parser inseguro de YAML o unserialize sobre input externo.`
En este ejemplo, lo vulnerable es parsear YAML con capacidades de construccion rica sobre una fuente no confiable. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca loaders YAML inseguros, deserializacion de objetos y aceptacion de tags o tipos definidos por el usuario.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { YAML::Load($body); }`
En este ejemplo, lo vulnerable es parsear YAML con capacidades de construccion rica sobre una fuente no confiable. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca loaders YAML inseguros, deserializacion de objetos y aceptacion de tags o tipos definidos por el usuario.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin YamlText := Request.Content; end.`
En este ejemplo, lo vulnerable es parsear YAML con capacidades de construccion rica sobre una fuente no confiable. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca loaders YAML inseguros, deserializacion de objetos y aceptacion de tags o tipos definidos por el usuario.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) YAML.load(request.body.read) end`
En este ejemplo, lo vulnerable es parsear YAML con capacidades de construccion rica sobre una fuente no confiable. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca loaders YAML inseguros, deserializacion de objetos y aceptacion de tags o tipos definidos por el usuario.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let value: serde_yaml::Value = serde_yaml::from_str(body)?; }`
En este ejemplo, lo vulnerable es parsear YAML con capacidades de construccion rica sobre una fuente no confiable. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca loaders YAML inseguros, deserializacion de objetos y aceptacion de tags o tipos definidos por el usuario.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { var obj = new Deserializer().Deserialize<object>(body); } }`
En este ejemplo, lo vulnerable es parsear YAML con capacidades de construccion rica sobre una fuente no confiable. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca loaders YAML inseguros, deserializacion de objetos y aceptacion de tags o tipos definidos por el usuario.
