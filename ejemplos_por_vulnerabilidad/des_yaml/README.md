# YAML Deserialization

## Descripción General
Los analizadores de YAML permiten instanciar tipos complejos mediante etiquetas de tipo personalizadas (ej. `!!python/object/apply`). Si se utiliza un loader inseguro (como `yaml.load()` con `Loader=yaml.Loader` en PyYAML), el parser ejecutará constructores de clases arbitrarias, derivando en Remote Code Execution.

## Patrones y Señales para Análisis SAST
- Uso de `yaml.load()` sin especificar `Loader=yaml.SafeLoader` o sin usar `yaml.safe_load()`.
- Librerías de otros lenguajes con resolución polimórfica activada (ej. SnakeYAML con `Constructor` por defecto).

## Estrategia de Mitigación y Buenas Prácticas
- Utilizar siempre cargadores seguros: `yaml.safe_load()` en Python.
- Restringir constructores de clases en Java (ej. `new SafeConstructor()`).
- Evitar instanciar tipos de código o funciones a partir de documentos YAML.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# YAML Deserialization - Python
yaml.load(request.data, Loader=yaml.Loader)
```
- **Sink peligroso y causa raíz:** `yaml.load(request.data, Loader=yaml.Loader)` con loader completo.
- **Mecanismo de explotación y vector:** RCE mediante etiquetas como `!!python/object/apply:os.system ["id"]`.
- **Remediación idiomática:** Cambiar inmediatamente a `yaml.safe_load(request.data)`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// YAML Deserialization - Python
function demo(req, res) {
  yaml.load(req.body);
  }
```
- **Sink peligroso y causa raíz:** Uso de `js-yaml` con esquemas inseguros (`DEFAULT_SAFE_SCHEMA` no aplicado en versiones viejas).
- **Mecanismo de explotación y vector:** Ejecución de código o prototipos contaminados.
- **Remediación idiomática:** Usar `yaml.load(data, { schema: yaml.FAILSAFE_SCHEMA })` o `yaml.safeLoad`.

### 3. Java ([java.java](./java.java))
```java
// YAML Deserialization - Python
public class Example {
  public void demo() throws Exception {
    new org.yaml.snakeyaml.Yaml().load(body);
      }
}
```
- **Sink peligroso y causa raíz:** Instanciación de `Yaml()` en SnakeYAML sin `SafeConstructor`.
- **Mecanismo de explotación y vector:** RCE a través de clases en classpath (ej. `javax.script.ScriptEngineManager`).
- **Remediación idiomática:** Usar `new Yaml(new SafeConstructor(new LoaderOptions()))`.

### 4. Go ([go.go](./go.go))
```go
// YAML Deserialization - Python
package main
func demo() {
  yaml.Unmarshal(body,&obj)
  }
```
- **Sink peligroso y causa raíz:** Uso de librerías YAML (`gopkg.in/yaml.v3`).
- **Mecanismo de explotación y vector:** Go no ejecuta código arbitrario al desempaquetar, pero estructuras sin validar pueden causar DoS.
- **Remediación idiomática:** Desempaquetar en structs fijos y acotar el tamaño del payload.

### 5. PHP ([php.php](./php.php))
```php
<?php
// YAML Deserialization - Python
// equivalente en PHP: parser inseguro de YAML o unserialize sobre input externo.
```
- **Sink peligroso y causa raíz:** Uso de extensiones YAML o `yaml_parse()` con callbacks activos.
- **Mecanismo de explotación y vector:** Invocación de funciones PHP mediante tags.
- **Remediación idiomática:** Deshabilitar evaluación de callbacks en la función de parseo.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// YAML Deserialization - Python
public class Example {
  public void Demo() {
    var obj = new Deserializer().Deserialize<object>(body);
      }
}
```
- **Sink peligroso y causa raíz:** Uso de `YamlDotNet` permitiendo tags de tipos de .NET.
- **Mecanismo de explotación y vector:** Instanciación de tipos peligrosos.
- **Remediación idiomática:** No configurar resolvers de tipos que permitan clases arbitrarias.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# YAML Deserialization - Python
def demo(params)
  YAML.load(request.body.read)
  end
```
- **Sink peligroso y causa raíz:** Uso de `YAML.load` en versiones antiguas de Psych o sin `safe_load`.
- **Mecanismo de explotación y vector:** RCE mediante tags de clases internas de Ruby.
- **Remediación idiomática:** Usar siempre `YAML.safe_load(yaml_str, permitted_classes: [])`.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// YAML Deserialization - Python
fn demo() {
  let value: serde_yaml::Value = serde_yaml::from_str(body)?;
  }
```
- **Sink peligroso y causa raíz:** Uso de `serde_yaml`.
- **Mecanismo de explotación y vector:** Rust valida fuertemente contra structs y no ejecuta código arbitrario.
- **Remediación idiomática:** Mantener el tipado fuerte y limitar el tamaño del buffer.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# YAML Deserialization - Python
sub demo {
  YAML::Load($body);
  }
```
- **Sink peligroso y causa raíz:** Uso de `YAML::Load` con soporte para deserializar objetos.
- **Mecanismo de explotación y vector:** Ejecución de código mediante objetos bendecidos.
- **Remediación idiomática:** Usar `YAML::Tiny` o `Safe` loaders.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ YAML Deserialization - Python }
program Example;
begin
  YamlText := Request.Content;
  end.
```
- **Sink peligroso y causa raíz:** Procesamiento de documentos YAML complejos.
- **Mecanismo de explotación y vector:** Lógica inesperada por datos maliciosos.
- **Remediación idiomática:** Validar la estructura resultante.
