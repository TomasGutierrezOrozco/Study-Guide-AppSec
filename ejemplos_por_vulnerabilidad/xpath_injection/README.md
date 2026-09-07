# XPath Injection

## Descripción General
Similar a SQL Injection, ocurre cuando una aplicación construye consultas XPath concatenando entradas del usuario sin sanitizar para buscar nodos en documentos XML. Permite eludir autenticaciones mediante payloads como `' or '1'='1` o extraer la estructura completa del documento XML.

## Patrones y Señales para Análisis SAST
- Concatenación de variables en expresiones XPath (`//user[name='" + input + "']`).
- Ausencia de variables parametrizadas en compiladores XPath.

## Estrategia de Mitigación y Buenas Prácticas
- Utilizar consultas XPath parametrizadas mediante `XPathVariableResolver` en Java o APIs equivalentes.
- Validar y restringir los caracteres de entrada (permitiendo solo alfanuméricos en búsquedas simples).
- Migrar a bases de datos relacionales o estructuradas para autenticación en lugar de XML.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# XPath Injection
expr=f"//user[name='{request.args['user']}']"
```
- **Sink peligroso y causa raíz:** `expr = f"//user[name='{request.args['user']}']"` evaluado en `lxml.etree`.
- **Mecanismo de explotación y vector:** Bypass de login inyectando `' or '1'='1`.
- **Remediación idiomática:** Usar variables de XPath: `root.xpath("//user[name=$u]", u=user_input)`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// XPath Injection
function demo(req, res) {
  const expr=`//user[name='${req.query.user}']`;
  }
```
- **Sink peligroso y causa raíz:** Construcción de expresiones XPath mediante strings en librerías XML.
- **Mecanismo de explotación y vector:** Acceso a nodos protegidos del XML.
- **Remediación idiomática:** Sanitizar comillas simples y dobles o validar estrictamente el formato de entrada.

### 3. Java ([java.java](./java.java))
```java
// XPath Injection
public class Example {
  public void demo() throws Exception {
    String expr="//user[name='"+request.getParameter("user")+"']";
      }
}
```
- **Sink peligroso y causa raíz:** `xpath.evaluate("//user[name='" + user + "']", xmlDoc)`.
- **Mecanismo de explotación y vector:** Extracción de credenciales contenidas en el árbol XML.
- **Remediación idiomática:** Usar `XPathVariableResolver` para inyectar variables de forma parametrizada.

### 4. Go ([go.go](./go.go))
```go
// XPath Injection
package main
func demo() {
  // XPath en Go suele venir de librerias externas; concatenar input en la expresion es el fallo.
  }
```
- **Sink peligroso y causa raíz:** Concatenación de parámetros en librerías XPath de Go.
- **Mecanismo de explotación y vector:** Manipulación de la consulta XPath.
- **Remediación idiomática:** Escapar comillas o validar con regex alfanumérica.

### 5. PHP ([php.php](./php.php))
```php
<?php
// XPath Injection
$expr="//user[name='".$_GET['user']."']";
```
- **Sink peligroso y causa raíz:** `$expr = "//user[name='" . $_GET["user"] . "']"; $xpath->query($expr)`.
- **Mecanismo de explotación y vector:** Bypass de comprobaciones de autenticación basadas en XML.
- **Remediación idiomática:** Sanitizar comillas o usar parámetros predefinidos.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// XPath Injection
public class Example {
  public void Demo() {
    var expr = "//user[name='" + Request.Query["user"] + "']";
      }
}
```
- **Sink peligroso y causa raíz:** `xmlDoc.SelectSingleNode("//user[name='" + user + "']")`.
- **Mecanismo de explotación y vector:** Bypass de autenticación en esquemas XML.
- **Remediación idiomática:** Usar `XsltContext` con resolución de variables parametrizadas.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# XPath Injection
def demo(params)
  xpath = "//user[name='#{params[:user]}']"
  end
```
- **Sink peligroso y causa raíz:** `doc.xpath("//user[name='#{params[:user]}']")` en Nokogiri.
- **Mecanismo de explotación y vector:** Extracción de nodos confidenciales.
- **Remediación idiomática:** Usar consultas con variables: `doc.xpath("//user[name=$u]", nil, u: user_input)`.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// XPath Injection
fn demo() {
  let expr = format!("//user[name='{}']", user);
  }
```
- **Sink peligroso y causa raíz:** Construcción de queries XPath con format strings.
- **Mecanismo de explotación y vector:** Alteración del resultado de búsqueda XML.
- **Remediación idiomática:** Sanitizar la entrada o usar parsers seguros.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# XPath Injection
sub demo {
  $expr = "//user[name='" . param('user') . "']";
  }
```
- **Sink peligroso y causa raíz:** Interpolación de variables en `XML::XPath`.
- **Mecanismo de explotación y vector:** Extracción no autorizada de datos XML.
- **Remediación idiomática:** Validar la entrada antes de evaluar la expresión.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ XPath Injection }
program Example;
begin
  Expr := '//user[name=''' + Request.QueryFields.Values['user'] + ''']';
  end.
```
- **Sink peligroso y causa raíz:** Consultas XPath construidas dinámicamente.
- **Mecanismo de explotación y vector:** Bypass de filtros en XML.
- **Remediación idiomática:** Filtrar metacaracteres de XPath.
