# 🏥 MediRecords - Sistema de Gestión Clínica (Microservicios)

Sistema backend modular e interconectado basado en una arquitectura de microservicios para la gestión integral de clínicas y hospitales.

## 📋 Requisitos del Sistema

- **Java:** 24.0.2
- **Maven:** 3.9.15
- **Base de Datos:** MySQL 8.0 o superior

## 🚀 Stack Tecnológico

- **Lenguaje:** Java 24
- **Framework Base:** Spring Boot 4.0.6
- **Ecosistema Cloud:** Spring Cloud 2025.1.1
- **Enrutamiento y Seguridad:** Spring Cloud Gateway
- **Descubrimiento de Servicios:** Spring Cloud Netflix Eureka
- **Persistencia:** Spring Data JPA & MySQL
- **Comunicación Inter-servicio:** Spring Cloud OpenFeign
- **Herramientas Adicionales:** Lombok & Maven
- **Pruebas Unitarias:** JUnit 5 + Mockito (Batería de 160 tests automatizados)

## 🔌 Mapeo de Puertos

| Componente / Microservicio | Puerto | Descripción |
|----------------------------|--------|-------------|
| **Eureka Server** | `8761` | Servidor de descubrimiento y registro de servicios |
| **API Gateway** | `8080` | Punto de entrada único y enrutador de peticiones |
| **Hospital** | `8081` | Gestión de sedes y clínicas |
| **Paciente** | `8082` | Gestión de datos demográficos de pacientes |
| **Medicamento** | `8083` | Catálogo de fármacos disponibles |
| **Cargo** | `8084` | Roles y puestos del personal médico/administrativo |
| **Especialidad** | `8085` | Especialidades médicas de la clínica |
| **Personal** | `8086` | Gestión de empleados, médicos y staff |
| **Receta** | `8087` | Emisión y control de recetas de medicamentos |
| **Consulta** | `8088` | Agendamiento y registro de citas médicas |
| **Historial** | `8089` | Expediente clínico consolidado del paciente |

## 🎯 Funcionalidades CRUD

Cada uno de los microservicios de negocio implementa de forma nativa las operaciones estándar orientadas a REST:

- 🔍 **Listar:** `GET` - Obtener la colección completa de registros.
- 🆔 **Buscar:** `GET /{id}` - Obtener un registro específico por su identificador único.
- ➕ **Crear:** `POST` - Registrar un nuevo elemento en el sistema.
- 🔄 **Actualizar:** `PUT /{id}` - Modificar los datos de un registro existente.
- ❌ **Borrar:** `DELETE /{id}` - Eliminación lógica o física de un registro.

## 🔗 Endpoints Disponibles (A través del API Gateway)

**URL Base del Ecosistema:** `http://localhost:8080`

| Recurso Base | Listar | Buscar | Crear | Actualizar | Borrar |
|--------------| :---: | :---: | :---: | :---: | :---: |
| `/api/hospitales` | `GET` | `GET /{id}` | `POST` | `PUT /{id}` | `DELETE /{id}` |
| `/api/pacientes` | `GET` | `GET /{id}` | `POST` | `PUT /{id}` | `DELETE /{id}` |
| `/api/medicamentos` | `GET` | `GET /{id}` | `POST` | `PUT /{id}` | `DELETE /{id}` |
| `/api/cargos` | `GET` | `GET /{id}` | `POST` | `PUT /{id}` | `DELETE /{id}` |
| `/api/especialidades`| `GET` | `GET /{id}` | `POST` | `PUT /{id}` | `DELETE /{id}` |
| `/api/personal` | `GET` | `GET /{id}` | `POST` | `PUT /{id}` | `DELETE /{id}` |
| `/api/consultas` | `GET` | `GET /{id}` | `POST` | `PUT /{id}` | `DELETE /{id}` |
| `/api/recetas` | `GET` | `GET /{id}` | `POST` | `PUT /{id}` | `DELETE /{id}` |
| `/api/historiales` | `GET` | `GET /{id}` | `POST` | `PUT /{id}` | `DELETE /{id}` |

## 🔗 Matriz de Comunicación Inter-servicio (OpenFeign)

Para resolver dependencias de datos entre dominios de forma síncrona, se utiliza Spring Cloud OpenFeign bajo el siguiente esquema de consumo:

| Microservicio Origen (Cliente) | Microservicio Destino (Proveedor) | Propósito del Consumo |
|--------------------------------|-----------------------------------|-----------------------|
| `paciente` | `hospital` | Validar el hospital de adscripción del paciente. |
| `personal` | `cargo`, `especialidad` | Asociar el rol laboral y la especialidad médica al empleado. |
| `consulta` | `paciente`, `personal`, `hospital` | Reunir los actores y la sede para consolidar el acto médico. |
| `receta` | `consulta`, `medicamento` | Vincular la prescripción médica a una consulta y fármacos válidos. |
| `historial` | `paciente`, `consulta` | Agrupar las consultas de un paciente dentro de su expediente clínico. |

## 🐳 Despliegue Automatizado con Docker

### 1. Descarga de Artefactos Compilados
Los archivos `.jar` ya compilados y listos para empaquetarse en contenedores deben descargarse del siguiente enlace:

* 📦 [Descargar JARs Compilados de MediRecords](https://drive.google.com/file/d/1mmAQYAMaTjm8AYDJkeO051D3Cs-a4uu0/view?usp=drive_link)

Una vez descargado el archivo comprimido, extrae todo su contenido estrictamente en la siguiente ruta del proyecto:
`medirecords-docker/apps/`

### 2. Inicialización del Entorno
Para compilar las imágenes locales de Docker y levantar todo el ecosistema de microservicios junto con la base de datos MySQL, ejecuta el script automatizado desde tu terminal de comandos de Windows (`cmd`):

```cmd
cd medirecords-docker
arrancar-docker.bat