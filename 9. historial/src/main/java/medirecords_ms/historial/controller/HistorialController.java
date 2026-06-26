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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import medirecords_ms.historial.dto.HistorialRequestDTO;
import medirecords_ms.historial.dto.HistorialResponseDTO;
import medirecords_ms.historial.service.HistorialService;

@RestController
@RequestMapping("/api/historiales")
@Tag(name = "Historial", description = "Endpoints para la gestión de historiales clínicos")
public class HistorialController {
    @Autowired private HistorialService historialService;

    @Operation(summary = "Listar todos los historiales", description = "Retorna una lista con todos los historiales clínicos registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de historiales obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<HistorialResponseDTO>> listarTodos(){
        return ResponseEntity.ok(historialService.listarTodos());
    }

    @Operation(summary = "Buscar historial por ID", description = "Retorna los datos de un historial clínico según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial encontrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe historial con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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

    @Operation(summary = "Crear un nuevo historial", description = "Registra un nuevo historial clínico en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Historial creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody HistorialRequestDTO request){
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(historialService.crear(request));
    }

    @Operation(summary = "Actualizar historial", description = "Actualiza los datos de un historial clínico existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "ID inexistente o datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody HistorialRequestDTO request){
        if (historialService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(historialService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("historial con id " + id + " no existe");
    }

    @Operation(summary = "Eliminar historial", description = "Elimina un historial clínico del sistema según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Historial eliminado exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe historial con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!historialService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("historial con id " + id + " no existe");
        }
        historialService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("historial con id " + id + " borrado exitosamente");
    }
}