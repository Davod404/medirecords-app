package medirecords_ms.paciente.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import medirecords_ms.paciente.dto.HospitalDTO;

@FeignClient(name = "hospital")
public interface HospitalCliente {
    @GetMapping("/api/hospitales/{id}")
    HospitalDTO buscarId(@PathVariable("id") Long id);
}