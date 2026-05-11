package medirecords_ms.hospital.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hospitales")
@Data
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="error: nombre no puede ser nulo")
    @Column(nullable = false)
    private String nombre;
    @NotBlank(message="error: direccion no puede ser nula")
    @Column(nullable = false, unique = true)
    private String direccion;
    @NotBlank(message="error: numero no puede ser nulo")
    @Column(nullable = false)
    private String telefono;
    
}
