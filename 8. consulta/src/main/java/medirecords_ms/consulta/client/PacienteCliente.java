package medirecords_ms.consulta.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import medirecords_ms.consulta.dto.PacienteDTO;

@FeignClient(name = "paciente")
public interface PacienteCliente {
    @GetMapping("/api/pacientes/{id}")
    PacienteDTO buscarDetallado(@PathVariable("id") Long id);
}
