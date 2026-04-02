# WebDAV Enumeration and Exploitation

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad existe cuando un endpoint WebDAV permite listar, subir, mover o sobrescribir recursos sin autorizacion suficiente.
Metodos como `PROPFIND`, `PUT`, `MOVE` o `DELETE` amplian mucho la superficie. Si estan habilitados sin controles estrictos, el atacante puede manipular contenido del servidor.

## Como identificar casos similares
- WebDAV activo en rutas publicas sin autenticacion fuerte.
- Aceptacion de `PUT` o `MOVE` sobre rutas arbitrarias.
- Respuestas a `PROPFIND` que revelan estructura o metadatos sensibles.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `if request.method=='PUT': open(request.path.lstrip('/'),'wb').write(request.data)`
En este ejemplo, lo vulnerable es exponer operaciones de gestion de archivos sobre el servidor sin controles estrictos por metodo, ruta y usuario. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca manejo de metodos WebDAV o escritura directa de recursos HTTP sin checks fuertes de autenticacion y ruta.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { if(req.method==='PUT')fs.writeFileSync(req.path,req.body); }`
En este ejemplo, lo vulnerable es exponer operaciones de gestion de archivos sobre el servidor sin controles estrictos por metodo, ruta y usuario. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca manejo de metodos WebDAV o escritura directa de recursos HTTP sin checks fuertes de autenticacion y ruta.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { if(request.getMethod().equals("PUT")) Files.write(Path.of(request.getRequestURI()), request.getInputStream().readAll...`
En este ejemplo, lo vulnerable es exponer operaciones de gestion de archivos sobre el servidor sin controles estrictos por metodo, ruta y usuario. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca manejo de metodos WebDAV o escritura directa de recursos HTTP sin checks fuertes de autenticacion y ruta.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { if r.Method=="PUT" { os.WriteFile(r.URL.Path[1:],body,0644) } }`
En este ejemplo, lo vulnerable es exponer operaciones de gestion de archivos sobre el servidor sin controles estrictos por metodo, ruta y usuario. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca manejo de metodos WebDAV o escritura directa de recursos HTTP sin checks fuertes de autenticacion y ruta.

### PHP (`php.php`)
Fragmento representativo: `if($_SERVER['REQUEST_METHOD']==='PUT'){file_put_contents($_SERVER['REQUEST_URI'],file_get_contents('php://input'));}`
En este ejemplo, lo vulnerable es exponer operaciones de gestion de archivos sobre el servidor sin controles estrictos por metodo, ruta y usuario. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca manejo de metodos WebDAV o escritura directa de recursos HTTP sin checks fuertes de autenticacion y ruta.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { if (request_method() eq 'PUT') { ... } }`
En este ejemplo, lo vulnerable es exponer operaciones de gestion de archivos sobre el servidor sin controles estrictos por metodo, ruta y usuario. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca manejo de metodos WebDAV o escritura directa de recursos HTTP sin checks fuertes de autenticacion y ruta.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin if Request.Method = 'PUT' then SaveFile(Request.PathInfo, Request.Content); end.`
En este ejemplo, lo vulnerable es exponer operaciones de gestion de archivos sobre el servidor sin controles estrictos por metodo, ruta y usuario. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca manejo de metodos WebDAV o escritura directa de recursos HTTP sin checks fuertes de autenticacion y ruta.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) File.binwrite(request.path, request.body.read) if request.put? end`
En este ejemplo, lo vulnerable es exponer operaciones de gestion de archivos sobre el servidor sin controles estrictos por metodo, ruta y usuario. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca manejo de metodos WebDAV o escritura directa de recursos HTTP sin checks fuertes de autenticacion y ruta.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { std::fs::write(path, body)?; }`
En este ejemplo, lo vulnerable es exponer operaciones de gestion de archivos sobre el servidor sin controles estrictos por metodo, ruta y usuario. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca manejo de metodos WebDAV o escritura directa de recursos HTTP sin checks fuertes de autenticacion y ruta.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { if (Request.Method == "PUT") File.WriteAllBytes(Request.Path, body); } }`
En este ejemplo, lo vulnerable es exponer operaciones de gestion de archivos sobre el servidor sin controles estrictos por metodo, ruta y usuario. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca manejo de metodos WebDAV o escritura directa de recursos HTTP sin checks fuertes de autenticacion y ruta.
