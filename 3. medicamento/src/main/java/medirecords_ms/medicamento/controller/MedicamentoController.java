package medirecords_ms.medicamento.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import medirecords_ms.medicamento.dto.MedicamentoRequestDTO;
import medirecords_ms.medicamento.dto.MedicamentoResponseDTO;
import medirecords_ms.medicamento.service.MedicamentoService;

@RestController
@RequestMapping("/api/medicamentos")
@Tag(name = "Medicamento", description = "Endpoints para la gestión de medicamentos")
public class MedicamentoController {
    @Autowired private MedicamentoService medicamentoService;

    @Operation(summary = "Listar todos los medicamentos", description = "Retorna una lista con todos los medicamentos registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de medicamentos obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<MedicamentoResponseDTO>> listarTodos(){
        return ResponseEntity.ok(medicamentoService.listarTodos());
    }

    @Operation(summary = "Buscar medicamento por ID", description = "Retorna los datos de un medicamento específico según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Medicamento encontrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe medicamento con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id){
        if (!medicamentoService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("no existe medicamento con id: " + id);
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(medicamentoService.buscarDetallado(id));
    }

    @Operation(summary = "Buscar medicamentos por varios IDs", description = "Retorna una lista de medicamentos según los IDs proporcionados, separados por coma")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Medicamentos encontrados exitosamente"),
        @ApiResponse(responseCode = "400", description = "IDs no proporcionados"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<MedicamentoResponseDTO>> buscarVariosId(
            @Parameter(description = "IDs de medicamentos separados por coma", example = "1,2,3")
            @RequestParam("ids") String ids) {
        if (ids.isBlank()){
            ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("se debe ingresar identificadores de medicamento");
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(medicamentoService.buscarVariosId(ids));
    }

    @Operation(summary = "Crear un nuevo medicamento", description = "Registra un nuevo medicamento en el sistema con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Medicamento creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody MedicamentoRequestDTO request){
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(medicamentoService.crear(request));
    }

    @Operation(summary = "Actualizar medicamento", description = "Actualiza los datos de un medicamento existente identificado por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Medicamento actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "ID inexistente o datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody MedicamentoRequestDTO request){
        if (medicamentoService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(medicamentoService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("medicamento con id " + id + " no existe");
    }

    @Operation(summary = "Eliminar medicamento", description = "Elimina un medicamento del sistema según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Medicamento eliminado exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe medicamento con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!medicamentoService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("medicamento con id " + id + " no existe");
        }
        medicamentoService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("medicamento con id " + id + " borrado exitosamente");
    }
}