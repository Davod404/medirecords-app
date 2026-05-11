package medirecords_ms.cargo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import medirecords_ms.cargo.model.Cargo;

public interface CargoRepository extends JpaRepository<Cargo, Long>{
    
}
