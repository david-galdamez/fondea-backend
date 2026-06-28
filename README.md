# Fondea Backend

API REST de **Fondea**, una plataforma de _crowdfunding_ (financiamiento colectivo). Permite a los
**creadores** publicar campañas, a los **patrocinadores** (_sponsors_) donar mediante aportes
(_pledges_) con recompensas opcionales, y a los **administradores** moderar campañas, revisar
reportes de fraude y aprobar retiros de fondos.

Funcionalidades principales:

- Registro/login con verificación de correo y autenticación JWT.
- Gestión de campañas (borrador → revisión → aprobación/rechazo) con categorías y ubicaciones.
- Recompensas, actualizaciones y FAQs por campaña.
- Aportes (_pledges_) y certificados de donación.
- Solicitudes de retiro de fondos con límites y aprobación administrativa.
- Reportes de fraude y su moderación.
- Notificaciones de usuario.
- Exportación de campañas a CSV y Google Sheets.
- Liquidación automática de campañas mediante tareas programadas.

---

## Stack tecnológico

| Componente        | Tecnología                                  |
| ----------------- | ------------------------------------------- |
| Lenguaje          | Java 21                                     |
| Framework         | Spring Boot 4.0.6                            |
| Build             | Maven                                        |
| Base de datos     | PostgreSQL                                   |
| Persistencia      | Spring Data JPA / Hibernate (`ddl-auto=update`) |
| Seguridad         | Spring Security + JWT (`com.auth0:java-jwt 4.4.0`) |
| Hash de contraseña| BCrypt                                       |
| Correo            | Spring Boot Starter Mail (SMTP)             |
| Integración       | Google Sheets API                            |
| Utilidades        | Lombok                                        |

---

## Estructura del proyecto

```
src/main/java/com/project/fondea/
├── config/         # Configuración de Spring
├── controller/     # Controladores REST (17)
├── dto/            # Objetos de transferencia (request/response)
├── exception/      # Excepciones personalizadas y manejador global
├── export/         # Exportación (CSV, Google Sheets)
├── filter/         # Filtro de autenticación JWT y AuthContext
├── interceptor/    # Interceptor de API key
├── model/          # Entidades JPA
│   └── enums/      # Enumeraciones (roles y estados)
├── repository/     # Repositorios Spring Data
├── scheduler/      # Tareas programadas (liquidación de campañas)
├── security/       # SecurityConfig, CORS, WebConfig
├── service/        # Lógica de negocio
└── util/           # JwtUtil, ApiResponse
```

Punto de entrada: `src/main/java/com/project/fondea/FondeaApplication.java` (con `@EnableScheduling`).

---

## Requisitos previos

- **Java 21**
- **Maven**
- **PostgreSQL** (o usar Docker Compose)
- **Docker / Docker Compose** (opcional, recomendado para desarrollo local)

---

## Variables de entorno

Copia `.env.example` a `.env` y completa los valores:

| Categoría     | Variable                          | Descripción                                            |
| ------------- | --------------------------------- | ------------------------------------------------------ |
| PostgreSQL¹   | `POSTGRES_DB`                     | Nombre de la BD (solo Docker Compose local)            |
|               | `POSTGRES_USER`                  | Usuario de la BD (solo Docker Compose local)           |
|               | `POSTGRES_PASSWORD`              | Contraseña de la BD (solo Docker Compose local)        |
| App / BD      | `DATABASE_URL`                   | URL JDBC de PostgreSQL                                  |
|               | `DATABASE_USERNAME`             | Usuario de conexión                                    |
|               | `DATABASE_PASSWORD`             | Contraseña de conexión                                 |
| Servidor      | `SERVER_PORT`                    | Puerto de la app (por defecto `8080`)                  |
| Seguridad     | `JWT_SECRET`                     | Secreto HMAC256 para firmar tokens                     |
|               | `API_KEY`                        | API key opcional para el interceptor                   |
| Admin inicial | `ADMIN_NAME`                     | Nombre del admin sembrado al iniciar                   |
|               | `ADMIN_EMAIL`                    | Correo del admin inicial                               |
|               | `ADMIN_PASSWORD`                 | Contraseña del admin inicial                           |
| Correo (SMTP) | `MAIL_HOST` / `MAIL_PORT`        | Servidor SMTP                                          |
|               | `MAIL_USERNAME` / `MAIL_PASSWORD`| Credenciales SMTP                                      |
|               | `MAIL_FROM`                      | Remitente de los correos                               |
| CORS          | `CORS_ALLOWED_ORIGINS`           | Orígenes del frontend separados por coma               |
| Google Sheets | `GOOGLE_SHEETS_ENABLED`          | Habilita la exportación (`true`/`false`)               |
|               | `GOOGLE_SHEETS_CREDENTIALS_PATH` | Ruta a las credenciales de servicio                    |
|               | `GOOGLE_SHEETS_SPREADSHEET_ID`   | ID de la hoja de cálculo destino                       |
|               | `GOOGLE_SHEETS_RANGE`            | Rango destino (p. ej. `Campaigns!A1`)                  |

¹ Las variables `POSTGRES_*` solo se usan para levantar el contenedor de PostgreSQL en Docker Compose.

---

## Cómo ejecutar

### Con Docker

```bash
cp .env.example .env   # completa los valores
docker-compose up --build
```

La app queda disponible en `http://localhost:8080` y PostgreSQL en `localhost:5432`.
Ver detalles en [DOCKER.md](DOCKER.md).

### Con Maven

```bash
mvn spring-boot:run     # ejecutar en desarrollo
mvn clean package       # compilar y empaquetar
mvn test                # ejecutar pruebas
```

---

## Autenticación y autorización

La autenticación es **JWT stateless** (sin sesiones ni cookies).

**Flujo:**

1. El usuario se registra como **creador** o **patrocinador** (`/api/auth/register-creator` o
   `/api/auth/register-sponsor`) y recibe un código de verificación por correo.
2. Inicia sesión (`/api/auth/login`) y obtiene un **token JWT** (vigencia de 24 h, firmado con HMAC256).
3. En cada petición protegida envía el token en el header:

   ```
   Authorization: Bearer <token>
   ```

4. `JwtAuthenticationFilter` valida la firma y la expiración, extrae el `userId` y el `role`, y
   coloca las autoridades en el contexto de Spring Security (`ROLE_ADMIN`, `ROLE_CREATOR`, `ROLE_SPONSOR`).

### Roles y permisos

| Rol         | Capacidades                                                                                          |
| ----------- | ---------------------------------------------------------------------------------------------------- |
| **Público** | Buscar/ver campañas, ver recompensas, actualizaciones y FAQs públicas, listar categorías y ubicaciones, registro y login |
| **CREATOR** | Lo público + crear/editar campañas, enviar a revisión, publicar actualizaciones, gestionar recompensas, responder FAQs, solicitar retiros |
| **SPONSOR** | Lo público + crear aportes, ver sus aportes, preguntar FAQs, reportar fraude, ver certificados       |
| **ADMIN**   | Acceso total: gestión de usuarios, categorías y ubicaciones, aprobación/rechazo de campañas, moderación de fraude y aprobación de retiros |

> Cualquier usuario autenticado puede consultar y marcar sus **notificaciones** y exportar campañas a CSV.

---

## Formato de respuesta de la API

Todas las respuestas (salvo la descarga CSV) usan el envoltorio `ApiResponse<T>`:

**Éxito**

```json
{
  "success": true,
  "data": { },
  "message": "Operación exitosa",
  "timestamp": "2026-06-28T12:00:00.000",
  "path": "/api/..."
}
```

**Error**

```json
{
  "success": false,
  "data": null,
  "message": "Descripción del error",
  "timestamp": "2026-06-28T12:00:00.000",
  "path": "/api/..."
}
```

**Paginación** (`PageableResponse<T>`, usado en aportes por campaña)

```json
{
  "content": [ ],
  "page": 1,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5,
  "last": false
}
```

---

## Referencia de endpoints

**Base URL:** `http://localhost:8080`

La columna **Auth** indica el rol/condición requerido. Los endpoints públicos no requieren token.

### Autenticación — `/api/auth`

| Método | Ruta                        | Descripción                              | Auth          | Body                                  |
| ------ | --------------------------- | ---------------------------------------- | ------------- | ------------------------------------- |
| POST   | `/api/auth/register-creator`| Registra un usuario creador              | Público       | `{ name, email, password }`           |
| POST   | `/api/auth/register-sponsor`| Registra un usuario patrocinador         | Público       | `{ name, email, password }`           |
| POST   | `/api/auth/login`           | Inicia sesión y devuelve el token JWT    | Público       | `{ email, password }`                 |
| POST   | `/api/auth/verify`          | Verifica la cuenta con el código         | Autenticado²  | `{ code }`                            |
| POST   | `/api/auth/resend-verification` | Reenvía el código de verificación    | Autenticado²  | —                                     |
| GET    | `/api/auth/me`              | Devuelve el perfil del usuario actual    | Autenticado   | —                                     |
| PUT    | `/api/auth/update-profile`  | Actualiza el perfil del usuario actual   | Autenticado   | `{ name, city?, country?, bio? }`     |

² A nivel de Spring Security `POST /api/auth/**` es público, pero estos endpoints requieren un token
válido porque obtienen el `userId` desde él.

### Usuarios — `/api/users`

| Método | Ruta              | Descripción            | Auth  | Respuesta        |
| ------ | ----------------- | ---------------------- | ----- | ---------------- |
| GET    | `/api/users`      | Lista todos los usuarios | ADMIN | `List<UserDto>`  |
| GET    | `/api/users/{id}` | Obtiene un usuario por ID | ADMIN | `UserDto`        |

### Campañas — `/api/campaigns`

| Método | Ruta                              | Descripción                                | Auth    | Body / Params                                                                 |
| ------ | --------------------------------- | ------------------------------------------ | ------- | ----------------------------------------------------------------------------- |
| POST   | `/api/campaigns`                  | Crea una campaña (borrador)                | CREATOR | `{ title, description, goalAmount, coverImageUrl, isFlexibleGoal, deadline, categoryId, locationId, city }` |
| PUT    | `/api/campaigns/{id}`             | Actualiza una campaña                      | CREATOR | Igual que crear                                                               |
| POST   | `/api/campaigns/{id}/submit`      | Envía la campaña a revisión                | CREATOR | —                                                                             |
| GET    | `/api/campaigns/mine`             | Campañas del creador autenticado           | CREATOR | —                                                                             |
| GET    | `/api/campaigns/drafts`           | Borradores del creador autenticado         | CREATOR | —                                                                             |
| GET    | `/api/campaigns/{campaignId}/pledges` | Aportes de la campaña (paginado)       | CREATOR | Query: `page` (def. 1), `size` (def. 20)                                      |
| GET    | `/api/campaigns/search`           | Busca campañas                             | Público | Query: `categoryId?`, `locationId?`, `keyword?`                               |
| GET    | `/api/campaigns/featured`         | Campañas destacadas                        | Público | Query: `limit` (def. 6)                                                       |
| GET    | `/api/campaigns/{id}`             | Detalle de una campaña                     | Público | —                                                                             |

### Categorías — `/api/categories`

| Método | Ruta              | Descripción            | Auth    | Body          |
| ------ | ----------------- | ---------------------- | ------- | ------------- |
| POST   | `/api/categories` | Crea una categoría     | ADMIN   | `{ name }`    |
| GET    | `/api/categories` | Lista las categorías   | Público | —             |

### Ubicaciones — `/api/locations`

| Método | Ruta             | Descripción           | Auth    | Body                  |
| ------ | ---------------- | --------------------- | ------- | --------------------- |
| POST   | `/api/locations` | Crea una ubicación    | ADMIN   | `{ country, city }`   |
| GET    | `/api/locations` | Lista las ubicaciones | Público | —                     |

### Aportes (_pledges_) — `/api/pledges`

| Método | Ruta                | Descripción                          | Auth    | Body                                  |
| ------ | ------------------- | ------------------------------------ | ------- | ------------------------------------- |
| POST   | `/api/pledges`      | Crea un aporte (donación)            | SPONSOR | `{ campaignId, rewardId?, amount }`   |
| GET    | `/api/pledges/mine` | Aportes del patrocinador autenticado | SPONSOR | —                                     |

### Recompensas — `/api/campaigns/{campaignId}/rewards`

| Método | Ruta                                              | Descripción                       | Auth    | Body                                                       |
| ------ | ------------------------------------------------- | --------------------------------- | ------- | ---------------------------------------------------------- |
| POST   | `/api/campaigns/{campaignId}/rewards`             | Crea una recompensa               | CREATOR | `{ title, description?, minAmount, stock?, estimatedDelivery? }` |
| GET    | `/api/campaigns/{campaignId}/rewards`             | Recompensas disponibles (público) | Público | —                                                          |
| GET    | `/api/campaigns/{campaignId}/rewards/manage`      | Recompensas con detalle (creador) | CREATOR | —                                                          |
| DELETE | `/api/campaigns/{campaignId}/rewards/{rewardId}`  | Elimina una recompensa            | CREATOR | —                                                          |

### Actualizaciones de campaña — `/api/campaigns/{campaignId}/updates`

| Método | Ruta                                         | Descripción                | Auth    | Body                              |
| ------ | -------------------------------------------- | -------------------------- | ------- | --------------------------------- |
| POST   | `/api/campaigns/{campaignId}/updates`        | Publica una actualización  | CREATOR | `{ title, body, visibility }`³    |
| GET    | `/api/campaigns/{campaignId}/updates`        | Lista las actualizaciones  | Público | —                                 |
| DELETE | `/api/campaigns/{campaignId}/updates/{id}`   | Elimina una actualización  | CREATOR | —                                 |

³ `visibility`: `PUBLIC` o `SPONSORS`.

### FAQs — `/api/campaigns/{campaignId}/faqs`

| Método | Ruta                                              | Descripción                        | Auth    | Body            |
| ------ | ------------------------------------------------- | ---------------------------------- | ------- | --------------- |
| POST   | `/api/campaigns/{campaignId}/faqs`                | Hace una pregunta                  | SPONSOR | `{ question }`  |
| PUT    | `/api/campaigns/{campaignId}/faqs/{faqId}/answer` | Responde una pregunta              | CREATOR | `{ answer }`    |
| GET    | `/api/campaigns/{campaignId}/faqs`                | FAQs públicas (solo respondidas)   | Público | —               |
| GET    | `/api/campaigns/{campaignId}/faqs/manage`         | Todas las FAQs (creador)           | CREATOR | —               |
| DELETE | `/api/campaigns/{campaignId}/faqs/{faqId}`        | Elimina una pregunta               | CREATOR | —               |

### Reportes de fraude — `/api/fraud-reports`

| Método | Ruta                 | Descripción          | Auth    | Body                       |
| ------ | -------------------- | -------------------- | ------- | -------------------------- |
| POST   | `/api/fraud-reports` | Reporta un fraude    | SPONSOR | `{ campaignId, reason }`   |

### Retiros — `/api/withdrawals`

| Método | Ruta                    | Descripción                          | Auth    | Body / Respuesta                       |
| ------ | ----------------------- | ------------------------------------ | ------- | -------------------------------------- |
| POST   | `/api/withdrawals`      | Solicita un retiro                   | CREATOR | `{ campaignId, grossAmount }`          |
| GET    | `/api/withdrawals/mine` | Retiros del creador autenticado      | CREATOR | `List<WithdrawalDto>`                  |
| GET    | `/api/withdrawals/limits` | Límites de retiro disponibles      | CREATOR | `WithdrawalLimitsDto`                  |

### Notificaciones — `/api/notifications`

| Método | Ruta                          | Descripción                          | Auth        |
| ------ | ----------------------------- | ------------------------------------ | ----------- |
| GET    | `/api/notifications`          | Lista las notificaciones del usuario | Autenticado |
| GET    | `/api/notifications/unread`   | Notificaciones no leídas             | Autenticado |
| PUT    | `/api/notifications/read-all` | Marca todas como leídas              | Autenticado |

### Certificados de donación — `/api/certificates`

| Método | Ruta                     | Descripción                    | Auth    |
| ------ | ------------------------ | ------------------------------ | ------- |
| GET    | `/api/certificates/mine` | Certificados del patrocinador  | SPONSOR |
| GET    | `/api/certificates/{id}` | Certificado por ID             | SPONSOR |

### Exportación — `/api/export`

| Método | Ruta                                | Descripción                              | Auth        | Respuesta                                  |
| ------ | ----------------------------------- | ---------------------------------------- | ----------- | ------------------------------------------ |
| GET    | `/api/export/campaigns/csv`         | Exporta campañas a CSV (descarga)        | Autenticado | Archivo `text/csv`                         |
| POST   | `/api/export/campaigns/google-sheets` | Exporta campañas a Google Sheets (async) | ADMIN       | `{ spreadsheetUrl }`                       |

### Admin · Campañas — `/api/admin/campaigns`

| Método | Ruta                                  | Descripción                  | Auth  | Body                        |
| ------ | ------------------------------------- | ---------------------------- | ----- | --------------------------- |
| GET    | `/api/admin/campaigns`                | Lista todas las campañas     | ADMIN | —                           |
| GET    | `/api/admin/campaigns/pending`        | Campañas pendientes de revisión | ADMIN | —                        |
| POST   | `/api/admin/campaigns/{id}/approve`   | Aprueba una campaña          | ADMIN | —                           |
| POST   | `/api/admin/campaigns/{id}/reject`    | Rechaza una campaña          | ADMIN | `{ rejectionReason }` (opcional) |

### Admin · Reportes de fraude — `/api/admin/fraud-reports`

| Método | Ruta                                    | Descripción                  | Auth  | Body / Params                       |
| ------ | --------------------------------------- | ---------------------------- | ----- | ----------------------------------- |
| GET    | `/api/admin/fraud-reports`              | Lista reportes de fraude     | ADMIN | Query: `status?` (enum)             |
| GET    | `/api/admin/fraud-reports/{id}`         | Detalle de un reporte        | ADMIN | —                                   |
| POST   | `/api/admin/fraud-reports/{id}/review`  | Marca el reporte en revisión | ADMIN | —                                   |
| POST   | `/api/admin/fraud-reports/{id}/resolve` | Resuelve el reporte          | ADMIN | `{ resolutionNotes }` (opcional)    |
| POST   | `/api/admin/fraud-reports/{id}/dismiss` | Descarta el reporte          | ADMIN | `{ resolutionNotes }` (opcional)    |

### Admin · Retiros — `/api/admin/withdrawals`

| Método | Ruta                                  | Descripción                  | Auth  |
| ------ | ------------------------------------- | ---------------------------- | ----- |
| GET    | `/api/admin/withdrawals/pending`      | Retiros pendientes           | ADMIN |
| POST   | `/api/admin/withdrawals/{id}/approve` | Aprueba un retiro            | ADMIN |
| POST   | `/api/admin/withdrawals/{id}/reject`  | Rechaza un retiro            | ADMIN |

---

## Modelo de datos

| Entidad                  | Descripción                              | Relaciones principales                                  |
| ------------------------ | ---------------------------------------- | ------------------------------------------------------- |
| **User**                 | Usuario del sistema (admin/creador/sponsor) | Crea campañas, aportes y notificaciones                |
| **CreatorProfile**       | Datos específicos del creador            | 1‑a‑1 con `User`; datos bancarios y límites de retiro   |
| **Campaign**             | Campaña de financiamiento                | `ManyToOne` con `User`, `Category`, `Location`; `OneToMany` con actualizaciones, FAQs, recompensas, aportes |
| **Category**             | Categoría de campaña                     | `OneToMany` con `Campaign`                               |
| **Location**             | Ubicación (país/ciudad)                  | `OneToMany` con `Campaign`                               |
| **Reward**               | Nivel de recompensa                      | `ManyToOne` con `Campaign`                               |
| **Pledge**               | Aporte/donación                          | `ManyToOne` con `User`, `Campaign`, `Reward`; `OneToOne` con `Payment` |
| **Payment**              | Transacción de pago                      | `OneToOne` con `Pledge`                                  |
| **CampaignUpdate**       | Novedad/avance de campaña                | `ManyToOne` con `Campaign`                               |
| **CampaignFaq**          | Pregunta/respuesta de campaña            | `ManyToOne` con `Campaign`                               |
| **DonationCertificate**  | Certificado de donación                  | `OneToOne` con `Pledge`                                  |
| **WithdrawalRequest**    | Solicitud de retiro de fondos            | `ManyToOne` con `User` y `Campaign`                      |
| **FraudReport**          | Reporte de fraude                        | `ManyToOne` con `User` (reportante y revisor) y `Campaign` |
| **Notification**         | Notificación de usuario                  | `ManyToOne` con `User` y `Campaign`                      |

### Enumeraciones

| Enum                     | Valores                                                                                   |
| ------------------------ | ----------------------------------------------------------------------------------------- |
| `Role`                   | `ADMIN`, `CREATOR`, `SPONSOR`                                                              |
| `CampaignStatus`         | `DRAFT`, `UNDER_REVIEW`, `ACTIVE`, `SUCCESSFUL`, `FAILED`                                  |
| `PledgeStatus`           | `PENDING`, `CHARGED`, `REFUNDED`, `EXPIRED`                                                |
| `PaymentStatus`          | `SUCCESS`, `FAILED`, `REFUNDED`                                                            |
| `WithdrawalStatus`       | `PENDING`, `APPROVED`, `REJECTED`, `PAID`                                                  |
| `FraudReportStatus`      | `PENDING`, `REVIEWING`, `RESOLVED`, `DISMISSED`                                            |
| `CampaignUpdateVisibility` | `PUBLIC`, `SPONSORS`                                                                     |
| `NotificationType`       | `NEAR_GOAL`, `CAMPAIGN_ENDED`, `CAMPAIGN_APPROVED`, `CAMPAIGN_REJECTED`, `PLEDGE_CHARGED`, `PLEDGE_REFUNDED`, `NEW_UPDATE`, `FRAUD_REPORT_RESOLVED`, `FRAUD_REPORT_DISMISSED` |

---

## Tareas programadas

La aplicación habilita `@EnableScheduling`. El paquete `scheduler/` contiene la **liquidación de
campañas**: al vencer el plazo (`deadline`), se evalúa si la campaña alcanzó su meta para marcarla
como `SUCCESSFUL` o `FAILED` y procesar los aportes y notificaciones correspondientes.
