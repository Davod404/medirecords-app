package medirecords_ms.receta.controller;

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
import medirecords_ms.receta.dto.RecetaRequestDTO;
import medirecords_ms.receta.dto.RecetaResponseDTO;
import medirecords_ms.receta.service.RecetaService;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {
    @Autowired private RecetaService recetaService;

    @GetMapping
    public ResponseEntity<List<RecetaResponseDTO>> listarTodos(){
        return ResponseEntity.ok(recetaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id){
        if (!recetaService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("no existe receta con id: " + id);
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(recetaService.buscarDetallado(id));
    }
    
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody RecetaRequestDTO request){
        if (!recetaService.existeId(request.getId())){
            return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(recetaService.crear(request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("receta con id " + request.getId() + " ya existe");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody RecetaRequestDTO request){
        if (recetaService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(recetaService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("receta con id " + id + "no existe");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!recetaService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("receta con id " + id + "no existe");
        }
        recetaService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("receta con id " + id + "borrado exitosamente");
    }
}
