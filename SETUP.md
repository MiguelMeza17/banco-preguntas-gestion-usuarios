# Guía de instalación y ejecución (VS Code)

## 1. Requisitos previos

- **JDK 17 o superior**
- **Visual Studio Code** con estas extensiones:
  - `Extension Pack for Java` (`vscjava.vscode-java-pack`)
  - `Maven for Java` (`vscjava.vscode-maven`)
  - `Debugger for Java` (`vscjava.vscode-java-debug`)
- **Conexión a internet** la primera vez que abras el proyecto: Maven necesita descargar las dependencias (JavaFX, driver de PostgreSQL, JUnit, Postgres embebido para pruebas). No hace falta tener Maven instalado aparte — la extensión de VS Code trae uno embebido si no encuentra uno en el sistema.

## 2. Instalar Docker (motor headless en WSL2)

Este proyecto necesita una base de datos PostgreSQL corriendo en Docker. Sigue estos pasos en orden, en PowerShell.

**Importante:** abre PowerShell **como administrador** para los pasos 1 a 4 (clic derecho sobre el ícono de PowerShell → "Ejecutar como administrador"). Sin permisos de administrador, `wsl --install` y la instalación de Docker dentro de la distro fallarán. Los pasos 5 en adelante (y el uso diario de `docker ...`) sí puedes ejecutarlos en una terminal normal, sin administrador.

1. Instalar la distro WSL2 Ubuntu:
   ```powershell
   wsl --install -d Ubuntu
   ```
   Si te pide reiniciar el PC, hazlo. Al abrir Ubuntu por primera vez, te pedirá crear un usuario y contraseña (son para Linux, no para Windows).

2. Instalar Docker Engine dentro de la distro:(Ejecutar en orden)
   ```powershell
   wsl -d Ubuntu -- sudo apt-get update
   wsl -d Ubuntu -- sudo apt-get install -y docker.io
   ```

3. Activar el servicio Docker para que arranque solo:
   ```powershell
   wsl -d Ubuntu -- sudo systemctl enable --now docker
   ```

4. Instalar el plugin `docker compose` : (Ejecutar en orden)
   ```powershell
   wsl -d Ubuntu -- sudo mkdir -p /usr/local/lib/docker/cli-plugins
   wsl -d Ubuntu -- sudo curl -fsSL -o /usr/local/lib/docker/cli-plugins/docker-compose https://github.com/docker/compose/releases/latest/download/docker-compose-linux-x86_64
   wsl -d Ubuntu -- sudo chmod +x /usr/local/lib/docker/cli-plugins/docker-compose
   ```

5. Para poder escribir `docker ...` directo en PowerShell (sin anteponer `wsl -d Ubuntu --` cada vez):
   ```powershell
   notepad $PROFILE
   ```
   Agrega esta línea al archivo que se abre, guarda y cierra:
   function docker { wsl -d Ubuntu -- docker $args }
   ```
   Luego recarga el perfil en la terminal actual:
   ```powershell
   . $PROFILE
   ```

6. Verificar que quedó bien instalado:
   ```powershell
   docker version
   docker compose version
   ```
   Ambos deben responder sin error.

**Nota:** los pasos 1-5 solo se hacen una vez. Si reinicias el PC, basta con abrir una terminal y ejecutar cualquier comando `docker ...` — Windows arranca WSL automáticamente y el servicio Docker ya quedó configurado para iniciar solo (paso 3).

## 3. Ejecutar la aplicación

**Importante:** el punto de entrada que debes ejecutar es **`Launcher.java`**, no `MainApp.java`.

`Launcher` levanta solo el contenedor de PostgreSQL (`docker compose up -d --wait`, definido en `docker-compose.yml`) y espera a que esté sano antes de abrir la ventana — no hace falta correr `docker compose up -d` a mano ni antes ni después de cada `▶ Run`, incluso si cerraste la app, dormiste o reiniciaste el PC.

Pasos:

1. Abre `src/main/java/bancopreguntas/view/Launcher.java`.
2. Haz clic en **▶ Run**.

Si algo falla al levantar la base de datos, verás un mensaje claro (no un stack trace) indicando que revises Docker/WSL. Para revisar el estado del contenedor a mano en cualquier momento:
```powershell
docker compose ps
```
