# Mass Assignment

## Descripción General
Ocurre cuando una aplicación vincula automáticamente los parámetros de una solicitud HTTP a las propiedades de un objeto o modelo de datos sin filtrar qué campos pueden modificarse. Un atacante puede agregar campos privilegiados a la petición (como `is_admin=true`, `role=admin`, `balance=999999`) y modificar atributos críticos.

## Patrones y Señales para Análisis SAST
- Uso de `Object.assign(user, req.body)`, `user.__dict__.update()`, `foreach($_POST as $k=>$v)`.
- Model binding automático en frameworks sin DTOs o sin listas de propiedades permitidas (`bind`, `fillable`).

## Estrategia de Mitigación y Buenas Prácticas
- Utilizar Data Transfer Objects (DTOs) o ViewModels que contengan exclusivamente los campos permitidos.
- Definir allowlists explícitas en el modelo (ej. `$fillable` en Laravel, `strong_parameters` en Rails).
- Evitar asignar directamente diccionarios o cuerpos JSON al modelo de datos.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Mass Assignment
user.__dict__.update(request.get_json())
```
- **Sink peligroso y causa raíz:** `user.__dict__.update(request.get_json())` sin filtrar claves.
- **Mecanismo de explotación y vector:** Escalamiento de privilegios modificando atributos como `role` o `is_active`.
- **Remediación idiomática:** Extraer campos explícitos: `user.name = data.get("name")` o usar esquemas con Pydantic/Marshmallow.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Mass Assignment
function demo(req, res) {
  Object.assign(user,req.body);
  }
```
- **Sink peligroso y causa raíz:** `Object.assign(user, req.body)` asignando todos los campos entrantes.
- **Mecanismo de explotación y vector:** Modificación no autorizada de roles (`role: "admin"`) o estado de cuenta.
- **Remediación idiomática:** Filtrar explícitamente: `const { name, bio } = req.body; Object.assign(user, { name, bio });`.

### 3. Java ([java.java](./java.java))
```java
// Mass Assignment
public class Example {
  public void demo() throws Exception {
    BeanUtils.populate(user, request.getParameterMap());
      }
}
```
- **Sink peligroso y causa raíz:** Binding directo del cuerpo de la petición sobre la entidad JPA/Hibernate.
- **Mecanismo de explotación y vector:** Modificación de identificadores de rol o saldo de cuenta.
- **Remediación idiomática:** Crear DTOs dedicados con solo los campos mutables por el usuario.

### 4. Go ([go.go](./go.go))
```go
// Mass Assignment
package main
func demo() {
  json.NewDecoder(r.Body).Decode(&user)
  }
```
- **Sink peligroso y causa raíz:** `json.NewDecoder(r.Body).Decode(&user)` decodificando sobre la entidad completa.
- **Mecanismo de explotación y vector:** Alteración de campos protegidos del struct de usuario.
- **Remediación idiomática:** Crear un struct específico `UserUpdateInput` con los únicos campos permitidos.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Mass Assignment
foreach($_POST as $k=>$v){$user->$k=$v;}
```
- **Sink peligroso y causa raíz:** `foreach($_POST as $k => $v) { $user->$k = $v; }` sin filtros.
- **Mecanismo de explotación y vector:** Sobrescritura de atributos administrativos en la base de datos.
- **Remediación idiomática:** Definir allowlist de atributos o usar DTOs tipados.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Mass Assignment
public class Example {
  public void Demo() {
    TryUpdateModelAsync(user).Wait();
      }
}
```
- **Sink peligroso y causa raíz:** Acciones de ASP.NET Core que reciben la entidad de base de datos directamente en el parámetro.
- **Mecanismo de explotación y vector:** Over-posting attack: el atacante modifica propiedades como `IsAdmin`.
- **Remediación idiomática:** Usar ViewModels/DTOs específicos y no vincular entidades de EF Core directamente.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Mass Assignment
def demo(params)
  user.update(params.require(:user).permit!)
  end
```
- **Sink peligroso y causa raíz:** Uso de `User.update(params[:user])` sin Strong Parameters.
- **Mecanismo de explotación y vector:** Modificación de atributos protegidos como `admin: true`.
- **Remediación idiomática:** Usar `params.require(:user).permit(:name, :email)` en Rails.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Mass Assignment
fn demo() {
  user.role = form.role.clone();
  }
```
- **Sink peligroso y causa raíz:** Deserialización de `req` directamente sobre el struct de base de datos.
- **Mecanismo de explotación y vector:** Modificación de propiedades restringidas.
- **Remediación idiomática:** Deserializar en un struct `UpdateUserRequest` con campos acotados.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Mass Assignment
sub demo {
  $user->{$_} = $params->{$_} for keys %$params;
  }
```
- **Sink peligroso y causa raíz:** Mapeo ciego de parámetros a claves de hash del objeto.
- **Mecanismo de explotación y vector:** Elevación de privilegios.
- **Remediación idiomática:** Asignar únicamente los campos autorizados de forma explícita.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Mass Assignment }
program Example;
begin
  User.Role := Request.ContentFields.Values['role'];
  end.
```
- **Sink peligroso y causa raíz:** Asignación directa de campos desde la petición al registro.
- **Mecanismo de explotación y vector:** Sobrescritura de estado privilegiado.
- **Remediación idiomática:** Copiar exclusivamente propiedades públicas autorizadas.
