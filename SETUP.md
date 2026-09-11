# Guía de instalación y ejecución (VS Code)

## 1. Requisitos previos

- **JDK 17 o superior**
- **Visual Studio Code** con estas extensiones:
  - `Extension Pack for Java` (`vscjava.vscode-java-pack`) — incluye soporte de lenguaje y el
    `Debugger for Java`.

El proyecto **no usa Maven ni ningún gestor de dependencias**: los `.jar` que necesita (JavaFX, driver
de SQLite, JUnit) ya están en la carpeta `lib/` del repo, y `.vscode/settings.json` le dice al Language
Support for Java dónde están. No hace falta internet para abrir el proyecto — solo para clonarlo la
primera vez.

Tampoco se necesita ningún motor de base de datos instalado ni Docker: SQLite es un solo archivo
(`banco_preguntas.db`) que la app crea sola en el directorio del proyecto la primera vez que corre.

## 2. Ejecutar la aplicación

**Importante:** el punto de entrada que debes ejecutar es **`Launcher.java`**, no `MainApp.java`.

Pasos:

1. Abre la carpeta del proyecto en VS Code (espera a que el Language Support for Java termine de
   indexar — barra de estado abajo).
2. Abre `src/main/java/bancopreguntas/view/Launcher.java`.
3. Haz clic en **▶ Run**.

Si `banco_preguntas.db` no existe, se crea automáticamente. Para reiniciar los datos desde cero, basta
con borrar ese archivo.

## 3. Correr las pruebas

Con el `Test Explorer` de VS Code (viene en `Extension Pack for Java`): abre cualquier clase en
`src/test/java` y usa los íconos ▶ que aparecen junto a cada `@Test`, o el ícono de frasco en la barra
lateral para correrlas todas.

## 4. Si agregan una librería nueva

No hay `pom.xml` que edite un gestor de dependencias por ustedes. Para agregar un `.jar` nuevo:

1. Descárguenlo (con su código fuente/javadoc si quieren, aunque no es obligatorio) y cópienlo dentro
   de `lib/`.
2. VS Code lo detecta solo gracias a `"java.project.referencedLibraries": ["lib/**/*.jar"]` en
   `.vscode/settings.json` — no hay que tocar configuración.
3. Hagan commit del `.jar` junto con el resto del cambio (por eso `lib/` sí se versiona, a diferencia
   de `.vscode/` que se ignora casi entero).
