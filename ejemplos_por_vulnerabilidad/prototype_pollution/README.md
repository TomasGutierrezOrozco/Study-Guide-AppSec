# Prototype Pollution

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad aparece cuando un merge o asignacion de objetos permite escribir propiedades especiales del prototipo compartido.
Si el atacante controla claves como `__proto__` o `constructor.prototype`, puede alterar el comportamiento de objetos presentes o futuros y romper supuestos globales del programa.

## Como identificar casos similares
- Merge profundo de objetos del usuario sin filtrar claves peligrosas.
- Helpers que copian propiedades arbitrarias a objetos globales o de configuracion.
- Parsers que convierten query params o JSON en estructuras anidadas sin saneamiento.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `config.update(request.get_json())`
En este ejemplo, lo vulnerable es copiar sin restricciones claves especiales que modifican la cadena de prototipos o estructuras compartidas. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca merges, `assign`, setters profundos o parsers de objetos que acepten claves anidadas del usuario.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { Object.assign(config,req.body); }`
En este ejemplo, lo vulnerable es copiar sin restricciones claves especiales que modifican la cadena de prototipos o estructuras compartidas. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca merges, `assign`, setters profundos o parsers de objetos que acepten claves anidadas del usuario.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { } }`
En este ejemplo, lo vulnerable es copiar sin restricciones claves especiales que modifican la cadena de prototipos o estructuras compartidas. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca merges, `assign`, setters profundos o parsers de objetos que acepten claves anidadas del usuario.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { }`
En este ejemplo, lo vulnerable es copiar sin restricciones claves especiales que modifican la cadena de prototipos o estructuras compartidas. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca merges, `assign`, setters profundos o parsers de objetos que acepten claves anidadas del usuario.

### PHP (`php.php`)
Fragmento representativo: `$config=array_merge($config,json_decode(file_get_contents('php://input'),true));`
En este ejemplo, lo vulnerable es copiar sin restricciones claves especiales que modifican la cadena de prototipos o estructuras compartidas. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca merges, `assign`, setters profundos o parsers de objetos que acepten claves anidadas del usuario.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { }`
En este ejemplo, lo vulnerable es copiar sin restricciones claves especiales que modifican la cadena de prototipos o estructuras compartidas. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca merges, `assign`, setters profundos o parsers de objetos que acepten claves anidadas del usuario.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin end.`
En este ejemplo, lo vulnerable es copiar sin restricciones claves especiales que modifican la cadena de prototipos o estructuras compartidas. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca merges, `assign`, setters profundos o parsers de objetos que acepten claves anidadas del usuario.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) end`
En este ejemplo, lo vulnerable es copiar sin restricciones claves especiales que modifican la cadena de prototipos o estructuras compartidas. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca merges, `assign`, setters profundos o parsers de objetos que acepten claves anidadas del usuario.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { }`
En este ejemplo, lo vulnerable es copiar sin restricciones claves especiales que modifican la cadena de prototipos o estructuras compartidas. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca merges, `assign`, setters profundos o parsers de objetos que acepten claves anidadas del usuario.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { } }`
En este ejemplo, lo vulnerable es copiar sin restricciones claves especiales que modifican la cadena de prototipos o estructuras compartidas. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca merges, `assign`, setters profundos o parsers de objetos que acepten claves anidadas del usuario.
