# Race Condition

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad existe cuando el resultado depende del orden o simultaneidad de operaciones que no estan sincronizadas correctamente.
El patron clasico es `verificar y luego usar`. Si dos peticiones pasan la validacion al mismo tiempo, ambas pueden consumir un recurso, gastar saldo o escribir un estado que debia ser unico.

## Como identificar casos similares
- Patrones TOCTOU sobre saldo, stock, permisos o existencia.
- Lectura y escritura separadas sobre el mismo recurso sensible.
- Ausencia de locks, transacciones o operaciones atomicas.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `if not used(code): credit(user) mark_used(code)`
En este ejemplo, lo vulnerable es separar la verificacion del uso final de un recurso sin una garantia atomica entre ambos pasos. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca secuencias de leer-validar-escribir sobre el mismo recurso sin lock, transaccion o constraint atomico.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { if(!coupon.used){credit();coupon.used=true;} }`
En este ejemplo, lo vulnerable es separar la verificacion del uso final de un recurso sin una garantia atomica entre ambos pasos. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca secuencias de leer-validar-escribir sobre el mismo recurso sin lock, transaccion o constraint atomico.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { if(!coupon.isUsed()){credit();coupon.setUsed(true);} } }`
En este ejemplo, lo vulnerable es separar la verificacion del uso final de un recurso sin una garantia atomica entre ambos pasos. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca secuencias de leer-validar-escribir sobre el mismo recurso sin lock, transaccion o constraint atomico.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { if !coupon.Used { credit(user); coupon.Used=true } }`
En este ejemplo, lo vulnerable es separar la verificacion del uso final de un recurso sin una garantia atomica entre ambos pasos. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca secuencias de leer-validar-escribir sobre el mismo recurso sin lock, transaccion o constraint atomico.

### PHP (`php.php`)
Fragmento representativo: `if(!used($code)){credit($user);markUsed($code);}`
En este ejemplo, lo vulnerable es separar la verificacion del uso final de un recurso sin una garantia atomica entre ambos pasos. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca secuencias de leer-validar-escribir sobre el mismo recurso sin lock, transaccion o constraint atomico.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { unless ($coupon->{used}) { credit($user); $coupon->{used}=1; } }`
En este ejemplo, lo vulnerable es separar la verificacion del uso final de un recurso sin una garantia atomica entre ambos pasos. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca secuencias de leer-validar-escribir sobre el mismo recurso sin lock, transaccion o constraint atomico.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin if not Coupon.Used then begin Credit(User); Coupon.Used := True; end; end.`
En este ejemplo, lo vulnerable es separar la verificacion del uso final de un recurso sin una garantia atomica entre ambos pasos. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca secuencias de leer-validar-escribir sobre el mismo recurso sin lock, transaccion o constraint atomico.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) unless coupon.used credit(user) coupon.update!(used: true) end end`
En este ejemplo, lo vulnerable es separar la verificacion del uso final de un recurso sin una garantia atomica entre ambos pasos. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca secuencias de leer-validar-escribir sobre el mismo recurso sin lock, transaccion o constraint atomico.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { if !coupon.used { credit(user); coupon.used = true; } }`
En este ejemplo, lo vulnerable es separar la verificacion del uso final de un recurso sin una garantia atomica entre ambos pasos. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca secuencias de leer-validar-escribir sobre el mismo recurso sin lock, transaccion o constraint atomico.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { if (!coupon.Used) { Credit(user); coupon.Used = true; } } }`
En este ejemplo, lo vulnerable es separar la verificacion del uso final de un recurso sin una garantia atomica entre ambos pasos. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca secuencias de leer-validar-escribir sobre el mismo recurso sin lock, transaccion o constraint atomico.
