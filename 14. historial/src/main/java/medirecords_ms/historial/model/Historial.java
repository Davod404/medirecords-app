package medirecords_ms.historial.model;

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
@Table(name = "historiales")
@Data
public class Historial {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String notas;

    @Column(nullable = false)
    private LocalDate fechaActualizacion;

    private Long pacienteId;
    @Column(columnDefinition = "LONGTEXT")
    private String consultasId;
}
