# Preguntas Tecnicas de Entrevista con Respuesta

## SAST

### 1. ¿Como priorizas un hallazgo cuando el sink es peligroso pero no ves explotacion inmediata?

Respuesta:

Primero valido control de entrada, alcance del sink y controles existentes. Si el flujo llega a un sink critico como `exec`, `readObject`, `http.Get(userURL)` o SQL dinamico, ya hay una señal fuerte. Luego estimo explotabilidad real: formato requerido, privilegios, autenticacion, restricciones de longitud y si hay defensas de contexto. Priorizo alto cuando la vulnerabilidad permite RCE, acceso a datos sensibles o pivot interno aunque requiera condiciones razonables.

### 2. ¿Que diferencia hay entre source, sink y sanitizer?

Respuesta:

La source es el origen de datos no confiables, por ejemplo un parametro HTTP. El sink es la operacion sensible donde esos datos causan impacto, como ejecutar comandos, construir SQL o renderizar HTML. El sanitizer es el control que transforma o valida el dato para que deje de ser peligroso en un contexto especifico. Un sanitizer valido para HTML no sirve automaticamente para SQL o shell.

### 3. ¿Como detectas falsos positivos en SQLi durante revision manual?

Respuesta:

Busco si realmente hay concatenacion de datos no confiables dentro de la consulta final. Si se usa `PreparedStatement`, placeholders o query builders seguros, normalmente descarto. Tambien reviso si el dato solo selecciona entre valores constantes por allowlist. Lo importante es entender la consulta final ejecutada, no solo ver un string cerca de una query.

### 4. ¿Cuando una SSRF es critica?

Respuesta:

Cuando permite alcanzar red interna, metadata cloud, servicios de administracion, sockets locales o hacer pivote hacia otras superficies. Sube aun mas si soporta metodos arbitrarios, cabeceras custom, lectura de respuesta, redirects y DNS manipulable.

### 5. ¿Como explicarias un IDOR a un desarrollador backend?

Respuesta:

Le diria que autenticar al usuario no basta si luego el objeto se busca solo por su ID. El backend debe comprobar, en la misma consulta o politica de autorizacion, que el recurso pertenece o esta permitido para ese usuario. Si no, cambiar el identificador en la URL o JSON permite acceso horizontal.

## Burp Suite y DAST

### 6. ¿Que herramienta de Burp usarias primero ante una sospecha de command injection?

Respuesta:

Repeater. Me permite iterar rapido con payloads cortos y observar diferencias directas de contenido o tiempo. Si confirmo un patron, despues paso a Intruder para automatizar variaciones o fuzz de delimitadores.

### 7. ¿Como pruebas una race condition con Burp?

Respuesta:

Capturo una request valida, la duplico o la mando a Intruder y lanzo varias solicitudes concurrentes dentro de la misma ventana temporal. Luego comparo respuestas, saldos, estados o consumo de recursos para verificar si la operacion se ejecuto mas de una vez.

### 8. ¿Como validas un XSS si la respuesta refleja el payload pero no ejecuta?

Respuesta:

Primero identifico el contexto exacto: HTML, atributo, JavaScript, URL o comentario. Luego ajusto el payload para salir de ese contexto. Si aparece escapado como texto, no es explotable en ese punto. Si hay CSP o encoding parcial, sigo probando payloads contextuales antes de concluir.

### 9. ¿Que harías si sospechas NoSQL injection en JSON?

Respuesta:

Probaria si el backend acepta objetos en lugar de strings, por ejemplo `{"username":{"$ne":null}}`. Observaria cambios de autenticacion, errores del motor o resultados demasiado amplios. Tambien verificaria si el backend valida tipos antes de consultar.

### 10. ¿Como diferencias una SQLi ciega de un error funcional normal?

Respuesta:

Busco comportamientos consistentes ante condiciones booleanas opuestas o payloads time-based. Si `AND 1=1` y `AND 1=2` producen diferencias medibles, o un `sleep` retrasa la respuesta, tengo evidencia de control sobre la consulta aunque la respuesta visible sea generica.

## Lenguajes y patrones

### 11. ¿Por que `subprocess.run([...], shell=False)` suele ser mas seguro que `shell=True`?

Respuesta:

Porque evita que el shell reinterprete metacaracteres del usuario. El comando recibe argumentos delimitados en vez de una sola cadena evaluada por `/bin/sh`. Aun asi, sigue siendo necesario validar entradas y limitar que binario o parametros se permiten.

### 12. ¿Por que `pickle` es peligroso aunque el archivo parezca interno?

Respuesta:

Porque el riesgo real es la confianza en el origen. Si un atacante puede influir directa o indirectamente en ese blob, `pickle.loads` puede ejecutar codigo. Incluso canales "internos" pueden romperse por compromisos previos, uploads o integraciones inseguras.

### 13. ¿Que revisarías en un uso de JWT durante una auditoria?

Respuesta:

Verificacion de firma, algoritmo permitido, expiracion, issuer, audience, manejo de revocacion y uso de claims en autorizacion. Tambien comprobaria si alguien usa `decode` en vez de `verify`, si el secreto es debil o si el backend acepta claves controladas por el header.

### 14. ¿Como detectas path traversal si el codigo usa `normalize()`?

Respuesta:

`normalize()` por si sola no basta. Hay que comprobar que la ruta final permanezca bajo el directorio base previsto, preferiblemente comparando la ruta canonicalizada con `startsWith(base)`. Tambien reviso symlinks, rutas absolutas y dobles decodificaciones.

### 15. ¿Que indicios te hacen pensar en deserializacion explotable en Java?

Respuesta:

`ObjectInputStream.readObject()`, datos provenientes de request, cookies, colas o archivos subidos, clases gadget presentes en dependencias y ausencia de filtros o allowlists. Tambien ayuda ver errores de serializacion o formatos binarios sospechosos en trafico HTTP.

## Preguntas de criterio

### 16. ¿Como redactarias un hallazgo de alta calidad?

Respuesta:

Describiendo el flujo exacto desde la entrada hasta el sink, citando el archivo y linea vulnerable, explicando impacto realista y proponiendo una remediacion especifica. Un buen hallazgo no solo dice "hay SQLi"; demuestra por que la consulta es alterable y como corregirla.

### 17. ¿Que harías si el desarrollador dice "nadie controla ese parametro"?

Respuesta:

Verificaria el origen real del dato y si puede ser controlado en escenarios alternos: APIs internas, proxies, cabeceras, jobs, archivos importados o integraciones. En AppSec conviene desconfiar de supuestos implícitos y seguir el flujo completo.

### 18. ¿Cuando una blacklist puede ser aceptable?

Respuesta:

Casi nunca como defensa principal. Puede complementar controles, pero no sustituye parametrizacion, escaping contextual, allowlists o autorizacion robusta. Para sinks de alto riesgo, blacklist sola es fragil por definicion.

### 19. ¿Como responder si no conoces el framework exacto del codigo?

Respuesta:

Me enfoco en el patron de seguridad, no en el nombre del framework. Puedo decir: "Independientemente del framework, aqui hay entrada controlada por usuario llegando a un sink SQL por concatenacion; la correccion es usar consultas parametrizadas y validar el tipo esperado".

### 20. ¿Que suele diferenciar a un candidato fuerte en entrevistas tipo AppSec?

Respuesta:

La claridad del razonamiento. Un candidato fuerte no solo identifica el bug: explica explotabilidad, impacto, remediacion, limitaciones y como lo probaria con Burp. Tambien sabe distinguir un riesgo real de un falso positivo y comunicarlo sin ambigüedad.
