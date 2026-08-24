# Guía de instalación y ejecución (VS Code)

## 1. Requisitos previos

- **JDK 17 o superior** instalado (probado con JDK 22).
- **Visual Studio Code** con estas extensiones (si usas el mismo perfil del equipo, ya las tienes):
  - `Extension Pack for Java` (`vscjava.vscode-java-pack`)
  - `Maven for Java` (`vscjava.vscode-maven`)
  - `Debugger for Java` (`vscjava.vscode-java-debug`)
- **Conexión a internet** la primera vez que abras el proyecto: Maven necesita descargar las dependencias (JavaFX, driver de PostgreSQL, JUnit, Postgres embebido para pruebas). No hace falta tener Maven instalado aparte — la extensión de VS Code trae uno embebido si no encuentra uno en el sistema.
- **PostgreSQL instalado y corriendo localmente**, solo para *ejecutar la aplicación* (las pruebas unitarias NO lo necesitan, ver sección 5). Instálalo desde https://www.postgresql.org/download/ (en Windows, el instalador oficial trae pgAdmin incluido). Durante la instalación te pedirá una contraseña para el usuario `postgres` — anótala.

No necesitas IntelliJ ni NetBeans: el proyecto es un `pom.xml` estándar, cualquier IDE con soporte Maven lo reconoce igual.

## 2. Abrir el proyecto

1. `Archivo` → `Abrir carpeta...` → selecciona `banco-preguntas-gestion-usuarios/` (la carpeta que tiene el `pom.xml`, no una carpeta superior).
2. Espera a que la barra de estado inferior termine de decir "Java: Loading..." / "Importing Maven projects" (la primera vez descarga las dependencias, puede tardar 1-2 minutos).

## 3. Crear la base de datos

La app se conecta por defecto a `jdbc:postgresql://localhost:5432/banco_preguntas` con usuario
`postgres` (ver constantes al inicio de `src/main/java/bancopreguntas/conexion/UsuarioRepository.java`
si necesitas cambiarlas). Antes de correr la app, crea esa base una sola vez, desde **pgAdmin** (se
instala junto con PostgreSQL):

1. Abre pgAdmin → expande **Servers** → tu servidor de PostgreSQL (te pedirá la contraseña que
   pusiste durante la instalación).
2. Clic derecho en **Databases** → **Create** → **Database...**
3. En el campo "Database" escribe `banco_preguntas` → **Save**.

Si prefieres terminal (`psql` no siempre queda en el PATH en Windows por defecto):

```bash
psql -U postgres -c "CREATE DATABASE banco_preguntas;"
```

La tabla `Usuario` se crea sola la primera vez que la app se conecta (`CREATE TABLE IF NOT EXISTS`).

## 4. Ejecutar la aplicación

**Importante:** el punto de entrada que debes ejecutar es **`Launcher.java`**, no `MainApp.java`.

`MainApp` extiende `javafx.application.Application`; si Java la ejecuta directamente sin que JavaFX esté en el *module-path*, lanza el error `Error: JavaFX runtime components are missing`. `Launcher` es una clase intermedia sin esa herencia que simplemente llama a `MainApp.main(args)` — evita el error y permite correr la app con el classpath normal, tal como lo hace el botón "Run" de VS Code sin configuración extra.

Pasos:

1. Abre `src/main/java/bancopreguntas/view/Launcher.java`.
2. Haz clic en **▶ Run** (aparece justo encima de `public static void main`), o `F5` para depurar.
3. Debería abrirse una ventana titulada **"Banco de Preguntas Saber Pro - Gestión de Usuarios"** con el formulario de login. Los botones todavía no hacen nada (los `TODO` en `LoginView`, `UsuarioController`, `AuthController`, etc. están pendientes de implementar) — lo que sí queda probado al abrir la ventana es que la conexión a PostgreSQL funcionó (si fallara, la app lanzaría una excepción antes de mostrar la ventana).

### Alternativa por terminal

```bash
mvn javafx:run
```

⚠️ En algunos entornos este plugin (`javafx-maven-plugin`) puede terminar con "BUILD SUCCESS" sin abrir ninguna ventana (falla silenciosa al construir el module-path internamente). Si te pasa eso, usa el botón Run sobre `Launcher.java` — es el método verificado que sí funciona.

## 5. Pruebas unitarias

Ya hay una prueba base para la capa de conexión (`src/test/java/bancopreguntas/conexion/UsuarioRepositoryTest.java`).
**No necesitas tener PostgreSQL instalado para correr las pruebas**: usan `embedded-postgres`, que
descarga un binario real de PostgreSQL una sola vez (caché local) y levanta una instancia temporal y
desechable — se destruye sola al terminar. A medida que implementen los `TODO` de `controller/`,
agreguen ahí sus propias pruebas siguiendo el mismo patrón (mismo paquete, mismo mecanismo).

```bash
mvn test
```

## 6. Cambiar host/usuario/contraseña de conexión

Si tu instalación local de PostgreSQL usa otro puerto, usuario o contraseña, edita las tres
constantes al inicio de `src/main/java/bancopreguntas/conexion/UsuarioRepository.java`:

```java
private static final String URL_POR_DEFECTO = "jdbc:postgresql://localhost:5432/banco_preguntas";
private static final String USUARIO_POR_DEFECTO = "postgres";
private static final String PASSWORD_POR_DEFECTO = "postgres";
```

## Resumen de verificación ya realizada

- `mvn compile` → `BUILD SUCCESS` con la estructura MVC actual
- `mvn test` → `UsuarioRepositoryTest` pasa (1/1), corrido contra un PostgreSQL real y desechable
- Ejecución real de `Launcher` → ventana JavaFX confirmada abriendo correctamente contra PostgreSQL local (verificado por título de ventana del proceso)
