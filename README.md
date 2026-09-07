# Guia de aprendizaje AppSec para entrevistas tecnicas

Este repositorio es un kit de estudio practico para prepararte en seguridad de aplicaciones con enfoque de entrevista tecnica. El material combina revision manual de codigo, explotacion con Burp Suite y laboratorios vulnerables con Docker para entrenar criterio tecnico, no solo memoria.

> Aviso de seguridad: este repositorio contiene codigo vulnerable de forma intencional para fines educativos. Ejecuta los laboratorios solo en local o en entornos controlados, y no los expongas a internet ni a redes compartidas.

El proyecto esta orientado especialmente a practicar:

- SAST: identificar vulnerabilidades leyendo codigo y razonando sobre source, sink, controles y explotabilidad.
- DAST: validar hallazgos desde HTTP usando Burp Suite.
- Comunicacion tecnica: explicar evidencia, impacto, severidad y remediacion de forma clara.

## Que contiene este proyecto

El repositorio esta organizado para cubrir varias formas de estudio sobre un mismo tema:

- teoria guiada en una guia principal de estudio;
- snippets pequenos para practicar lectura de codigo;
- laboratorios vulnerables para explotar manualmente;
- ejercicios "a ciegas" con nombres neutrales;
- ejemplos por vulnerabilidad y por lenguaje.

## Estructura del repositorio

- `docs/`: guia base del plan de estudio.
- `ejercicios_sast/`: snippets vulnerables y soluciones comentadas para revision estatica.
- `ejercicios_docker/`: 5 laboratorios vulnerables con nombres descriptivos para practicar SAST + DAST.
- `ejercicios_numerados/`: 25 ejercicios con nombres neutrales para simular una evaluacion sin pistas.
- `ejemplos_por_vulnerabilidad/`: 36 familias de vulnerabilidades con ejemplos breves en varios lenguajes.

## Ruta recomendada de estudio

Si quieres sacarle provecho al repositorio sin perderte, esta es la secuencia mas util:

1. Empieza por la guia principal de `docs/` para entender metodologia, checklist mental y plan de practica.
2. Pasa a `ejercicios_sast/snippets/` e intenta detectar la vulnerabilidad sin mirar la solucion.
3. Contrasta tu analisis con `ejercicios_sast/soluciones/ejercicios_resueltos.md`.
4. Abre los labs de `ejercicios_docker/` para revisar codigo y luego explotar con Burp Suite.
5. Cuando ya reconozcas patrones, practica con `ejercicios_numerados/` para resolver escenarios sin que el nombre revele el fallo.
6. Usa `ejemplos_por_vulnerabilidad/` como catalogo rapido para comparar una misma debilidad en distintos lenguajes.

## Como practicar dentro del repo

Una dinamica efectiva para cada ejercicio es:

1. Identificar la entrada controlada por el usuario.
2. Seguir su propagacion hasta un sink peligroso.
3. Verificar si hay validacion positiva, control de contexto o autorizacion real.
4. Formular el hallazgo en tres partes: evidencia, impacto y remediacion.
5. Si aplica, reproducir la explotacion con Burp Suite.

Este repositorio esta pensado para que repitas ese ciclo muchas veces hasta volverlo reflejo.

## Carpetas clave

### `docs/`

Contiene la guia principal del proyecto:

- [`docs/guia_estudio_appsec.md`](docs/guia_estudio_appsec.md): contiene la guia principal del plan de estudio.

La guia incluye objetivo, plan de dos semanas, checklist mental de reviewer y modulos por vulnerabilidad.

### `ejercicios_sast/`

Se centra en lectura de codigo vulnerable.

- [`ejercicios_sast/snippets/`](ejercicios_sast/snippets/): archivos cortos en Bash, C, Go, Java, JavaScript, PHP y Python.
- [`ejercicios_sast/soluciones/ejercicios_resueltos.md`](ejercicios_sast/soluciones/ejercicios_resueltos.md): explicacion del hallazgo, impacto y remediacion.

Es el mejor punto de entrada si quieres fortalecer criterio de revision manual.

### `ejercicios_docker/`

Incluye laboratorios con nombres descriptivos para que sea facil relacionar codigo y explotacion. Los contenedores se definen como servicios usando guiones (ej. `php-cmdi-sqli`), pero sus archivos fuente correspondientes estan en el directorio `ejercicios_docker/labs/` usando guiones bajos:

- [`ejercicios_docker/labs/php_cmdi_sqli`](ejercicios_docker/labs/php_cmdi_sqli)
- [`ejercicios_docker/labs/go_ssrf_lfi`](ejercicios_docker/labs/go_ssrf_lfi)
- [`ejercicios_docker/labs/node_xss_jwt`](ejercicios_docker/labs/node_xss_jwt)
- [`ejercicios_docker/labs/python_idor_race`](ejercicios_docker/labs/python_idor_race)
- [`ejercicios_docker/labs/java_ssrf_traversal`](ejercicios_docker/labs/java_ssrf_traversal)

Estos labs sirven para practicar el flujo completo: leer el codigo, formular la hipotesis y validarla por HTTP.

### `ejercicios_numerados/`

Incluye 25 ejercicios con nombres neutrales (`Ejercicio1` a `Ejercicio25`) para evitar pistas obvias. Es ideal para simulacros o practica cronometrada.

Las pistas y soluciones estan en [`ejercicios_numerados/soluciones.md`](ejercicios_numerados/soluciones.md).

### `ejemplos_por_vulnerabilidad/`

Agrupa 36 vulnerabilidades por carpeta. Cada una contiene ejemplos breves en multiples lenguajes para que compares patrones de implementacion insegura y remediaciones posibles.

Es util como mapa mental de vulnerabilidades frecuentes en AppSec y como material de repaso rapido antes de una entrevista.

## Requisitos

Para usar todo el repositorio con comodidad, lo normal es tener:

- Docker y Docker Compose, para levantar los laboratorios.
- Burp Suite, para la practica DAST.
- Un editor o IDE, para revisar el codigo y tomar notas.

## Como levantar los laboratorios

### Labs descriptivos

Desde `ejercicios_docker/`:

```bash
docker compose up --build
```

Puertos principales:

- `http://localhost:8081` PHP
- `http://localhost:8082` Go
- `http://localhost:8083` Node.js
- `http://localhost:8084` Python
- `http://localhost:8085` Java

### Ejercicios numerados

Desde `ejercicios_numerados/`:

```bash
docker compose up --build
```

Los ejercicios exponen puertos `8101` a `8125`, segun el laboratorio.

## Licencia

Este repositorio se distribuye bajo una licencia personalizada de uso educativo, no
comercial y sin aprovechamiento reputacional. Puedes usarlo para estudiar y
practicar, pero no para venderlo, monetizarlo, promocionarte con el material o
presentarlo como propio.

Consulta el archivo `LICENSE` para los terminos completos.

## Resultado esperado al estudiar este repo

Si trabajas el material de forma disciplinada, deberias terminar pudiendo:

- detectar vulnerabilidades comunes leyendo codigo desconocido;
- explicar por que una entrada es explotable y que impacto real tiene;
- validar hallazgos con Burp Suite sin depender de scanners;
- proponer remediaciones concretas y contextualizadas.

## Punto de inicio recomendado

Si solo quieres empezar ya, abre en este orden:

1. La guia principal en [`docs/guia_estudio_appsec.md`](docs/guia_estudio_appsec.md)
2. [`ejercicios_sast/README.md`](ejercicios_sast/README.md)
3. [`ejercicios_docker/README.md`](ejercicios_docker/README.md)
4. [`ejercicios_numerados/README.md`](ejercicios_numerados/README.md)
