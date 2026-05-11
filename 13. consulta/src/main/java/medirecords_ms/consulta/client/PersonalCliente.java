package medirecords_ms.consulta.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import medirecords_ms.consulta.dto.PersonalDTO;

@FeignClient(name = "personal")
public interface PersonalCliente {
    @GetMapping("/api/personal/{id}")
    PersonalDTO buscarDetalle(@PathVariable("id") Long id);
}
