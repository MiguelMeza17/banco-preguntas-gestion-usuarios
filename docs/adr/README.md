# Registro de Decisiones de Arquitectura (ADR)

Cada archivo documenta una decisión técnica importante del proyecto: qué se decidió, por qué, y qué
ventajas/desventajas trae. Útil para la sustentación — si el docente pregunta "¿por qué eligieron X?",
la respuesta está aquí.

| # | Decisión | Estado |
|---|---|---|
| [0001](0001-java-javafx-maven.md) | Java + JavaFX puro (sin FXML) + Maven | Aceptado |
| [0002](0002-postgresql-en-vez-de-sqlite.md) | PostgreSQL en vez de SQLite | Aceptado |
| [0003](0003-arquitectura-mvc-por-capas.md) | Arquitectura MVC por capas (model/conexion/controller/view) | Aceptado |
| [0004](0004-maven-como-build-tool.md) | Maven como gestor de dependencias y build | Aceptado |
| [0005](0005-pruebas-con-postgresql-embebido.md) | Pruebas con PostgreSQL embebido (embedded-postgres) | Aceptado |
| [0006](0006-docker-compose-para-postgresql.md) | Docker Compose para levantar PostgreSQL | Aceptado |

## Formato

Cada ADR sigue esta estructura corta:

- **Contexto**: qué problema o restricción llevó a tomar la decisión.
- **Decisión**: qué se decidió hacer.
- **Consecuencias**: ventajas y desventajas de esa decisión (ninguna decisión es gratis).
