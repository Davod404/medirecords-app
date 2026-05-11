package medirecords_ms.personal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import medirecords_ms.personal.model.Personal;

public interface PersonalRepository extends JpaRepository<Personal, Long>{
    
}
