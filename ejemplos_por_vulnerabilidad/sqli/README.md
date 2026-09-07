# SQL Injection (SQLi)

## Descripción General
Aparece cuando una aplicación construye sentencias SQL concatenando o interpolando datos de usuario directamente en la cadena de la consulta en lugar de usar parámetros preparados. El motor de base de datos interpreta los datos como comandos SQL, permitiendo eludir autenticaciones, extraer datos confidenciales (`UNION`), alterar registros o ejecutar comandos en el sistema operativo.

## Patrones y Señales para Análisis SAST
- Concatenación de strings con `+`, f-strings, `%s` o `.` dentro de consultas `SELECT`, `INSERT`, `UPDATE`, `DELETE`.
- Llamadas a `db.query()`, `Statement.executeQuery()`, `cursor.execute()` pasando consultas no parametrizadas.

## Estrategia de Mitigación y Buenas Prácticas
- Utilizar siempre consultas parametrizadas (Prepared Statements) en todas las operaciones con bases de datos.
- Emplear Object-Relational Mappings (ORMs) de manera segura sin recurrir a consultas raw sin sanitizar.
- Para cláusulas dinámicas que no admiten placeholders (como nombres de tablas o columnas en `ORDER BY`), utilizar una allowlist estricta.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# SQL Injection (SQLI)
query=f"SELECT * FROM users WHERE id={request.args['id']}"
conn.execute(query)
```
- **Sink peligroso y causa raíz:** `f"SELECT * FROM users WHERE id={request.args['id']}"`.
- **Mecanismo de explotación y vector:** Bypass de login, volcado de tablas vía `UNION SELECT` y lectura de credenciales.
- **Remediación idiomática:** Usar placeholders: `cursor.execute("SELECT * FROM users WHERE id = ?", (id_val,))`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// SQL Injection (SQLI)
function demo(req, res) {
  const sql=`SELECT * FROM users WHERE id=${req.query.id}`;
  }
```
- **Sink peligroso y causa raíz:** `const sql = \`SELECT * FROM users WHERE id=${req.query.id}\``.
- **Mecanismo de explotación y vector:** Extracción completa de la base de datos y alteración de registros.
- **Remediación idiomática:** Usar consultas parametrizadas: `db.query("SELECT * FROM users WHERE id = ?", [req.query.id])`.

### 3. Java ([java.java](./java.java))
```java
// SQL Injection (SQLI)
public class Example {
  public void demo() throws Exception {
    String sql="SELECT * FROM users WHERE id="+request.getParameter("id");
      }
}
```
- **Sink peligroso y causa raíz:** `statement.executeQuery("SELECT * FROM users WHERE id=" + id)`.
- **Mecanismo de explotación y vector:** Extracción no autorizada de datos mediante inyección SQL clásica y boolean-based.
- **Remediación idiomática:** Usar `PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?"); ps.setString(1, id);`.

### 4. Go ([go.go](./go.go))
```go
// SQL Injection (SQLI)
package main
func demo() {
  query:="SELECT * FROM users WHERE id="+r.URL.Query().Get("id")
  }
```
- **Sink peligroso y causa raíz:** `db.Query("SELECT * FROM users WHERE id=" + id)`.
- **Mecanismo de explotación y vector:** Inyección SQL alterando la estructura semántica de la consulta.
- **Remediación idiomática:** Usar parámetros: `db.Query("SELECT * FROM users WHERE id = ?", id)`.

### 5. PHP ([php.php](./php.php))
```php
<?php
// SQL Injection (SQLI)
$id=$_GET['id'];
$db->query("SELECT * FROM users WHERE id=$id");
```
- **Sink peligroso y causa raíz:** `$db->query("SELECT * FROM users WHERE id=" . $_GET["id"])`.
- **Mecanismo de explotación y vector:** Volcado de contraseñas, bypass de login e inyección de datos.
- **Remediación idiomática:** Usar PDO con prepared statements: `$stmt = $db->prepare("SELECT * FROM users WHERE id = ?"); $stmt->execute([$id]);`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// SQL Injection (SQLI)
public class Example {
  public void Demo() {
    var sql = "SELECT * FROM users WHERE id = " + id;
      }
}
```
- **Sink peligroso y causa raíz:** `var sql = "SELECT * FROM users WHERE id = " + id;` en SqlCommand.
- **Mecanismo de explotación y vector:** Inyección SQL.
- **Remediación idiomática:** Usar `SqlCommand` con `Parameters.AddWithValue("@id", id)` o Entity Framework LINQ.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# SQL Injection (SQLI)
def demo(params)
  sql = "SELECT * FROM users WHERE id = #{params[:id]}"
  end
```
- **Sink peligroso y causa raíz:** `where("id = #{params[:id]}")` en consultas de ActiveRecord.
- **Mecanismo de explotación y vector:** Inyección SQL.
- **Remediación idiomática:** Usar consultas parametrizadas: `where("id = ?", params[:id])` o hash syntax: `where(id: params[:id])`.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// SQL Injection (SQLI)
fn demo() {
  let sql = format!("SELECT * FROM users WHERE id = {}", id);
  }
```
- **Sink peligroso y causa raíz:** Formateo de queries con `format!("SELECT * FROM users WHERE id = {}", id)`.
- **Mecanismo de explotación y vector:** Inyección SQL.
- **Remediación idiomática:** Usar consultas vinculadas con bind en SQLx: `sqlx::query("SELECT * FROM users WHERE id = ?").bind(id)`.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# SQL Injection (SQLI)
sub demo {
  $sql = "SELECT * FROM users WHERE id = $id";
  }
```
- **Sink peligroso y causa raíz:** Interpolación de `$id` en strings de DBI.
- **Mecanismo de explotación y vector:** Extracción no autorizada de registros.
- **Remediación idiomática:** Usar `prepare("SELECT * FROM users WHERE id = ?")` y pasar el parámetro en `execute($id)`.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ SQL Injection (SQLI) }
program Example;
begin
  Query.SQL.Text := 'SELECT * FROM users WHERE id = ' + ParamStr(1);
  end.
```
- **Sink peligroso y causa raíz:** `Query.SQL.Text := "SELECT * FROM users WHERE id = " + Param;`.
- **Mecanismo de explotación y vector:** Manipulación de la consulta SQL.
- **Remediación idiomática:** Usar parámetros en la consulta: `Query.SQL.Text := "SELECT * FROM users WHERE id = :id"; Query.ParamByName("id").AsString := val;`.
