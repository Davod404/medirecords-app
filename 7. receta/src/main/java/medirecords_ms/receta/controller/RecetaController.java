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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import medirecords_ms.receta.dto.RecetaRequestDTO;
import medirecords_ms.receta.dto.RecetaResponseDTO;
import medirecords_ms.receta.service.RecetaService;

@RestController
@RequestMapping("/api/recetas")
@Tag(name = "Receta", description = "Endpoints para la gestión de recetas médicas")
public class RecetaController {
    @Autowired private RecetaService recetaService;

    @Operation(summary = "Listar todas las recetas", description = "Retorna una lista con todas las recetas médicas registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de recetas obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<RecetaResponseDTO>> listarTodos(){
        return ResponseEntity.ok(recetaService.listarTodos());
    }

    @Operation(summary = "Buscar receta por ID", description = "Retorna los datos de una receta médica según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receta encontrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe receta con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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

    @Operation(summary = "Crear una nueva receta", description = "Registra una nueva receta médica en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Receta creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody RecetaRequestDTO request){
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(recetaService.crear(request));
    }

    @Operation(summary = "Actualizar receta", description = "Actualiza los datos de una receta médica existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Receta actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "ID inexistente o datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody RecetaRequestDTO request){
        if (recetaService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(recetaService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("receta con id " + id + " no existe");
    }

    @Operation(summary = "Eliminar receta", description = "Elimina una receta médica del sistema según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Receta eliminada exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe receta con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!recetaService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("receta con id " + id + " no existe");
        }
        recetaService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("receta con id " + id + " borrado exitosamente");
    }
}