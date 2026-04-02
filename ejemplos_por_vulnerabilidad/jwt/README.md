# JWT Enumeration and Exploitation

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
El patron vulnerable aparece cuando la aplicacion confia en JWTs mal validados o usa claims inseguros para decisiones criticas.
Decodificar un token no es validarlo. Si no se comprueba firma, algoritmo esperado, expiracion, emisor o audiencia, el atacante puede forjar identidad o privilegios.

## Como identificar casos similares
- Codigo que hace `decode` sin `verify`.
- Algoritmo tomado del propio token o secretos debiles.
- Uso de claims como `role` o `sub` sin validacion completa.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `payload=json.loads(base64.urlsafe_b64decode(token.split('.')[1]+'=='))`
En este ejemplo, lo vulnerable es tratar el contenido del JWT como confiable antes de validar su autenticidad e integridad. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca codigo que lea claims directamente, acepte algoritmos inseguros o no verifique firma, expiracion y contexto.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { const payload=JSON.parse(Buffer.from(token.split('.')[1],'base64url').toString()); }`
En este ejemplo, lo vulnerable es tratar el contenido del JWT como confiable antes de validar su autenticidad e integridad. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca codigo que lea claims directamente, acepte algoritmos inseguros o no verifique firma, expiracion y contexto.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { String payload=new String(Base64.getUrlDecoder().decode(token.split("\\.")[1])); } }`
En este ejemplo, lo vulnerable es tratar el contenido del JWT como confiable antes de validar su autenticidad e integridad. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca codigo que lea claims directamente, acepte algoritmos inseguros o no verifique firma, expiracion y contexto.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { payload,_:=base64.RawURLEncoding.DecodeString(strings.Split(token,".")[1]) _ = payload }`
En este ejemplo, lo vulnerable es tratar el contenido del JWT como confiable antes de validar su autenticidad e integridad. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca codigo que lea claims directamente, acepte algoritmos inseguros o no verifique firma, expiracion y contexto.

### PHP (`php.php`)
Fragmento representativo: `$payload=json_decode(base64_decode(explode('.',$_SERVER['HTTP_AUTHORIZATION'])[1]),true);`
En este ejemplo, lo vulnerable es tratar el contenido del JWT como confiable antes de validar su autenticidad e integridad. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca codigo que lea claims directamente, acepte algoritmos inseguros o no verifique firma, expiracion y contexto.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { $payload = decode_base64((split /\./, $token)[1]); }`
En este ejemplo, lo vulnerable es tratar el contenido del JWT como confiable antes de validar su autenticidad e integridad. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca codigo que lea claims directamente, acepte algoritmos inseguros o no verifique firma, expiracion y contexto.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Payload := DecodeBase64(SplitString(Token, '.')[1]); end.`
En este ejemplo, lo vulnerable es tratar el contenido del JWT como confiable antes de validar su autenticidad e integridad. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca codigo que lea claims directamente, acepte algoritmos inseguros o no verifique firma, expiracion y contexto.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) payload = JSON.parse(Base64.urlsafe_decode64(token.split('.')[1])) end`
En este ejemplo, lo vulnerable es tratar el contenido del JWT como confiable antes de validar su autenticidad e integridad. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca codigo que lea claims directamente, acepte algoritmos inseguros o no verifique firma, expiracion y contexto.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let payload = base64::decode_config(parts[1], base64::URL_SAFE_NO_PAD)?; }`
En este ejemplo, lo vulnerable es tratar el contenido del JWT como confiable antes de validar su autenticidad e integridad. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca codigo que lea claims directamente, acepte algoritmos inseguros o no verifique firma, expiracion y contexto.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { var payload = Base64UrlEncoder.Decode(token.Split('.')[1]); } }`
En este ejemplo, lo vulnerable es tratar el contenido del JWT como confiable antes de validar su autenticidad e integridad. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca codigo que lea claims directamente, acepte algoritmos inseguros o no verifique firma, expiracion y contexto.
