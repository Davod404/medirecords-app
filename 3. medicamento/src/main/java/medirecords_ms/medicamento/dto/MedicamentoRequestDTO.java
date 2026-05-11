package medirecords_ms.medicamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicamentoRequestDTO {
    public Long id;
    public String nombre;
    public String marca;
    public String tipo;
    public Integer precio;
    public Integer stock;
}
