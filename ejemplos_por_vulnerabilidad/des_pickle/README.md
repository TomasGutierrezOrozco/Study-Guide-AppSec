# Pickle Deserialization - Python

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
Aunque `pickle` es especifico de Python, el patron es deserializacion insegura de datos no confiables en cualquier lenguaje.
El problema aparece cuando la aplicacion reconstruye objetos desde un blob controlado por el atacante. En formatos con comportamiento o tipos ricos, eso puede activar logica peligrosa durante la carga.

## Como identificar casos similares
- Uso de `pickle.loads`, `unserialize`, `ObjectInputStream` o equivalentes sobre datos externos.
- Payloads serializados recibidos por cookies, headers, archivos o colas.
- Confianza en blobs serializados solo porque tienen el formato esperado.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `pickle.loads(request.data)`
En este ejemplo, lo vulnerable es rehidratar un objeto desde datos controlados por el atacante, permitiendole influir en el tipo, estado o comportamiento reconstruido. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca funciones de deserializacion aplicadas a datos del usuario o a blobs cuya integridad no este garantizada.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { }`
En este ejemplo, lo vulnerable es rehidratar un objeto desde datos controlados por el atacante, permitiendole influir en el tipo, estado o comportamiento reconstruido. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca funciones de deserializacion aplicadas a datos del usuario o a blobs cuya integridad no este garantizada.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { } }`
En este ejemplo, lo vulnerable es rehidratar un objeto desde datos controlados por el atacante, permitiendole influir en el tipo, estado o comportamiento reconstruido. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca funciones de deserializacion aplicadas a datos del usuario o a blobs cuya integridad no este garantizada.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { }`
En este ejemplo, lo vulnerable es rehidratar un objeto desde datos controlados por el atacante, permitiendole influir en el tipo, estado o comportamiento reconstruido. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca funciones de deserializacion aplicadas a datos del usuario o a blobs cuya integridad no este garantizada.

### PHP (`php.php`)
Fragmento representativo: `Pickle Deserialization - Python equivalente en PHP: unserialize sobre input controlado.`
En este ejemplo, lo vulnerable es rehidratar un objeto desde datos controlados por el atacante, permitiendole influir en el tipo, estado o comportamiento reconstruido. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca funciones de deserializacion aplicadas a datos del usuario o a blobs cuya integridad no este garantizada.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { }`
En este ejemplo, lo vulnerable es rehidratar un objeto desde datos controlados por el atacante, permitiendole influir en el tipo, estado o comportamiento reconstruido. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca funciones de deserializacion aplicadas a datos del usuario o a blobs cuya integridad no este garantizada.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin end.`
En este ejemplo, lo vulnerable es rehidratar un objeto desde datos controlados por el atacante, permitiendole influir en el tipo, estado o comportamiento reconstruido. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca funciones de deserializacion aplicadas a datos del usuario o a blobs cuya integridad no este garantizada.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) end`
En este ejemplo, lo vulnerable es rehidratar un objeto desde datos controlados por el atacante, permitiendole influir en el tipo, estado o comportamiento reconstruido. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca funciones de deserializacion aplicadas a datos del usuario o a blobs cuya integridad no este garantizada.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { }`
En este ejemplo, lo vulnerable es rehidratar un objeto desde datos controlados por el atacante, permitiendole influir en el tipo, estado o comportamiento reconstruido. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca funciones de deserializacion aplicadas a datos del usuario o a blobs cuya integridad no este garantizada.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { } }`
En este ejemplo, lo vulnerable es rehidratar un objeto desde datos controlados por el atacante, permitiendole influir en el tipo, estado o comportamiento reconstruido. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca funciones de deserializacion aplicadas a datos del usuario o a blobs cuya integridad no este garantizada.
