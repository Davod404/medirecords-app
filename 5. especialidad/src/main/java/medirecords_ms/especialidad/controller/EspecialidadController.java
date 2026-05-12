package medirecords_ms.especialidad.controller;

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
import medirecords_ms.especialidad.dto.EspecialidadRequestDTO;
import medirecords_ms.especialidad.dto.EspecialidadResponseDTO;
import medirecords_ms.especialidad.service.EspecialidadService;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {
    @Autowired private EspecialidadService especialidadService;   

    @GetMapping
    public ResponseEntity<List<EspecialidadResponseDTO>> listarTodos(){
        return ResponseEntity.ok(especialidadService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id){
        if (!especialidadService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("no existe especialidad con id: " + id);
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(especialidadService.buscarDetallado(id));
    }


    @GetMapping("/especialidades")
    public ResponseEntity<List<EspecialidadResponseDTO>> buscarVariosId(@RequestParam("consultas") List<Long> consultas) {
        if (especialidadService.buscarVariosId(consultas).isEmpty()){
            ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("se debe ingresar identificadores de especialidades");
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(especialidadService.buscarVariosId(consultas));
    }

    @PostMapping
    public ResponseEntity<?> nuevoPaciente(@Valid @RequestBody EspecialidadRequestDTO request){
        if (!especialidadService.existeId(request.getId())){
            return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(especialidadService.crear(request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("especialidad con id " + request.getId() + " ya existe");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody EspecialidadRequestDTO request){
        if (especialidadService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(especialidadService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("especialidad con id " + id + "no existe");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!especialidadService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("especialidad con id " + id + "no existe");
        }
        especialidadService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("especialidad con id " + id + "borrado exitosamente");
    }
}