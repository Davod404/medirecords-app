    package medirecords_ms.medicamento.controller;

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
import medirecords_ms.medicamento.dto.MedicamentoRequestDTO;
import medirecords_ms.medicamento.dto.MedicamentoResponseDTO;
import medirecords_ms.medicamento.service.MedicamentoService;

@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoController {
    @Autowired private MedicamentoService medicamentoService;

    @GetMapping
    public ResponseEntity<List<MedicamentoResponseDTO>> listarTodos(){
        return ResponseEntity.ok(medicamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id){
        if (medicamentoService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("no existe medicamento con id: " + id);
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(medicamentoService.buscarDetallado(id));
    }

    @GetMapping("/medicamentos")
    public ResponseEntity<List<MedicamentoResponseDTO>> buscarVariosId(@RequestParam("consultas") List<Long> consultas) {
        if (medicamentoService.buscarVariosId(consultas).isEmpty()){
            ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("se debe ingresar identificadores de medicamentos");
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(medicamentoService.buscarVariosId(consultas));
    }

    @PostMapping
    public ResponseEntity<?> nuevoPaciente(@Valid @RequestBody MedicamentoRequestDTO request){
        if (!medicamentoService.existeId(request.getId())){
            return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(medicamentoService.crear(request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("medicamento con id " + request.getId() + " ya existe");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody MedicamentoRequestDTO request){
        if (medicamentoService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(medicamentoService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("medicamento con id " + id + "no existe");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!medicamentoService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("medicamento con id " + id + "no existe");
        }
        medicamentoService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("medicamento con id " + id + "borrado exitosamente");
    }
}