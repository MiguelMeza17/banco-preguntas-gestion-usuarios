# 0007 — Volver a SQLite y eliminar Docker

**Estado:** Aceptado

**Reemplaza a:** [0002](0002-postgresql-en-vez-de-sqlite.md), [0005](0005-pruebas-con-postgresql-embebido.md),
[0006](0006-docker-compose-para-postgresql.md)

## Contexto

La guía oficial del Taller 2 pide SQLite explícitamente (ver [ADR 0002](0002-postgresql-en-vez-de-sqlite.md)).
Se había optado por PostgreSQL por consistencia con el proyecto de curso completo y para que un
integrante practicara ese motor, autorizado por la docente. En la práctica, esa elección trajo fricción
sostenida documentada en las ADR reemplazadas:

- Cada integrante necesitaba un servidor PostgreSQL local, lo que llevó a instalar Docker Compose
  ([ADR 0006](0006-docker-compose-para-postgresql.md)) como solución.
- El motor Docker headless en WSL2 apagaba el contenedor por inactividad, requiriendo mantener un
  proceso vivo (`wsl -d Ubuntu -- sleep infinity`) mientras se trabajaba — una carga operativa constante
  para un taller universitario.
- Las pruebas necesitaron una dependencia extra (`embedded-postgres`, [ADR 0005](0005-pruebas-con-postgresql-embebido.md))
  solo para simular lo que SQLite en memoria da gratis.

Ninguna de estas ventajas (practicar PostgreSQL, consistencia con Actividad 1) compensaba ya el costo
de day-to-day para este taller específico.

## Decisión

Volver a **SQLite** como motor de persistencia (un solo archivo `banco_preguntas.db`, autogenerado por
la app) y **eliminar Docker y Docker Compose** del proyecto por completo: no queda ningún servicio que
levantar, así que `docker-compose.yml` sobra.

Cambios concretos:
- `pom.xml`: driver `org.xerial:sqlite-jdbc` en vez de `org.postgresql:postgresql` +
  `io.zonky.test:embedded-postgres`.
- `UsuarioRepository`: URL `jdbc:sqlite:banco_preguntas.db` (sin usuario/contraseña), `SERIAL` →
  `INTEGER ... AUTOINCREMENT`.
- `Launcher`: ya no ejecuta `docker compose up -d --wait` vía WSL antes de abrir la ventana — arranca
  la UI directamente.
- Pruebas (`UsuarioRepositoryTest`, `AuthFlowTest`): usan `jdbc:sqlite::memory:`, sin librerías extra.
- `docker-compose.yml` eliminado; `SETUP.md` ya no tiene la sección de instalación de WSL/Docker.

## Consecuencias

**Ventajas**
- Cero requisitos previos de infraestructura: no hace falta instalar WSL, Docker Engine ni configurar
  un servidor — solo JDK 17 y Maven.
- `git clone` + **▶ Run** funciona en cualquier máquina de inmediato, sin pasos de administrador de
  PowerShell.
- Pruebas más simples y rápidas (SQLite en memoria es instantáneo, sin descargar binarios ni levantar
  procesos externos).
- Alineado con lo que pedía la guía del taller desde el inicio.
- Elimina por completo la clase de bug que motivó [ADR 0006](0006-docker-compose-para-postgresql.md)
  (WSL2 apagando el contenedor por inactividad).

**Desventajas**
- Se pierde la práctica de PostgreSQL que uno de los integrantes buscaba (mitigado: sigue disponible en
  el proyecto de curso completo, Actividad 1, que no se ve afectado por este cambio).
- SQLite es un motor embebido de un solo archivo, menos representativo de un entorno cliente-servidor
  de producción que PostgreSQL — aceptable para el alcance de este taller.
- El archivo `banco_preguntas.db` es local a cada máquina/checkout (no se versiona, ver `.gitignore`):
  cada integrante tiene sus propios datos de prueba, no una base compartida.
