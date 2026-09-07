# Type Juggling / Loose Comparison

## Descripción General
Ocurre en lenguajes de tipado dinámico (principalmente PHP y JavaScript) cuando se utiliza el operador de comparación débil (`==` en vez de `===`). En PHP, comparar un string con formato científico como `"0e123456"` con otro `"0e999999"` resulta en `true` porque ambos se convierten al número flotante `0`. Esto permite eludir comprobaciones de hash de contraseñas o tokens.

## Patrones y Señales para Análisis SAST
- Uso de `==` o `!=` en lugar de `===` o `!==` al comparar contraseñas, hashes o tokens.
- Comparación de funciones hash (`md5()`, `sha1()`) con strings usando operadores no estrictos.

## Estrategia de Mitigación y Buenas Prácticas
- Utilizar siempre comparación estricta de tipo y valor (`===` y `!==`).
- Para credenciales y hashes criptográficos, utilizar funciones seguras en tiempo constante como `hash_equals()` en PHP o `crypto.timingSafeEqual()` en Node.js.
- Utilizar algoritmos de hashing modernos de contraseñas (`password_hash` / `password_verify` con bcrypt o argon2).

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Type Juggling
if request.json['value']==0:
    pass
```
- **Sink peligroso y causa raíz:** `request.json["value"] == 0` donde tipos booleanos (`True`/`False`) pueden evaluar a 1 o 0.
- **Mecanismo de explotación y vector:** Bypass lógico de verificaciones de estado o flags.
- **Remediación idiomática:** Validar tipos estrictamente con `isinstance(val, int) and not isinstance(val, bool)`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Type Juggling
function demo(req, res) {
  if(req.body.value==0){res.send('ok');}
  }
```
- **Sink peligroso y causa raíz:** Uso de `==` comparando arrays vacíos, ceros o strings (ej. `[] == 0` es true).
- **Mecanismo de explotación y vector:** Bypass de autenticación o validación de tokens.
- **Remediación idiomática:** Usar siempre el operador de igualdad estricta `===`.

### 3. Java ([java.java](./java.java))
```java
// Type Juggling
public class Example {
  public void demo() throws Exception {
    // Java es tipado fuerte; el riesgo aparece en parsers o comparaciones custom debiles.
      }
}
```
- **Sink peligroso y causa raíz:** Java es de tipado estático, pero comparar Strings con `==` en vez de `.equals()` causa fallos lógicos.
- **Mecanismo de explotación y vector:** Fallas de validación de tokens.
- **Remediación idiomática:** Usar `.equals()` o `MessageDigest.isEqual()` para evitar además ataques de timing.

### 4. Go ([go.go](./go.go))
```go
// Type Juggling
package main
func demo() {
  // Go es tipado fuerte; el riesgo aparece al mapear interfaces o JSON debilmente.
  }
```
- **Sink peligroso y causa raíz:** En Go el tipado estático previene type juggling; el análogo es mapear JSON a `interface{}`.
- **Mecanismo de explotación y vector:** Comportamiento inesperado al castear.
- **Remediación idiomática:** Desempaquetar en structs tipados y validar tipos explícitamente.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Type Juggling
if(md5($_POST['password'])=='0e123456'){echo 'ok';}
```
- **Sink peligroso y causa raíz:** `md5($_POST["password"]) == "0e123456"`.
- **Mecanismo de explotación y vector:** Bypass de autenticación encontrando cualquier contraseña cuyo hash comience en `0e` seguido de números.
- **Remediación idiomática:** Usar comparación estricta y segura en tiempo constante con `hash_equals()`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Type Juggling
public class Example {
  public void Demo() {
    if ((Request.Form["value"] ?? "0") == "0") { }
      }
}
```
- **Sink peligroso y causa raíz:** No aplica type juggling implícito.
- **Mecanismo de explotación y vector:** N/A.
- **Remediación idiomática:** Usar métodos de comparación seguros en tiempo constante para hashes.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Type Juggling
def demo(params)
  if params[:value].to_i == 0
  end
  end
```
- **Sink peligroso y causa raíz:** Comparación de objetos con conversiones implícitas no deseadas.
- **Mecanismo de explotación y vector:** Bypass de controles de acceso.
- **Remediación idiomática:** Verificar clases de objetos y usar comparación estricta.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Type Juggling
fn demo() {
  // Rust es tipado fuerte; el riesgo aparece en parsers o coerciones manuales.
  }
```
- **Sink peligroso y causa raíz:** Rust previene conversiones implícitas por diseño de su sistema de tipos.
- **Mecanismo de explotación y vector:** N/A.
- **Remediación idiomática:** Uso estándar de tipos fuertemente definidos.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Type Juggling
sub demo {
  if (param('value') == 0) { }
  }
```
- **Sink peligroso y causa raíz:** Uso de `==` en lugar de `eq` para strings en Perl.
- **Mecanismo de explotación y vector:** Bypass lógico de comprobaciones.
- **Remediación idiomática:** Usar `eq` para cadenas y `==` únicamente para valores numéricos explícitos.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Type Juggling }
program Example;
begin
  if StrToIntDef(Request.ContentFields.Values['value'], 0) = 0 then ;
  end.
```
- **Sink peligroso y causa raíz:** Tipado estático; no aplica type juggling dinámico.
- **Mecanismo de explotación y vector:** N/A.
- **Remediación idiomática:** Mantener el uso de tipos nativos.
