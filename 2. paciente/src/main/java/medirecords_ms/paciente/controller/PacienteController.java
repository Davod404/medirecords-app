package medirecords_ms.paciente.controller;

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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import medirecords_ms.paciente.dto.PacienteRequestDTO;
import medirecords_ms.paciente.dto.PacienteResponseDTO;
import medirecords_ms.paciente.service.PacienteService;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {
    @Autowired private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> listarTodos(){
        return ResponseEntity.ok(pacienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id){
        if (!pacienteService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("no existe hospital con id: " + id);
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(pacienteService.buscarDetallado(id));
    }

    @PostMapping
    public ResponseEntity<?> nuevoPaciente(@Valid @RequestBody PacienteRequestDTO request){
        if (!pacienteService.existeId(request.getId())){
            return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(pacienteService.crear(request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("hospital con id " + request.getId() + " ya existe");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody PacienteRequestDTO request){
        if (pacienteService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(pacienteService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("hospital con id " + id + "no existe");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!pacienteService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("hospital con id " + id + "no existe");
        }
        pacienteService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("hospital con id " + id + "borrado exitosamente");
    }
}
