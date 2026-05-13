package medirecords_ms.receta.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecetaRequestDTO {
    private Long id;
    private LocalDate fechaReceta;
    private String instrucciones;

    private Long consultaId;
    private String medicamentosId;
}
