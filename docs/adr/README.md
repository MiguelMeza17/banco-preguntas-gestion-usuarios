# Registro de Decisiones de Arquitectura (ADR)

Cada archivo documenta una decisión técnica importante del proyecto: qué se decidió, por qué, y qué
ventajas/desventajas trae. Útil para la sustentación — si el docente pregunta "¿por qué eligieron X?",
la respuesta está aquí.

| # | Decisión | Estado |
|---|---|---|
| [0001](0001-java-javafx-maven.md) | Java + JavaFX puro (sin FXML) + Maven | Parcial (Maven reemplazado por 0008) |
| [0002](0002-postgresql-en-vez-de-sqlite.md) | PostgreSQL en vez de SQLite | Reemplazado por 0007 |
| [0003](0003-arquitectura-mvc-por-capas.md) | Arquitectura MVC por capas (model/conexion/controller/view) | Parcial (interfaces/factories reemplazadas por 0009) |
| [0004](0004-maven-como-build-tool.md) | Maven como gestor de dependencias y build | Reemplazado por 0008 |
| [0005](0005-pruebas-con-postgresql-embebido.md) | Pruebas con PostgreSQL embebido (embedded-postgres) | Reemplazado por 0007 |
| [0006](0006-docker-compose-para-postgresql.md) | Docker Compose para levantar PostgreSQL | Reemplazado por 0007 |
| [0007](0007-sqlite-y-eliminacion-de-docker.md) | Volver a SQLite y eliminar Docker | Aceptado |
| [0008](0008-eliminar-maven.md) | Eliminar Maven, gestión manual de dependencias | Aceptado |
| [0009](0009-simplificar-a-mvc-plano.md) | Simplificar a MVC plano, sin interfaces ni factories | Aceptado |

## Formato

Cada ADR sigue esta estructura corta:

- **Contexto**: qué problema o restricción llevó a tomar la decisión.
- **Decisión**: qué se decidió hacer.
- **Consecuencias**: ventajas y desventajas de esa decisión (ninguna decisión es gratis).
