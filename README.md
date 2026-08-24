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

## Estado actual

El **esqueleto completo del proyecto ya existe y está conectado** (todas las clases, con sus
constructores e inyección de dependencias armados), pero la lógica de negocio dentro de cada método
está marcada con `// TODO` y todavía hay que implementarla: validación de contraseñas, cifrado,
registro, autenticación y menús por rol. La UI (login, registro, dashboard) ya recibe los servicios
por constructor, pero los botones aún no hacen nada. Ideal para dividir el trabajo en pareja: cada
quien puede tomar un paquete y llenar los `TODO` sin tener que tocar el resto de la conexión.

## Arquitectura

Modelo-Vista-Controlador clásico. Dentro de `controller`, el acceso a datos sigue el patrón del
**ejemplo 5 (Principio de Inversión de Dependencias)** visto en la teoría: los controladores dependen
de una interfaz de repositorio (`IUsuarioRepository`), no de la implementación concreta, y una fábrica
(`Factory`) se encarga de instanciarla.

```
src/main/java/bancopreguntas/
  model/
    Usuario.java, Rol.java, EstadoUsuario.java   # entidad y enums
    exception/                    # excepciones de dominio — completas, no requieren TODO
  conexion/
    IUsuarioRepository.java       # contrato (abstracción)
    UsuarioRepository.java        # implementación concreta (PostgreSQL) — ya funciona
    Factory.java                  # fábrica que instancia el repositorio — ya funciona
  controller/
    UsuarioController.java        # registro de usuarios — TODO
    AuthController.java           # autenticación — TODO
    security/
      IPasswordEncoder.java, Sha256PasswordEncoder.java   # cifrado de contraseñas — TODO
    validation/
      IPasswordRule.java, PasswordValidator.java, rules/  # política de contraseñas — TODO
    menu/
      IMenuRol.java, Menu*.java, MenuFactory.java          # menú según el rol — TODO (opciones)
  view/
    MainApp.java                  # arranque JavaFX (composition root): arma toda la conexión — ya funciona
    Launcher.java                 # punto de entrada real (ver SETUP.md)
    LoginView.java, RegistroUsuarioView.java, DashboardView.java   # reciben los controllers, botones sin conectar — TODO

src/test/java/bancopreguntas/
  conexion/
    UsuarioRepositoryTest.java    # prueba de la capa de conexión (PostgreSQL desechable de prueba)
```

## Stack tecnológico

- Java
- JavaFX (interfaz gráfica)
- Maven (gestión de dependencias y build)
- PostgreSQL (persistencia)

## Cómo compilar y ejecutar

Guía completa (requisitos, extensiones de VS Code, cómo correr y solución del error típico de
JavaFX) en **[SETUP.md](SETUP.md)**.

Resumen rápido:

```bash
mvn clean compile
```

**El archivo que se ejecuta (al que le das ▶ Run en VS Code) es:**
`src/main/java/bancopreguntas/view/Launcher.java` — **no** `MainApp.java` (ver por qué en [SETUP.md](SETUP.md#4-ejecutar-la-aplicación)).

## Decisiones de arquitectura (ADR)

El porqué de cada elección tecnológica (Java+JavaFX, PostgreSQL en vez de SQLite, la estructura MVC
por capas, Maven, pruebas con PostgreSQL embebido) está documentado en **[docs/adr/](docs/adr/)** —
útil para responder preguntas del docente en la sustentación.

## Flujo de trabajo en equipo (Git)

Antes de ponerse a picar código, lean **[RAMAS.md](RAMAS.md)** — explica cómo usar ramas para que
los dos trabajen en paralelo sin pisarse (una rama por TODO/paquete, Pull Request hacia `main`,
nunca commitear directo a `main`).

## Principios SOLID aplicados

| Principio | Dónde se aplica |
|---|---|
| SRP (Responsabilidad única) | *(completar durante el desarrollo)* |
| OCP (Abierto/cerrado) | *(completar durante el desarrollo)* |
| LSP (Sustitución de Liskov) | *(completar durante el desarrollo)* |
| ISP (Segregación de interfaces) | *(completar durante el desarrollo)* |
| DIP (Inversión de dependencias) | Los controllers dependen de `IUsuarioRepository`, no de `UsuarioRepository`; `Factory` desacopla la creación de la implementación concreta. |

## Integrantes

- *(Miguel Angel Meza Lopez)*
- *(Oscar Alejandro Menza Chaviznan)*

## Curso

Laboratorio de Ingeniería de Software II — Grupo B
Docente: Paola Andrea Bedoya Toro
