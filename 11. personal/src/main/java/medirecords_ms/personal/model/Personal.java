package medirecords_ms.personal.model;

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
@Table(name = "personales")
@Data
public class Personal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rut;
    @Column(nullable = false)
    private Character dvRut;
    @Column(nullable = false)
    private String nombresPersonal;
    @Column(nullable = false)
    private String apellidosPersonal;
    @Column(nullable = false)
    private String telefono;
    @Column
    @Email
    private String email;

    private Long cargoId;

    @Column(columnDefinition = "LONGTEXT")
    private String especialidadesId;
}
