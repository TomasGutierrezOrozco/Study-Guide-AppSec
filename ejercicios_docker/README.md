# Laboratorios Vulnerables con Docker

Estos labs estan pensados para practicar el doble enfoque de la entrevista:

- leer codigo y detectar la vulnerabilidad;
- explotarla en la web usando Burp Suite.

## Arranque

```bash
docker compose up --build
```

## Servicios

- `php-cmdi-sqli`: command injection y SQLi en PHP.
- `go-ssrf-lfi`: SSRF y path traversal en Go.
- `node-xss-jwt`: XSS y JWT sin verificacion en Node.js.
- `python-idor-race`: IDOR y race condition en Python.
- `java-ssrf-traversal`: SSRF y path traversal en Java.

## Puertos

- PHP: `http://localhost:8081`
- Go: `http://localhost:8082`
- Node: `http://localhost:8083`
- Python: `http://localhost:8084`
- Java: `http://localhost:8085`

## Sugerencia de practica

1. Lee primero el codigo del lab.
2. Formula el hallazgo sin ejecutar nada.
3. Intercepta la peticion legitima en Burp.
4. Explota con Repeater o Intruder.
5. Describe impacto, evidencia y remediacion.
