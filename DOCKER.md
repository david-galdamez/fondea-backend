# Docker - Guía para Desarrolladores

## Prerequisitos
- Docker instalado
- Docker Compose instalado

## Levantar el proyecto

1. Copiar archivo de variables de entorno:
   ```bash
   cp .env.example .env
   ```

2. Editar `.env` con las credenciales deseadas

3. Construir y levantar servicios:
   ```bash
   docker-compose up --build
   ```

4. Verificar que esté corriendo:
   - App: http://localhost:8080
   - PostgreSQL: localhost:5432

## Comandos útiles

### Levantar en background
```bash
docker compose up -d --build
```

### Ver logs de la app
```bash
docker compose logs -f app
```

### Detener servicios
```bash
docker compose down
```

### Detener y eliminar datos (reset completo)
```bash
docker compose down -v
```

### Reconstruir sin cache
```bash
docker compose build --no-cache
```

### Ver status de los servicios
```bash
docker compose ps
```

## Estructura de servicios

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| app | 8080 | Aplicación Spring Boot (Java 21) |
| postgres | 5432 | Base de datos PostgreSQL 16 |

## Variables de entorno

Consultar `.env.example` para las variables disponibles.

## Notas

- El volumen `postgres_data` persiste los datos de la base de datos
- El healthcheck en postgres asegura que la app no inicie antes de que la BD esté lista
- La aplicación espera automáticamente a que postgres estéhealthy gracias a `depends_on`