package medirecords_ms.consulta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import medirecords_ms.consulta.model.Consulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long>{
}
