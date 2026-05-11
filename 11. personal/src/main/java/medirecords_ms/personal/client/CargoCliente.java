package medirecords_ms.personal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import medirecords_ms.personal.dto.CargoDTO;

@FeignClient(name = "cargo")
public interface CargoCliente {
    @GetMapping("/api/cargos/{id}")
    CargoDTO buscarId(@PathVariable("id") Long cargoId);
    
    @GetMapping("/api/cargos/existe/{id}")
    Boolean existeCargo(@PathVariable("id") Long id);
}
