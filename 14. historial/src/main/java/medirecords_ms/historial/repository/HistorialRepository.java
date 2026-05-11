package medirecords_ms.historial.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import medirecords_ms.historial.model.Historial;

public interface HistorialRepository extends JpaRepository<Historial, Long>{
}
