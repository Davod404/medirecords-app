package medirecords_ms.receta.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import medirecords_ms.receta.dto.MedicamentoDTO;

@FeignClient(name = "medicamento")
public interface MedicamentoCliente {
    @GetMapping("/api/medicamentos/{id}")
    MedicamentoDTO buscarId(@PathVariable String id);

    @GetMapping("/api/medicamentos/buscar")
    List<MedicamentoDTO> buscarVariosId(@RequestParam("ids") String ids);
}
