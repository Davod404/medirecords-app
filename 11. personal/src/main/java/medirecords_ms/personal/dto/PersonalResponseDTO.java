package medirecords_ms.personal.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonalResponseDTO {
    private Long id;
    private String rut;
    private Character dvRut;
    private String nombresPersonal;
    private String apellidosPersonal;
    private String telefono;
    private String email;

    private CargoDTO cargo;
    private List<EspecialidadDTO> especialidades;   
}
