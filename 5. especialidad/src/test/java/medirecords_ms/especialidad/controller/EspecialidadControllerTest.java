package medirecords_ms.especialidad.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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

import medirecords_ms.especialidad.dto.EspecialidadRequestDTO;
import medirecords_ms.especialidad.dto.EspecialidadResponseDTO;
import medirecords_ms.especialidad.service.EspecialidadService;

@ExtendWith(MockitoExtension.class)
public class EspecialidadControllerTest {

    @Mock
    private EspecialidadService especialidadService;

    @InjectMocks
    private EspecialidadController especialidadController;

    private EspecialidadResponseDTO especialidadResponse;
    private EspecialidadRequestDTO especialidadRequest;

    @BeforeEach
    void setUp() {
        especialidadResponse = new EspecialidadResponseDTO(1L, "Cardiología");
        especialidadRequest = new EspecialidadRequestDTO(null, "Cardiología");
    }

    @Test
    void listarTodos_deberiaRetornarStatus200YLista() {
        when(especialidadService.listarTodos()).thenReturn(Arrays.asList(especialidadResponse));

        ResponseEntity<List<EspecialidadResponseDTO>> respuesta = especialidadController.listarTodos();

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        assertEquals("Cardiología", respuesta.getBody().get(0).getEspecialidad());
        verify(especialidadService, times(1)).listarTodos();
    }

    @Test
    void buscarId_cuandoExiste_deberiaRetornarStatus200() {
        when(especialidadService.existeId(1L)).thenReturn(true);
        when(especialidadService.buscarDetallado(1L)).thenReturn(especialidadResponse);

        ResponseEntity<?> respuesta = especialidadController.buscarId(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        EspecialidadResponseDTO body = (EspecialidadResponseDTO) respuesta.getBody();
        assertEquals("Cardiología", body.getEspecialidad());
        verify(especialidadService, times(1)).buscarDetallado(1L);
    }

    @Test
    void buscarId_cuandoNoExiste_deberiaRetornarStatus400() {
        when(especialidadService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = especialidadController.buscarId(99L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("no existe especialidad con id: 99", respuesta.getBody());
        verify(especialidadService, never()).buscarDetallado(anyLong());
    }

    @Test
    void buscarVariosId_deberiaRetornarLista() {
        when(especialidadService.buscarVariosId("1,2")).thenReturn(Arrays.asList(especialidadResponse));

        ResponseEntity<List<EspecialidadResponseDTO>> respuesta = especialidadController.buscarVariosId("1,2");

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(especialidadService, times(1)).buscarVariosId("1,2");
    }

    @Test
    void crear_deberiaRetornarStatus201() {
        when(especialidadService.crear(any(EspecialidadRequestDTO.class))).thenReturn(especialidadResponse);

        ResponseEntity<?> respuesta = especialidadController.crear(especialidadRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        EspecialidadResponseDTO body = (EspecialidadResponseDTO) respuesta.getBody();
        assertEquals("Cardiología", body.getEspecialidad());
        verify(especialidadService, times(1)).crear(any(EspecialidadRequestDTO.class));
    }

    @Test
    void actualizar_cuandoExiste_deberiaRetornarStatus200() {
        when(especialidadService.existeId(1L)).thenReturn(true);
        when(especialidadService.actualizar(eq(1L), any(EspecialidadRequestDTO.class))).thenReturn(especialidadResponse);

        ResponseEntity<?> respuesta = especialidadController.actualizar(1L, especialidadRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        verify(especialidadService, times(1)).actualizar(eq(1L), any(EspecialidadRequestDTO.class));
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaRetornarStatus400() {
        when(especialidadService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = especialidadController.actualizar(99L, especialidadRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("especialidad con id 99no existe", respuesta.getBody());
        verify(especialidadService, never()).actualizar(anyLong(), any());
    }

    @Test
    void borrar_cuandoExiste_deberiaRetornarStatus204() {
        when(especialidadService.existeId(1L)).thenReturn(true);
        doNothing().when(especialidadService).borrar(1L);

        ResponseEntity<?> respuesta = especialidadController.borrar(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        verify(especialidadService, times(1)).borrar(1L);
    }

    @Test
    void borrar_cuandoNoExiste_deberiaRetornarStatus400() {
        when(especialidadService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = especialidadController.borrar(99L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("especialidad con id 99no existe", respuesta.getBody());
        verify(especialidadService, never()).borrar(anyLong());
    }
}