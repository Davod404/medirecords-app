package medirecords_ms.paciente.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pacientes")
@Data
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String rut;

    @Column(nullable = false)
    private Character dvRut;

    @Column(nullable = false)
    private String nombresPaciente;
    
    @Column(nullable = false)
    private String apellidosPaciente;
    
    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false, unique = true)
    @Email
    private String email;   

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    private Long hospitalId;
}
