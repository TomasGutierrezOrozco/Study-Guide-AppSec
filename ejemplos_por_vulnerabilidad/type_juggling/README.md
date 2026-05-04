# Type Juggling

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad aparece cuando comparaciones o validaciones dependen de conversiones implicitas entre tipos.
Si el lenguaje convierte automaticamente strings, numeros, booleanos o nulos, un atacante puede enviar valores que parezcan distintos para la aplicacion pero equivalentes para la operacion critica.

## Como identificar casos similares
- Uso de comparaciones debiles o no estrictas.
- Conversion automatica de tipos en autenticacion, hashes o permisos.
- Datos del usuario comparados sin normalizacion previa.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `if request.json['value']==0: pass`
En este ejemplo, lo vulnerable es basar una decision de seguridad en una comparacion donde el lenguaje puede cambiar el tipo o valor efectivo. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca comparaciones debiles, coerciones implicitas y mezclas de strings/numeros/booleanos en decisiones sensibles.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { if(req.body.value==0){res.send('ok');} }`
En este ejemplo, lo vulnerable es basar una decision de seguridad en una comparacion donde el lenguaje puede cambiar el tipo o valor efectivo. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca comparaciones debiles, coerciones implicitas y mezclas de strings/numeros/booleanos en decisiones sensibles.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { } }`
En este ejemplo, lo vulnerable es basar una decision de seguridad en una comparacion donde el lenguaje puede cambiar el tipo o valor efectivo. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca comparaciones debiles, coerciones implicitas y mezclas de strings/numeros/booleanos en decisiones sensibles.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { }`
En este ejemplo, lo vulnerable es basar una decision de seguridad en una comparacion donde el lenguaje puede cambiar el tipo o valor efectivo. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca comparaciones debiles, coerciones implicitas y mezclas de strings/numeros/booleanos en decisiones sensibles.

### PHP (`php.php`)
Fragmento representativo: `if(md5($_POST['password'])=='0e123456'){echo 'ok';}`
En este ejemplo, lo vulnerable es basar una decision de seguridad en una comparacion donde el lenguaje puede cambiar el tipo o valor efectivo. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca comparaciones debiles, coerciones implicitas y mezclas de strings/numeros/booleanos en decisiones sensibles.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { if (param('value') == 0) { } }`
En este ejemplo, lo vulnerable es basar una decision de seguridad en una comparacion donde el lenguaje puede cambiar el tipo o valor efectivo. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca comparaciones debiles, coerciones implicitas y mezclas de strings/numeros/booleanos en decisiones sensibles.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin if StrToIntDef(Request.ContentFields.Values['value'], 0) = 0 then ; end.`
En este ejemplo, lo vulnerable es basar una decision de seguridad en una comparacion donde el lenguaje puede cambiar el tipo o valor efectivo. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca comparaciones debiles, coerciones implicitas y mezclas de strings/numeros/booleanos en decisiones sensibles.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) if params[:value].to_i == 0 end end`
En este ejemplo, lo vulnerable es basar una decision de seguridad en una comparacion donde el lenguaje puede cambiar el tipo o valor efectivo. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca comparaciones debiles, coerciones implicitas y mezclas de strings/numeros/booleanos en decisiones sensibles.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { }`
En este ejemplo, lo vulnerable es basar una decision de seguridad en una comparacion donde el lenguaje puede cambiar el tipo o valor efectivo. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca comparaciones debiles, coerciones implicitas y mezclas de strings/numeros/booleanos en decisiones sensibles.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { if ((Request.Form["value"] ?? "0") == "0") { } } }`
En este ejemplo, lo vulnerable es basar una decision de seguridad en una comparacion donde el lenguaje puede cambiar el tipo o valor efectivo. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca comparaciones debiles, coerciones implicitas y mezclas de strings/numeros/booleanos en decisiones sensibles.
