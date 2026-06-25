package medirecords_ms.paciente.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import medirecords_ms.paciente.dto.PacienteRequestDTO;
import medirecords_ms.paciente.dto.PacienteResponseDTO;
import medirecords_ms.paciente.service.PacienteService;

@ExtendWith(MockitoExtension.class)
public class PacienteControllerTest {

    @Mock
    private PacienteService pacienteService;

    @InjectMocks
    private PacienteController pacienteController;

    private PacienteResponseDTO pacienteResponse;
    private PacienteRequestDTO pacienteRequest;

    @BeforeEach
    void setUp() {
        pacienteResponse = new PacienteResponseDTO(1L, "12345678", '9', "Juan Carlos",
                "Pérez González", "912345678", "juan@email.com", LocalDate.of(1990, 5, 15), null);
        pacienteRequest = new PacienteRequestDTO(null, "12345678", '9', "Juan Carlos",
                "Pérez González", "912345678", "juan@email.com", LocalDate.of(1990, 5, 15), 1L);
    }

    /*
     * TEST 1: GET /api/pacientes
     */
    @Test
    void listarTodos_deberiaRetornarStatus200YListaDePacientes() {
        when(pacienteService.listarTodos()).thenReturn(Arrays.asList(pacienteResponse));

        ResponseEntity<List<PacienteResponseDTO>> respuesta = pacienteController.listarTodos();

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        assertEquals("Juan Carlos", respuesta.getBody().get(0).getNombresPaciente());

        verify(pacienteService, times(1)).listarTodos();

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el endpoint GET /api/pacientes no responde 200 OK,
           verificar que el Service esté retornando datos correctamente.
        */
    }

    /*
     * TEST 2: GET /api/pacientes/{id} - Existe
     */
    @Test
    void buscarId_cuandoExiste_deberiaRetornarStatus200YPaciente() {
        when(pacienteService.existeId(1L)).thenReturn(true);
        when(pacienteService.buscarDetallado(1L)).thenReturn(pacienteResponse);

        ResponseEntity<?> respuesta = pacienteController.buscarId(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        PacienteResponseDTO body = (PacienteResponseDTO) respuesta.getBody();
        assertNotNull(body);
        assertEquals("Juan Carlos", body.getNombresPaciente());

        verify(pacienteService, times(1)).existeId(1L);
        verify(pacienteService, times(1)).buscarDetallado(1L);

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el ID existe pero el DTO retornado es incorrecto,
           verificar el mapeo en buscarDetallado() del Service.
        */
    }

    /*
     * TEST 3: GET /api/pacientes/{id} - No existe
     */
    @Test
    void buscarId_cuandoNoExiste_deberiaRetornarStatus400() {
        when(pacienteService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = pacienteController.buscarId(99L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("no existe hospital con id: 99", respuesta.getBody());

        verify(pacienteService, times(1)).existeId(99L);
        verify(pacienteService, never()).buscarDetallado(anyLong());

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           El mensaje dice "hospital" pero es un paciente.
           Sería bueno corregir el mensaje en el Controller.
        */
    }

    /*
     * TEST 4: POST /api/pacientes
     */
    @Test
    void nuevoPaciente_deberiaRetornarStatus201() {
        when(pacienteService.crear(any(PacienteRequestDTO.class))).thenReturn(pacienteResponse);

        ResponseEntity<?> respuesta = pacienteController.nuevoPaciente(pacienteRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        PacienteResponseDTO body = (PacienteResponseDTO) respuesta.getBody();
        assertNotNull(body);
        assertEquals("Juan Carlos", body.getNombresPaciente());

        verify(pacienteService, times(1)).crear(any(PacienteRequestDTO.class));

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el @Valid falla (campos nulos), el Controller debería retornar 400.
           Este test asume datos válidos.
        */
    }

    /*
     * TEST 5: PUT /api/pacientes/{id} - Existe
     */
    @Test
    void actualizar_cuandoExiste_deberiaRetornarStatus200() {
        when(pacienteService.existeId(1L)).thenReturn(true);
        when(pacienteService.actualizar(eq(1L), any(PacienteRequestDTO.class))).thenReturn(pacienteResponse);

        ResponseEntity<?> respuesta = pacienteController.actualizar(1L, pacienteRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        PacienteResponseDTO body = (PacienteResponseDTO) respuesta.getBody();
        assertNotNull(body);
        assertEquals("Juan Carlos", body.getNombresPaciente());

        verify(pacienteService, times(1)).existeId(1L);
        verify(pacienteService, times(1)).actualizar(eq(1L), any(PacienteRequestDTO.class));

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el ID no existe, el Controller retorna 400.
           Este test cubre solo el caso exitoso.
        */
    }

    /*
     * TEST 6: PUT /api/pacientes/{id} - No existe
     */
    @Test
    void actualizar_cuandoNoExiste_deberiaRetornarStatus400() {
        when(pacienteService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = pacienteController.actualizar(99L, pacienteRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("hospital con id 99no existe", respuesta.getBody());

        verify(pacienteService, times(1)).existeId(99L);
        verify(pacienteService, never()).actualizar(anyLong(), any());

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Consistencia de mensajes de error con el Controller.
        */
    }

    /*
     * TEST 7: DELETE /api/pacientes/{id} - Existe
     */
    @Test
    void borrar_cuandoExiste_deberiaRetornarStatus204() {
        when(pacienteService.existeId(1L)).thenReturn(true);
        doNothing().when(pacienteService).borrar(1L);

        ResponseEntity<?> respuesta = pacienteController.borrar(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());

        verify(pacienteService, times(1)).existeId(1L);
        verify(pacienteService, times(1)).borrar(1L);

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el Service lanza excepción inesperada, el Controller
           debería manejarla y no retornar 500.
        */
    }

    /*
     * TEST 8: DELETE /api/pacientes/{id} - No existe
     */
    @Test
    void borrar_cuandoNoExiste_deberiaRetornarStatus400() {
        when(pacienteService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = pacienteController.borrar(99L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("hospital con id 99no existe", respuesta.getBody());

        verify(pacienteService, times(1)).existeId(99L);
        verify(pacienteService, never()).borrar(anyLong());

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Consistencia con PUT: el mensaje para ID 99 debe ser el mismo.
        */
    }
}