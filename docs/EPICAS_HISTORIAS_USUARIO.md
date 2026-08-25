# Épicas e historias de usuario — Gestión de usuarios (Taller 2 SOLID)

Las épicas e historias de usuario de la Actividad 1 del curso teórico (`Prototipo 1.docx`,
HE-01/HE-02) cubren el módulo de **Banco de Preguntas** (crear preguntas, flujo de revisión).
Ninguna cubre el módulo de **Gestión de usuarios**, que es el alcance de este repositorio. Estas
son las épicas e historias correspondientes a lo que sí se implementa aquí, con el mismo formato
de la plantilla del curso (HE ID/Título/Descripción, HU Rol/Funcionalidad/Razón + criterios de
aceptación Contexto-Evento-Resultado). Están alineadas 1 a 1 con `docs/REGLAS_NEGOCIO.md` — cada
criterio de aceptación corresponde a una rama de lógica que hay que implementar en un `// TODO`.

## 1. Historias épicas

| HE ID | Título | Descripción |
|---|---|---|
| HE-03 | Registro y autenticación de usuarios | El sistema debe permitir que una persona se registre con un rol del sistema (Administrador, Autor de preguntas, Revisor, Docente, Estudiante) y que un usuario registrado inicie sesión de forma segura para acceder a las opciones de su rol. |
| HE-04 | Administración del ciclo de vida de usuarios | El sistema debe permitir consultar el listado de usuarios registrados y activar/desactivar su acceso, sin eliminar su registro. |

*(Numeradas HE-03/HE-04 para no chocar con HE-01/HE-02 del módulo de Banco de Preguntas.)*

## 2. Historias de usuario y criterios de aceptación

### HU-05 (HE-03) — Yo como persona interesada en usar el sistema

**Enunciado:** Yo como persona interesada en usar el sistema necesito registrarme indicando mi
login, nombre completo, rol y contraseña, para poder acceder a las funcionalidades de mi rol.

**Contexto adicional:** El login es único en todo el sistema. La contraseña debe cumplir la
política de seguridad (mínimo 6 caracteres, un dígito, una mayúscula y un carácter especial —
ver `docs/REGLAS_NEGOCIO.md` §3). Todo usuario nace en estado Activo.

| # | Criterio de aceptación | Contexto | Evento | Resultado esperado |
|---|---|---|---|---|
| 1 | Registro exitoso | Login no existe, contraseña cumple la política, todos los campos diligenciados. | El usuario hace clic en "Registrar". | El sistema crea el usuario en estado Activo, cifra la contraseña y muestra un mensaje de confirmación. |
| 2 | Login duplicado | Ya existe un usuario registrado con ese login. | El usuario hace clic en "Registrar". | El sistema muestra "Ya existe un usuario registrado con el login: {login}" y no crea el usuario. |
| 3 | Contraseña insegura | La contraseña incumple una o más reglas de la política. | El usuario hace clic en "Registrar". | El sistema muestra la lista completa de reglas incumplidas y no crea el usuario. |

**Prototipo de interfaz:** ya implementado en `RegistroUsuarioView.java` (login, nombre completo,
combo de rol, contraseña, botón "Registrar", enlace "Volver a iniciar sesión").

### HU-06 (HE-03) — Yo como usuario registrado

**Enunciado:** Yo como usuario registrado necesito iniciar sesión con mi login y contraseña,
para acceder al tablero de opciones correspondiente a mi rol.

**Contexto adicional:** Un usuario en estado Inactivo no puede iniciar sesión aunque su
contraseña sea correcta.

| # | Criterio de aceptación | Contexto | Evento | Resultado esperado |
|---|---|---|---|---|
| 1 | Inicio de sesión exitoso | El login existe, la contraseña coincide y el usuario está Activo. | El usuario hace clic en "Iniciar sesión". | El sistema abre el Dashboard con las opciones del rol del usuario. |
| 2 | Credenciales incorrectas | El login no existe, o el login existe pero la contraseña no coincide. | El usuario hace clic en "Iniciar sesión". | El sistema muestra un único mensaje de error genérico, sin indicar cuál de los dos datos falló. |
| 3 | Usuario inactivo | El login y la contraseña son correctos, pero el usuario está Inactivo. | El usuario hace clic en "Iniciar sesión". | El sistema rechaza el inicio de sesión. |

**Prototipo de interfaz:** ya implementado en `LoginView.java` → tras éxito navega a
`DashboardView.java`.

### HU-07 (HE-04) — Yo como Administrador

**Enunciado:** Yo como Administrador necesito ver el listado de todos los usuarios registrados,
para conocer quiénes tienen acceso al sistema y en qué rol.

**Contexto adicional:** El código actual (`UsuarioController.listarUsuarios`) no restringe esta
consulta a un rol específico — la restricción por rol queda pendiente de definir junto con
`MenuFactory`/`MenuAdministrador` (ver "Fuera de alcance" en `docs/REGLAS_NEGOCIO.md`).

| # | Criterio de aceptación | Contexto | Evento | Resultado esperado |
|---|---|---|---|---|
| 1 | Listado con usuarios | Existen usuarios registrados en el sistema. | El Administrador abre la sección de usuarios. | El sistema muestra login, nombre completo, rol y estado de cada usuario. |
| 2 | Sin usuarios registrados | No hay ningún usuario registrado aún. | El Administrador abre la sección de usuarios. | El sistema muestra la lista vacía sin error. |

### HU-08 (HE-04) — Yo como Administrador

**Enunciado:** Yo como Administrador necesito activar o desactivar un usuario por su login, para
revocar o restaurar su acceso al sistema sin eliminar su registro.

| # | Criterio de aceptación | Contexto | Evento | Resultado esperado |
|---|---|---|---|---|
| 1 | Desactivación exitosa | El usuario existe y está Activo. | El Administrador selecciona el usuario y hace clic en "Desactivar". | El sistema cambia su estado a Inactivo; el usuario ya no puede iniciar sesión (HU-06, criterio 3). |
| 2 | Reactivación exitosa | El usuario existe y está Inactivo. | El Administrador hace clic en "Activar". | El sistema cambia su estado a Activo; el usuario puede volver a iniciar sesión. |
| 3 | Login inexistente | No existe ningún usuario con el login indicado. | Se invoca el cambio de estado. | El sistema no realiza ningún cambio e informa que el usuario no existe. |

## 3. Trazabilidad con el código

| Historia | Método a implementar |
|---|---|
| HU-05 | `UsuarioController.registrarUsuario(...)` + `PasswordValidator` + `Sha256PasswordEncoder.encode` |
| HU-06 | `AuthController.autenticar(...)` + `Sha256PasswordEncoder.matches` |
| HU-07 | `UsuarioController.listarUsuarios()` (ya implementado) |
| HU-08 | `UsuarioController.cambiarEstado(...)` (ya implementado) |
