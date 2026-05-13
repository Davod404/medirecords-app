package medirecords_ms.personal.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import medirecords_ms.personal.dto.EspecialidadDTO;

@FeignClient(name = "especialidad")
public interface EspecialidadCliente {
    @GetMapping("/api/especialidades/{id}")
    EspecialidadDTO buscarId(@PathVariable("id") Long id);

    @GetMapping("/api/especialidades/buscar")
    List<EspecialidadDTO> buscarVariosId(@RequestParam("ids") String ids);
}
