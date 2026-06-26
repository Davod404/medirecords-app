package medirecords_ms.consulta.controller;

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
import medirecords_ms.consulta.dto.ConsultaRequestDTO;
import medirecords_ms.consulta.dto.ConsultaResponseDTO;
import medirecords_ms.consulta.service.ConsultaService;

@RestController
@RequestMapping("/api/consultas")
@Tag(name = "Consulta", description = "Endpoints para la gestión de consultas médicas")
public class ConsultaController {
    @Autowired private ConsultaService consultaService;

    @Operation(summary = "Listar todas las consultas", description = "Retorna una lista con todas las consultas médicas registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de consultas obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<ConsultaResponseDTO>> listarTodos(){
        return ResponseEntity.ok(consultaService.listarTodos());
    }

    @Operation(summary = "Buscar consulta por ID", description = "Retorna los datos de una consulta médica según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta encontrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe consulta con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id){
        if (!consultaService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("no existe consulta con id: " + id);
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(consultaService.buscarDetallado(id));
    }

    @Operation(summary = "Buscar consultas por varios IDs", description = "Retorna una lista de consultas según los IDs proporcionados, separados por coma")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consultas encontradas exitosamente"),
        @ApiResponse(responseCode = "400", description = "IDs no proporcionados"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<ConsultaResponseDTO>> buscarVariosId(
            @Parameter(description = "IDs de consultas separados por coma", example = "1,2,3")
            @RequestParam("ids") String ids) {
        if (ids.isBlank()){
            ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("se debe ingresar identificadores de consultas");
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(consultaService.buscarVariosId(ids));
    }

    @Operation(summary = "Crear una nueva consulta", description = "Registra una nueva consulta médica en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Consulta creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody ConsultaRequestDTO request){
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(consultaService.crear(request));
    }

    @Operation(summary = "Actualizar consulta", description = "Actualiza los datos de una consulta médica existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "ID inexistente o datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody ConsultaRequestDTO request){
        if (consultaService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(consultaService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("consulta con id " + id + " no existe");
    }

    @Operation(summary = "Eliminar consulta", description = "Elimina una consulta médica del sistema según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Consulta eliminada exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe consulta con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!consultaService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("consulta con id " + id + " no existe");
        }
        consultaService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("consulta con id " + id + " borrado exitosamente");
    }
}