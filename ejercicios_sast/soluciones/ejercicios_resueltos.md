# Ejercicios Resueltos de Revision de Codigo Vulnerable

## Formato sugerido para responder en entrevista

- Vulnerabilidad
- Fuente controlada por usuario
- Sink peligroso
- Linea exacta o bloque vulnerable
- Impacto explotable
- Remediacion concreta

## Bash

### Ejercicio 1

Archivo: `snippets/bash_cmdi.sh`

Hallazgo:

- Vulnerabilidad: command injection.
- Fuente: variable `TARGET` tomada de `$1`.
- Sink: `bash -c "$cmd"` en linea 4.
- Explicacion: el dato llega al shell como parte de una cadena y permite separadores como `;`.
- Remediacion: validar el hostname y pasar argumentos sin invocar shell.

## Go

### Ejercicio 2

Archivo: `snippets/go_ssrf.go`

Hallazgo:

- Vulnerabilidad: SSRF.
- Fuente: parametro `u` desde query string.
- Sink: `http.Get(u)` en la línea 10.
- Impacto: acceso a servicios internos, metadata cloud y escaneo interno.
- Remediacion: allowlist de host, verificacion DNS/IP final y sin redirects.

### Ejercicio 3

Archivo: `snippets/go_path_traversal.go`

Hallazgo:

- Vulnerabilidad: path traversal.
- Fuente: parametro `name`.
- Sink: `os.ReadFile("/data/" + name)` en la línea 10.
- Impacto: lectura arbitraria de archivos.
- Remediacion: canonicalizar, unir rutas de forma segura y comprobar que la ruta final siga bajo la base.

## PHP

### Ejercicio 4

Archivo: `snippets/php_sqli.php`

Hallazgo:

- Vulnerabilidad: SQL injection.
- Fuente: `$_GET['id']`.
- Sink: string SQL concatenado en linea 3.
- Impacto: lectura o modificacion arbitraria de datos segun privilegios.
- Remediacion: prepared statements.

### Ejercicio 5

Archivo: `snippets/php_cmdi.php`

Hallazgo:

- Vulnerabilidad: command injection.
- Fuente: `$_POST['host']`.
- Sink: `shell_exec` en linea 3.
- Impacto: ejecucion remota de comandos.
- Remediacion: evitar shell, validar entrada y reducir privilegios.

## Python

### Ejercicio 6

Archivo: `snippets/python_pickle.py`

Hallazgo:

- Vulnerabilidad: deserializacion insegura.
- Fuente: `request.data`.
- Sink: `pickle.loads` en la línea 8.
- Impacto: ejecucion arbitraria de codigo al deserializar.
- Remediacion: usar JSON u otro formato de datos seguro.

### Ejercicio 7

Archivo: `snippets/python_csrf.py`

Hallazgo:

- Vulnerabilidad: CSRF.
- Evidencia: accion sensible autenticada solo por cookie de sesion.
- Problema: falta token anti-CSRF y validacion de origen.
- Remediacion: token por sesion o formulario, validacion `Origin` y cookies `SameSite`.

## Java

### Ejercicio 8

Archivo: `snippets/java_deser.java`

Hallazgo:

- Vulnerabilidad: deserializacion insegura.
- Fuente: `data` proveniente de un request o archivo externo.
- Sink: `readObject()` en la línea 7.
- Impacto: gadget chains, RCE o DoS.
- Remediacion: eliminar Java native serialization o aplicar `ObjectInputFilter`.

### Ejercicio 9

Archivo: `snippets/java_path_traversal.java`

Hallazgo:

- Vulnerabilidad: path traversal.
- Fuente: `filename`.
- Sink: `Files.readString(base.resolve(filename))`.
- Remediacion: `normalize`, `toRealPath` y check de `startsWith`.

## JavaScript / Node.js

### Ejercicio 10

Archivo: `snippets/node_xss.js`

Hallazgo:

- Vulnerabilidad: reflected XSS.
- Fuente: `req.query.msg`.
- Sink: interpolacion directa en HTML.
- Remediacion: escape contextual o template engine seguro.

### Ejercicio 11

Archivo: `snippets/node_jwt.js`

Hallazgo:

- Vulnerabilidad: JWT sin verificacion.
- Fuente: token del cliente.
- Sink: uso de `jwt.decode` o parseo manual para autorizar.
- Impacto: elevacion de privilegios.
- Remediacion: `jwt.verify`, validacion de algoritmo y claims.

## C

### Ejercicio 12

Archivo: `snippets/c_overflow.c`

Hallazgo:

- Vulnerabilidad: stack buffer overflow.
- Fuente: argumento `argv[1]`.
- Sink: `strcpy` en linea 6.
- Impacto: crash o corrupcion de memoria.
- Remediacion: `snprintf`, limites de longitud y compilacion endurecida.

## Mini reto mixto

### Ejercicio 13

Archivo: `snippets/python_idor.py`

Hallazgo:

- Vulnerabilidad: IDOR.
- Fuente: `doc_id` en la ruta.
- Problema: se consulta por ID sin filtrar por propietario.
- Impacto: acceso horizontal a recursos de otros usuarios.
- Remediacion: autorizacion por objeto en la misma consulta o capa de servicio.
