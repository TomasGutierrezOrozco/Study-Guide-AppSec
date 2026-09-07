# Soluciones de ejercicios numerados

## [Ejercicio1](Ejercicio1/)

- Stack: PHP
- Vulnerabilidades: SQLi, XSS y Open Redirect.
- Burp: prueba `/user?id=1`, `/search?q=test` y `/redirect?next=https://example.com`.

## [Ejercicio2](Ejercicio2/)

- Stack: PHP
- Vulnerabilidades: LFI, RFI y Log Poisoning.
- Burp: prueba `/include?page=home.php`, `/include?page=http://...`, y para log poisoning inyecta PHP en `User-Agent` y luego carga `logs/access.log`.

## [Ejercicio3](Ejercicio3/)

- Stack: PHP
- Vulnerabilidades: CSRF, Type Juggling y SQL Truncation.
- Burp: usa `/login`, `/change-email` y `/register`.

## [Ejercicio4](Ejercicio4/)

- Stack: PHP
- Vulnerabilidades: Upload Abuse, Session Puzzling/Fixation/Variable Overloading e IDOR.
- Burp: prueba `/upload`, fija `sid` por URL y cambia variables de sesion, y enumera `/invoice?id=...`.

## [Ejercicio5](Ejercicio5/)

- Stack: Python
- Vulnerabilidades: SSTI, XXE y DES-Yaml.
- Burp: `/hello?name={{7*7}}`, `POST /xml` y `POST /yaml`.

## [Ejercicio6](Ejercicio6/)

- Stack: Python
- Vulnerabilidades: DES-Pickle, IDOR y Race Condition.
- Burp: `POST /pickle`, `/invoice/2` y peticiones concurrentes a `/redeem`.

## [Ejercicio7](Ejercicio7/)

- Stack: Go
- Vulnerabilidades: SSRF, API Abuse y CORS.
- Burp: `/fetch?url=http://127.0.0.1:9191/internal`, `/api/report?limit=999999` y revisa cabeceras CORS.

## [Ejercicio8](Ejercicio8/)

- Stack: Node.js
- Vulnerabilidades: CSTI, Prototype Pollution y CSS Injection.
- Burp: `/view`, `/style` y `POST /profile`.

## [Ejercicio9](Ejercicio9/)

- Stack: Node.js
- Vulnerabilidades: JWT inseguro, NoSQLi y Mass Assignment.
- Burp: `POST /login`, `/admin` y `POST /account`.

## [Ejercicio10](Ejercicio10/)

- Stack: Node.js
- Vulnerabilidades: GraphQL Introspection/Mutation/IDOR.
- Burp: `POST /graphql` con consultas `__schema`, `user(id:"2")` y `updateUser`.

## [Ejercicio11](Ejercicio11/)

- Stack: Java
- Vulnerabilidades: LDAP Injection, XPath Injection y LaTeX Injection.
- Burp: `/ldap?user=*`, `/xpath?user=' or '1'='1` y `/latex?name=...`.

## [Ejercicio12](Ejercicio12/)

- Stack: Java
- Vulnerabilidades: XXE, Deserializacion insegura y Mass Assignment.
- Burp: `POST /xml`, `/deserialize?data=...` y `POST /bind`.

## [Ejercicio13](Ejercicio13/)

- Stack: Bash CGI
- Vulnerabilidad: ShellShock.
- Burp: envia `User-Agent: () { :;}; echo; echo pwned` a `/cgi-bin/status.sh`.

## [Ejercicio14](Ejercicio14/)

- Stack: Python crypto
- Vulnerabilidad: Padding Oracle.
- Burp: usa `/encrypt` para obtener un token y modifica bloques hacia `/oracle`.

## [Ejercicio15](Ejercicio15/)

- Stack: Python WebDAV
- Vulnerabilidad: WebDAV.
- Burp: usa `OPTIONS`, `PROPFIND`, `PUT`, `GET` y `DELETE`.

## [Ejercicio16](Ejercicio16/)

- Stack: Squid
- Vulnerabilidad: proxy abierto.
- Burp: configura upstream proxy apuntando a `localhost:8116`.

## [Ejercicio17](Ejercicio17/)

- Stack: Bind9
- Vulnerabilidad: AXFR.
- Nota: sigue siendo desplegable, pero la validacion real se hace mejor con `dig axfr @localhost -p 8117 study.local`.

## [Ejercicio18](Ejercicio18/)

- Stack: PHP
- Vulnerabilidades: Command Injection, SSRF y CORS.
- Burp: prueba `/ping?host=127.0.0.1`, `/proxy?url=http://127.0.0.1:9091/internal` y revisa cabeceras.

## [Ejercicio19](Ejercicio19/)

- Stack: Python
- Vulnerabilidades: LFI, Open Redirect y Upload Abuse.
- Burp: prueba `/read?file=...`, `/jump?next=...` y `POST /upload`.

## [Ejercicio20](Ejercicio20/)

- Stack: Java
- Vulnerabilidades: IDOR, Open Redirect y SSRF.
- Burp: prueba `/invoice?id=2`, `/jump?next=https://...` y `/fetch?url=http://127.0.0.1:9192/internal`.

## [Ejercicio21](Ejercicio21/)

- Stack: Node.js
- Vulnerabilidades: Reflected XSS, CORS y CSRF.
- Burp: usa `/search?q=...`, revisa `Access-Control-Allow-Origin` y reenvia el `POST /email` sin token.

## [Ejercicio22](Ejercicio22/)

- Stack: Python
- Vulnerabilidades: NoSQLi, Mass Assignment y JWT inseguro.
- Burp: `POST /login`, `POST /profile` y `GET /admin` con token modificado.

## [Ejercicio23](Ejercicio23/)

- Stack: Java
- Vulnerabilidades: SQLi, XXE y XPath Injection.
- Burp: `GET /user?id=...`, `POST /xml` y `/xpath?user=' or '1'='1`.

## [Ejercicio24](Ejercicio24/)

- Stack: Node.js
- Vulnerabilidades: GraphQL introspection, IDOR y CORS.
- Burp: `POST /graphql` con `__schema`, `invoice(id:"2")` y revisa cabeceras permisivas.

## [Ejercicio25](Ejercicio25/)

- Stack: PHP
- Vulnerabilidades: CSRF, Session Fixation y IDOR.
- Burp: fija `sid`, reenvia `POST /change-plan` y enumera `/profile?id=...`.
