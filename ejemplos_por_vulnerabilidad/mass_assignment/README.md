# Mass Assignment

Este README aplica a todos los ejemplos de la carpeta. Aunque la sintaxis cambia entre lenguajes, la causa raiz de la vulnerabilidad es la misma.

## Que hace vulnerable al patron
La vulnerabilidad aparece cuando el backend asigna automaticamente todos los campos enviados por el cliente a un objeto de dominio.
El binding automatico copia tambien atributos internos como `role`, `is_admin`, `owner_id` o `balance`. El atacante no necesita un endpoint especial: solo enviar campos extra.

## Como identificar casos similares
- Uso de `update(req.body)`, `Model.new(params)` o binders sobre modelos persistentes.
- Ausencia de DTOs o allowlists de campos.
- Campos sensibles presentes en el modelo pero no en el formulario oficial.

## Explicacion por lenguaje
### Python (`python.py`)
Fragmento representativo: `user.__dict__.update(request.get_json())`
En este ejemplo, lo vulnerable es dejar que el cliente decida que atributos internos del objeto se van a poblar. En Python, usar f-strings, concatenacion, carga directa de objetos o parseo sin restricciones no agrega ninguna proteccion automatica.
Para encontrar casos parecidos en Python, busca mapeo automatico de request a modelos persistentes sin lista permitida de campos.

### JavaScript (`javascript.js`)
Fragmento representativo: `function demo(req, res) { Object.assign(user,req.body); }`
En este ejemplo, lo vulnerable es dejar que el cliente decida que atributos internos del objeto se van a poblar. En JavaScript, los valores que vienen de `req`, `body`, `query` o merges de objetos llegan intactos al sink si no se validan antes.
Para encontrar casos parecidos en JavaScript, busca mapeo automatico de request a modelos persistentes sin lista permitida de campos.

### Java (`java.java`)
Fragmento representativo: `public class Example { public void demo() throws Exception { BeanUtils.populate(user, request.getParameterMap()); } }`
En este ejemplo, lo vulnerable es dejar que el cliente decida que atributos internos del objeto se van a poblar. En Java, concatenar `String`, deserializar o consumir datos de `request` deja toda la seguridad en la logica de la aplicacion.
Para encontrar casos parecidos en Java, busca mapeo automatico de request a modelos persistentes sin lista permitida de campos.

### Go (`go.go`)
Fragmento representativo: `package main func demo() { json.NewDecoder(r.Body).Decode(&user) }`
En este ejemplo, lo vulnerable es dejar que el cliente decida que atributos internos del objeto se van a poblar. En Go, tomar valores desde `r.URL.Query()`, `body` o estructuras similares y pasarlos a APIs sensibles no introduce sanitizacion por defecto.
Para encontrar casos parecidos en Go, busca mapeo automatico de request a modelos persistentes sin lista permitida de campos.

### PHP (`php.php`)
Fragmento representativo: `foreach($_POST as $k=>$v){$user->$k=$v;}`
En este ejemplo, lo vulnerable es dejar que el cliente decida que atributos internos del objeto se van a poblar. En PHP, usar `$_GET`, `$_POST`, `php://input` o variables equivalentes directamente es un patron clasico de vulnerabilidad.
Para encontrar casos parecidos en PHP, busca mapeo automatico de request a modelos persistentes sin lista permitida de campos.

### Perl (`perl.pl`)
Fragmento representativo: `sub demo { $user->{$_} = $params->{$_} for keys %$params; }`
En este ejemplo, lo vulnerable es dejar que el cliente decida que atributos internos del objeto se van a poblar. En Perl, las variables interpoladas o concatenadas conservan el control del atacante sobre la operacion final.
Para encontrar casos parecidos en Perl, busca mapeo automatico de request a modelos persistentes sin lista permitida de campos.

### Pascal (`pascal.pas`)
Fragmento representativo: `program Example; begin User.Role := Request.ContentFields.Values['role']; end.`
En este ejemplo, lo vulnerable es dejar que el cliente decida que atributos internos del objeto se van a poblar. En Pascal, concatenar strings o reutilizar datos de `Request` transmite el valor no confiable hasta la operacion sensible.
Para encontrar casos parecidos en Pascal, busca mapeo automatico de request a modelos persistentes sin lista permitida de campos.

### Ruby (`ruby.rb`)
Fragmento representativo: `def demo(params) user.update(params.require(:user).permit!) end`
En este ejemplo, lo vulnerable es dejar que el cliente decida que atributos internos del objeto se van a poblar. En Ruby, `params` e interpolacion hacen muy facil que la entrada del usuario llegue intacta a una API peligrosa.
Para encontrar casos parecidos en Ruby, busca mapeo automatico de request a modelos persistentes sin lista permitida de campos.

### Rust (`rust.rs`)
Fragmento representativo: `fn demo() { user.role = form.role.clone(); }`
En este ejemplo, lo vulnerable es dejar que el cliente decida que atributos internos del objeto se van a poblar. En Rust, la seguridad de memoria no evita fallas de logica: deserializar, concatenar o invocar APIs peligrosas con datos no confiables sigue siendo riesgoso.
Para encontrar casos parecidos en Rust, busca mapeo automatico de request a modelos persistentes sin lista permitida de campos.

### C# (`csharp.cs`)
Fragmento representativo: `public class Example { public void Demo() { TryUpdateModelAsync(user).Wait(); } }`
En este ejemplo, lo vulnerable es dejar que el cliente decida que atributos internos del objeto se van a poblar. En C#, interpolacion, concatenacion, model binding o deserializacion no sustituyen validacion, autorizacion ni listas permitidas.
Para encontrar casos parecidos en C#, busca mapeo automatico de request a modelos persistentes sin lista permitida de campos.
