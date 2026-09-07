# Insecure Deserialization (General)

## Descripción General
La deserialización insegura ocurre cuando una aplicación reconstruye objetos en memoria a partir de flujos de datos serializados (binarios, JSON con tipos polimórficos, etc.) controlados por un atacante. Puede permitir la ejecución de cadenas de gadgets existentes en el classpath/runtime, manipulación de atributos internos, denegación de servicio o Remote Code Execution (RCE).

## Patrones y Señales para Análisis SAST
- Uso de `ObjectInputStream.readObject()`, `pickle.loads()`, `unserialize()`, `BinaryFormatter`, `Marshal.load`.
- Formatos JSON/XML configurados para resolver tipos de clases arbitrarias (ej. Jackson `enableDefaultTyping`).

## Estrategia de Mitigación y Buenas Prácticas
- Reemplazar la deserialización nativa por formatos de intercambio de datos seguros como JSON o Protocol Buffers con esquemas estrictos.
- Si la deserialización es indispensable, aplicar filtros de clases estrictos (Allowlist) antes de reconstruir el objeto.
- Firmar criptográficamente los datos serializados con HMAC y validar antes de deserializar.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Insecure Deserialization
pickle.loads(request.data)
```
- **Sink peligroso y causa raíz:** `pickle.loads()` sobre datos de usuario.
- **Mecanismo de explotación y vector:** Ejecución remota de comandos mediante `__reduce__`.
- **Remediación idiomática:** Migrar a `json.loads()` o aplicar firmas HMAC verificadas antes de deserializar.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Insecure Deserialization
function demo(req, res) {
  const obj=unserialize(req.body.data);
  }
```
- **Sink peligroso y causa raíz:** Uso de `node-serialize` con funciones IIFE `_$$ND_FUNC$$_`.
- **Mecanismo de explotación y vector:** Ejecución arbitraria de código en el servidor Node.js.
- **Remediación idiomática:** Usar exclusivamente `JSON.parse()` nativo.

### 3. Java ([java.java](./java.java))
```java
// Insecure Deserialization
public class Example {
  public void demo() throws Exception {
    new ObjectInputStream(request.getInputStream()).readObject();
      }
}
```
- **Sink peligroso y causa raíz:** `ObjectInputStream.readObject()` sin `ObjectInputFilter`.
- **Mecanismo de explotación y vector:** RCE mediante cadenas de gadgets de librerías comunes (Commons Collections, Spring).
- **Remediación idiomática:** Migrar a Jackson/Gson o configurar `ObjectInputFilter` restrictivo.

### 4. Go ([go.go](./go.go))
```go
// Insecure Deserialization
package main
func demo() {
  gob.NewDecoder(r.Body).Decode(&obj)
  }
```
- **Sink peligroso y causa raíz:** Decodificación de flujos `gob` no confiables en interfaces genéricas.
- **Mecanismo de explotación y vector:** Pánico en memoria o corrupción de estructuras internas.
- **Remediación idiomática:** Decodificar únicamente en structs con tipos fuertemente definidos o usar JSON/protobuf.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Insecure Deserialization
unserialize($_POST['data']);
```
- **Sink peligroso y causa raíz:** Uso de `unserialize($_POST["data"])`.
- **Mecanismo de explotación y vector:** Invocación de métodos mágicos (`__destruct`, `__wakeup`) logrando RCE o borrado de archivos.
- **Remediación idiomática:** Reemplazar con `json_decode()` o restringir con `unserialize($data, ["allowed_classes" => false])`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Insecure Deserialization
public class Example {
  public void Demo() {
    new BinaryFormatter().Deserialize(stream);
      }
}
```
- **Sink peligroso y causa raíz:** Uso de `BinaryFormatter.Deserialize()`.
- **Mecanismo de explotación y vector:** Ejecución arbitraria de comandos en el servidor.
- **Remediación idiomática:** Eliminar `BinaryFormatter` y usar `System.Text.Json` sin serialización polimórfica de tipos.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Insecure Deserialization
def demo(params)
  Marshal.load(request.body.read)
  end
```
- **Sink peligroso y causa raíz:** Uso de `Marshal.load()` sobre datos de la petición.
- **Mecanismo de explotación y vector:** RCE mediante gadgets en clases estándar de Ruby.
- **Remediación idiomática:** Usar `JSON.parse()` con tipos primitivos.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Insecure Deserialization
fn demo() {
  let obj: T = bincode::deserialize(bytes)?;
  }
```
- **Sink peligroso y causa raíz:** Deserialización con `serde` permitiendo tipos dinámicos o punteros inseguros.
- **Mecanismo de explotación y vector:** Aunque Rust evita corrupción de memoria, bugs lógicos o DoS son posibles.
- **Remediación idiomática:** Validar esquemas mediante structs fuertemente tipados sin deserialización arbitraria.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Insecure Deserialization
sub demo {
  thaw($body);
  }
```
- **Sink peligroso y causa raíz:** Uso de `Storable::thaw` o `Sereal` con objetos externos.
- **Mecanismo de explotación y vector:** Ejecución de código arbitrario al reconstruir objetos bendecidos.
- **Remediación idiomática:** Reemplazar con módulos JSON seguros (`Cpanel::JSON::XS`).

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Insecure Deserialization }
program Example;
begin
  ReadComponent(Stream);
  end.
```
- **Sink peligroso y causa raíz:** Deserialización de componentes y registros desde flujos externos.
- **Mecanismo de explotación y vector:** Corrupción de punteros y manipulación de estado.
- **Remediación idiomática:** Validar la estructura de datos campo por campo.
