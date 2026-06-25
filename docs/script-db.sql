-- =========================================================
-- MEDIRECORDS - SCRIPT DE BASES DE DATOS
-- Sistema de microservicios para gestión hospitalaria
-- MySQL / XAMPP / Puerto 3306
-- =========================================================

CREATE DATABASE IF NOT EXISTS hospital_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS paciente_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS medicamento_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS cargo_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS especialidad_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS personal_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS consulta_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS receta_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS historial_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;


-- =========================================================
-- BD HOSPITAL
-- =========================================================

USE hospital_db;

CREATE TABLE IF NOT EXISTS hospital (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(255) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL
);


-- =========================================================
-- BD PACIENTE
-- =========================================================

USE paciente_db;

CREATE TABLE IF NOT EXISTS paciente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rut VARCHAR(12) NOT NULL,
    dv_rut CHAR(1) NOT NULL,
    nombres_paciente VARCHAR(100) NOT NULL,
    apellidos_paciente VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    fecha_nacimiento DATE NOT NULL,
    hospital_id BIGINT NULL
);


-- =========================================================
-- BD MEDICAMENTO
-- =========================================================

USE medicamento_db;

CREATE TABLE IF NOT EXISTS medicamento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    precio INT NULL,
    stock INT NOT NULL
);


-- =========================================================
-- BD CARGO
-- =========================================================

USE cargo_db;

CREATE TABLE IF NOT EXISTS cargo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cargo VARCHAR(100) NOT NULL UNIQUE
);


-- =========================================================
-- BD ESPECIALIDAD
-- =========================================================

USE especialidad_db;

CREATE TABLE IF NOT EXISTS especialidad (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    especialidad VARCHAR(100) NOT NULL UNIQUE
);


-- =========================================================
-- BD PERSONAL
-- =========================================================

USE personal_db;

CREATE TABLE IF NOT EXISTS personal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rut VARCHAR(12) NOT NULL UNIQUE,
    dv_rut CHAR(1) NOT NULL,
    nombres_personal VARCHAR(100) NOT NULL,
    apellidos_personal VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    email VARCHAR(100) NULL,
    cargo_id BIGINT NULL,
    especialidades_id LONGTEXT NULL
);


-- =========================================================
-- BD CONSULTA
-- =========================================================

USE consulta_db;

CREATE TABLE IF NOT EXISTS consulta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_consulta DATE NOT NULL,
    motivo VARCHAR(255) NOT NULL,
    diagnostico VARCHAR(255) NOT NULL,
    paciente_id BIGINT NULL,
    personal_id BIGINT NULL,
    hospital_id BIGINT NULL
);


-- =========================================================
-- BD RECETA
-- =========================================================

USE receta_db;

CREATE TABLE IF NOT EXISTS receta (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_receta DATE NOT NULL,
    instrucciones VARCHAR(255) NOT NULL,
    consulta_id BIGINT NULL,
    medicamentos_id LONGTEXT NULL
);


-- =========================================================
-- BD HISTORIAL
-- =========================================================

USE historial_db;

CREATE TABLE IF NOT EXISTS historial (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    notas VARCHAR(255) NULL,
    fecha_actualizacion DATE NOT NULL,
    paciente_id BIGINT NULL,
    consultas_id LONGTEXT NOT NULL
);


-- =========================================================
-- DATOS DE PRUEBA INICIALES
-- =========================================================

USE hospital_db;

INSERT INTO hospital (nombre, direccion, telefono) VALUES
('Hospital Central', 'Av. Principal 123', '271234567'),
('Clínica Norte', 'Av. Las Condes 456', '272345678'),
('Hospital Sur', 'Av. La Florida 789', '273456789');


USE cargo_db;

INSERT INTO cargo (cargo) VALUES
('Médico General'),
('Enfermero'),
('Especialista'),
('Administrativo');


USE especialidad_db;

INSERT INTO especialidad (especialidad) VALUES
('Cardiología'),
('Pediatría'),
('Neurología'),
('Medicina General');


USE paciente_db;

INSERT INTO paciente (rut, dv_rut, nombres_paciente, apellidos_paciente, telefono, email, fecha_nacimiento, hospital_id) VALUES
('12345678', '9', 'Juan Carlos', 'Pérez González', '912345678', 'juan@email.com', '1990-05-15', 1),
('23456789', '0', 'María Elena', 'Soto Rojas', '923456789', 'maria@email.com', '1985-08-22', 2),
('34567890', 'K', 'Pedro Andrés', 'Muñoz Silva', '934567890', 'pedro@email.com', '2000-12-10', 1);


USE personal_db;

INSERT INTO personal (rut, dv_rut, nombres_personal, apellidos_personal, telefono, email, cargo_id, especialidades_id) VALUES
('87654321', '1', 'Ana María', 'González López', '987654321', 'ana@email.com', 1, '1,4'),
('76543210', '5', 'Luis Alberto', 'Contreras Díaz', '976543210', 'luis@email.com', 3, '3');


USE medicamento_db;

INSERT INTO medicamento (nombre, marca, tipo, precio, stock) VALUES
('Paracetamol', 'Genfar', 'Tableta', 3500, 100),
('Ibuprofeno', 'Bayer', 'Cápsula', 5200, 80),
('Amoxicilina', 'Saval', 'Cápsula', 8900, 50);


USE consulta_db;

INSERT INTO consulta (fecha_consulta, motivo, diagnostico, paciente_id, personal_id, hospital_id) VALUES
('2026-06-22', 'Dolor de cabeza persistente', 'Migraña crónica', 1, 1, 1),
('2026-06-22', 'Mareos y visión borrosa', 'Hipertensión arterial', 2, 2, 2),
('2026-06-22', 'Control de rutina', 'Paciente sano', 3, 1, 1);


USE receta_db;

INSERT INTO receta (fecha_receta, instrucciones, consulta_id, medicamentos_id) VALUES
('2026-06-22', 'Tomar 500mg cada 8 horas por 7 días', 1, '1'),
('2026-06-22', 'Tomar 400mg cada 12 horas por 5 días', 2, '2');


USE historial_db;

INSERT INTO historial (notas, fecha_actualizacion, paciente_id, consultas_id) VALUES
('Paciente con antecedentes de migraña crónica', '2026-06-22', 1, '1'),
('Paciente con hipertensión controlada', '2026-06-22', 2, '2');