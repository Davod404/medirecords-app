# MediRecords - Endpoints principales

Este documento resume los endpoints principales del sistema **MediRecords**, un proyecto basado en microservicios con Spring Boot, Eureka, API Gateway, Feign Client, MySQL y Swagger.

## Puertos del sistema

| Servicio           | Puerto | Descripción                    |
| ------------------ | -----: | ------------------------------ |
| Eureka Server      |   8761 | Servidor de descubrimiento     |
| API Gateway        |   8080 | Punto único de entrada         |
| hospital           |   8081 | Gestión de hospitales          |
| paciente           |   8082 | Gestión de pacientes           |
| medicamento        |   8083 | Gestión de medicamentos        |
| cargo              |   8084 | Gestión de cargos              |
| especialidad       |   8085 | Gestión de especialidades      |
| personal           |   8086 | Gestión de personal médico     |
| consulta           |   8087 | Gestión de consultas médicas   |
| receta             |   8088 | Gestión de recetas             |
| historial          |   8089 | Gestión de historiales clínicos|

> **Nota:** Los microservicios usan `server.port=0` (puerto aleatorio).  
> Se acceden a través del API Gateway en el puerto 8080.

---

# 1. Acceso por API Gateway

El API Gateway permite consumir todos los microservicios desde un único puerto:

```text
http://localhost:8080