package medirecords_ms.especialidad.controller;

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
import medirecords_ms.especialidad.dto.EspecialidadRequestDTO;
import medirecords_ms.especialidad.dto.EspecialidadResponseDTO;
import medirecords_ms.especialidad.service.EspecialidadService;

@RestController
@RequestMapping("/api/especialidades")
@Tag(name = "Especialidad", description = "Endpoints para la gestión de especialidades médicas")
public class EspecialidadController {
    @Autowired private EspecialidadService especialidadService;

    @Operation(summary = "Listar todas las especialidades", description = "Retorna una lista con todas las especialidades registradas en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de especialidades obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<EspecialidadResponseDTO>> listarTodos(){
        return ResponseEntity.ok(especialidadService.listarTodos());
    }

    @Operation(summary = "Buscar especialidad por ID", description = "Retorna los datos de una especialidad específica según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidad encontrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe especialidad con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id){
        if (!especialidadService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("no existe especialidad con id: " + id);
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(especialidadService.buscarDetallado(id));
    }

    @Operation(summary = "Buscar especialidades por varios IDs", description = "Retorna una lista de especialidades según los IDs proporcionados, separados por coma")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidades encontradas exitosamente"),
        @ApiResponse(responseCode = "400", description = "IDs no proporcionados"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<EspecialidadResponseDTO>> buscarVariosId(
            @Parameter(description = "IDs de especialidades separados por coma", example = "1,2,3")
            @RequestParam("ids") String ids) {
        if (ids.isBlank()){
            ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("se debe ingresar identificadores de especialidades");
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(especialidadService.buscarVariosId(ids));
    }

    @Operation(summary = "Crear una nueva especialidad", description = "Registra una nueva especialidad en el sistema con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Especialidad creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody EspecialidadRequestDTO request){
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(especialidadService.crear(request));
    }

    @Operation(summary = "Actualizar especialidad", description = "Actualiza los datos de una especialidad existente identificada por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidad actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "ID inexistente o datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody EspecialidadRequestDTO request){
        if (especialidadService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(especialidadService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("especialidad con id " + id + " no existe");
    }

    @Operation(summary = "Eliminar especialidad", description = "Elimina una especialidad del sistema según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Especialidad eliminada exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe especialidad con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!especialidadService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("especialidad con id " + id + " no existe");
        }
        especialidadService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("especialidad con id " + id + " borrado exitosamente");
    }
}