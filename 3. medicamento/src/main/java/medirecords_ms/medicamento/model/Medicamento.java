package medirecords_ms.medicamento.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medicamentos")
@Data
public class Medicamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String nombre;
    @Column(nullable = false)
    public String marca;
    @Column(nullable = false)
    public String tipo;
    @Column(nullable = true)
    public Integer precio;
    @Column(nullable = false)
    public Integer stock;
}
