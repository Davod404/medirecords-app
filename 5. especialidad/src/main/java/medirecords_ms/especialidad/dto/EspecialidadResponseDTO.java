package medirecords_ms.especialidad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EspecialidadResponseDTO {
    private Long id;
    private String especialidad;
}