package medirecords_ms.cargo.controller;

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
import medirecords_ms.cargo.dto.CargoRequestDTO;
import medirecords_ms.cargo.dto.CargoResponseDTO;
import medirecords_ms.cargo.service.CargoService;

@RestController
@RequestMapping("/api/cargos")
public class CargoController {
    @Autowired private CargoService cargoService;

    @GetMapping
    public ResponseEntity<List<CargoResponseDTO>> listarTodos(){
        return ResponseEntity.ok(cargoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id){
        if (!cargoService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("no existe cargo con id: " + id);
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(cargoService.buscarDetallado(id));
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody CargoRequestDTO request){
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(cargoService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody CargoRequestDTO request){
        if (cargoService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(cargoService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("cargo con id " + id + "no existe");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!cargoService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("cargo con id " + id + "no existe");
        }
        cargoService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("cargo con id " + id + "borrado exitosamente");
    }
}