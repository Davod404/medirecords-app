package medirecords_ms.cargo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CargoResponseDTO {
    private Long id;
    private String cargo;
}