# 0001 — Java + JavaFX puro (sin FXML) + Maven

**Estado:** Aceptado

## Contexto

La guía del Taller 2 exige explícitamente una "aplicación de escritorio monolítica en Java". Además,
esta práctica hace parte del proyecto de curso más grande (Sistema de Gestión de Banco de Preguntas
Saber Pro), cuya Actividad 1 ya había definido el stack de front-end: JavaFX programado en código
Java puro (sin FXML ni Scene Builder), con Maven como herramienta de build, para que todo el equipo
trabaje con el mismo editor (VS Code + Extension Pack for Java) tanto en front-end como en back-end.

## Decisión

Usar JavaFX puro (clases Java que construyen la UI con código, sin archivos `.fxml`), gestionado con
Maven (`javafx-controls` como dependencia + `javafx-maven-plugin` para poder correr con `mvn javafx:run`).

## Consecuencias

**Ventajas**
- Un solo editor y build tool para todo el proyecto (front y back), sin depender de un IDE
  específico como NetBeans o IntelliJ con Scene Builder.
- Multiplataforma sin fricción adicional.
- Cumple la sugerencia tecnológica del curso (Java, Swing/JavaFX).

**Desventajas**
- Construir la UI a mano en Java es más verboso que con FXML (no hay editor visual de
  arrastrar-y-soltar).
- `javafx-maven-plugin` resultó poco confiable en la práctica: en algunas ejecuciones terminaba en
  "BUILD SUCCESS" sin abrir ninguna ventana. Se solucionó agregando una clase `Launcher` (sin
  herencia de `Application`) como punto de entrada real, que sí funciona de forma consistente tanto
  desde Maven como desde el botón "Run" de VS Code (ver `SETUP.md`).
