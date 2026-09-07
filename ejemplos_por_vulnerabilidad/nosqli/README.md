# NoSQL Injection

## Descripción General
Aparece en bases de datos orientadas a documentos (como MongoDB) cuando las entradas del usuario se pasan directamente como objetos de consulta en lugar de cadenas de texto primitivas. Un atacante puede enviar operadores de consulta como `{"$ne": null}` o `{"$gt": ""}` para alterar la lógica booleana y eludir autenticaciones o volcar colecciones.

## Patrones y Señales para Análisis SAST
- Paso de cuerpos JSON completos a métodos de consulta (`db.users.find(req.body)`).
- Ausencia de validación de tipo (`typeof param !== "string"`).

## Estrategia de Mitigación y Buenas Prácticas
- Validar estrictamente que los parámetros de autenticación y filtros sean cadenas de texto primitivas.
- Utilizar librerías de esquemas (como Zod, Joi, Pydantic) para asegurar que no se inyecten objetos u operadores.
- Usar el operador `$eq` explícito: `{"username": {"$eq": String(userInput)}}`.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# NoSQL Injection
mongo.db.users.find_one(request.get_json())
```
- **Sink peligroso y causa raíz:** `mongo.db.users.find_one(request.get_json())` directo.
- **Mecanismo de explotación y vector:** Bypass de login enviando `{"username": {"$ne": null}, "password": {"$ne": null}}`.
- **Remediación idiomática:** Validar tipos: `if not isinstance(u, str): return 400` y consultar con strings explícitos.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// NoSQL Injection
function demo(req, res) {
  db.users.findOne(req.body);
  }
```
- **Sink peligroso y causa raíz:** `db.users.findOne({ username: req.body.username, password: req.body.password })`.
- **Mecanismo de explotación y vector:** Autenticación como administrador sin conocer la contraseña.
- **Remediación idiomática:** Validar con `typeof req.body.username === "string"` o usar `mongo-sanitize`.

### 3. Java ([java.java](./java.java))
```java
// NoSQL Injection
public class Example {
  public void demo() throws Exception {
    collection.find(new Document(request.getParameterMap()));
      }
}
```
- **Sink peligroso y causa raíz:** Construcción de `BasicDBObject` con mapas anidados provenientes del request.
- **Mecanismo de explotación y vector:** Manipulación de operadores de consulta en MongoDB Java Driver.
- **Remediación idiomática:** Validar esquemas y construir queries con tipos fuertemente definidos.

### 4. Go ([go.go](./go.go))
```go
// NoSQL Injection
package main
func demo() {
  json.NewDecoder(r.Body).Decode(&filter)
  }
```
- **Sink peligroso y causa raíz:** `bson.M` construido con estructuras dinámicas del cliente.
- **Mecanismo de explotación y vector:** Bypass de consultas y lectura de documentos ajenos.
- **Remediación idiomática:** Validar que los valores sean tipos primitivos antes de estructurar el filtro BSON.

### 5. PHP ([php.php](./php.php))
```php
<?php
// NoSQL Injection
$collection->findOne(json_decode(file_get_contents('php://input'),true));
```
- **Sink peligroso y causa raíz:** Paso de `$_POST` (que puede contener arrays asociativos) a consultas MongoDB.
- **Mecanismo de explotación y vector:** Bypass de autenticación mediante inyección de operadores `$gt`/`$ne`.
- **Remediación idiomática:** Forzar conversión a string: `$user = (string)$_POST["username"];`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// NoSQL Injection
public class Example {
  public void Demo() {
    collection.Find(BsonDocument.Parse(body)).FirstOrDefault();
      }
}
```
- **Sink peligroso y causa raíz:** `Builders<BsonDocument>.Filter` construido a partir de objetos dinámicos.
- **Mecanismo de explotación y vector:** Inyección de operadores NoSQL.
- **Remediación idiomática:** Usar modelos de filtro fuertemente tipados en el driver de C#.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# NoSQL Injection
def demo(params)
  User.where(params.permit!).first
  end
```
- **Sink peligroso y causa raíz:** Paso de parámetros sin sanear a consultas de Mongoid.
- **Mecanismo de explotación y vector:** Bypass de login mediante hash params.
- **Remediación idiomática:** Verificar que los valores no sean hashes antes de consultar.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// NoSQL Injection
fn demo() {
  let filter: serde_json::Value = serde_json::from_str(body)?;
  }
```
- **Sink peligroso y causa raíz:** Deserialización dinámica de BSON con operadores arbitrarios.
- **Mecanismo de explotación y vector:** Alteración de consultas NoSQL.
- **Remediación idiomática:** Usar structs fuertemente tipados para los filtros de búsqueda.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# NoSQL Injection
sub demo {
  $collection->find_one(decode_json($body));
  }
```
- **Sink peligroso y causa raíz:** Paso de estructuras hash externas a consultas de MongoDB.
- **Mecanismo de explotación y vector:** Alteración de la lógica del filtro.
- **Remediación idiomática:** Verificar que los valores de búsqueda sean escalares.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ NoSQL Injection }
program Example;
begin
  Filter := Request.Content;
  end.
```
- **Sink peligroso y causa raíz:** Consultas NoSQL construidas con datos del cliente.
- **Mecanismo de explotación y vector:** Manipulación de condiciones booleanas.
- **Remediación idiomática:** Validar el tipo de dato de cada parámetro.
