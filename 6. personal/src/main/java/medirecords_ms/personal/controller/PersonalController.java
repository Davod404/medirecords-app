package medirecords_ms.personal.controller;

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
import medirecords_ms.personal.dto.PersonalRequestDTO;
import medirecords_ms.personal.dto.PersonalResponseDTO;
import medirecords_ms.personal.service.PersonalService;

@RestController
@RequestMapping("/api/personal")
public class PersonalController {
    @Autowired private PersonalService personalService;

    @GetMapping
    public ResponseEntity<List<PersonalResponseDTO>> listarTodos(){
        return ResponseEntity.ok(personalService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id){
        if (!personalService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("no existe personal con id: " + id);
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(personalService.buscarDetallado(id));
    }
    
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody PersonalRequestDTO request){
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(personalService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody PersonalRequestDTO request){
        if (personalService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(personalService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("personal con id " + id + "no existe");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!personalService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("personal con id " + id + "no existe");
        }
        personalService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("personal con id " + id + "borrado exitosamente");
    }
}
