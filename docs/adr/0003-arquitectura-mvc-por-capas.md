# 0003 — Arquitectura MVC por capas (model/conexion/controller/view)

**Estado:** Aceptado

## Contexto

La guía del taller exige que el diseño "se base en la estructura del ejemplo 5 de Inversión de
Dependencias" visto en la teoría (entidad → interfaz de repositorio → implementación concreta →
fábrica → servicio inyectado por constructor), pero **no exige nombres de paquete específicos** — el
ejemplo de clase simplemente anidaba todo bajo un paquete `domain` (`domain.access`,
`domain.service`, `domain.main`).

La primera versión de este proyecto replicó esa convención (`domain/access`, `domain/service`,
`domain/security`, `domain/validation`, `domain/menu`, `domain/exception`, `ui/`), pero mezclar tantas
responsabilidades distintas bajo un mismo paquete "domain" resultaba confuso para dividir el trabajo
en pareja y para explicar la estructura.

## Decisión

Reorganizar el código en capas explícitas de **Modelo-Vista-Controlador clásico**, sin el prefijo de
paquete de la universidad:

```
bancopreguntas/
  model/        entidades (Usuario, Rol, EstadoUsuario) y excepciones de dominio
  conexion/     acceso a datos: IUsuarioRepository, UsuarioRepository, Factory
  controller/   lógica de negocio: UsuarioController, AuthController, y sub-paquetes
                security/, validation/, menu/
  view/         pantallas JavaFX: MainApp, Launcher, LoginView, RegistroUsuarioView, DashboardView
```

El patrón de Inversión de Dependencias que pide la guía se mantiene íntegro **dentro de `conexion`**:
los controllers dependen de la interfaz `IUsuarioRepository`, nunca de `UsuarioRepository`
directamente, y `Factory` es quien decide la implementación concreta — exactamente el mismo mecanismo
del ejemplo 5, solo que ubicado en un paquete con nombre más descriptivo.

## Consecuencias

**Ventajas**
- Estructura fácil de reconocer para cualquiera con nociones de MVC (patrón muy enseñado, no exclusivo
  de este curso).
- Facilita dividir el trabajo en pareja: cada quien puede tomar un paquete completo (p. ej. uno
  `controller/validation` + `controller/security`, el otro `controller` + `view`) sin pisarse archivos.
- El patrón DIP exigido por la guía sigue intacto y localizado en un solo lugar (`conexion`), fácil de
  señalar en la sustentación.

**Desventajas**
- Los nombres de paquete ya no calcan literalmente al ejemplo de clase (`domain.access` →
  `conexion`, `domain.service` → `controller`) — hay que estar preparados para explicarle al docente
  que el patrón (interfaz + implementación + fábrica + inyección) es el mismo, solo reorganizado por
  capas con nombres más claros.
- `UsuarioService`/`AuthService` se renombraron a `UsuarioController`/`AuthController` para encajar
  con la nomenclatura MVC; cualquier material de apoyo previo que use el nombre "Service" ya no
  coincide literalmente con el código.
