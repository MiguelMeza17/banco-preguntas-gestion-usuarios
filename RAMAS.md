# Flujo de trabajo con ramas (Git/GitHub)

Guía corta para que los dos trabajen sin pisarse el código ni romper lo que ya funciona.

## Regla de oro

**Nunca se trabaja directo sobre `main`.** `main` siempre debe quedar en un estado que compile y
corra — es lo que vas a mostrar en la sustentación. Todo cambio nuevo va primero en su propia rama.

## Estructura de ramas

- **`main`** — única rama estable. Solo se actualiza recibiendo Pull Requests ya revisados.
- **`feature/<algo-corto>`** — una rama por tarea/TODO que estés implementando. Vive poco tiempo:
  se crea, se trabaja, se sube, se fusiona a `main` y se borra.

El proyecto está organizado por capas (`model`, `conexion`, `controller`, `view`), así que cada uno
puede tomar una capa o una vista y trabajar en su propia rama sin tocar los archivos del otro.
Ejemplos de nombres:

- `feature/registro-usuario` (`UsuarioController`)
- `feature/autenticacion` (`AuthController`)
- `feature/conectar-login-view`
- `feature/pruebas-auth-flow`

## Flujo paso a paso

1. **Antes de empezar algo nuevo**, asegúrate de tener el `main` más reciente:
   ```bash
   git checkout main
   git pull origin main
   ```

2. **Crea tu rama** desde ese `main` actualizado:
   ```bash
   git checkout -b feature/registro-usuario
   ```

3. **Trabaja y haz commits pequeños y descriptivos** (mejor varios commits chicos que uno gigante):
   ```bash
   git add src/main/java/bancopreguntas/controller/UsuarioController.java
   git commit -m "Validar la contraseña antes de registrar el usuario"
   ```

4. **Sube tu rama** a GitHub:
   ```bash
   git push -u origin feature/registro-usuario
   ```

5. **Abre un Pull Request** en GitHub: de tu rama hacia `main`. En la descripción escribe qué
   TODO(s) resolviste.

6. **El otro integrante revisa el PR** (aunque sea rápido, con comentarios o un simple "se ve
   bien") antes de aprobarlo — así los dos conocen todo el código para la sustentación individual.

7. **Fusiona el PR** (botón "Merge pull request" en GitHub) y **borra la rama** (GitHub te lo
   ofrece justo después de fusionar).

8. **Actualiza tu `main` local** antes de crear la siguiente rama:
   ```bash
   git checkout main
   git pull origin main
   ```

## Si hay conflictos

Si GitHub avisa que el PR tiene conflictos con `main`, en tu rama local:

```bash
git checkout feature/tu-rama
git pull origin main
# resuelves los conflictos en los archivos marcados
git add <archivos-resueltos>
git commit
git push
```

## Antes de la sustentación

Revisen juntos que `main` tenga todo fusionado, compile sin errores en VS Code y corra
(`Launcher.java`) — es lo que van a mostrar al docente.
