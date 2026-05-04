# Insecure Deserialization

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
El patron vulnerable consiste en reconstruir objetos desde datos manipulables por el atacante sin restringir tipos, estructura o comportamiento.
No solo se leen datos: se revive un objeto con semantica de aplicacion. Eso puede activar metodos magicos, hooks, gadgets del ecosistema o estados internos que el atacante no deberia controlar.

## Como identificar casos similares
- Deserializacion aplicada a cookies, body, archivos, colas o caches.
- Formatos binarios o ricos en tipos usados entre cliente y servidor.
- Rehidratacion automatica de objetos sin esquema ni allowlist de clases.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `pickle.loads(request.data)`
En este ejemplo, lo vulnerable es devolver a la vida un objeto cuya forma y contenido vienen del atacante. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca funciones de deserializacion o reconstruccion de objetos sobre datos externos sin esquema ni allowlist.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { const obj=unserialize(req.body.data); }`
En este ejemplo, lo vulnerable es devolver a la vida un objeto cuya forma y contenido vienen del atacante. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca funciones de deserializacion o reconstruccion de objetos sobre datos externos sin esquema ni allowlist.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { new ObjectInputStream(request.getInputStream()).readObject(); } }`
En este ejemplo, lo vulnerable es devolver a la vida un objeto cuya forma y contenido vienen del atacante. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca funciones de deserializacion o reconstruccion de objetos sobre datos externos sin esquema ni allowlist.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { gob.NewDecoder(r.Body).Decode(&obj) }`
En este ejemplo, lo vulnerable es devolver a la vida un objeto cuya forma y contenido vienen del atacante. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca funciones de deserializacion o reconstruccion de objetos sobre datos externos sin esquema ni allowlist.

### PHP (`php.php`)
Fragmento representativo: `unserialize($_POST['data']);`
En este ejemplo, lo vulnerable es devolver a la vida un objeto cuya forma y contenido vienen del atacante. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca funciones de deserializacion o reconstruccion de objetos sobre datos externos sin esquema ni allowlist.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { thaw($body); }`
En este ejemplo, lo vulnerable es devolver a la vida un objeto cuya forma y contenido vienen del atacante. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca funciones de deserializacion o reconstruccion de objetos sobre datos externos sin esquema ni allowlist.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin ReadComponent(Stream); end.`
En este ejemplo, lo vulnerable es devolver a la vida un objeto cuya forma y contenido vienen del atacante. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca funciones de deserializacion o reconstruccion de objetos sobre datos externos sin esquema ni allowlist.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) Marshal.load(request.body.read) end`
En este ejemplo, lo vulnerable es devolver a la vida un objeto cuya forma y contenido vienen del atacante. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca funciones de deserializacion o reconstruccion de objetos sobre datos externos sin esquema ni allowlist.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let obj: T = bincode::deserialize(bytes)?; }`
En este ejemplo, lo vulnerable es devolver a la vida un objeto cuya forma y contenido vienen del atacante. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca funciones de deserializacion o reconstruccion de objetos sobre datos externos sin esquema ni allowlist.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { new BinaryFormatter().Deserialize(stream); } }`
En este ejemplo, lo vulnerable es devolver a la vida un objeto cuya forma y contenido vienen del atacante. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca funciones de deserializacion o reconstruccion de objetos sobre datos externos sin esquema ni allowlist.
