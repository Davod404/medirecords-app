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

import medirecords_ms.receta.dto.RecetaRequestDTO;
import medirecords_ms.receta.dto.RecetaResponseDTO;
import medirecords_ms.receta.service.RecetaService;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {
    @Autowired
    private RecetaService recetaService;

    @GetMapping
    public ResponseEntity<List<RecetaResponseDTO>> listarTodos(){
        return ResponseEntity.status(HttpStatus.OK).body(recetaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecetaResponseDTO> buscarDetalle(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(recetaService.buscarDetallada(id));
    }
    
    @PostMapping
    public ResponseEntity<RecetaResponseDTO> nuevoReceta(@RequestBody RecetaRequestDTO receta){
        return ResponseEntity.status(HttpStatus.CREATED).body(recetaService.crearReceta(receta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecetaResponseDTO> actualizarReceta(@PathVariable Long id, @RequestBody RecetaRequestDTO receta){
        return ResponseEntity.status(HttpStatus.OK).body(recetaService.actualizarReceta(id, receta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarReceta(@PathVariable Long id){
        recetaService.borrarReceta(id);
        return ResponseEntity.status(HttpStatus.OK).body("Borrado: " + id);
    }
}
