# LDAP Injection

## Descripción General
Aparece cuando una aplicación construye filtros de búsqueda LDAP concatenando entradas de usuario sin sanitizar. Un atacante puede inyectar operadores como `*`, `)(uid=*))` o `(|(password=*))` para eludir autenticaciones o volcar el directorio corporativo.

## Patrones y Señales para Análisis SAST
- Concatenación de cadenas en filtros LDAP como `(&(uid=" + user + "))`.
- Falta de escape de caracteres reservados de LDAP (`*`, `(`, `)`, `\`, `NUL`).

## Estrategia de Mitigación y Buenas Prácticas
- Utilizar funciones de escape contextual específicas para LDAP (ej. escapar RFC 4515).
- Usar frameworks y APIs que admitan filtros parametrizados.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# LDAP Injection
ldap_filter=f'(uid={request.args["user"]})'
```
- **Sink peligroso y causa raíz:** Interpolación en `ldap_filter = f"(uid={request.args['user']})"`.
- **Mecanismo de explotación y vector:** Bypass de login inyectando `*)(uid=*))(|(uid=*`.
- **Remediación idiomática:** Usar `ldap.filter.escape_filter_chars(user)` de `python-ldap`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// LDAP Injection
function demo(req, res) {
  const filter=`(uid=${req.query.user})`;
  }
```
- **Sink peligroso y causa raíz:** Construcción de filtros LDAP mediante template strings.
- **Mecanismo de explotación y vector:** Autenticación como cualquier usuario del directorio activo.
- **Remediación idiomática:** Usar librerías de escape de LDAP o parámetros tipados en `ldapjs`.

### 3. Java ([java.java](./java.java))
```java
// LDAP Injection
public class Example {
  public void demo() throws Exception {
    String filter="(uid="+request.getParameter("user")+")";
      }
}
```
- **Sink peligroso y causa raíz:** Concatenación de cadenas en `DirContext.search("(&(uid=" + user + "))")`.
- **Mecanismo de explotación y vector:** Extracción completa del árbol de usuarios del directorio.
- **Remediación idiomática:** Usar búsquedas parametrizadas con placeholders en Spring LDAP o escapar con `LdapEncoder.filterEncode()`.

### 4. Go ([go.go](./go.go))
```go
// LDAP Injection
package main
func demo() {
  filter:="(uid="+r.URL.Query().Get("user")+")"
  }
```
- **Sink peligroso y causa raíz:** Formateo de filtros LDAP con `fmt.Sprintf`.
- **Mecanismo de explotación y vector:** Alteración de la lógica de consulta y bypass de contraseñas.
- **Remediación idiomática:** Usar `ldap.EscapeFilter(user)` del paquete `go-ldap/ldap`.

### 5. PHP ([php.php](./php.php))
```php
<?php
// LDAP Injection
$filter='(uid='.$_GET['user'].')';
```
- **Sink peligroso y causa raíz:** `$filter = "(uid=" . $_GET["user"] . ")";` en llamadas a `ldap_search`.
- **Mecanismo de explotación y vector:** Extracción de atributos sensibles del directorio corporativo.
- **Remediación idiomática:** Sanitizar con `ldap_escape($user, "", LDAP_ESCAPE_FILTER)`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// LDAP Injection
public class Example {
  public void Demo() {
    var filter = "(uid=" + Request.Query["user"] + ")";
      }
}
```
- **Sink peligroso y causa raíz:** Uso de `DirectorySearcher` concatenando texto en `Filter`.
- **Mecanismo de explotación y vector:** Bypass de autenticación en Active Directory.
- **Remediación idiomática:** Usar funciones de escape para caracteres LDAP o codificadores anti-XSS de Microsoft.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# LDAP Injection
def demo(params)
  filter = "(uid=#{params[:user]})"
  end
```
- **Sink peligroso y causa raíz:** Concatenación de entradas en `net-ldap` queries.
- **Mecanismo de explotación y vector:** Volcado de cuentas del directorio.
- **Remediación idiomática:** Usar `Net::LDAP::Filter.eq("uid", user)` que aplica escape automático.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// LDAP Injection
fn demo() {
  let filter = format!("(uid={})", user);
  }
```
- **Sink peligroso y causa raíz:** Formateo de filtros LDAP con strings crudos.
- **Mecanismo de explotación y vector:** Bypass de consultas LDAP.
- **Remediación idiomática:** Escapar caracteres de control según RFC 4515.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# LDAP Injection
sub demo {
  $filter = '(uid=' . param('user') . ')';
  }
```
- **Sink peligroso y causa raíz:** Interpolación en filtros de búsqueda LDAP.
- **Mecanismo de explotación y vector:** Modificación de la lógica de autenticación.
- **Remediación idiomática:** Usar `Net::LDAP::Filter` con escape adecuado.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ LDAP Injection }
program Example;
begin
  Filter := '(uid=' + Request.QueryFields.Values['user'] + ')';
  end.
```
- **Sink peligroso y causa raíz:** Construcción manual de cadenas de consulta LDAP.
- **Mecanismo de explotación y vector:** Bypass de comprobaciones de credenciales.
- **Remediación idiomática:** Escapar paréntesis y asteriscos en la entrada.
