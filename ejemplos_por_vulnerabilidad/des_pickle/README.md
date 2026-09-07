# Pickle Deserialization (Python)

## Descripción General
`pickle` es el módulo de serialización nativo de Python diseñado para persistir objetos complejos. Dado que incluye la capacidad de invocar funciones ejecutables durante la reconstrucción (a través del método especial `__reduce__`), deserializar cualquier dato que provenga de una fuente no confiable permite a un atacante ejecutar código arbitrario en el sistema operativo.

## Patrones y Señales para Análisis SAST
- Llamadas a `pickle.loads()`, `pickle.load()`, `_pickle.loads()` o librerías que lo envuelvan (ej. `joblib.load`).
- Lectura de datos en base64 que se decodifican y pasan a un deserializador binario.

## Estrategia de Mitigación y Buenas Prácticas
- Nunca usar `pickle` para procesar datos recibidos de clientes, APIs o almacenamiento no confiable.
- Utilizar formatos estándar de serialización de datos como JSON, MessagePack o Protocol Buffers.
- En entornos de Machine Learning, preferir formatos seguros como `safetensors` u ONNX en lugar de checkpoints en pickle.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Pickle Deserialization - Python
pickle.loads(request.data)
```
- **Sink peligroso y causa raíz:** `pickle.loads(request.data)` procesa bytes controlados por el usuario.
- **Mecanismo de explotación y vector:** RCE inmediato: un payload malicioso con `__reduce__` invoca `os.system` o `subprocess.Popen`.
- **Remediación idiomática:** Migrar a `json.loads()` o `msgpack.unpackb()` con validación de tipos.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Pickle Deserialization - Python
function demo(req, res) {
  // Pickle es especifico de Python; en JS el equivalente es deserializacion insegura de objetos.
  }
```
- **Sink peligroso y causa raíz:** Equivalente conceptual en Node.js al usar `node-serialize` inseguro.
- **Mecanismo de explotación y vector:** Ejecución remota de comandos en el proceso de Node.
- **Remediación idiomática:** Utilizar `JSON.parse()` estándar.

### 3. Java ([java.java](./java.java))
```java
// Pickle Deserialization - Python
public class Example {
  public void demo() throws Exception {
    // Pickle es especifico de Python; en Java el analogo es readObject().
      }
}
```
- **Sink peligroso y causa raíz:** Equivalente en Java a la deserialización nativa con `readObject()`.
- **Mecanismo de explotación y vector:** RCE a través de clases gadget.
- **Remediación idiomática:** Reemplazar con serializadores JSON o aplicar `ObjectInputFilter`.

### 4. Go ([go.go](./go.go))
```go
// Pickle Deserialization - Python
package main
func demo() {
  // Pickle es especifico de Python; en Go el analogo seria gob o decode inseguro.
  }
```
- **Sink peligroso y causa raíz:** En Go no existe pickle; el análogo es `gob` o parseo dinámico de interfaces.
- **Mecanismo de explotación y vector:** Fallo de integridad o DoS.
- **Remediación idiomática:** Usar `json.Unmarshal` en structs concretos.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Pickle Deserialization - Python
// equivalente en PHP: unserialize sobre input controlado.
```
- **Sink peligroso y causa raíz:** Análogo al `unserialize()` de PHP con objetos y clases mágicas.
- **Mecanismo de explotación y vector:** Ejecución de código a través de destructores.
- **Remediación idiomática:** Usar `json_decode()`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Pickle Deserialization - Python
public class Example {
  public void Demo() {
    // Pickle es especifico de Python.
      }
}
```
- **Sink peligroso y causa raíz:** Análogo a `BinaryFormatter` o `NetDataContractSerializer`.
- **Mecanismo de explotación y vector:** RCE mediante ObjectDataProvider gadgets.
- **Remediación idiomática:** Usar `System.Text.Json`.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Pickle Deserialization - Python
def demo(params)
  # Pickle es especifico de Python; en Ruby el analogo es Marshal.load.
  end
```
- **Sink peligroso y causa raíz:** Análogo a `Marshal.load` en Ruby.
- **Mecanismo de explotación y vector:** RCE por deserialización de objetos.
- **Remediación idiomática:** Usar `JSON.load` seguro.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Pickle Deserialization - Python
fn demo() {
  // Pickle es especifico de Python.
  }
```
- **Sink peligroso y causa raíz:** Rust no posee deserializador con capacidades de ejecución arbitraria por diseño.
- **Mecanismo de explotación y vector:** DoS por consumo de memoria.
- **Remediación idiomática:** Usar `serde_json`.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Pickle Deserialization - Python
sub demo {
  # Pickle es especifico de Python.
  }
```
- **Sink peligroso y causa raíz:** Análogo a `Storable::thaw`.
- **Mecanismo de explotación y vector:** Invocación de código en la fase de descongelado.
- **Remediación idiomática:** Usar serializadores JSON seguros.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Pickle Deserialization - Python }
program Example;
begin
  // Pickle es especifico de Python; en Pascal el equivalente seria carga insegura de objetos/streams.
  end.
```
- **Sink peligroso y causa raíz:** Deserialización de objetos binarios en memoria.
- **Mecanismo de explotación y vector:** Comportamiento inesperado o corrupción.
- **Remediación idiomática:** Serializar en texto estructurado validado.
