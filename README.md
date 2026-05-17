# 🏥 Sistema de Gestión Clínica - Microservicios

Sistema backend basado en microservicios para la gestión de hospitales, pacientes, consultas médicas, recetas e historiales clínicos.

## 📋 Arquitectura

El sistema está compuesto por los siguientes microservicios:

| Microservicio | Puerto | Descripción |
|---------------|--------|-------------|
| **Hospital** | 8081 | Gestión de hospitales |
| **Paciente** | 8082 | Gestión de pacientes |
| **Personal** | 8083 | Gestión de médicos y personal |
| **Consulta** | 8084 | Gestión de consultas médicas |
| **Receta** | 8085 | Gestión de recetas médicas |
| **Historial** | 8094 | Gestión de historiales clínicos |
| **Medicamento** | 8086 | Gestión de medicamentos |
| **Cargo** | 8087 | Gestión de cargos del personal |
| **Especialidad** | 8088 | Gestión de especialidades médicas |

## 🚀 Tecnologías

- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring Cloud OpenFeign** (Comunicación entre microservicios)
- **MySQL** (Base de datos)
- **Maven** (Gestor de dependencias)
- **Lombok** (Reducción de código boilerplate)

## 📦 Requisitos Previos

- JDK 21 o superior
- Maven 3.6+
- MySQL 8.0+
- Postman (opcional, para pruebas)

## ⚙️ Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/sistema-clinico-microservicios.git
cd sistema-clinico-microservicios
