package medirecords_ms.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import medirecords_ms.hospital.model.Hospital;

public interface HospitalRepository extends JpaRepository<Hospital, Long>{
}
