# MediRecords - Orden de ejecución

## Requisitos previos

Antes de levantar el sistema se debe tener iniciado:

- XAMPP (o Laragon)
- MySQL en puerto 3306 (o el configurado)
- Java 24 instalado
- Maven 3.9.15 instalado
- Eureka Server
- Microservicios de negocio
- API Gateway

## Bases de datos

El proyecto usa una base de datos independiente por microservicio:

| Microservicio  | Base de datos      |
|----------------|--------------------|
| hospital       | hospital_db        |
| paciente       | paciente_db        |
| medicamento    | medicamento_db     |
| cargo          | cargo_db           |
| especialidad   | especialidad_db    |
| personal       | personal_db        |
| consulta       | consulta_db        |
| receta         | receta_db          |
| historial      | historial_db       |

El script de creación se encuentra en:

```text
docs/bd-general.sql