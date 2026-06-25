package medirecords_ms.historial.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import medirecords_ms.historial.dto.HistorialRequestDTO;
import medirecords_ms.historial.dto.HistorialResponseDTO;
import medirecords_ms.historial.service.HistorialService;

@ExtendWith(MockitoExtension.class)
public class HistorialControllerTest {

    @Mock
    private HistorialService historialService;

    @InjectMocks
    private HistorialController historialController;

    private HistorialResponseDTO historialResponse;
    private HistorialRequestDTO historialRequest;

    @BeforeEach
    void setUp() {
        historialResponse = new HistorialResponseDTO(1L, "Paciente con migraña", LocalDate.now(), null, Arrays.asList());
        historialRequest = new HistorialRequestDTO(null, "Paciente con migraña", LocalDate.now(), 1L, "1");
    }

    @Test
    void listarTodos_deberiaRetornarStatus200YLista() {
        when(historialService.listarTodos()).thenReturn(Arrays.asList(historialResponse));

        ResponseEntity<List<HistorialResponseDTO>> respuesta = historialController.listarTodos();

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        assertEquals("Paciente con migraña", respuesta.getBody().get(0).getNotas());
        verify(historialService, times(1)).listarTodos();
    }

    @Test
    void buscarId_cuandoExiste_deberiaRetornarStatus200() {
        when(historialService.existeId(1L)).thenReturn(true);
        when(historialService.buscarDetallado(1L)).thenReturn(historialResponse);

        ResponseEntity<?> respuesta = historialController.buscarId(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        HistorialResponseDTO body = (HistorialResponseDTO) respuesta.getBody();
        assertEquals("Paciente con migraña", body.getNotas());
        verify(historialService, times(1)).buscarDetallado(1L);
    }

    @Test
    void buscarId_cuandoNoExiste_deberiaRetornarStatus400() {
        when(historialService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = historialController.buscarId(99L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("no existe historial con id: 99", respuesta.getBody());
        verify(historialService, never()).buscarDetallado(anyLong());
    }

    @Test
    void crear_deberiaRetornarStatus201() {
        when(historialService.crear(any(HistorialRequestDTO.class))).thenReturn(historialResponse);

        ResponseEntity<?> respuesta = historialController.crear(historialRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        HistorialResponseDTO body = (HistorialResponseDTO) respuesta.getBody();
        assertEquals("Paciente con migraña", body.getNotas());
        verify(historialService, times(1)).crear(any(HistorialRequestDTO.class));
    }

    @Test
    void actualizar_cuandoExiste_deberiaRetornarStatus200() {
        when(historialService.existeId(1L)).thenReturn(true);
        when(historialService.actualizar(eq(1L), any(HistorialRequestDTO.class))).thenReturn(historialResponse);

        ResponseEntity<?> respuesta = historialController.actualizar(1L, historialRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        verify(historialService, times(1)).actualizar(eq(1L), any(HistorialRequestDTO.class));
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaRetornarStatus400() {
        when(historialService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = historialController.actualizar(99L, historialRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("receta con id 99no existe", respuesta.getBody());
        verify(historialService, never()).actualizar(anyLong(), any());
    }

    @Test
    void borrar_cuandoExiste_deberiaRetornarStatus204() {
        when(historialService.existeId(1L)).thenReturn(true);
        doNothing().when(historialService).borrar(1L);

        ResponseEntity<?> respuesta = historialController.borrar(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        verify(historialService, times(1)).borrar(1L);
    }

    @Test
    void borrar_cuandoNoExiste_deberiaRetornarStatus400() {
        when(historialService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = historialController.borrar(99L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("historial con id 99no existe", respuesta.getBody());
        verify(historialService, never()).borrar(anyLong());
    }
}