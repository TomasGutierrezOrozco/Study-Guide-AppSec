# XML External Entity Injection (XXE)

## Descripción General
Ocurre cuando un analizador XML procesa documentos que contienen referencias a entidades externas (DOCTYPE / SYSTEM) con la resolución de entidades externas habilitada. Un atacante puede definir entidades que apunten a archivos locales (`file:///etc/passwd`) para exfiltrar información confidencial, forzar peticiones HTTP internas (SSRF) o causar denegación de servicio (Billion Laughs attack).

## Patrones y Señales para Análisis SAST
- Configuración de parsers XML (`DocumentBuilderFactory`, `XMLParser`, `DOMDocument`, `SAXParserFactory`) sin deshabilitar DTDs o entidades externas.
- Uso de `resolve_entities=True` en `lxml`.

## Estrategia de Mitigación y Buenas Prácticas
- Deshabilitar completamente la declaración y procesamiento de DTDs externos en el parser XML.
- Desactivar la resolución de entidades externas generales y parametrizadas (`setFeature("http://xml.org/sax/features/external-general-entities", false)`).
- Migrar a formatos de datos más simples y seguros como JSON si XML no es indispensable.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# XML External Entity Injection (XXE)
root=etree.fromstring(request.data, parser=etree.XMLParser(resolve_entities=True))
```
- **Sink peligroso y causa raíz:** `lxml.etree.XMLParser(resolve_entities=True)` con parseo de entrada del usuario.
- **Mecanismo de explotación y vector:** Lectura arbitraria de archivos locales (`file:///etc/passwd`) y SSRF.
- **Remediación idiomática:** Desactivar resolución de entidades: `XMLParser(resolve_entities=False, no_network=True)` o usar `defusedxml`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// XML External Entity Injection (XXE)
function demo(req, res) {
  parser.parse(req.body,{processEntities:true});
  }
```
- **Sink peligroso y causa raíz:** Parsers XML en Node (como `libxmljs`) configurados con `noent: true`.
- **Mecanismo de explotación y vector:** Exfiltración de archivos del servidor.
- **Remediación idiomática:** Deshabilitar entidades externas en las opciones del parser XML.

### 3. Java ([java.java](./java.java))
```java
// XML External Entity Injection (XXE)
public class Example {
  public void demo() throws Exception {
    DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(request.getInputStream());
      }
}
```
- **Sink peligroso y causa raíz:** `DocumentBuilderFactory.newInstance()` sin configurar características de seguridad.
- **Mecanismo de explotación y vector:** Lectura de archivos del sistema y SSRF interno.
- **Remediación idiomática:** Configurar: `dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true)`.

### 4. Go ([go.go](./go.go))
```go
// XML External Entity Injection (XXE)
package main
func demo() {
  decoder:=xml.NewDecoder(r.Body)
  }
```
- **Sink peligroso y causa raíz:** Uso de parsers XML externos que resuelven DTDs.
- **Mecanismo de explotación y vector:** Fuga de información confidencial.
- **Remediación idiomática:** El paquete nativo `encoding/xml` de Go no soporta entidades externas por diseño; evitar librerías C-bindings inseguras.

### 5. PHP ([php.php](./php.php))
```php
<?php
// XML External Entity Injection (XXE)
$dom=new DOMDocument();
$dom->loadXML(file_get_contents('php://input'), LIBXML_NOENT);
```
- **Sink peligroso y causa raíz:** `$dom->loadXML($xml)` en versiones con `libxml_disable_entity_loader(false)`.
- **Mecanismo de explotación y vector:** Lectura de archivos del servidor mediante `<!ENTITY xxe SYSTEM "file:///etc/passwd">`.
- **Remediación idiomática:** Deshabilitar entidades con `libxml_disable_entity_loader(true)` o usar `LIBXML_NONET`.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// XML External Entity Injection (XXE)
public class Example {
  public void Demo() {
    var doc = new XmlDocument(); doc.LoadXml(body);
      }
}
```
- **Sink peligroso y causa raíz:** `XmlDocument` con `XmlResolver` configurado sin protección en .NET Framework viejo.
- **Mecanismo de explotación y vector:** Lectura de archivos confidenciales y SSRF.
- **Remediación idiomática:** Configurar `xmlReaderSettings.DtdProcessing = DtdProcessing.Prohibit`.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# XML External Entity Injection (XXE)
def demo(params)
  doc = Nokogiri::XML(request.body.read) { |c| c.noent }
  end
```
- **Sink peligroso y causa raíz:** `Nokogiri::XML(xml) { |config| config.nonet }` sin la bandera adecuada.
- **Mecanismo de explotación y vector:** Exfiltración de archivos mediante entidades XML.
- **Remediación idiomática:** Configurar Nokogiri con `config.nonet.noent.strict` o deshabilitar DTDs.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// XML External Entity Injection (XXE)
fn demo() {
  // XML parser con entidades habilitadas o configuracion insegura.
  }
```
- **Sink peligroso y causa raíz:** Parsers XML en Rust configurados para resolver entidades externas.
- **Mecanismo de explotación y vector:** Fuga de archivos.
- **Remediación idiomática:** Usar parsers seguros como `quick-xml` que no procesan DTDs externas por defecto.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# XML External Entity Injection (XXE)
sub demo {
  $parser->parse_string($xml);
  }
```
- **Sink peligroso y causa raíz:** Uso de `XML::LibXML` con opciones `load_ext_dtd` activas.
- **Mecanismo de explotación y vector:** Inyección de entidades externas.
- **Remediación idiomática:** Configurar el parser con `expand_entities(0)` y `load_ext_dtd(0)`.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ XML External Entity Injection (XXE) }
program Example;
begin
  XMLDoc.LoadFromXML(Request.Content);
  end.
```
- **Sink peligroso y causa raíz:** Parseo de documentos XML con DTDs activas.
- **Mecanismo de explotación y vector:** Lectura de archivos locales.
- **Remediación idiomática:** Desactivar la resolución de entidades externas en el componente DOM.
