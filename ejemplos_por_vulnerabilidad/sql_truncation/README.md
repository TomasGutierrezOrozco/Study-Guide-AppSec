# SQL Truncation

## Descripción General
Ocurre cuando una base de datos SQL (típicamente MySQL en modo no estricto) trunca silenciosamente los strings que superan la longitud máxima definida en una columna (ej. `VARCHAR(8)`). Un atacante puede registrar un usuario como `admin[muchos espacios]x`, la base de datos trunca a `admin   `, y al realizar comparaciones que ignoran espacios finales, el atacante puede suplantar la cuenta del administrador legítimo.

## Patrones y Señales para Análisis SAST
- Uso de `substr(username, 0, N)` en el backend antes de insertar en la base de datos.
- Bases de datos SQL con tablas donde la columna `username` tiene longitud corta y colación que ignora espacios en blanco finales (PAD SPACE).

## Estrategia de Mitigación y Buenas Prácticas
- Habilitar el modo estricto en la base de datos (`STRICT_ALL_TABLES` o `STRICT_TRANS_TABLES` en MySQL).
- Aplicar `trim()` en el servidor para eliminar espacios en blanco antes de validar longitud y unicidad.
- Definir restricciones de clave primaria o índices únicos que rechacen colisiones en lugar de truncar.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# SQL Truncation
username=request.form['username'][:8]
create_user(username)
```
- **Sink peligroso y causa raíz:** `username = request.form["username"][:8]` recortando el texto antes de insertar.
- **Mecanismo de explotación y vector:** Creación de cuentas colisionantes que suplantan a otros usuarios.
- **Remediación idiomática:** No truncar manualmente; validar con regex `^[a-zA-Z0-9_]{3,20}$` y aplicar `strip()`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// SQL Truncation
function demo(req, res) {
  const username=req.body.username.slice(0,8);
  }
```
- **Sink peligroso y causa raíz:** `username.substring(0, 8)` antes de la inserción en BD.
- **Mecanismo de explotación y vector:** Suplantación de identidad en sistemas con comparación flexible de espacios.
- **Remediación idiomática:** Validar la longitud máxima en lugar de truncar silenciosamente.

### 3. Java ([java.java](./java.java))
```java
// SQL Truncation
public class Example {
  public void demo() throws Exception {
    String username=request.getParameter("username").substring(0,8);
      }
}
```
- **Sink peligroso y causa raíz:** Recorte de strings con `substring` antes de persistir en JPA.
- **Mecanismo de explotación y vector:** Colisión de nombres de usuario únicos.
- **Remediación idiomática:** Usar validadores de Bean Validation `@Size(min=3, max=20)` y rechazar excesos.

### 4. Go ([go.go](./go.go))
```go
// SQL Truncation
package main
func demo() {
  username:=r.FormValue("username")[:8]
  }
```
- **Sink peligroso y causa raíz:** `r.FormValue("username")[:8]` truncando el slice de string.
- **Mecanismo de explotación y vector:** Suplantación de cuentas.
- **Remediación idiomática:** Validar la longitud con `len()` y rechazar solicitudes que excedan el límite.

### 5. PHP ([php.php](./php.php))
```php
<?php
// SQL Truncation
$username=substr($_POST['username'],0,8);
createUser($username);
```
- **Sink peligroso y causa raíz:** `$username = substr($_POST["username"], 0, 8);` insertado en BD.
- **Mecanismo de explotación y vector:** Un atacante registra `admin   x`, la BD trunca a `admin` y suplanta al administrador.
- **Remediación idiomática:** Eliminar espacios con `trim()` y rechazar entradas demasiado largas en lugar de truncar.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// SQL Truncation
public class Example {
  public void Demo() {
    var username = (Request.Form["username"] ?? "").Substring(0, 8);
      }
}
```
- **Sink peligroso y causa raíz:** `username.Substring(0, 8)` antes de invocar `SaveChanges`.
- **Mecanismo de explotación y vector:** Suplantación de cuentas.
- **Remediación idiomática:** Configurar `HasMaxLength(20)` y validar con Data Annotations `[StringLength]`.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# SQL Truncation
def demo(params)
  username = params[:username][0,8]
  end
```
- **Sink peligroso y causa raíz:** Truncado con `username[0..7]` antes de guardar en base de datos.
- **Mecanismo de explotación y vector:** Ataque de truncamiento de usuario.
- **Remediación idiomática:** Usar validaciones de modelo en Rails: `validates :username, length: { maximum: 20 }`.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// SQL Truncation
fn demo() {
  let username = &username[..8];
  }
```
- **Sink peligroso y causa raíz:** Truncado de strings con slices antes de almacenar.
- **Mecanismo de explotación y vector:** Colisiones de registro.
- **Remediación idiomática:** Rechazar strings que excedan el tamaño máximo permitido.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# SQL Truncation
sub demo {
  $username = substr(param('username'), 0, 8);
  }
```
- **Sink peligroso y causa raíz:** Uso de `substr` para ajustar longitud de campos clave.
- **Mecanismo de explotación y vector:** Suplantación de usuarios.
- **Remediación idiomática:** Validar longitud con expresiones regulares estrictas.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ SQL Truncation }
program Example;
begin
  Username := Copy(Request.ContentFields.Values['username'], 1, 8);
  end.
```
- **Sink peligroso y causa raíz:** Copias de string acotadas en campos de longitud fija.
- **Mecanismo de explotación y vector:** Colisión de identidades.
- **Remediación idiomática:** Verificar la longitud total antes de guardar.
