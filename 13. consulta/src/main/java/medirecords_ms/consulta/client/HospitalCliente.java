package medirecords_ms.consulta.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import medirecords_ms.consulta.dto.HospitalDTO;

@FeignClient(name = "hospital")
public interface HospitalCliente {
    @GetMapping("/api/hospitales/{id}")
    HospitalDTO buscarHospitalPorId(@PathVariable("id") Long id);
}
