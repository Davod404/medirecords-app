package medirecords_ms.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HospitalResponseDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;    
}
