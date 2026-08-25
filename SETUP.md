# Guía de instalación y ejecución (VS Code)

## 1. Requisitos previos

- **JDK 17 o superior** instalado (probado con JDK 22).
- **Visual Studio Code** con estas extensiones (si usas el mismo perfil del equipo, ya las tienes):
  - `Extension Pack for Java` (`vscjava.vscode-java-pack`)
  - `Maven for Java` (`vscjava.vscode-maven`)
  - `Debugger for Java` (`vscjava.vscode-java-debug`)
- **Conexión a internet** la primera vez que abras el proyecto: Maven necesita descargar las dependencias (JavaFX, driver de PostgreSQL, JUnit, Postgres embebido para pruebas). No hace falta tener Maven instalado aparte — la extensión de VS Code trae uno embebido si no encuentra uno en el sistema.
- **Docker**, solo para *ejecutar la aplicación* (las pruebas unitarias NO lo necesitan, ver sección 5) — se usa para levantar PostgreSQL sin instalarlo nativo. Ver [ADR 0006](docs/adr/0006-docker-compose-para-postgresql.md) y la sección 3 para instalarlo.

No necesitas IntelliJ ni NetBeans: el proyecto es un `pom.xml` estándar, cualquier IDE con soporte Maven lo reconoce igual.

## 2. Abrir el proyecto

1. `Archivo` → `Abrir carpeta...` → selecciona `banco-preguntas-gestion-usuarios/` (la carpeta que tiene el `pom.xml`, no una carpeta superior).
2. Espera a que la barra de estado inferior termine de decir "Java: Loading..." / "Importing Maven projects" (la primera vez descarga las dependencias, puede tardar 1-2 minutos).

## 3. Instalar Docker y levantar la base de datos

### 3.1. Instalar Docker (una sola vez)

Elige una opción según lo que prefieras tener instalado:

**Opción A — Docker Desktop (más simple, recomendado si no usas Docker para nada más):**

1. Descarga e instala desde https://www.docker.com/products/docker-desktop/ (en Windows requiere WSL2;
   el instalador lo configura solo si no lo tienes).
2. Ábrelo una vez para que arranque el motor. Con eso `docker` y `docker compose` ya funcionan desde
   cualquier terminal de Windows.

**Opción B — Docker Engine headless en WSL2 (sin la app gráfica), la que usa este equipo:**

1. Asegúrate de tener una distro WSL2 (ej. `Ubuntu`): `wsl --install -d Ubuntu` si no la tienes.
2. Instala el motor dentro de la distro:
   ```bash
   wsl -d Ubuntu -- sudo apt-get update
   wsl -d Ubuntu -- sudo apt-get install -y docker.io
   wsl -d Ubuntu -- sudo systemctl enable --now docker
   ```
3. Instala el plugin de `docker compose` (no viene incluido con `docker.io`):
   ```bash
   wsl -d Ubuntu -- sudo mkdir -p /usr/local/lib/docker/cli-plugins
   wsl -d Ubuntu -- sudo curl -fsSL -o /usr/local/lib/docker/cli-plugins/docker-compose https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64
   wsl -d Ubuntu -- sudo chmod +x /usr/local/lib/docker/cli-plugins/docker-compose
   ```
4. Para poder escribir `docker ...` directo en PowerShell (sin anteponer `wsl -d Ubuntu --` cada vez),
   agrega esta función a tu perfil de PowerShell (`$PROFILE`):
   ```powershell
   function docker { wsl -d Ubuntu -- docker $args }
   ```
5. Verifica: `docker version` y `docker compose version` deberían responder sin error.

⚠️ **Sin Docker Desktop, WSL2 apaga la distro por inactividad y se lleva el contenedor con ella** (se
observó reiniciándose solo cada ~15-20 s). `vmIdleTimeout=-1` en `%USERPROFILE%\.wslconfig` ayuda pero
no lo elimina del todo. La forma confiable de evitarlo es mantener un proceso corriendo dentro de la
distro mientras trabajas en la app:
```powershell
Start-Process wsl -ArgumentList '-d','Ubuntu','--','sleep','infinity' -WindowStyle Hidden
```
Corre esto una vez por sesión de Windows (antes de `docker compose up -d` o después, da igual) — sin
esto, la conexión desde `Launcher` puede fallar con `Connection refused` de forma intermitente. Con la
**Opción A (Docker Desktop)** no aplica: la propia app mantiene su VM viva mientras esté abierta.

Con cualquiera de las dos opciones, WSL2 reenvía el puerto publicado por el contenedor a
`localhost` en Windows automáticamente — no hace falta configuración extra de red.

### 3.2. Levantar la base de datos

La app se conecta por defecto a `jdbc:postgresql://localhost:5432/banco_preguntas` con usuario
`postgres` (ver constantes al inicio de `src/main/java/bancopreguntas/conexion/UsuarioRepository.java`
si necesitas cambiarlas). El `docker-compose.yml` de la raíz del repo levanta exactamente eso, con la
base ya creada — ver [ADR 0006](docs/adr/0006-docker-compose-para-postgresql.md).

```bash
docker compose up -d
```

Con eso ya queda PostgreSQL escuchando en `localhost:5432` y la base `banco_preguntas` creada — no hace
falta pgAdmin ni `psql` a mano. Para detenerlo: `docker compose down` (agrega `-v` si además quieres
borrar los datos guardados).

La tabla `Usuario` se crea sola la primera vez que la app se conecta (`CREATE TABLE IF NOT EXISTS`).

### Alternativa sin Docker

Si prefieres instalar PostgreSQL nativo, descárgalo de https://www.postgresql.org/download/ y crea la
base manualmente:

```bash
psql -U postgres -c "CREATE DATABASE banco_preguntas;"
```

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
