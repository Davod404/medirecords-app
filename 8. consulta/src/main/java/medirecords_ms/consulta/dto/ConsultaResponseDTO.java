package medirecords_ms.consulta.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConsultaResponseDTO {
    private Long id;
    private LocalDate fechaConsulta;
    private String motivo;
    private String diagnostico;
    
    private PacienteDTO paciente;
    private PersonalDTO personal;
    private HospitalDTO hospital;
}
