# Reglas de negocio — Gestión de usuarios (Taller 2 SOLID)

Estas son las reglas de negocio que hay que implementar dentro de los `// TODO` de este
entregable. No son reglas nuevas: están implícitas en las firmas de método, los mensajes de
error ya escritos y las excepciones de dominio que ya existen en el código — este documento
solo las hace explícitas para que las dos personas del equipo implementen lo mismo sin tener
que releer cada clase para adivinar el comportamiento esperado.

## 1. Roles soportados

El sistema reconoce exactamente 5 roles, definidos en `model/Rol.java`. No se permite ningún
rol fuera de este conjunto (lo garantiza el compilador al ser un `enum`):

- Administrador
- Autor de preguntas
- Revisor
- Docente
- Estudiante

## 2. Registro de usuario — `UsuarioController.registrarUsuario(...)`

Al registrar un usuario nuevo, en este orden:

1. **Validar la contraseña** con `passwordValidator` (ver regla 3). Si no cumple alguna regla,
   lanzar `PasswordInvalidaException` con la lista completa de errores (no solo el primero que
   falle) — el constructor de la excepción ya recibe un `List<String>` para esto.
2. **Verificar que el login no exista** (`repository.existsByLogin(login)`). El login es el
   identificador de negocio del usuario: no puede haber dos usuarios con el mismo login. Si ya
   existe, lanzar `UsuarioYaExisteException(login)`.
3. **Cifrar la contraseña** con `passwordEncoder.encode(passwordPlano)` — nunca se guarda ni se
   compara en texto plano (ver regla 4).
4. **Construir el `Usuario`** con `estado = ACTIVO` (todo usuario nace activo; no existe un
   estado "pendiente de aprobación" en este alcance).
5. **Guardar** con `repository.save(usuario)` y devolver el usuario creado.

El orden importa: se valida la contraseña *antes* de tocar la base de datos, para no hacer una
consulta de existencia si la contraseña ya es inválida.

## 3. Política de contraseñas — `PasswordValidator` + `rules/*Rule.java`

Una contraseña es válida solo si cumple **todas** las siguientes reglas (AND, no OR):

| Regla | Clase | Condición |
|---|---|---|
| Longitud mínima | `LongitudMinimaRule` | Al menos 6 caracteres |
| Dígito | `DigitoRule` | Al menos un carácter `0-9` |
| Mayúscula | `MayusculaRule` | Al menos una letra mayúscula |
| Carácter especial | `CaracterEspecialRule` | Al menos un carácter que no sea letra ni dígito |

- `PasswordValidator.esValida(password)` → `true` solo si **ninguna** regla falla.
- `PasswordValidator.obtenerErrores(password)` → recorre **todas** las reglas (no corta en la
  primera que falle) y devuelve el `getMensajeError()` de cada una que no se cumpla. Esto es lo
  que después se muestra en pantalla y lo que viaja dentro de `PasswordInvalidaException`.
- Estas reglas son las que trae `PasswordValidator.reglasPorDefecto()`. Agregar una regla nueva
  en el futuro (p. ej. "no reutilizar las últimas N contraseñas") no debe requerir modificar
  `PasswordValidator`: solo se implementa una nueva clase `IPasswordRule` y se agrega a la lista
  (principio OCP, ya aplicado en el diseño).

## 4. Cifrado de contraseña — `Sha256PasswordEncoder`

- `encode(passwordPlano)` produce el hash que se guarda en `Usuario.passwordHash`. Nunca se
  persiste la contraseña en texto plano.
- `matches(passwordPlano, passwordHash)` verifica una contraseña candidata contra un hash ya
  guardado **sin necesidad de desencriptarlo** (se re-cifra el candidato y se comparan los
  hashes, no al revés).
- Importante para la sustentación: SHA-256 puro sin sal es vulnerable a tablas rainbow. Si se
  quiere ir más allá del mínimo del taller, usar una sal aleatoria por usuario (guardada junto
  al hash) antes de cifrar. `IPasswordEncoder` ya está diseñado para que este cambio no afecte a
  `UsuarioController` ni a `AuthController` (dependen de la interfaz, no de la implementación).

## 5. Autenticación — `AuthController.autenticar(login, passwordPlano)`

En este orden:

1. Buscar el usuario por login (`repository.findByLogin(login)`).
2. Si no existe → lanzar `CredencialesInvalidasException`.
3. Verificar la contraseña con `passwordEncoder.matches(passwordPlano, usuario.getPasswordHash())`.
   Si no coincide → lanzar `CredencialesInvalidasException`.
4. Verificar que `usuario.getEstado() == EstadoUsuario.ACTIVO`. Si está `INACTIVO` → lanzar
   `CredencialesInvalidasException` (un usuario inactivo no puede iniciar sesión aunque la
   contraseña sea correcta).
5. Si las tres validaciones pasan, devolver el `Usuario` autenticado.

**Regla de seguridad a decidir en equipo:** usar el mismo mensaje de `CredencialesInvalidasException`
para "el login no existe", "la contraseña no coincide" e " el usuario está inactivo" evita revelar a
un atacante si un login específico existe en el sistema. Si se quiere dar un mensaje distinto para
"usuario inactivo" (más amigable para el usuario real), hacerlo con un mensaje aparte pero sin
exponer si el login existe cuando la causa es la contraseña.

## 6. Cambio de estado — `UsuarioController.cambiarEstado(login, nuevoEstado)`

Ya delega directamente en `repository.updateEstado(...)` (no tiene `// TODO`). La regla de
negocio asociada es la de la sección 5: desactivar a un usuario (`INACTIVO`) debe bloquear su
próximo intento de login, aunque su sesión ya abierta no se controla en este alcance (no hay
invalidación de sesión activa — la app es de un solo usuario por proceso).

## 7. Fuera de alcance en esta entrega

Estas reglas mencionadas en la documentación del proyecto de curso (ver `docs/adr/` y la
Actividad 2 de la teoría) **no** se implementan en este taller de laboratorio, para no ampliar el
alcance más allá de lo que pide la guía de Gestión de usuarios + SOLID:

- Autorización por rol sobre acciones del banco de preguntas (quién puede aprobar/rechazar
  preguntas, etc.) — pertenece al módulo de preguntas, no al de usuarios.
- Auditoría de acciones (log de quién hizo qué y cuándo).
- Recuperación de contraseña / expiración de contraseña.
- Restricción de quién puede registrar usuarios de rol Administrador (`RegistroUsuarioView`
  permite elegir cualquier rol libremente desde el formulario de registro — no hay control de
  "solo un Administrador puede crear otro Administrador").
