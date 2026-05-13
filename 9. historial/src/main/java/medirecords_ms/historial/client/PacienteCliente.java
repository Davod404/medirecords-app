package medirecords_ms.historial.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import medirecords_ms.historial.dto.PacienteDTO;

@FeignClient(name = "paciente")
public interface PacienteCliente {
    @GetMapping("/api/pacientes/{id}")
    PacienteDTO buscarId(@PathVariable Long id);
}
