# Padding Oracle (Crypto)

## Descripción General
Aparece en sistemas de cifrado por bloques en modo CBC (Cipher Block Chaining) con relleno PKCS#7 cuando el servidor responde de manera diferenciada (código HTTP, mensaje de error o tiempo de respuesta) ante un error de relleno (*padding error*) frente a un error de descifrado válido. Esta fuga de información lateral permite descifrar el texto plano bloque por bloque sin conocer la clave secreta.

## Patrones y Señales para Análisis SAST
- Captura explícita de excepciones de padding (`ValueError`, `BadPaddingException`) devolviendo un estado o mensaje diferente.
- Uso del modo CBC sin autenticación de integridad (Encrypt-then-MAC ausente).

## Estrategia de Mitigación y Buenas Prácticas
- Migrar a algoritmos de cifrado autenticado (AEAD) como AES-GCM o ChaCha20-Poly1305.
- Si se debe mantener CBC, aplicar el esquema Encrypt-then-MAC con HMAC-SHA256 y validar la firma antes de descifrar.
- Retornar respuestas genéricas idénticas en tiempo constante ante cualquier falla de validación o descifrado.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Padding Oracle
def demo():
    try:
        unpad(cipher.decrypt(token), 16)
    except ValueError:
        return 'bad padding', 403
```
- **Sink peligroso y causa raíz:** Captura de `ValueError` en `unpad()` retornando HTTP 403 vs 200.
- **Mecanismo de explotación y vector:** Descifrado completo del token bloque por bloque utilizando herramientas como PadBuster.
- **Remediación idiomática:** Migrar a `AES.MODE_GCM` con verificación automática de tag de autenticación.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Padding Oracle
function demo(req, res) {
  try{decrypt(req.query.token);}catch{res.status(403).send('bad padding');}
  }
```
- **Sink peligroso y causa raíz:** Manejo de errores de descifrado que delatan excepciones de relleno.
- **Mecanismo de explotación y vector:** Fuga de texto plano criptográfico.
- **Remediación idiomática:** Usar `crypto.createCipheriv("aes-256-gcm", ...)` con autenticación integrada.

### 3. Java ([java.java](./java.java))
```java
// Padding Oracle
public class Example {
  public void demo() throws Exception {
    try{cipher.doFinal(token);}catch(BadPaddingException e){response.sendError(403,"bad padding");}
      }
}
```
- **Sink peligroso y causa raíz:** Diferenciación entre `BadPaddingException` y otras fallas en `Cipher`.
- **Mecanismo de explotación y vector:** Ataque de Padding Oracle contra tokens o cookies de sesión.
- **Remediación idiomática:** Usar `AES/GCM/NoPadding` y validar integridad antes del procesamiento.

### 4. Go ([go.go](./go.go))
```go
// Padding Oracle
package main
func demo() {
  if _,err:=decrypt(token); err!=nil { http.Error(w,"bad padding",403) }
  }
```
- **Sink peligroso y causa raíz:** Respuestas con errores de relleno diferenciados en modo CBC.
- **Mecanismo de explotación y vector:** Descifrado no autorizado de datos confidenciales.
- **Remediación idiomática:** Usar `cipher.NewGCM` que previene ataques de oráculo por diseño.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Padding Oracle
if(!openssl_decrypt($_GET['token'],'AES-128-CBC',$key,0,$iv)){echo 'bad padding';}
```
- **Sink peligroso y causa raíz:** Comprobación con `openssl_decrypt` retornando falso por mal padding.
- **Mecanismo de explotación y vector:** Extracción del contenido de tokens cifrados.
- **Remediación idiomática:** Migrar a cifrado autenticado `aes-256-gcm` con openssl.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Padding Oracle
public class Example {
  public void Demo() {
    try { Decrypt(token); } catch { Response.StatusCode = 403; }
      }
}
```
- **Sink peligroso y causa raíz:** Captura de `CryptographicException` asociada a relleno en `AesManaged`.
- **Mecanismo de explotación y vector:** Ataque de oráculo contra tokens cifrados.
- **Remediación idiomática:** Usar `AesGcm` disponible en .NET Core / .NET 6+.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Padding Oracle
def demo(params)
  rescue OpenSSL::Cipher::CipherError then render plain: 'bad padding', status: 403
  end
```
- **Sink peligroso y causa raíz:** Manejo de `OpenSSL::Cipher::CipherError` indicando padding inválido.
- **Mecanismo de explotación y vector:** Recuperación de datos cifrados.
- **Remediación idiomática:** Usar `aes-256-gcm` en `OpenSSL::Cipher`.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Padding Oracle
fn demo() {
  if decrypt(token).is_err() { return Err(StatusCode::FORBIDDEN); }
  }
```
- **Sink peligroso y causa raíz:** Diferenciación de errores al desaplicar PKCS#7.
- **Mecanismo de explotación y vector:** Ataques de canal lateral.
- **Remediación idiomática:** Usar crates de cifrado moderno como `aes-gcm`.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Padding Oracle
sub demo {
  eval { decrypt($token) }; if ($@) { print 'bad padding' }
  }
```
- **Sink peligroso y causa raíz:** Manejo diferencial de errores de desempacado de padding.
- **Mecanismo de explotación y vector:** Vulnerabilidad ante criptoanálisis de oráculo.
- **Remediación idiomática:** Implementar cifrado autenticado con HMAC.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Padding Oracle }
program Example;
begin
  try Decrypt(Token); except on E: Exception do Response.Code := 403; end;
  end.
```
- **Sink peligroso y causa raíz:** Respuestas distintas ante fallas de padding en bloques.
- **Mecanismo de explotación y vector:** Descifrado sin clave.
- **Remediación idiomática:** Retornar errores genéricos y usar modos autenticados.
