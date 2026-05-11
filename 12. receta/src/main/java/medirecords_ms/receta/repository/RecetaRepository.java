package medirecords_ms.receta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import medirecords_ms.receta.model.Receta;

public interface RecetaRepository extends JpaRepository<Receta, Long>{
}
