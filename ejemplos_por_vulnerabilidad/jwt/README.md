# JWT Enumeration and Exploitation

## Descripción General
Las fallas en JWT surgen cuando los tokens no se verifican criptográficamente en el servidor, se aceptan algoritmos nulos (`alg: none`), se utilizan secretos simétricos débiles susceptibles a ataques de diccionario offline, o se confía en claims de autorización sin contrastarlos con el backend.

## Patrones y Señales para Análisis SAST
- Uso de `jwt.decode()` en lugar de `jwt.verify()` para autorizar peticiones.
- Decodificación manual con base64 de la segunda parte del token (`split(".")[1]`).
- Aceptación de claves HMAC en bibliotecas configuradas para verificar firmas RSA (confusión de algoritmos).

## Estrategia de Mitigación y Buenas Prácticas
- Verificar siempre la firma con `jwt.verify()` especificando explícitamente los algoritmos permitidos (ej. `algorithms: ["HS256"]`).
- Utilizar claves secretas con alta entropía (mínimo 256 bits generados aleatoriamente).
- Validar siempre los claims estándar `exp`, `nbf`, `iss` y `aud`.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# JWT Enumeration and Exploitation
payload=json.loads(base64.urlsafe_b64decode(token.split('.')[1]+'=='))
```
- **Sink peligroso y causa raíz:** Decodificación directa de base64url del payload sin verificar firma.
- **Mecanismo de explotación y vector:** Escalamiento a rol `admin` modificando el JSON en el token.
- **Remediación idiomática:** Usar `jwt.decode(token, secret, algorithms=["HS256"])` de PyJWT.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// JWT Enumeration and Exploitation
function demo(req, res) {
  const payload=JSON.parse(Buffer.from(token.split('.')[1],'base64url').toString());
  }
```
- **Sink peligroso y causa raíz:** Uso de `jwt.decode()` o extracción de `split(".")[1]` para tomar decisiones de rol.
- **Mecanismo de explotación y vector:** Suplantación de cualquier identidad modificando el cuerpo del token.
- **Remediación idiomática:** Usar `jwt.verify(token, secret, { algorithms: ["HS256"] })` y capturar excepciones.

### 3. Java ([java.java](./java.java))
```java
// JWT Enumeration and Exploitation
public class Example {
  public void demo() throws Exception {
    String payload=new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
      }
}
```
- **Sink peligroso y causa raíz:** Parseo del payload con `new String(Base64.decode(...))` sin verificar firma.
- **Mecanismo de explotación y vector:** Elevación de privilegios inmediata.
- **Remediación idiomática:** Usar `Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)`.

### 4. Go ([go.go](./go.go))
```go
// JWT Enumeration and Exploitation
package main
func demo() {
  payload,_:=base64.RawURLEncoding.DecodeString(strings.Split(token,".")[1])
  _ = payload
  }
```
- **Sink peligroso y causa raíz:** Decodificación de claims sin invocar `jwt.Parse` con clave de firma.
- **Mecanismo de explotación y vector:** Bypass total de autenticación.
- **Remediación idiomática:** Usar `golang-jwt` con `jwt.ParseWithClaims` verificando el método de firma.

### 5. PHP ([php.php](./php.php))
```php
<?php
// JWT Enumeration and Exploitation
$payload=json_decode(base64_decode(explode('.',$_SERVER['HTTP_AUTHORIZATION'])[1]),true);
```
- **Sink peligroso y causa raíz:** `json_decode(base64_decode(explode(".", $token)[1]))` sin validar firma.
- **Mecanismo de explotación y vector:** Creación de tokens con privilegios de administrador arbitrarios.
- **Remediación idiomática:** Usar librerías como `firebase/php-jwt` con `JWT::decode($token, new Key($key, "HS256"))`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// JWT Enumeration and Exploitation
public class Example {
  public void Demo() {
    var payload = Base64UrlEncoder.Decode(token.Split('.')[1]);
      }
}
```
- **Sink peligroso y causa raíz:** Lectura con `JwtSecurityTokenHandler.ReadJwtToken` en lugar de `ValidateToken`.
- **Mecanismo de explotación y vector:** Suplantación de roles administrativos.
- **Remediación idiomática:** Usar `tokenHandler.ValidateToken(token, tokenValidationParameters, out _)`.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# JWT Enumeration and Exploitation
def demo(params)
  payload = JSON.parse(Base64.urlsafe_decode64(token.split('.')[1]))
  end
```
- **Sink peligroso y causa raíz:** `JWT.decode(token, nil, false)` con verificación desactivada.
- **Mecanismo de explotación y vector:** Modificación libre de roles por el cliente.
- **Remediación idiomática:** Usar `JWT.decode(token, secret, true, { algorithm: "HS256" })`.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// JWT Enumeration and Exploitation
fn demo() {
  let payload = base64::decode_config(parts[1], base64::URL_SAFE_NO_PAD)?;
  }
```
- **Sink peligroso y causa raíz:** Extracción de datos sin pasar por `jsonwebtoken::decode`.
- **Mecanismo de explotación y vector:** Bypass de control de acceso.
- **Remediación idiomática:** Validar con `decode::<Claims>(token, &DecodingKey::from_secret(secret), &Validation::default())`.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# JWT Enumeration and Exploitation
sub demo {
  $payload = decode_base64((split /\./, $token)[1]);
  }
```
- **Sink peligroso y causa raíz:** Extracción de claims sin comprobar la firma criptográfica.
- **Mecanismo de explotación y vector:** Falsificación de identidad de usuario.
- **Remediación idiomática:** Validar con el módulo `Crypt::JWT`.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ JWT Enumeration and Exploitation }
program Example;
begin
  Payload := DecodeBase64(SplitString(Token, '.')[1]);
  end.
```
- **Sink peligroso y causa raíz:** Lectura de campos de identidad de un token sin firma.
- **Mecanismo de explotación y vector:** Compromiso de la autorización de la aplicación.
- **Remediación idiomática:** Implementar validación estricta de firma HMAC.
