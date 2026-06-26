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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import medirecords_ms.cargo.dto.CargoRequestDTO;
import medirecords_ms.cargo.dto.CargoResponseDTO;
import medirecords_ms.cargo.service.CargoService;

@RestController
@RequestMapping("/api/cargos")
@Tag(name = "Cargo", description = "Endpoints para la gestión de cargos")
public class CargoController {
    @Autowired private CargoService cargoService;

    @Operation(summary = "Listar todos los cargos", description = "Retorna una lista con todos los cargos registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de cargos obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<CargoResponseDTO>> listarTodos(){
        return ResponseEntity.ok(cargoService.listarTodos());
    }

    @Operation(summary = "Buscar cargo por ID", description = "Retorna los datos de un cargo específico según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cargo encontrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe cargo con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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

    @Operation(summary = "Crear un nuevo cargo", description = "Registra un nuevo cargo en el sistema con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cargo creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody CargoRequestDTO request){
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(cargoService.crear(request));
    }

    @Operation(summary = "Actualizar cargo", description = "Actualiza los datos de un cargo existente identificado por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cargo actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "ID inexistente o datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody CargoRequestDTO request){
        if (cargoService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(cargoService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("cargo con id " + id + " no existe");
    }

    @Operation(summary = "Eliminar cargo", description = "Elimina un cargo del sistema según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cargo eliminado exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe cargo con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!cargoService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("cargo con id " + id + " no existe");
        }
        cargoService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("cargo con id " + id + " borrado exitosamente");
    }
}