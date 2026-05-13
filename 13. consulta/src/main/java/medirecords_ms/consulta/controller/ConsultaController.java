package medirecords_ms.consulta.controller;

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

import jakarta.validation.Valid;
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
        return ResponseEntity.ok(consultaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id){
        if (consultaService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("no existe consulta con id: " + id);
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(consultaService.buscarDetallado(id));
    }

    @GetMapping("/consultas")
    public ResponseEntity<?> buscarVariosId(@RequestParam("consultas") List<Long> consultas) {
        if (consultaService.buscarVariosId(consultas).isEmpty()){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("se debe ingresar identificadores de consultas");
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(consultaService.buscarVariosId(consultas));
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody ConsultaRequestDTO request){
        if (!consultaService.existeId(request.getId())){
            return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(consultaService.crear(request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("consulta con id " + request.getId() + " ya existe");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody ConsultaRequestDTO request){
        if (consultaService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(consultaService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("consulta con id " + id + "no existe");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!consultaService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("consulta con id " + id + "no existe");
        }
        consultaService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("consulta con id " + id + "borrado exitosamente");
    }
}
