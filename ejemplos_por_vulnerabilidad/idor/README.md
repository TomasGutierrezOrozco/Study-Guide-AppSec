# Insecure Direct Object References (IDOR)

## Descripción General
Ocurre cuando una aplicación expone una referencia directa a un objeto interno (como un ID numérico o clave en base de datos) en la URL o parámetros, y no comprueba si el usuario que realiza la petición tiene los privilegios adecuados para acceder o modificar dicho objeto.

## Patrones y Señales para Análisis SAST
- Consultas a base de datos tipo `SELECT * FROM table WHERE id = ?` sin filtrar por el ID del usuario en sesión.
- Rutas parametrizadas (`/invoice/<id>`, `/user/<id>`) donde el ID del recurso viene del cliente.

## Estrategia de Mitigación y Buenas Prácticas
- Filtrar siempre las consultas por el identificador del usuario autenticado: `WHERE id = ? AND owner_id = ?`.
- Implementar una capa de autorización basada en políticas que valide la pertenencia del recurso antes de entregarlo.
- Utilizar identificadores indirectos o aleatorios (UUIDv4) como defensa secundaria.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# IDOR
def demo():
    return db.execute('SELECT * FROM invoices WHERE id=?', (request.view_args['id'],)).fetchone()
```
- **Sink peligroso y causa raíz:** `SELECT * FROM invoices WHERE id = ?` sin validar el `owner_id`.
- **Mecanismo de explotación y vector:** Acceso a facturas, perfiles o documentos privados de otros usuarios.
- **Remediación idiomática:** Añadir el filtro de sesión: `SELECT * FROM invoices WHERE id = ? AND owner_id = ?`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// IDOR
function demo(req, res) {
  res.json(invoices[req.params.id]);
  }
```
- **Sink peligroso y causa raíz:** `invoices[req.params.id]` consultado directamente desde memoria o base de datos.
- **Mecanismo de explotación y vector:** Lectura horizontal de datos de cualquier cliente cambiando el número de ID.
- **Remediación idiomática:** Validar: `if (invoice.userId !== req.user.id) return res.status(403).send("Forbidden");`.

### 3. Java ([java.java](./java.java))
```java
// IDOR
public class Example {
  public void demo() throws Exception {
    invoiceService.findById(Long.parseLong(request.getParameter("id")));
      }
}
```
- **Sink peligroso y causa raíz:** `invoiceRepository.findById(id)` devuelto al cliente sin comparar propietario.
- **Mecanismo de explotación y vector:** Exposición masiva de datos personales y sensibles entre usuarios.
- **Remediación idiomática:** Verificar: `if (!invoice.getOwnerId().equals(currentUserId)) throw new AccessDeniedException();`.

### 4. Go ([go.go](./go.go))
```go
// IDOR
package main
func demo() {
  db.QueryRow("SELECT * FROM invoices WHERE id=?",r.URL.Query().Get("id"))
  }
```
- **Sink peligroso y causa raíz:** Query por ID sin validación de contexto de usuario.
- **Mecanismo de explotación y vector:** Fuga de información confidencial.
- **Remediación idiomática:** Incluir el usuario de sesión en la consulta SQL o validar en la capa de servicio.

### 5. PHP ([php.php](./php.php))
```php
<?php
// IDOR
echo json_encode(getInvoice($_GET['id']));
```
- **Sink peligroso y causa raíz:** `getInvoice($_GET["id"])` ejecutado sin comprobar `$_SESSION["user_id"]`.
- **Mecanismo de explotación y vector:** Extracción completa de registros cambiando el parámetro numérico en la URL.
- **Remediación idiomática:** Restringir la consulta: `WHERE id = ? AND user_id = ?` pasando `$_SESSION["user_id"]`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// IDOR
public class Example {
  public void Demo() {
    return Json(invoices[int.Parse(Request.Query["id"])]);
      }
}
```
- **Sink peligroso y causa raíz:** `_context.Invoices.FindAsync(id)` retornado directamente en API controller.
- **Mecanismo de explotación y vector:** Acceso a registros de otros usuarios.
- **Remediación idiomática:** Filtrar: `_context.Invoices.FirstOrDefaultAsync(x => x.Id == id && x.UserId == currentUserId)`.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# IDOR
def demo(params)
  render json: Invoice.find(params[:id])
  end
```
- **Sink peligroso y causa raíz:** `Invoice.find(params[:id])` sin scope de usuario.
- **Mecanismo de explotación y vector:** Bypass de autorización horizontal.
- **Remediación idiomática:** Usar relaciones de usuario seguras: `current_user.invoices.find(params[:id])`.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// IDOR
fn demo() {
  let invoice = find_invoice(id);
  }
```
- **Sink peligroso y causa raíz:** Búsqueda de entidad sin verificar el claim del usuario autenticado.
- **Mecanismo de explotación y vector:** Lectura no autorizada de recursos.
- **Remediación idiomática:** Comprobar en la función: `ensure!(invoice.user_id == current_user.id, Error::Unauthorized)`.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# IDOR
sub demo {
  print encode_json(get_invoice(param('id')));
  }
```
- **Sink peligroso y causa raíz:** Búsqueda por clave primaria enviada por parámetro HTTP.
- **Mecanismo de explotación y vector:** Visualización no autorizada de registros ajenos.
- **Remediación idiomática:** Validar la pertenencia del registro antes de formatear la salida.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ IDOR }
program Example;
begin
  Response.Content := GetInvoice(Request.QueryFields.Values['id']);
  end.
```
- **Sink peligroso y causa raíz:** Consulta por parámetro de URL sin filtro de sesión.
- **Mecanismo de explotación y vector:** Acceso a recursos de otros clientes.
- **Remediación idiomática:** Verificar credenciales de propiedad del registro en la base de datos.
