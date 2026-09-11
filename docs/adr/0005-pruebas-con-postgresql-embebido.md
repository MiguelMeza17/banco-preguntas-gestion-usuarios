# 0005 — Pruebas con PostgreSQL embebido (embedded-postgres)

**Estado:** Reemplazado por [0007](0007-sqlite-y-eliminacion-de-docker.md)

> Se revirtió esta decisión junto con [0002](0002-postgresql-en-vez-de-sqlite.md): al volver a SQLite,
> las pruebas usan `jdbc:sqlite::memory:` directamente, sin ninguna librería adicional. El contenido de
> abajo se conserva como registro histórico.

## Contexto

La rúbrica del taller exige pruebas unitarias automatizadas sobre las clases del dominio (20% de la
nota). El motor de persistencia elegido es PostgreSQL ([ADR 0002](0002-postgresql-en-vez-de-sqlite.md)),
que —a diferencia de SQLite— no tiene un modo "en memoria" nativo. Probar la capa `conexion` contra
mocks/dobles de prueba daría menos confianza de que el código realmente funciona contra PostgreSQL; y
exigirle a cada integrante tener un servidor PostgreSQL instalado y corriendo solo para poder ejecutar
`mvn test` agrega fricción innecesaria (y sería un problema en un entorno de integración continua).

## Decisión

Usar la librería `io.zonky.test:embedded-postgres` como dependencia de prueba (`scope: test`). Antes
de cada clase de prueba levanta una instancia real de PostgreSQL, temporal y desechable (descarga el
binario una sola vez, queda en caché local), y la destruye automáticamente al terminar.

## Consecuencias

**Ventajas**
- Las pruebas corren contra el motor real, no un simulacro — más confianza en que el código
  efectivamente funciona con PostgreSQL.
- `mvn test` funciona en cualquier máquina sin necesitar PostgreSQL instalado previamente, lo que
  facilita que el compañero de equipo corra las pruebas sin configurar nada.

**Desventajas**
- Las pruebas son más lentas que con mocks (segundos en vez de milisegundos por prueba).
- La primera ejecución en una máquina nueva descarga ~40 MB (el binario de PostgreSQL); ejecuciones
  posteriores usan la caché local.
