package medirecords_ms.hospital.controller;

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
import medirecords_ms.hospital.dto.HospitalRequestDTO;
import medirecords_ms.hospital.dto.HospitalResponseDTO;
import medirecords_ms.hospital.service.HospitalService;

@RestController
@RequestMapping("/api/hospitales")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;
    
    @GetMapping
    public ResponseEntity<List<HospitalResponseDTO>> listarTodos(){
        return ResponseEntity
        .ok(hospitalService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id){
        if (!hospitalService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("no existe hospital con id: " + id);
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(hospitalService.buscarDetallado(id));
    }
    
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody HospitalRequestDTO request){
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(hospitalService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody HospitalRequestDTO request){
        if (hospitalService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(hospitalService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("hospital con id " + id + "no existe");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!hospitalService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("hospital con id " + id + "no existe");
        }
        hospitalService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("hospital con id " + id + "borrado exitosamente");
    }
}
