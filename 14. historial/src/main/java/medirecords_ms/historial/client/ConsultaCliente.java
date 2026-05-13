package medirecords_ms.historial.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import medirecords_ms.historial.dto.ConsultaDTO;

@FeignClient(name = "consulta")
public interface ConsultaCliente {
    @GetMapping("/api/consultas/{id}")
    ConsultaDTO buscarDetallado(@PathVariable("id") Long id);

    @GetMapping("/api/consultas")
    List<ConsultaDTO> buscarVariosId(@RequestParam("consultas") List<Long> consultas);
}
