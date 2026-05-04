# Open Redirect

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad aparece cuando el destino de una redireccion depende directamente de un valor controlado por el usuario.
El servidor se convierte en intermediario confiable hacia una URL arbitraria. Eso facilita phishing, evasiones de filtros y cadenas inseguras en flujos de login, OAuth o SSO.

## Como identificar casos similares
- Parametros como `next`, `returnUrl` o `redirect` usados directo en `Location`.
- Validaciones debiles basadas en prefijos o subcadenas.
- Aceptacion de URLs absolutas, esquemas no esperados o doble codificacion.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `return redirect(request.args['next'])`
En este ejemplo, lo vulnerable es delegar al usuario final la eleccion del destino de confianza al que el servidor redirecciona. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca endpoints de redireccion donde el destino salga de la request sin normalizacion ni allowlist estricta.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { res.redirect(req.query.next); }`
En este ejemplo, lo vulnerable es delegar al usuario final la eleccion del destino de confianza al que el servidor redirecciona. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca endpoints de redireccion donde el destino salga de la request sin normalizacion ni allowlist estricta.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { response.sendRedirect(request.getParameter("next")); } }`
En este ejemplo, lo vulnerable es delegar al usuario final la eleccion del destino de confianza al que el servidor redirecciona. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca endpoints de redireccion donde el destino salga de la request sin normalizacion ni allowlist estricta.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { http.Redirect(w,r,r.URL.Query().Get("next"),302) }`
En este ejemplo, lo vulnerable es delegar al usuario final la eleccion del destino de confianza al que el servidor redirecciona. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca endpoints de redireccion donde el destino salga de la request sin normalizacion ni allowlist estricta.

### PHP (`php.php`)
Fragmento representativo: `header('Location: '.$_GET['next']);`
En este ejemplo, lo vulnerable es delegar al usuario final la eleccion del destino de confianza al que el servidor redirecciona. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca endpoints de redireccion donde el destino salga de la request sin normalizacion ni allowlist estricta.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { redirect param('next'); }`
En este ejemplo, lo vulnerable es delegar al usuario final la eleccion del destino de confianza al que el servidor redirecciona. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca endpoints de redireccion donde el destino salga de la request sin normalizacion ni allowlist estricta.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Response.Code := 302; Response.Location := Request.QueryFields.Values['next']; end.`
En este ejemplo, lo vulnerable es delegar al usuario final la eleccion del destino de confianza al que el servidor redirecciona. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca endpoints de redireccion donde el destino salga de la request sin normalizacion ni allowlist estricta.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) redirect_to params[:next], allow_other_host: true end`
En este ejemplo, lo vulnerable es delegar al usuario final la eleccion del destino de confianza al que el servidor redirecciona. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca endpoints de redireccion donde el destino salga de la request sin normalizacion ni allowlist estricta.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { redirect(next); }`
En este ejemplo, lo vulnerable es delegar al usuario final la eleccion del destino de confianza al que el servidor redirecciona. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca endpoints de redireccion donde el destino salga de la request sin normalizacion ni allowlist estricta.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { return Redirect(Request.Query["next"]); } }`
En este ejemplo, lo vulnerable es delegar al usuario final la eleccion del destino de confianza al que el servidor redirecciona. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca endpoints de redireccion donde el destino salga de la request sin normalizacion ni allowlist estricta.
