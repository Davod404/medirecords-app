package medirecords_ms.personal.controller;

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
import medirecords_ms.personal.dto.PersonalRequestDTO;
import medirecords_ms.personal.dto.PersonalResponseDTO;
import medirecords_ms.personal.service.PersonalService;

@RestController
@RequestMapping("/api/personal")
@Tag(name = "Personal", description = "Endpoints para la gestión del personal médico")
public class PersonalController {
    @Autowired private PersonalService personalService;

    @Operation(summary = "Listar todo el personal", description = "Retorna una lista con todo el personal médico registrado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de personal obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<PersonalResponseDTO>> listarTodos(){
        return ResponseEntity.ok(personalService.listarTodos());
    }

    @Operation(summary = "Buscar personal por ID", description = "Retorna los datos de un miembro del personal según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Personal encontrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe personal con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarId(@PathVariable Long id){
        if (!personalService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("no existe personal con id: " + id);
        }
        return ResponseEntity
        .status(HttpStatus.OK)
        .body(personalService.buscarDetallado(id));
    }

    @Operation(summary = "Crear nuevo personal", description = "Registra un nuevo miembro del personal médico en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Personal creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody PersonalRequestDTO request){
        return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(personalService.crear(request));
    }

    @Operation(summary = "Actualizar personal", description = "Actualiza los datos de un miembro del personal existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Personal actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "ID inexistente o datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody PersonalRequestDTO request){
        if (personalService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(personalService.actualizar(id, request));
        }
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body("personal con id " + id + " no existe");
    }

    @Operation(summary = "Eliminar personal", description = "Elimina un miembro del personal médico del sistema según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Personal eliminado exitosamente"),
        @ApiResponse(responseCode = "400", description = "No existe personal con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id){
        if(!personalService.existeId(id)){
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("personal con id " + id + " no existe");
        }
        personalService.borrar(id);
        return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body("personal con id " + id + " borrado exitosamente");
    }
}