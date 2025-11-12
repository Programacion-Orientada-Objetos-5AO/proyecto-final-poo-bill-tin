# Evaluación del Backend

- Puntaje final: 8/10

## Puntaje de código: 8/10
Argumentos (resumen):
- Arquitectura razonable: capas separadas (controller, service, repository, mapper, dto) y tests unitarios presentes para dominios clave (usuarios, transacciones, etc.).
- Seguridad JWT implementada con filtros y manejo de 401/403 correcto en general.
- Validaciones con Bean Validation en DTOs y entidades.
- Observaciones a mejorar:
  - Endpoint “de atajo” sin servicio ni seguridad estricta: `POST /api/gasto` usa el repositorio directamente y queda abierto (permite crear transacciones sin autenticación ni validaciones de dominio). Debería eliminarse o protegerse.
  - Faltan @Valid en algunos POST/PUT (por ejemplo Moneda/Membresía), y falta un manejador global de errores (ControllerAdvice) para mapear NotFound/violaciones de integridad a 404/409 en lugar de 500.
  - Diseño de DTOs de salida sin IDs (Objetivo/Membresía) dificulta usar GET por id/PUT/DELETE tras listar.
  - Inconsistencias de nombres/atributos (p. ej. `Beneficios` con mayúscula en Membresia, ids mixtos `id`, `idMoneda`, `idCategoria`).

## Puntaje de endpoints (tests): 8/10
Argumentos (resumen de pruebas reales):
- Autenticación
  - POST /api/auth/login → 200 (ADMIN y CLIENTE) devuelve JWT.
  - Acceso sin token a rutas seguras → 401.
  - Acceso con CLIENTE a POST admin-only (p. ej. /api/monedas) → 403.
- Endpoints públicos (sin token)
  - GET /api/saludo → 200.
  - GET /api/estado → 200.
  - GET /api/gastos → 200 (lista vacía/inicial).
  - POST /api/gasto → 200 Crea transacción sin auth (riesgo de seguridad/consistencia).
- Transacciones (/api/transacciones) [con ADMIN]
  - GET / → 200
  - GET /{id} → 200
  - POST → 201
  - PUT /{id} → 200
  - DELETE /{id} → 200
  - GET /gasto-entre-fechas?inicio&fin → 200
  - GET /gastos-por-categoria/{usuarioId} → 200
  - GET /gasto-convertido?inicio&fin&monedaDestino → 200 (usa API externa)
- Monedas (/api/monedas) [con ADMIN]
  - POST → 200 Crea
  - GET / → 200 Lista
  - GET /{id} → 200 y 404 cuando no existe
  - PUT /{id} → 200 (existente) y 404 (no existente)
  - DELETE /{id} → 200 (cuando no hay referencias)
- Categorías (/api/categorias) [con ADMIN]
  - POST → 201 Crea
  - GET / → 200 Lista; GET /{id} → 200 y 404 si no existe
  - PUT /{id} → 200 (existente); PUT no existente → 500 (esperable 404)
  - DELETE /{id} con referencias → 500 (esperable 409 por integridad referencial)
- Membresías (/api/membresias) [con ADMIN]
  - POST → 201 Crea
  - GET / → 200 Lista; GET /{id} → 200
  - PUT /{id} → 200; DELETE /{id} → 200
  - Observación: DTO de salida no incluye id → dificulta operar tras listar.
- Usuarios (/api/usuarios)
  - POST /registrar → 200 crea CLIENTE y luego puede loguear (200).
  - GET / (con ADMIN) → 200 lista usuarios.

Cobertura y calidad observada:
- Cobertura amplia de CRUDs y agregaciones. Respuestas y códigos correctos en la mayoría de casos.
- Puntos a mejorar que afectaron el puntaje: endpoint abierto `/api/gasto`, errores 500 donde deberían ser 404/409, y DTOs sin id.
