package medirecords_ms.consulta.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import medirecords_ms.consulta.dto.ConsultaRequestDTO;
import medirecords_ms.consulta.dto.ConsultaResponseDTO;
import medirecords_ms.consulta.service.ConsultaService;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {
    @Autowired
    private ConsultaService consultaService;

    @GetMapping
    public ResponseEntity<List<ConsultaResponseDTO>> listarTodos(){
        return ResponseEntity.status(HttpStatus.OK).body(consultaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> buscarDetalle(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(consultaService.buscarDetalle(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ConsultaResponseDTO>> buscarVariosId(@RequestParam("ids") String ids) {
        String[] partes = ids.split(",");
        List<Long> listaIds = new ArrayList<>();
        for (String parte : partes) {
            listaIds.add(Long.parseLong(parte));
        }
        return ResponseEntity.status(HttpStatus.OK).body(consultaService.buscarVariosId(listaIds));
    }

    @PostMapping
    public ResponseEntity<ConsultaResponseDTO> crearConsulta(@RequestBody ConsultaRequestDTO Consulta){
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.crearConsulta(Consulta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> actualizarConsulta(@PathVariable Long id, @RequestBody ConsultaRequestDTO Consulta){
        return ResponseEntity.status(HttpStatus.OK).body(consultaService.actualizarConsulta(id, Consulta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarConsulta(@PathVariable Long id){
        consultaService.borrarConsulta(id);
        return ResponseEntity.status(HttpStatus.OK).body("Borrado: " + id);
    }
}
