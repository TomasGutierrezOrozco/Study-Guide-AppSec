# GraphQL Introspection, Mutation and IDOR

## Descripción General
Las APIs GraphQL vulnerables exponen la introspección completa (`__schema`) en producción, lo que permite a un atacante mapear todos los tipos, queries y mutaciones internas. Además, la falta de controles de autorización en resolvers permite alterar objetos ajenos (IDOR) o saturar el servidor mediante consultas anidadas recursivas (DoS).

## Patrones y Señales para Análisis SAST
- Introspección habilitada en servidores de producción (`introspection: true`).
- Resolvers que consultan o mutan datos basados en el parámetro `id` sin verificar el usuario autenticado.
- Falta de validación de profundidad máxima de consultas (`query complexity` / `query depth`).

## Estrategia de Mitigación y Buenas Prácticas
- Deshabilitar la introspección en entornos de producción.
- Implementar autorización a nivel de campo/resolver verificando la propiedad del objeto antes de retornar o mutar.
- Limitar la profundidad y complejidad máxima de las consultas para prevenir ataques de denegación de servicio.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# GraphQL Introspection, Mutation and IDOR
query=request.get_json()['query']
execute_graphql(query)
```
- **Sink peligroso y causa raíz:** Resolvers de Graphene/Strawberry que retornan registros por ID sin control.
- **Mecanismo de explotación y vector:** Enumeración de esquema interno y acceso no autorizado a datos de otros usuarios.
- **Remediación idiomática:** Deshabilitar introspección en producción y validar pertenencia en resolvers: `if obj.owner != user: raise Forbidden`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// GraphQL Introspection, Mutation and IDOR
function demo(req, res) {
  graphql(schema,req.body.query);
  }
```
- **Sink peligroso y causa raíz:** Apollo Server con `introspection: true` y mutaciones sin check de permisos.
- **Mecanismo de explotación y vector:** Descubrimiento de endpoints ocultos y escalamiento de privilegios.
- **Remediación idiomática:** Configurar `introspection: false` en producción y usar directivas de autorización o middleware Shield.

### 3. Java ([java.java](./java.java))
```java
// GraphQL Introspection, Mutation and IDOR
public class Example {
  public void demo() throws Exception {
    graphql.execute(body);
      }
}
```
- **Sink peligroso y causa raíz:** GraphQL Java sin verificador de complejidad ni autorización de contexto.
- **Mecanismo de explotación y vector:** DoS por consultas circulares y bypass de autorización horizontal.
- **Remediación idiomática:** Integrar `MaxQueryDepthInstrumentation` y verificar autorización con Spring Security en DataFetchers.

### 4. Go ([go.go](./go.go))
```go
// GraphQL Introspection, Mutation and IDOR
package main
func demo() {
  executeGraphQL(query)
  }
```
- **Sink peligroso y causa raíz:** Resolvers en `graphql-go` que ejecutan mutaciones sin validar sesión.
- **Mecanismo de explotación y vector:** Manipulación de registros de otros usuarios.
- **Remediación idiomática:** Pasar el contexto de autenticación en `context.Context` y validar permisos antes de la mutación.

### 5. PHP ([php.php](./php.php))
```php
<?php
// GraphQL Introspection, Mutation and IDOR
$query=file_get_contents('php://input');
executeGraphql($query);
```
- **Sink peligroso y causa raíz:** Servidores GraphQL que exponen el esquema completo a usuarios anónimos.
- **Mecanismo de explotación y vector:** Reconocimiento de APIs internas y modificación de datos sensibles.
- **Remediación idiomática:** Desactivar introspección y validar permisos de acceso por campo en los resolvers.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// GraphQL Introspection, Mutation and IDOR
public class Example {
  public void Demo() {
    var result = schema.Execute(_ => _.Query = query);
      }
}
```
- **Sink peligroso y causa raíz:** HotChocolate o GraphQL .NET con esquemas abiertos y sin `[Authorize]`.
- **Mecanismo de explotación y vector:** Acceso no autorizado a consultas y mutaciones sensibles.
- **Remediación idiomática:** Aplicar directivas `@authorize` y deshabilitar introspección en `Program.cs`.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# GraphQL Introspection, Mutation and IDOR
def demo(params)
  result = Schema.execute(params[:query])
  end
```
- **Sink peligroso y causa raíz:** Gema `graphql-ruby` con introspección activa y sin políticas Pundit.
- **Mecanismo de explotación y vector:** Exposición de mutaciones administrativas y acceso horizontal.
- **Remediación idiomática:** Configurar `disable_introspection_entry_points` y usar `authorize: true` en tipos.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// GraphQL Introspection, Mutation and IDOR
fn demo() {
  let response = schema.execute(query).await;
  }
```
- **Sink peligroso y causa raíz:** Implementaciones con `async-graphql` sin límites de profundidad.
- **Mecanismo de explotación y vector:** Agotamiento de CPU por consultas anidadas y mutaciones no autorizadas.
- **Remediación idiomática:** Usar `.depth_limit(5)` y guards de autorización `#[graphql(guard = "AuthGuard")]`.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# GraphQL Introspection, Mutation and IDOR
sub demo {
  $schema->execute($query);
  }
```
- **Sink peligroso y causa raíz:** Manejadores de GraphQL sin control de acceso granular.
- **Mecanismo de explotación y vector:** Acceso a entidades confidenciales.
- **Remediación idiomática:** Añadir filtros de autorización previa en los resolvers.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ GraphQL Introspection, Mutation and IDOR }
program Example;
begin
  Query := Request.Content;
  end.
```
- **Sink peligroso y causa raíz:** Endpoints de GraphQL sin verificación de rol.
- **Mecanismo de explotación y vector:** Ejecución de mutaciones arbitrarias.
- **Remediación idiomática:** Validar la identidad del usuario en cada función de resolución.
