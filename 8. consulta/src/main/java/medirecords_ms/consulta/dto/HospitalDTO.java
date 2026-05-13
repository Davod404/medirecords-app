package medirecords_ms.consulta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HospitalDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
}
