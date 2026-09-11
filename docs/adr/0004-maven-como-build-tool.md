# 0004 — Maven como gestor de dependencias y build

**Estado:** Reemplazado por [0008](0008-eliminar-maven.md)

> Se revirtió esta decisión: el proyecto dejó de usar Maven (ni ningún otro gestor de build) y pasó a
> gestionar los `.jar` a mano en `lib/`. El contenido de abajo se conserva como registro histórico de
> por qué se eligió Maven en su momento.

## Contexto

El proyecto necesita gestionar dependencias externas (JavaFX, driver JDBC de SQLite, JUnit) y un
proceso de compilación/ejecución reproducible entre los dos integrantes, que probablemente usan
configuraciones de máquina distintas.

## Decisión

Usar **Apache Maven** con un `pom.xml` estándar como única fuente de verdad de dependencias y build,
en vez de gestionar los `.jar` manualmente o depender de configuración específica de un IDE.

## Consecuencias

**Ventajas**
- Cualquier IDE con soporte Maven (VS Code, IntelliJ, NetBeans) reconoce el proyecto igual, sin
  reconfigurar classpath a mano en cada máquina.
- Facilita agregar pruebas automatizadas (`mvn test`) y plugins de build sin tocar la configuración
  del IDE.
- No es necesario tener Maven instalado en el sistema para trabajar en VS Code: la extensión
  `vscjava.vscode-maven` trae uno embebido.

**Desventajas**
- Requiere conexión a internet la primera vez que se abre el proyecto (para descargar las
  dependencias desde Maven Central).
- El plugin `javafx-maven-plugin` (necesario para `mvn javafx:run`) resultó frágil en algunos entornos
  (ver [ADR 0001](0001-java-javafx-maven.md)) — mitigado ejecutando la app directamente desde VS Code
  en vez de depender exclusivamente del plugin.
