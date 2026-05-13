package medirecords_ms.receta.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import medirecords_ms.receta.dto.ConsultaDTO;

@FeignClient(name = "consulta")
public interface ConsultaCliente {
    @GetMapping("/api/consultas/{id}")
    ConsultaDTO buscarId(@PathVariable Long id);

    @GetMapping("/api/consultas/buscar")
    List<ConsultaDTO> buscarVariosId(@RequestParam("ids") String ids);
}
