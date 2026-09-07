# API Abuse y Falta de Rate Limiting

## Descripción General
Ocurre cuando una API expone recursos o acciones sin límites de consumo, cuotas ni validación de límites numéricos en los parámetros. Un cliente malicioso puede solicitar millones de registros, provocar sobrecarga de memoria, consumo excesivo de CPU o scraping no autorizado.

## Patrones y Señales para Análisis SAST
- Conversión de parámetros HTTP (`limit`, `count`, `size`) a enteros sin validar cotas máximas.
- Llamadas a `range()`, `Array(n)`, `SELECT *` o bucles basados en parámetros de entrada.
- Falta de middlewares o anotaciones de rate limiting (Throttling) en controladores de API.

## Estrategia de Mitigación y Buenas Prácticas
- Aplicar una cota superior estricta en el servidor (ej. `limit = min(parsed_limit, 100)`).
- Implementar paginación obligatoria basada en cursores o offsets limitados.
- Configurar Rate Limiting por IP y por token de usuario (ej. Token Bucket / Leaky Bucket).

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# API Abuse
def demo():
    return {'items': list(range(int(request.args.get('limit', '1000000'))))}
```
- **Sink peligroso y causa raíz:** `request.args.get("limit")` pasado a `int()` y `range()` sin cota máxima.
- **Mecanismo de explotación y vector:** Agotamiento de memoria (OOM) en el worker de Python al crear listas gigantes.
- **Remediación idiomática:** Establecer cota: `limit = min(int(request.args.get("limit", 50)), 100)` y usar generadores o paginación en BD.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// API Abuse
function demo(req, res) {
  res.json({items:[...Array(Number(req.query.limit||1000000)).keys()]});
  }
```
- **Sink peligroso y causa raíz:** `Number(req.query.limit)` usado en `Array(...)` sin límite.
- **Mecanismo de explotación y vector:** Bloqueo del event loop de Node.js por asignación masiva en el heap V8.
- **Remediación idiomática:** Validar con `Math.min(Number(req.query.limit) || 20, 100)` y aplicar middleware `express-rate-limit`.

### 3. Java ([java.java](./java.java))
```java
// API Abuse
public class Example {
  public void demo() throws Exception {
    int limit=Integer.parseInt(request.getParameter("limit"));
      }
}
```
- **Sink peligroso y causa raíz:** `Integer.parseInt(request.getParameter("limit"))` consumido directamente.
- **Mecanismo de explotación y vector:** Consumo excesivo de memoria en JVM y saturación del pool de conexiones.
- **Remediación idiomática:** Restringir: `int limit = Math.min(Math.max(Integer.parseInt(val), 1), 100);` y paginar consultas JPA/JDBC.

### 4. Go ([go.go](./go.go))
```go
// API Abuse
package main
func demo() {
  limit,_:=strconv.Atoi(r.URL.Query().Get("limit"))
  }
```
- **Sink peligroso y causa raíz:** `strconv.Atoi(r.URL.Query().Get("limit"))` sin acotación.
- **Mecanismo de explotación y vector:** Alojamientos masivos de slices y presión excesiva sobre el Garbage Collector.
- **Remediación idiomática:** Acotar el entero: `if limit <= 0 || limit > 100 { limit = 50 }` y limitar tamaño con `io.LimitReader`.

### 5. PHP ([php.php](./php.php))
```php
<?php
// API Abuse
echo json_encode(range(1,intval($_GET['limit']??1000000)));
```
- **Sink peligroso y causa raíz:** `intval($_GET["limit"])` pasado a `range()`.
- **Mecanismo de explotación y vector:** Superación del `memory_limit` de PHP produciendo error fatal 500.
- **Remediación idiomática:** Aplicar cota estricta: `$limit = min(max(1, (int)($_GET["limit"] ?? 20)), 100);`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// API Abuse
public class Example {
  public void Demo() {
    var limit = int.Parse(Request.Query["limit"] ?? "1000000");
      }
}
```
- **Sink peligroso y causa raíz:** `int.Parse(Request.Query["limit"])` sin validación.
- **Mecanismo de explotación y vector:** Saturación del heap en CLR y sobrecarga en Entity Framework.
- **Remediación idiomática:** Acotar: `int limit = Math.Clamp(parsedLimit, 1, 100);` y usar `Take(limit)`.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# API Abuse
def demo(params)
  render json: (1..params.fetch(:limit, 1_000_000).to_i).to_a
  end
```
- **Sink peligroso y causa raíz:** `params[:limit].to_i` pasado a colecciones.
- **Mecanismo de explotación y vector:** Creación masiva de objetos en Ruby VM generando degradación de rendimiento.
- **Remediación idiomática:** Limitar: `limit = [[params[:limit].to_i, 1].max, 100].min`.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// API Abuse
fn demo() {
  let limit: usize = limit.parse().unwrap_or(1_000_000);
  }
```
- **Sink peligroso y causa raíz:** Conversión de entrada a tamaño de iteradores o vectores.
- **Mecanismo de explotación y vector:** Aunque Rust maneja memoria de forma segura, allocations gigantes pueden causar OOM.
- **Remediación idiomática:** Validar: `let limit = limit_param.parse::<usize>().unwrap_or(20).clamp(1, 100);`.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# API Abuse
sub demo {
  @items = (1..(param('limit') || 1000000));
  }
```
- **Sink peligroso y causa raíz:** `param("limit")` usado en rangos `1..$limit`.
- **Mecanismo de explotación y vector:** Agotamiento de memoria en el proceso de Perl.
- **Remediación idiomática:** Acotar: `$limit = $limit > 100 ? 100 : ($limit < 1 ? 20 : $limit);`.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ API Abuse }
program Example;
begin
  Limit := StrToIntDef(Request.QueryFields.Values['limit'], 1000000);
  end.
```
- **Sink peligroso y causa raíz:** Asignación directa de límites sin verificación de rango.
- **Mecanismo de explotación y vector:** Desbordamiento o consumo masivo de memoria dinámica.
- **Remediación idiomática:** Validar límites con sentencias condicionales estrictas (`if Limit > 100 then Limit := 100;`).
