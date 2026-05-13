package medirecords_ms.personal.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonalRequestDTO {
    private Long id;
    private String rut;
    private Character dvRut;
    private String nombresPersonal;
    private String apellidosPersonal;
    private String telefono;
    private String email;

    private Long cargoId;
    private String especialidadesId;
}
