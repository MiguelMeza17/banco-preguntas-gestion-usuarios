# 0009 — Simplificar a MVC plano, sin interfaces ni factories

**Estado:** Aceptado

**Reemplaza a (parcialmente):** [0003](0003-arquitectura-mvc-por-capas.md)

## Contexto

La versión anterior ([ADR 0003](0003-arquitectura-mvc-por-capas.md)) seguía al pie de la letra el
ejemplo de Inversión de Dependencias visto en teoría: interfaz de repositorio (`IUsuarioRepository`)
más implementación concreta (`UsuarioRepository`) más fábrica (`Factory`) para desacoplar la creación;
un Strategy completo para las reglas de contraseña (`IPasswordRule` + cuatro clases, una por regla);
una interfaz de cifrado (`IPasswordEncoder`) con su implementación SHA-256; y hasta una fábrica de
menús por rol (`MenuFactory` + cinco clases `Menu*`) que en la práctica seguía vacía (puros `TODO`, sin
usarse desde ninguna vista).

Al ponernos a implementar el registro y el login nos dimos cuenta de que toda esa cantidad de capas
no aportaba nada concreto para el alcance de este taller: el repositorio solo tiene una implementación
posible (SQLite), la interfaz de cifrado solo tiene un algoritmo real, y las reglas de contraseña no
iban a cambiar a mitad de semestre. Mantenerlas solo agregaba archivos que recorrer y que explicar en
la sustentación sin ganar nada a cambio.

## Decisión

Aplanar el diseño a un MVC directo, sin abstracciones que no se estén usando:

- `IUsuarioRepository` y `Factory` desaparecen. `UsuarioRepository` es una clase concreta normal;
  `AuthController` y `UsuarioController` la reciben directamente por constructor.
- `IPasswordEncoder` y `Sha256PasswordEncoder` se combinan en una sola clase utilitaria,
  `controller/PasswordUtil.java`, con métodos estáticos `encode` y `matches`.
- `IPasswordRule`, `PasswordValidator` y las cuatro reglas (`LongitudMinimaRule`, `DigitoRule`,
  `MayusculaRule`, `CaracterEspecialRule`) se reemplazan por un único método privado
  `validarPassword(...)` dentro de `UsuarioController`, con los mismos cuatro chequeos en `if`.
- El paquete `controller/menu` (`IMenuRol`, `MenuFactory`, los cinco `Menu*`) se elimina por completo:
  no estaba conectado a ninguna vista, solo dejaba `TODO` sin resolver.
- Las excepciones de dominio (`CredencialesInvalidasException`, `PasswordInvalidaException`,
  `UsuarioYaExisteException`) se reemplazan por `Exception` simple con un mensaje descriptivo; las
  vistas atrapan `Exception` en vez de un catch por cada tipo.

Lo único que se mantiene sin cambios es la separación por capas (`model`, `conexion`, `controller`,
`view`) y las pruebas (`UsuarioRepositoryTest`, `AuthFlowTest`), adaptadas a las clases nuevas.

## Consecuencias

**Ventajas**
- Menos archivos y menos indirección: para entender cómo se registra un usuario basta con abrir
  `UsuarioController`, no saltar entre una interfaz, su implementación y una fábrica.
- Más fácil de sustentar individualmente: cada clase hace justo lo que su nombre dice, sin patrones
  de por medio que haya que justificar aparte.
- El paquete `menu`, que era código muerto, deja de aparecer en el árbol del proyecto.

**Desventajas**
- Se pierde la demostración explícita de OCP/DIP que sí se veía en la versión con interfaces (p. ej.
  "puedo cambiar el algoritmo de cifrado sin tocar `UsuarioController`"). Para el alcance de este
  módulo no hay un segundo motor de persistencia ni un segundo algoritmo de cifrado real, así que ese
  beneficio era teórico.
- Si más adelante el taller pidiera de verdad un segundo repositorio (por ejemplo, otra base de datos
  para pruebas de integración distintas a SQLite en memoria) o un segundo algoritmo de cifrado, habría
  que reintroducir la interfaz en ese momento en vez de tenerla ya lista.
