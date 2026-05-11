package medirecords_ms.medicamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import medirecords_ms.medicamento.model.Medicamento;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long>{
    
}
