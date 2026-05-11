package medirecords_ms.receta.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import medirecords_ms.receta.dto.ConsultaDTO;

@FeignClient(name = "consulta")
public interface ConsultaCliente {
    @GetMapping("/api/consultas/{id}")
    ConsultaDTO buscarId(@PathVariable Long id);
}
