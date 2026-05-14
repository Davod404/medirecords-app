package medirecords_ms.historial.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConsultaDTO {
    private Long id;
    private LocalDate fechaConsulta;
    private String motivo;
    private String diagnostico;
}
