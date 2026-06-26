package medirecords_ms.paciente.controller;

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
import medirecords_ms.paciente.dto.PacienteRequestDTO;
import medirecords_ms.paciente.dto.PacienteResponseDTO;
import medirecords_ms.paciente.service.PacienteService;

@RestController
@RequestMapping("/api/pacientes")
@Tag(name = "Paciente", description = "Endpoints para la gestión de pacientes")
public class PacienteController {
    @Autowired private PacienteService pacienteService;

    @Operation(summary = "Listar todos los pacientes", description = "Retorna una lista con todos los pacientes registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pacientes obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> listarTodos(){
        return ResponseEntity.ok(pacienteService.listarTodos());
    }

    @Operation(summary = "Buscar paciente por ID", description = "Retorna los datos de un paciente específico según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente encontrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe paciente con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id){
        if (!pacienteService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("no existe paciente con id: " + id);
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(pacienteService.buscarDetallado(id));
    }

    @Operation(summary = "Crear un nuevo paciente", description = "Registra un nuevo paciente en el sistema con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Paciente creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> nuevoPaciente(@Valid @RequestBody PacienteRequestDTO request){
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(pacienteService.crear(request));
    }

    @Operation(summary = "Actualizar paciente", description = "Actualiza los datos de un paciente existente identificado por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "ID inexistente o datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody PacienteRequestDTO request){
        if (pacienteService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(pacienteService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("paciente con id " + id + " no existe");
    }

    @Operation(summary = "Eliminar paciente", description = "Elimina un paciente del sistema según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Paciente eliminado exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe paciente con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!pacienteService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("paciente con id " + id + " no existe");
        }
        pacienteService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("paciente con id " + id + " borrado exitosamente");
    }
}