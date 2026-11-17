# Servicio de Pólizas

API REST para la gestión de pólizas y riesgos, construida con Spring Boot 4, Java 17, Spring Data JPA y H2 en memoria.

## Requisitos

- JDK 17 o superior

## Ejecución

```bash
./mvnw spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

## Pruebas

```bash
./mvnw test
```

## Seguridad

Todas las peticiones deben incluir el header:

```
x-api-key: 123456
```

Sin el header, o con un valor distinto, la API responde `401`.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/polizas?tipo=&estado=` | Lista pólizas; ambos filtros son opcionales |
| GET | `/polizas/{id}/riesgos` | Lista los riesgos de una póliza |
| POST | `/polizas/{id}/renovar` | Incrementa canon y prima en el IPC y deja la póliza en `RENOVADA` |
| POST | `/polizas/{id}/cancelar` | Cancela la póliza y todos sus riesgos |
| POST | `/polizas/{id}/riesgos` | Agrega un riesgo; solo para pólizas `COLECTIVA` |
| POST | `/riesgos/{id}/cancelar` | Cancela un riesgo |
| POST | `/core-mock/evento` | Mock del CORE: registra el evento en logs |

Valores admitidos:

- `tipo`: `INDIVIDUAL`, `COLECTIVA`
- `estado`: `ACTIVA`, `RENOVADA`, `CANCELADA`

## Reglas de negocio

- Una póliza individual solo puede tener 1 riesgo: agregar riesgos solo se permite en pólizas colectivas.
- No se puede renovar una póliza cancelada.
- Cancelar una póliza cancela todos sus riesgos.

Las violaciones de reglas de negocio responden `409`, los recursos inexistentes `404` y las peticiones inválidas `400`.

## Integración con el CORE

Renovar, cancelar una póliza, agregar un riesgo y cancelar un riesgo envían un evento por HTTP a `POST /core-mock/evento`:

```json
{
  "evento": "ACTUALIZACION",
  "polizaId": 555
}
```

Eventos: `RENOVACION`, `CANCELACION`, `ACTUALIZACION`. El mock solo registra en logs que la operación se intentó enviar al CORE.

## Configuración

En `src/main/resources/application.yaml`:

| Propiedad | Descripción | Valor |
|---|---|---|
| `app.api-key` | Valor esperado en el header `x-api-key` | `123456` |
| `app.ipc-percentage` | Porcentaje de IPC aplicado en la renovación | `5.2` |
| `app.core.base-url` | URL base del CORE | `http://localhost:8080` |

## Datos iniciales

Al iniciar se cargan desde `data.sql`:

| Póliza | Tipo | Estado | Riesgos |
|---|---|---|---|
| 1 | INDIVIDUAL | ACTIVA | 1 |
| 2 | COLECTIVA | ACTIVA | 2 |
| 3 | COLECTIVA | CANCELADA | 1 |
| 4 | INDIVIDUAL | RENOVADA | 1 |
| 5 | INDIVIDUAL | CANCELADA | 1 |
| 6 | COLECTIVA | RENOVADA | 3 |

## Ejemplos

```bash
curl -H "x-api-key: 123456" "http://localhost:8080/polizas?tipo=COLECTIVA&estado=ACTIVA"

curl -H "x-api-key: 123456" http://localhost:8080/polizas/2/riesgos

curl -X POST -H "x-api-key: 123456" http://localhost:8080/polizas/1/renovar

curl -X POST -H "x-api-key: 123456" http://localhost:8080/polizas/2/cancelar

curl -X POST -H "x-api-key: 123456" -H "Content-Type: application/json" \
  -d '{"description": "Bodega 7"}' http://localhost:8080/polizas/2/riesgos

curl -X POST -H "x-api-key: 123456" http://localhost:8080/riesgos/2/cancelar

curl -X POST -H "x-api-key: 123456" -H "Content-Type: application/json" \
  -d '{"evento": "ACTUALIZACION", "polizaId": 555}' http://localhost:8080/core-mock/evento
```

## Estructura

```
controller/   Endpoints REST
service/      Casos de uso y puerto CoreNotifier
client/       Implementación HTTP de CoreNotifier
repository/   Acceso a datos con Spring Data JPA
model/        Entidades Policy y Risk con sus reglas de negocio
dto/          Objetos de entrada y salida de la API
exception/    Excepciones y su traducción a respuestas HTTP
security/     Filtro de validación del header x-api-key
```
