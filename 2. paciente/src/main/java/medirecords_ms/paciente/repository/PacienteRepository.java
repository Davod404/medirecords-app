package medirecords_ms.paciente.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import medirecords_ms.paciente.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long>{

}
