# Race Conditions (TOCTOU)

## Descripción General
Ocurre cuando la lógica de una aplicación comprueba una condición (Time Of Check) y posteriormente ejecuta una acción (Time Of Use) en operaciones separadas no atómicas. Si un atacante envía múltiples solicitudes simultáneas que se procesan concurrentemente, todas pueden superar la verificación antes de que se registre el primer cambio de estado (ej. canje múltiple de cupones, doble retiro bancario).

## Patrones y Señales para Análisis SAST
- Consultas tipo `SELECT` seguidas de `UPDATE` sin bloqueos de fila (`SELECT ... FOR UPDATE`) ni transacciones.
- Operaciones de verificación de saldo y débito en llamadas de base de datos desacopladas.

## Estrategia de Mitigación y Buenas Prácticas
- Utilizar actualizaciones atómicas en una sola instrucción SQL: `UPDATE coupons SET used = 1 WHERE code = ? AND used = 0`.
- Emplear transacciones con nivel de aislamiento serializable o bloqueos pesimistas (`SELECT ... FOR UPDATE`).
- Implementar bloqueos distribuidos (ej. con Redis Redlock) para operaciones de alto valor.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Race Condition
if not used(code):
    credit(user)
    mark_used(code)
```
- **Sink peligroso y causa raíz:** `SELECT used` seguido de `UPDATE` sin transacción atómica.
- **Mecanismo de explotación y vector:** Uso concurrente de cupones y multiplicación de saldo de forma ilimitada.
- **Remediación idiomática:** Usar updates atómicos: `UPDATE coupons SET used = 1 WHERE code = ? AND used = 0` y verificar `rowcount == 1`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Race Condition
function demo(req, res) {
  if(!coupon.used){credit();coupon.used=true;}
  }
```
- **Sink peligroso y causa raíz:** Comprobación asíncrona de saldo y posterior deducción tras un `await`.
- **Mecanismo de explotación y vector:** Doble gasto y saldo negativo en la cuenta del usuario.
- **Remediación idiomática:** Ejecutar la transacción con bloqueos en la base de datos o transacciones atómicas de MongoDB (`$inc`).

### 3. Java ([java.java](./java.java))
```java
// Race Condition
public class Example {
  public void demo() throws Exception {
    if(!coupon.isUsed()){credit();coupon.setUsed(true);}
      }
}
```
- **Sink peligroso y causa raíz:** Lectura y escritura en base de datos en métodos sin anotación `@Transactional`.
- **Mecanismo de explotación y vector:** Inconsistencia financiera en operaciones concurrentes.
- **Remediación idiomática:** Usar `@Transactional` con bloqueo pesimista `LockModeType.PESSIMISTIC_WRITE`.

### 4. Go ([go.go](./go.go))
```go
// Race Condition
package main
func demo() {
  if !coupon.Used { credit(user); coupon.Used=true }
  }
```
- **Sink peligroso y causa raíz:** Lectura de flag en memoria o BD sin mutex ni transacción.
- **Mecanismo de explotación y vector:** Superación de límites de cuotas mediante requests concurrentes.
- **Remediación idiomática:** Usar canales, `sync.Mutex` o transacciones SQL a nivel de base de datos.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Race Condition
if(!used($code)){credit($user);markUsed($code);}
```
- **Sink peligroso y causa raíz:** Comprobación `if (!used($code))` y posterior llamada a `markUsed($code)`.
- **Mecanismo de explotación y vector:** Canje múltiple del mismo cupón con peticiones simultáneas.
- **Remediación idiomática:** Usar transacciones PDO con `FOR UPDATE` o actualización condicional en una sola sentencia.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Race Condition
public class Example {
  public void Demo() {
    if (!coupon.Used) { Credit(user); coupon.Used = true; }
      }
}
```
- **Sink peligroso y causa raíz:** Lectura y guardado con `DbContext.SaveChangesAsync` sin concurrencia optimista.
- **Mecanismo de explotación y vector:** Múltiples aprobaciones de la misma transacción.
- **Remediación idiomática:** Implementar tokens de concurrencia en EF Core o bloqueos pesimistas en SQL Server.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Race Condition
def demo(params)
  unless coupon.used
    credit(user)
    coupon.update!(used: true)
  end
  end
```
- **Sink peligroso y causa raíz:** `coupon.used?` seguido de `coupon.update(used: true)` en Rails.
- **Mecanismo de explotación y vector:** Doble canje mediante ataques de concurrencia.
- **Remediación idiomática:** Usar `coupon.with_lock` en ActiveRecord para adquirir bloqueo de fila en BD.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Race Condition
fn demo() {
  if !coupon.used { credit(user); coupon.used = true; }
  }
```
- **Sink peligroso y causa raíz:** Verificación y actualización sin bloqueos de base de datos en handlers async.
- **Mecanismo de explotación y vector:** Inconsistencias de balance.
- **Remediación idiomática:** Usar transacciones con aislamiento Serializable en SQLx / Diesel.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Race Condition
sub demo {
  unless ($coupon->{used}) { credit($user); $coupon->{used}=1; }
  }
```
- **Sink peligroso y causa raíz:** Verificación y marcado de estado en pasos separados.
- **Mecanismo de explotación y vector:** Ejecución repetida de acciones restringidas.
- **Remediación idiomática:** Aplicar bloqueos en la base de datos.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Race Condition }
program Example;
begin
  if not Coupon.Used then begin Credit(User); Coupon.Used := True; end;
  end.
```
- **Sink peligroso y causa raíz:** Comprobación de estado sin sincronización de subprocesos.
- **Mecanismo de explotación y vector:** Condición de carrera lógica.
- **Remediación idiomática:** Sincronizar el acceso mediante secciones críticas o bloqueos de base de datos.
