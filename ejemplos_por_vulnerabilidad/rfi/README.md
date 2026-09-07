# Remote File Inclusion (RFI)

## Descripción General
Permite a un atacante obligar al servidor a descargar y ejecutar código alojado en un servidor remoto externo. Ocurre principalmente en PHP cuando directivas como `allow_url_include` están habilitadas y se pasan URLs no confiables a funciones de inclusión como `include()` o `require()`.

## Patrones y Señales para Análisis SAST
- Llamadas a `include($_GET["url"])` o `require($_GET["url"])`.
- Configuración de PHP con `allow_url_include = On`.

## Estrategia de Mitigación y Buenas Prácticas
- Asegurarse de que `allow_url_include = Off` y `allow_url_fopen = Off` en `php.ini`.
- Nunca permitir que URLs externas alimenten funciones de inclusión de código.
- Cargar exclusivamente archivos locales validados contra una lista blanca fija.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# Remote File Inclusion (RFI)
def demo():
    return requests.get(request.args['url']).text
```
- **Sink peligroso y causa raíz:** En Python el análogo es descargar código de una URL y pasarlo a `exec()`.
- **Mecanismo de explotación y vector:** RCE inmediato en el servidor.
- **Remediación idiomática:** Nunca ejecutar código descargado dinámicamente de la red.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// Remote File Inclusion (RFI)
function demo(req, res) {
  fetch(req.query.url).then(r=>r.text()).then(t=>res.send(t));
  }
```
- **Sink peligroso y causa raíz:** En Node.js el análogo sería descargar scripts y cargarlos con `vm.runInNewContext`.
- **Mecanismo de explotación y vector:** Compromiso total del proceso Node.
- **Remediación idiomática:** Cargar únicamente módulos locales preempaquetados.

### 3. Java ([java.java](./java.java))
```java
// Remote File Inclusion (RFI)
public class Example {
  public void demo() throws Exception {
    new URL(request.getParameter("url")).openStream();
      }
}
```
- **Sink peligroso y causa raíz:** Uso de `URLClassLoader` con URLs arbitrarias provistas por el usuario.
- **Mecanismo de explotación y vector:** Carga y ejecución de bytecode malicioso remoto (RCE).
- **Remediación idiomática:** Cargar clases únicamente desde el classpath local predeterminado.

### 4. Go ([go.go](./go.go))
```go
// Remote File Inclusion (RFI)
package main
func demo() {
  http.Get(r.URL.Query().Get("url"))
  }
```
- **Sink peligroso y causa raíz:** En Go no hay RFI nativo; el análogo es compilar plugins remotos dinámicamente.
- **Mecanismo de explotación y vector:** RCE.
- **Remediación idiomática:** Compilar binarios estáticos sin plugins remotos dinámicos.

### 5. PHP ([php.php](./php.php))
```php
<?php
// Remote File Inclusion (RFI)
include($_GET['url']);
```
- **Sink peligroso y causa raíz:** `include($_GET["url"])` con `allow_url_include=On`.
- **Mecanismo de explotación y vector:** RCE total al cargar un script PHP desde un servidor externo del atacante.
- **Remediación idiomática:** Configurar `allow_url_include=Off` en `php.ini` y usar allowlists locales.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// Remote File Inclusion (RFI)
public class Example {
  public void Demo() {
    var body = new HttpClient().GetStringAsync(url).Result;
      }
}
```
- **Sink peligroso y causa raíz:** Carga de ensamblados remotos con `Assembly.LoadFrom(remoteUrl)`.
- **Mecanismo de explotación y vector:** Ejecución arbitraria de código .NET.
- **Remediación idiomática:** Cargar ensamblados únicamente desde ubicaciones locales firmadas con Strong Name.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# Remote File Inclusion (RFI)
def demo(params)
  render plain: URI.open(params[:url]).read
  end
```
- **Sink peligroso y causa raíz:** Descarga y ejecución con `eval(Net::HTTP.get(url))`.
- **Mecanismo de explotación y vector:** RCE en el servidor.
- **Remediación idiomática:** Prohibir la ejecución de código recuperado de la red.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// Remote File Inclusion (RFI)
fn demo() {
  let body = reqwest::blocking::get(url)?.text()?;
  }
```
- **Sink peligroso y causa raíz:** En Rust no existe inclusión dinámica de código remoto en tiempo de ejecución.
- **Mecanismo de explotación y vector:** N/A.
- **Remediación idiomática:** Mantener compilación estática de dependencias.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# Remote File Inclusion (RFI)
sub demo {
  print HTTP::Tiny->new->get($url)->{content};
  }
```
- **Sink peligroso y causa raíz:** Uso de `require` o `do` sobre archivos descargados de internet.
- **Mecanismo de explotación y vector:** RCE.
- **Remediación idiomática:** Cargar únicamente módulos Perl locales instalados.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ Remote File Inclusion (RFI) }
program Example;
begin
  Response.Content := TFPHTTPClient.SimpleGet(Request.QueryFields.Values['url']);
  end.
```
- **Sink peligroso y causa raíz:** Carga dinámica de librerías DLL/SO desde rutas de red no confiables.
- **Mecanismo de explotación y vector:** Ejecución de código malicioso.
- **Remediación idiomática:** Cargar librerías exclusivamente desde rutas locales protegidas.
