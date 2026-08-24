# 0002 — PostgreSQL en vez de SQLite

**Estado:** Aceptado

## Contexto

La guía oficial del Taller 2 pide explícitamente: *"La base de datos debe crearse en SQLite, ya sea
en memoria o en un archivo físico."* Es, además, uno de los criterios de la rúbrica (40% "Cumple
todos los requisitos funcionales"). Sin embargo, la docente confirmó directamente que se puede usar
cualquier motor de base de datos, sin penalización. Adicionalmente, el proyecto de curso completo
(Actividad 1) ya había elegido PostgreSQL para el Corte 1, y uno de los integrantes necesita practicar
PostgreSQL para otro proyecto.

## Decisión

Usar **PostgreSQL** como motor de persistencia en lugar de SQLite.

## Consecuencias

**Ventajas**
- Consistencia con la decisión de arquitectura ya tomada para el proyecto de curso completo (Actividad 1),
  en vez de mezclar dos motores distintos entre el Taller 2 y el proyecto final.
- Motor cliente-servidor real, más representativo de un entorno de producción que un archivo SQLite.
- Sirve como práctica reutilizable para otro proyecto del integrante.

**Desventajas**
- Requiere que cada integrante del equipo instale y configure un servidor PostgreSQL local (crear la
  base `banco_preguntas`, usuario y contraseña) — más fricción de puesta en marcha que SQLite, que es
  un solo archivo que se autogenera.
- No existe un modo "en memoria" nativo como en SQLite, lo que complica las pruebas unitarias
  (resuelto con `embedded-postgres`, ver [ADR 0005](0005-pruebas-con-postgresql-embebido.md)).
- Se desvía de lo sugerido literalmente en la guía del taller — mitigado porque la docente lo autorizó
  explícitamente; conviene mencionarlo en la sustentación para que no genere dudas.
