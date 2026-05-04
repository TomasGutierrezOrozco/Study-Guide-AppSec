# LDAP Injection

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
El patron vulnerable aparece al construir filtros o DN de LDAP con concatenacion de entrada no confiable.
Caracteres especiales de LDAP alteran la logica del filtro. El atacante puede ampliar resultados, saltar autenticaciones o consultar atributos que no deberia poder pedir.

## Como identificar casos similares
- Concatenacion manual en filtros LDAP.
- Escape ausente o incompleto de caracteres especiales.
- Parametros HTTP o campos de login usados directamente en consultas LDAP.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `ldap_filter=f'(uid={request.args["user"]})'`
En este ejemplo, lo vulnerable es dejar que el atacante modifique la estructura del filtro LDAP y no solo su valor. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca construccion manual de filtros/DN con datos externos y ausencia de escape o parametrizacion LDAP.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { const filter=`(uid=${req.query.user})`; }`
En este ejemplo, lo vulnerable es dejar que el atacante modifique la estructura del filtro LDAP y no solo su valor. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca construccion manual de filtros/DN con datos externos y ausencia de escape o parametrizacion LDAP.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { String filter="(uid="+request.getParameter("user")+")"; } }`
En este ejemplo, lo vulnerable es dejar que el atacante modifique la estructura del filtro LDAP y no solo su valor. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca construccion manual de filtros/DN con datos externos y ausencia de escape o parametrizacion LDAP.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { filter:="(uid="+r.URL.Query().Get("user")+")" }`
En este ejemplo, lo vulnerable es dejar que el atacante modifique la estructura del filtro LDAP y no solo su valor. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca construccion manual de filtros/DN con datos externos y ausencia de escape o parametrizacion LDAP.

### PHP (`php.php`)
Fragmento representativo: `$filter='(uid='.$_GET['user'].')';`
En este ejemplo, lo vulnerable es dejar que el atacante modifique la estructura del filtro LDAP y no solo su valor. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca construccion manual de filtros/DN con datos externos y ausencia de escape o parametrizacion LDAP.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { $filter = '(uid=' . param('user') . ')'; }`
En este ejemplo, lo vulnerable es dejar que el atacante modifique la estructura del filtro LDAP y no solo su valor. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca construccion manual de filtros/DN con datos externos y ausencia de escape o parametrizacion LDAP.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin Filter := '(uid=' + Request.QueryFields.Values['user'] + ')'; end.`
En este ejemplo, lo vulnerable es dejar que el atacante modifique la estructura del filtro LDAP y no solo su valor. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca construccion manual de filtros/DN con datos externos y ausencia de escape o parametrizacion LDAP.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) filter = "(uid=#{params[:user]})" end`
En este ejemplo, lo vulnerable es dejar que el atacante modifique la estructura del filtro LDAP y no solo su valor. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca construccion manual de filtros/DN con datos externos y ausencia de escape o parametrizacion LDAP.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { let filter = format!("(uid={})", user); }`
En este ejemplo, lo vulnerable es dejar que el atacante modifique la estructura del filtro LDAP y no solo su valor. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca construccion manual de filtros/DN con datos externos y ausencia de escape o parametrizacion LDAP.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { var filter = "(uid=" + Request.Query["user"] + ")"; } }`
En este ejemplo, lo vulnerable es dejar que el atacante modifique la estructura del filtro LDAP y no solo su valor. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca construccion manual de filtros/DN con datos externos y ausencia de escape o parametrizacion LDAP.
