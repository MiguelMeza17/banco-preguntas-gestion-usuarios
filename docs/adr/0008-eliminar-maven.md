# 0008 — Eliminar Maven, gestión manual de dependencias

**Estado:** Aceptado

**Reemplaza a:** [0004](0004-maven-como-build-tool.md)

## Contexto

Tras eliminar PostgreSQL y Docker ([ADR 0007](0007-sqlite-y-eliminacion-de-docker.md)), la única
dependencia externa real que le queda al proyecto es un puñado de `.jar` estables que no cambian
durante el taller: JavaFX, el driver de SQLite y JUnit. Se decidió simplificar aún más quitando también
Maven, para que el proyecto no dependa de ningún gestor de build externo — ni siquiera del que trae
empaquetado la extensión de VS Code.

## Decisión

Quitar `pom.xml` y gestionar las dependencias como archivos `.jar` versionados directamente en el
repositorio, dentro de `lib/`. VS Code (`Language Support for Java`, parte de `Extension Pack for
Java`) los reconoce vía `java.project.referencedLibraries` en `.vscode/settings.json`, que sí se
versiona (a diferencia del resto de `.vscode/`, que se ignora).

`Launcher` sigue sin extender `javafx.application.Application` directamente — ese truco (ya explicado
en su Javadoc) es justamente lo que permite lanzar la app por classpath plano, sin `--module-path` ni
`--add-modules`, así que no depende de Maven ni de ningún plugin.

## Consecuencias

**Ventajas**
- Cero dependencia de un gestor de build: `git clone` + abrir la carpeta en VS Code + ▶ Run, sin
  esperar ninguna descarga ni resolución de dependencias.
- No hay `pom.xml` que mantener sincronizado ni plugin de JavaFX que pueda romperse entre versiones
  (la fragilidad que ya mencionaba [ADR 0004](0004-maven-como-build-tool.md)).
- Funciona sin conexión a internet después del primer `clone` — ni siquiera para abrir el proyecto por
  primera vez (a diferencia de Maven, que necesitaba internet la primera vez para bajar todo).

**Desventajas**
- El repositorio pesa más: los `.jar` en `lib/` (JavaFX + SQLite + JUnit) suman ~25 MB versionados en
  Git, en vez de resolverse bajo demanda desde un repositorio remoto.
- Agregar o actualizar una dependencia es manual (descargar el `.jar` y copiarlo a `lib/`, ver
  `SETUP.md`), sin resolución automática de dependencias transitivas — hay que copiar también las
  dependencias de la dependencia (p. ej. JUnit necesitó copiar aparte `opentest4j` y `apiguardian-api`).
- Sin `mvn test` como comando único y estándar: correr las pruebas depende de que el IDE tenga el Test
  Runner de Java, no hay una forma de línea de comandos tan directa como antes.
- Menos representativo de cómo se maneja un proyecto Java real en la industria, donde un gestor de
  dependencias (Maven/Gradle) es prácticamente universal — aceptable para el alcance de este taller.
