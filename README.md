# 🏥 Sistema de Gestión Clínica - Microservicios

Sistema backend basado en microservicios para la gestión clínica.

## 📋 Requisitos

- Java 21 o superior
- Maven 3.6+
- MySQL 8.0+

## 🚀 Tecnologías

- Java 21
- Spring Boot 3.x
- Spring Cloud Gateway
- Spring Cloud Netflix Eureka
- Spring Data JPA
- Spring Cloud OpenFeign
- MySQL
- Maven
- Lombok

## 🔌 Puertos

| Componente | Puerto |
|------------|--------|
| Eureka Server | 8761 |
| API Gateway | 8080 |
| Hospital | 8081 |
| Paciente | 8082 |
| Medicamento | 8083 |
| Cargo | 8084 |
| Especialidad | 8085 |
| Personal | 8086 |
| Consulta | 8087 |
| Receta | 8088 |
| Historial | 8089 |

## 🎯 Funcionalidades

Cada microservicio implementa las siguientes operaciones CRUD:

- **Listar** - Obtener todos los registros
- **Buscar** - Obtener un registro por ID
- **Crear** - Registrar un nuevo elemento
- **Actualizar** - Modificar un registro existente
- **Borrar** - Eliminar un registro

## 🔗 Endpoints (a través del Gateway)

**URL Base:** `http://localhost:8080`

| pathing de ms | Listar | Buscar | Crear | Actualizar | Borrar |
|---------------|--------|--------|-------|------------|--------|
| /api/{microservicio} | `GET` | `GET /{id}` | `POST body con atributos` | `PUT /{id}`, `body con nuevos atributos` | `DELETE /{id}`|
