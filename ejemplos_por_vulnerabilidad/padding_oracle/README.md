# Padding Oracle

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
El patron vulnerable existe cuando un sistema cifrado revela, directa o indirectamente, si el padding de un mensaje es valido.
Errores distintos, tiempos de respuesta o codigos HTTP diferentes actuan como un oraculo. Con suficientes consultas, el atacante puede descifrar bloques o modificar ciphertext sin conocer la clave.

## Como identificar casos similares
- Cifrado por bloques con padding y respuestas diferenciadas ante errores.
- Manejo separado de errores de padding, formato o MAC.
- Uso de esquemas legados tipo CBC sin autenticacion de mensaje.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `try: unpad(cipher.decrypt(token),16) except ValueError: return 'bad padding',403`
En este ejemplo, lo vulnerable es exponer una diferencia observable entre un mensaje con padding correcto y uno incorrecto. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca codigo criptografico que responda de forma distinta ante errores de padding, formato o autenticacion.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { try{decrypt(req.query.token);}catch{res.status(403).send('bad padding');} }`
En este ejemplo, lo vulnerable es exponer una diferencia observable entre un mensaje con padding correcto y uno incorrecto. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca codigo criptografico que responda de forma distinta ante errores de padding, formato o autenticacion.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { try{cipher.doFinal(token);}catch(BadPaddingException e){response.sendError(403,"bad padding");} } }`
En este ejemplo, lo vulnerable es exponer una diferencia observable entre un mensaje con padding correcto y uno incorrecto. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca codigo criptografico que responda de forma distinta ante errores de padding, formato o autenticacion.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { if _,err:=decrypt(token); err!=nil { http.Error(w,"bad padding",403) } }`
En este ejemplo, lo vulnerable es exponer una diferencia observable entre un mensaje con padding correcto y uno incorrecto. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca codigo criptografico que responda de forma distinta ante errores de padding, formato o autenticacion.

### PHP (`php.php`)
Fragmento representativo: `if(!openssl_decrypt($_GET['token'],'AES-128-CBC',$key,0,$iv)){echo 'bad padding';}`
En este ejemplo, lo vulnerable es exponer una diferencia observable entre un mensaje con padding correcto y uno incorrecto. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca codigo criptografico que responda de forma distinta ante errores de padding, formato o autenticacion.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { eval { decrypt($token) }; if ($@) { print 'bad padding' } }`
En este ejemplo, lo vulnerable es exponer una diferencia observable entre un mensaje con padding correcto y uno incorrecto. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca codigo criptografico que responda de forma distinta ante errores de padding, formato o autenticacion.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin try Decrypt(Token); except on E: Exception do Response.Code := 403; end; end.`
En este ejemplo, lo vulnerable es exponer una diferencia observable entre un mensaje con padding correcto y uno incorrecto. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca codigo criptografico que responda de forma distinta ante errores de padding, formato o autenticacion.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) rescue OpenSSL::Cipher::CipherError then render plain: 'bad padding', status: 403 end`
En este ejemplo, lo vulnerable es exponer una diferencia observable entre un mensaje con padding correcto y uno incorrecto. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca codigo criptografico que responda de forma distinta ante errores de padding, formato o autenticacion.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { if decrypt(token).is_err() { return Err(StatusCode::FORBIDDEN); } }`
En este ejemplo, lo vulnerable es exponer una diferencia observable entre un mensaje con padding correcto y uno incorrecto. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca codigo criptografico que responda de forma distinta ante errores de padding, formato o autenticacion.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { try { Decrypt(token); } catch { Response.StatusCode = 403; } } }`
En este ejemplo, lo vulnerable es exponer una diferencia observable entre un mensaje con padding correcto y uno incorrecto. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca codigo criptografico que responda de forma distinta ante errores de padding, formato o autenticacion.
