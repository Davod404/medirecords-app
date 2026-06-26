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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import medirecords_ms.hospital.dto.HospitalRequestDTO;
import medirecords_ms.hospital.dto.HospitalResponseDTO;
import medirecords_ms.hospital.service.HospitalService;

@RestController
@RequestMapping("/api/hospitales")
@Tag(name = "Hospital", description = "Endpoints para la gestión de hospitales")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    @Operation(summary = "Listar todos los hospitales", description = "Retorna una lista con todos los hospitales registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de hospitales obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<HospitalResponseDTO>> listarTodos(){
        return ResponseEntity
        .ok(hospitalService.listarTodos());
    }

    @Operation(summary = "Buscar hospital por ID", description = "Retorna los datos de un hospital específico según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hospital encontrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe hospital con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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

    @Operation(summary = "Crear un nuevo hospital", description = "Registra un nuevo hospital en el sistema con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Hospital creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody HospitalRequestDTO request){
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(hospitalService.crear(request));
    }

    @Operation(summary = "Actualizar hospital", description = "Actualiza los datos de un hospital existente identificado por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Hospital actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "ID inexistente o datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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

    @Operation(summary = "Eliminar hospital", description = "Elimina un hospital del sistema según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Hospital eliminado exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe hospital con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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