# Gestión de Usuarios — Banco de Preguntas Saber Pro

Taller 2 — Principios de Diseño SOLID
Laboratorio de Ingeniería de Software II · Universidad del Cauca · Periodo 2026-2

## Contexto

Esta práctica implementa el requerimiento **"Gestión de usuarios del sistema"** del proyecto de curso
*Sistema para la Gestión, Validación y Administración de un Banco de Preguntas para la Preparación de
las Pruebas Saber Pro*. Aplica los principios SOLID para lograr un diseño legible y modificable.

## Roles soportados

- Administrador
- Autor de preguntas
- Revisor
- Docente
- Estudiante

Cada usuario tiene: nombre de usuario (login), nombre completo, rol, estado (Activo/Inactivo) y contraseña.

## Arquitectura

Modelo-Vista-Controlador clásico, sin capas de abstracción extra: cada clase tiene una
responsabilidad clara, pero se llaman directamente entre sí (ver [ADR 0009](docs/adr/0009-simplificar-a-mvc-plano.md)
sobre por qué se dejó así en vez de con interfaces/factories).

```
src/main/java/bancopreguntas/
  model/
    Usuario.java, Rol.java, EstadoUsuario.java   # entidad y enums
  conexion/
    UsuarioRepository.java        # acceso a datos (SQLite)
  controller/
    UsuarioController.java        # registro de usuarios, validación de contraseña, listado
    AuthController.java           # autenticación (login)
    PasswordUtil.java             # cifrado y verificación de contraseñas (SHA-256 + sal)
  view/
    MainApp.java                  # arranque JavaFX: crea el repositorio y los controllers
    Launcher.java                 # punto de entrada real (ver SETUP.md)
    LoginView.java, RegistroUsuarioView.java, DashboardView.java

src/test/java/bancopreguntas/
  conexion/
    UsuarioRepositoryTest.java    # prueba de la capa de conexión (SQLite en memoria)
  controller/
    AuthFlowTest.java             # registro y login (HU-05, HU-06)

lib/                             # dependencias como .jar (sin gestor de build, ver ADR 0008)
.vscode/settings.json            # le dice a VS Code dónde están los .jar de lib/
```

## Stack tecnológico

- Java (sin Maven/Gradle — dependencias como `.jar` en `lib/`, ver [ADR 0008](docs/adr/0008-eliminar-maven.md))
- JavaFX (interfaz gráfica)
- SQLite (persistencia, archivo `banco_preguntas.db`)

## Cómo compilar y ejecutar

Guía completa (requisitos, extensiones de VS Code, cómo correr y cómo agregar una librería nueva) en
**[SETUP.md](SETUP.md)**.

**El archivo que se ejecuta (al que le das ▶ Run en VS Code) es:**
`src/main/java/bancopreguntas/view/Launcher.java` — **no** `MainApp.java` (ver por qué en [SETUP.md](SETUP.md#2-ejecutar-la-aplicación)).

## Decisiones de arquitectura (ADR)

El porqué de cada elección tecnológica (Java+JavaFX, SQLite, la estructura MVC por capas, sin Maven,
pruebas con SQLite en memoria) está documentado en **[docs/adr/](docs/adr/)** — útil para responder
preguntas del docente en la sustentación.

## Reglas de negocio

- La contraseña debe tener mínimo 6 caracteres, un dígito, una mayúscula y un carácter especial.
- No se puede registrar dos veces el mismo login.
- Un usuario en estado `INACTIVO` no puede iniciar sesión, aunque la contraseña sea correcta.
- La contraseña nunca se guarda en texto plano: se cifra con SHA-256 + una sal aleatoria por usuario
  (ver `PasswordUtil`).

## Épicas e historias de usuario

Las épicas e historias de usuario específicas del módulo de Gestión de usuarios (registro, login,
listado, activar/desactivar), con sus criterios de aceptación, están en
**[docs/EPICAS_HISTORIAS_USUARIO.md](docs/EPICAS_HISTORIAS_USUARIO.md)**.

## Flujo de trabajo en equipo (Git)

Antes de ponerse a picar código, lean **[RAMAS.md](RAMAS.md)** — explica cómo usar ramas para que
los dos trabajen en paralelo sin pisarse (una rama por TODO/paquete, Pull Request hacia `main`,
nunca commitear directo a `main`).

## Sobre los principios SOLID

La idea original era aplicar los cinco principios con interfaces, una fábrica para el repositorio y un
Strategy para las reglas de contraseña, siguiendo el ejemplo de Inversión de Dependencias visto en
teoría. Al implementarlo nos dimos cuenta de que esa cantidad de capas era más difícil de sostener y de
explicar en la sustentación que el beneficio real que aportaba para el alcance de este taller, así que
la simplificamos. Nos quedamos con lo esencial: separar el modelo, la vista, el controlador y el acceso
a datos en clases distintas (SRP), y dejar la validación de contraseña y el cifrado en sus propios
métodos/clase (`PasswordUtil`) en vez de mezclarlos con la lógica de registro. El detalle de esta
decisión está en [ADR 0009](docs/adr/0009-simplificar-a-mvc-plano.md).

## Integrantes

- *(Miguel Angel Meza Lopez)*
- *(Oscar Alejandro Menza Chaviznan)*

## Curso

Laboratorio de Ingeniería de Software II — Grupo B
Docente: Paola Andrea Bedoya Toro
