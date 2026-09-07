# ShellShock (CVE-2014-6271)

## Descripción General
Es una vulnerabilidad crítica en versiones de GNU Bash anteriores a la 4.3 que permite la ejecución remota de comandos. Ocurre cuando Bash procesa variables de entorno que contienen definiciones de funciones con sintaxis `() { :; }; comando`. Al exportar la variable, Bash no se detiene en la definición de la función y ejecuta inmediatamente el comando concatenado.

## Patrones y Señales para Análisis SAST
- Scripts CGI que pasan cabeceras HTTP como variables de entorno a subshells Bash.
- Uso de `os.system()`, `shell_exec()`, `exec("bash -c ...")` en sistemas con Bash desactualizado.

## Estrategia de Mitigación y Buenas Prácticas
- Actualizar GNU Bash a una versión parcheada (Bash >= 4.3).
- Evitar pasar variables de entorno controladas por el usuario a subprocesos del shell.
- Reemplazar scripts CGI en Bash por lenguajes y controladores de aplicaciones modernos.

## Análisis Técnico y Ejemplos por Lenguaje

### 1. Python ([python.py](./python.py))
```python
# ShellShock
os.system('echo $HTTP_USER_AGENT')
```
- **Sink peligroso y causa raíz:** Invocación de shells Bash heredando variables de entorno de petición HTTP.
- **Mecanismo de explotación y vector:** RCE inmediato si la variable contiene payloads de Shellshock.
- **Remediación idiomática:** Actualizar Bash y pasar argumentos sin shell: `subprocess.run(["binary", arg])`.

### 2. JavaScript / Node.js ([javascript.js](./javascript.js))
```javascript
// ShellShock
function demo(req, res) {
  exec('bash -c "echo $HTTP_USER_AGENT"');
  }
```
- **Sink peligroso y causa raíz:** `exec("bash -c ...")` heredando `process.env` con cabeceras de usuario.
- **Mecanismo de explotación y vector:** RCE en el servidor.
- **Remediación idiomática:** Evitar invocar subshells Bash y actualizar el sistema operativo base.

### 3. Java ([java.java](./java.java))
```java
// ShellShock
public class Example {
  public void demo() throws Exception {
    new ProcessBuilder("bash","-c","echo $HTTP_USER_AGENT").start();
      }
}
```
- **Sink peligroso y causa raíz:** `Runtime.getRuntime().exec("bash ...")` en servidores con Bash vulnerable.
- **Mecanismo de explotación y vector:** Ejecución remota de comandos.
- **Remediación idiomática:** Usar `ProcessBuilder` directo sin shell y actualizar Bash en el contenedor.

### 4. Go ([go.go](./go.go))
```go
// ShellShock
package main
func demo() {
  exec.Command("bash","-c","echo $HTTP_USER_AGENT").Run()
  }
```
- **Sink peligroso y causa raíz:** Invocación de `bash` pasando variables de entorno del cliente.
- **Mecanismo de explotación y vector:** RCE.
- **Remediación idiomática:** Ejecutar binarios directamente con `exec.Command` sin shell wrapper.

### 5. PHP ([php.php](./php.php))
```php
<?php
// ShellShock
echo shell_exec('env');
```
- **Sink peligroso y causa raíz:** Servidores Apache con `mod_cgi` ejecutando scripts Bash ante peticiones web.
- **Mecanismo de explotación y vector:** RCE enviando cabecera `User-Agent: () { :; }; /bin/cat /etc/passwd`.
- **Remediación idiomática:** Actualizar el binario de Bash en el sistema y eliminar scripts CGI en shell.

### 6. C# (.NET) ([csharp.cs](./csharp.cs))
```csharp
// ShellShock
public class Example {
  public void Demo() {
    Process.Start("bash", "-c \"echo $HTTP_USER_AGENT\"");
      }
}
```
- **Sink peligroso y causa raíz:** Invocación de scripts Bash en entornos Linux/Docker heredando variables.
- **Mecanismo de explotación y vector:** RCE en el contenedor.
- **Remediación idiomática:** Actualizar las imágenes base de contenedor a versiones modernas protegidas.

### 7. Ruby ([ruby.rb](./ruby.rb))
```ruby
# ShellShock
def demo(params)
  system('bash -c "echo $HTTP_USER_AGENT"')
  end
```
- **Sink peligroso y causa raíz:** Llamadas a `system("bash -c ...")` con entorno contaminado.
- **Mecanismo de explotación y vector:** RCE.
- **Remediación idiomática:** Usar llamadas de sistema vectorizadas sin invocar shell.

### 8. Rust ([rust.rs](./rust.rs))
```rust
// ShellShock
fn demo() {
  Command::new("bash").arg("-c").arg("echo $HTTP_USER_AGENT").output()?;
  }
```
- **Sink peligroso y causa raíz:** Llamadas a `Command::new("bash")`.
- **Mecanismo de explotación y vector:** RCE si el sistema host tiene Bash vulnerable.
- **Remediación idiomática:** Actualizar Bash y no invocar shell.

### 9. Perl ([perl.pl](./perl.pl))
```perl
# ShellShock
sub demo {
  system('bash', '-c', 'echo $HTTP_USER_AGENT');
  }
```
- **Sink peligroso y causa raíz:** Scripts CGI en Perl que llaman a comandos shell vía `system()` o pipes.
- **Mecanismo de explotación y vector:** Compromiso total del servidor.
- **Remediación idiomática:** Actualizar Bash y evitar la interpolación en llamadas al shell.

### 10. Pascal / Free Pascal ([pascal.pas](./pascal.pas))
```pascal
{ ShellShock }
program Example;
begin
  RunCommand('/bin/bash', ['-c', 'echo $HTTP_USER_AGENT'], Output);
  end.
```
- **Sink peligroso y causa raíz:** Llamadas a `ExecuteProcess` pasando por shells vulnerables.
- **Mecanismo de explotación y vector:** Ejecución de código.
- **Remediación idiomática:** Actualizar el entorno de ejecución.
