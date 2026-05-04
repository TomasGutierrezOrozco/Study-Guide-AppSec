# Cross-Site Request Forgery (CSRF)

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad ocurre cuando una accion autenticada puede ejecutarse con la sesion de la victima sin una prueba adicional de intencion.
El servidor acepta la cookie de sesion y procesa cambios de estado sin token anti-CSRF, sin validar `Origin`/`Referer` o usando metodos inseguros para operaciones criticas.

## Como identificar casos similares
- Endpoints que cambian estado sin token anti-CSRF.
- Dependencia exclusiva de la cookie de sesion.
- Formularios o APIs para navegador sin validacion de origen.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `if request.method=='POST': change_email(request.form['email'])`
En este ejemplo, lo vulnerable es que la accion se autoriza solo porque la cookie de sesion acompana la peticion, no porque exista una prueba de intencion del usuario legitimo. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca endpoints de mutacion que acepten la sesion automaticamente y no exijan token, validacion de origen ni otra prueba anti-CSRF.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { app.post('/email',(req,res)=>changeEmail(req.body.email)); }`
En este ejemplo, lo vulnerable es que la accion se autoriza solo porque la cookie de sesion acompana la peticion, no porque exista una prueba de intencion del usuario legitimo. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca endpoints de mutacion que acepten la sesion automaticamente y no exijan token, validacion de origen ni otra prueba anti-CSRF.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { if(request.getMethod().equals("POST")){changeEmail(request.getParameter("email"));} } }`
En este ejemplo, lo vulnerable es que la accion se autoriza solo porque la cookie de sesion acompana la peticion, no porque exista una prueba de intencion del usuario legitimo. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca endpoints de mutacion que acepten la sesion automaticamente y no exijan token, validacion de origen ni otra prueba anti-CSRF.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { if r.Method==http.MethodPost { changeEmail(r.FormValue("email")) } }`
En este ejemplo, lo vulnerable es que la accion se autoriza solo porque la cookie de sesion acompana la peticion, no porque exista una prueba de intencion del usuario legitimo. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca endpoints de mutacion que acepten la sesion automaticamente y no exijan token, validacion de origen ni otra prueba anti-CSRF.

### PHP (`php.php`)
Fragmento representativo: `if($_SERVER['REQUEST_METHOD']==='POST'){changeEmail($_POST['email']);}`
En este ejemplo, lo vulnerable es que la accion se autoriza solo porque la cookie de sesion acompana la peticion, no porque exista una prueba de intencion del usuario legitimo. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca endpoints de mutacion que acepten la sesion automaticamente y no exijan token, validacion de origen ni otra prueba anti-CSRF.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { change_email(param('email')) if request_method() eq 'POST'; }`
En este ejemplo, lo vulnerable es que la accion se autoriza solo porque la cookie de sesion acompana la peticion, no porque exista una prueba de intencion del usuario legitimo. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca endpoints de mutacion que acepten la sesion automaticamente y no exijan token, validacion de origen ni otra prueba anti-CSRF.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin if Request.Method = 'POST' then ChangeEmail(Request.ContentFields.Values['email']); end.`
En este ejemplo, lo vulnerable es que la accion se autoriza solo porque la cookie de sesion acompana la peticion, no porque exista una prueba de intencion del usuario legitimo. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca endpoints de mutacion que acepten la sesion automaticamente y no exijan token, validacion de origen ni otra prueba anti-CSRF.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) change_email(params[:email]) end`
En este ejemplo, lo vulnerable es que la accion se autoriza solo porque la cookie de sesion acompana la peticion, no porque exista una prueba de intencion del usuario legitimo. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca endpoints de mutacion que acepten la sesion automaticamente y no exijan token, validacion de origen ni otra prueba anti-CSRF.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { change_email(email); }`
En este ejemplo, lo vulnerable es que la accion se autoriza solo porque la cookie de sesion acompana la peticion, no porque exista una prueba de intencion del usuario legitimo. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca endpoints de mutacion que acepten la sesion automaticamente y no exijan token, validacion de origen ni otra prueba anti-CSRF.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { if (Request.Method == "POST") ChangeEmail(Request.Form["email"]); } }`
En este ejemplo, lo vulnerable es que la accion se autoriza solo porque la cookie de sesion acompana la peticion, no porque exista una prueba de intencion del usuario legitimo. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca endpoints de mutacion que acepten la sesion automaticamente y no exijan token, validacion de origen ni otra prueba anti-CSRF.
