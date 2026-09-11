# 0006 — Docker Compose para levantar PostgreSQL

**Estado:** Reemplazado por [0007](0007-sqlite-y-eliminacion-de-docker.md)

> Se revirtió esta decisión junto con [0002](0002-postgresql-en-vez-de-sqlite.md): al volver a SQLite
> no hay ningún servidor que levantar, así que Docker Compose se eliminó del proyecto por completo. El
> contenido de abajo (incluyendo los problemas reales que causó WSL2 apagando el contenedor) se
> conserva como registro histórico.

## Contexto

El [ADR 0002](0002-postgresql-en-vez-de-sqlite.md) ya advertía la principal desventaja de usar
PostgreSQL en vez de SQLite: cada integrante debe instalar y configurar un servidor PostgreSQL local
(crear la base `banco_preguntas`, usuario y contraseña) solo para poder *ejecutar* la aplicación (las
pruebas ya no tenían este problema, ver [ADR 0005](0005-pruebas-con-postgresql-embebido.md)). Esa
fricción de puesta en marcha se confirmó en la práctica: una instalación nativa de PostgreSQL dejó
residuos en el sistema (`C:\Program Files\PostgreSQL\18\data` sin binarios ni servicio asociado) que
hubo que limpiar manualmente.

## Decisión

Usar **Docker Compose** (`docker-compose.yml` en la raíz del repo) para levantar PostgreSQL como
contenedor desechable, en vez de exigir una instalación nativa del motor. El servicio expone el puerto
`5432` y crea la base `banco_preguntas` con usuario/contraseña `postgres`/`postgres` automáticamente
vía variables de entorno — coincide exactamente con las constantes por defecto en
`src/main/java/bancopreguntas/conexion/UsuarioRepository.java`, así que la app no necesita ningún
cambio de código.

El motor Docker en sí se ejecuta como **Docker Engine headless dentro de WSL2** (distro `Ubuntu`), no
mediante la aplicación de escritorio Docker Desktop — se descartó por no ser necesaria para este caso
de uso (no se requiere la GUI, solo el motor accesible vía `docker`/`docker compose` desde una terminal
en Windows).

```bash
docker compose up -d      # levanta PostgreSQL en localhost:5432
docker compose down       # lo detiene (con -v además borra los datos)
```

## Consecuencias

**Ventajas**
- Puesta en marcha reproducible con un solo comando (`docker compose up -d`), sin instalar PostgreSQL
  como servicio de Windows ni configurar pgAdmin.
- La base y las credenciales se crean solas la primera vez (variables `POSTGRES_DB`, `POSTGRES_USER`,
  `POSTGRES_PASSWORD` del `docker-compose.yml`), en vez de crearlas a mano como describía `SETUP.md`.
- Limpieza trivial: `docker compose down -v` elimina el contenedor y el volumen sin dejar residuos en
  el sistema (a diferencia de una instalación nativa desinstalada a medias).
- Los datos persisten entre reinicios del contenedor gracias al volumen nombrado
  `banco-preguntas-db-data`, aunque el contenedor se recree.

**Desventajas**
- Agrega una dependencia externa más (Docker) a los requisitos previos del proyecto, junto a JDK 17 y
  Maven.
- El contenedor no arranca solo si Windows se reinicia y la distro WSL no se activa por sí sola; hay
  que correr `docker compose up -d` de nuevo (mitigado con `restart: unless-stopped` en el compose, que
  lo revive automáticamente en cuanto el motor Docker vuelve a estar activo).
- **Sin Docker Desktop específicamente** (motor headless en WSL2): WSL2 apaga la distro por inactividad
  y se lleva el contenedor con ella — se observó reiniciándose solo cada ~15-20 s cuando no había
  ningún proceso manteniendo la distro "despierta". `vmIdleTimeout=-1` en `.wslconfig` alarga el tiempo
  pero no lo elimina; la mitigación real es mantener un proceso vivo en la distro
  (`wsl -d Ubuntu -- sleep infinity`) mientras se trabaja — ver `SETUP.md` sección 3.1. Con Docker
  Desktop este problema no existe, porque la propia app gestiona el ciclo de vida de su VM.
- Sin la GUI de Docker Desktop, inspeccionar contenedores/volúmenes requiere la terminal
  (`docker ps`, `docker compose logs`) en vez de una ventana gráfica.
