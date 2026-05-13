package medirecords_ms.historial.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistorialRequestDTO {
    private Long id;
    private String notas;
    private LocalDate fechaActualizacion;
    private Long pacienteId;
    private String consultasId;
}