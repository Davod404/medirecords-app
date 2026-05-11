package medirecords_ms.consulta.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consultas")
@Data
public class Consulta {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate fechaConsulta;
    @Column(nullable = false)
    private String motivo;
    @Column(nullable = false)
    private String diagnostico;
    
    private Long pacienteId;
    private Long personalId;
    private Long hospitalId;

}
