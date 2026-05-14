package medirecords_ms.historial.controller;

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
import medirecords_ms.historial.dto.HistorialRequestDTO;
import medirecords_ms.historial.dto.HistorialResponseDTO;
import medirecords_ms.historial.service.HistorialService;

@RestController
@RequestMapping("/api/historiales")
public class HistorialController {
    @Autowired private HistorialService historialService;
    
    @GetMapping
    public ResponseEntity<List<HistorialResponseDTO>> listarTodos(){
        return ResponseEntity.ok(historialService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id){
        if (!historialService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("no existe historial con id: " + id);
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(historialService.buscarDetallado(id));
    }
    
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody HistorialRequestDTO request){
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(historialService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody HistorialRequestDTO request){
        if (historialService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(historialService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("receta con id " + id + "no existe");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!historialService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("historial con id " + id + "no existe");
        }
        historialService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("historial con id " + id + "borrado exitosamente");
    }
}
