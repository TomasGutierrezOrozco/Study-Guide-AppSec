# Prototype Pollution

## Descripción General
Es una vulnerabilidad específica de JavaScript donde una función recursiva de copia o merge fusiona un objeto controlado por el usuario sin filtrar propiedades mágicas como `__proto__` o `constructor.prototype`. Esto permite modificar el prototipo de todos los objetos de la aplicación (`Object.prototype`), pudiendo causar Denial of Service, bypass de autenticación o RCE si las propiedades alteradas se usan en llamadas a `child_process`.

## Patrones y Señales para Análisis SAST
- Funciones recursivas de `merge`, `clone` o `extend` que no ignoran `__proto__` ni `prototype`.
- Librerías vulnerables desactualizadas (ej. versiones antiguas de `lodash.merge`, `minimist`).

## Estrategia de Mitigación y Buenas Prácticas
- Ignorar explícitamente las claves `__proto__`, `constructor` y `prototype` en cualquier función de merge recursivo.
- Crear objetos sin prototipo para almacenar datos de configuración: `Object.create(null)`.
- Congelar el prototipo base al inicio de la aplicación: `Object.freeze(Object.prototype)`.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Prototype Pollution
config.update(request.get_json())
```
- **Sink peligroso y causa raíz:** Análogo en Python: manipulación de `__class__` o `__dict__`.
- **Mecanismo de explotación y vector:** Modificación de atributos internos de clases.
- **Remediación idiomática:** No permitir claves que comiencen con doble guion bajo en updates de diccionarios.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Prototype Pollution
function demo(req, res) {
  Object.assign(config,req.body);
  }
```
- **Sink peligroso y causa raíz:** `deepMerge(profile, data)` procesando claves `__proto__`.
- **Mecanismo de explotación y vector:** Contaminación de `Object.prototype`, bypass de comprobaciones lógicas y potencial RCE.
- **Remediación idiomática:** Filtrar claves peligrosas: `if (key === "__proto__" || key === "constructor") continue;`.

### 3. Java ([java.java](./java.java))
```java
// Prototype Pollution
public class Example {
  public void demo() throws Exception {
    // equivalente en Java: property binding inseguro.
      }
}
```
- **Sink peligroso y causa raíz:** En Java no aplica Prototype Pollution; el análogo es Reflection descontrolada.
- **Mecanismo de explotación y vector:** Manipulación de campos privados.
- **Remediación idiomática:** Usar tipado estático y prohibir reflection sobre clases de sistema.

### 4. Go ([go.go](./go.go))
```go
// Prototype Pollution
package main
func demo() {
  // En Go el analogo mas cercano es mapear JSON ciegamente a structs.
  }
```
- **Sink peligroso y causa raíz:** No aplica en Go por su sistema de tipos y ausencia de prototipos.
- **Mecanismo de explotación y vector:** Inyección de campos no prevista.
- **Remediación idiomática:** Desempaquetar en structs definidos.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Prototype Pollution
$config=array_merge($config,json_decode(file_get_contents('php://input'),true));
```
- **Sink peligroso y causa raíz:** En PHP no hay herencia prototípica; el análogo es sobreescritura de propiedades dinámicas.
- **Mecanismo de explotación y vector:** Inconsistencia de estado.
- **Remediación idiomática:** Definir propiedades de clase explícitas y desactivar propiedades dinámicas.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Prototype Pollution
public class Example {
  public void Demo() {
    // Prototype pollution no aplica directo en C#.
      }
}
```
- **Sink peligroso y causa raíz:** No aplica en C#; el análogo es manipulación de ExpandoObject o Dynamic.
- **Mecanismo de explotación y vector:** Modificación de propiedades dinámicas.
- **Remediación idiomática:** Usar clases fuertemente tipadas.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Prototype Pollution
def demo(params)
  # Prototype pollution no aplica directo; equivalente: merge inseguro de hashes.
  end
```
- **Sink peligroso y causa raíz:** Modificación de clases base mediante `send` o `instance_variable_set`.
- **Mecanismo de explotación y vector:** Alteración global de comportamiento.
- **Remediación idiomática:** No permitir que entradas de usuario determinen nombres de variables de instancia.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Prototype Pollution
fn demo() {
  // Prototype pollution no aplica directamente en Rust.
  }
```
- **Sink peligroso y causa raíz:** No aplica en Rust gracias a su sistema de tipos y ownership.
- **Mecanismo de explotación y vector:** N/A.
- **Remediación idiomática:** Usar structs tipados.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Prototype Pollution
sub demo {
  # No aplica directo; equivalente: merge inseguro de hashes.
  }
```
- **Sink peligroso y causa raíz:** Manipulación de tablas de símbolos (`stash`).
- **Mecanismo de explotación y vector:** Sobrescritura de métodos globales.
- **Remediación idiomática:** No usar nombres de parámetros para acceder a tablas de símbolos.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Prototype Pollution }
program Example;
begin
  // Prototype pollution no aplica directamente; equivalente: binding inseguro de propiedades.
  end.
```
- **Sink peligroso y causa raíz:** No aplica en Pascal.
- **Mecanismo de explotación y vector:** N/A.
- **Remediación idiomática:** Mantener tipado estricto.
