package medirecords_ms.consulta.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PacienteDTO {
    private Long id;
    private String rut;
    private Character dvRut;
    private String nombresPaciente;
    private String apellidosPaciente;
    private String telefono;
    private String email;
    private LocalDate fechaNacimiento;
}
