package medirecords_ms.consulta.controller;

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

import medirecords_ms.consulta.dto.ConsultaRequestDTO;
import medirecords_ms.consulta.dto.ConsultaResponseDTO;
import medirecords_ms.consulta.service.ConsultaService;

@ExtendWith(MockitoExtension.class)
public class ConsultaControllerTest {

    @Mock
    private ConsultaService consultaService;

    @InjectMocks
    private ConsultaController consultaController;

    private ConsultaResponseDTO consultaResponse;
    private ConsultaRequestDTO consultaRequest;

    @BeforeEach
    void setUp() {
        consultaResponse = new ConsultaResponseDTO(1L, LocalDate.now(), "Dolor de cabeza", "Migraña", null, null, null);
        consultaRequest = new ConsultaRequestDTO(null, LocalDate.now(), "Dolor de cabeza", "Migraña", 1L, 1L, 1L);
    }

    @Test
    void listarTodos_deberiaRetornarStatus200YLista() {
        when(consultaService.listarTodos()).thenReturn(Arrays.asList(consultaResponse));

        ResponseEntity<List<ConsultaResponseDTO>> respuesta = consultaController.listarTodos();

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        assertEquals("Dolor de cabeza", respuesta.getBody().get(0).getMotivo());
        verify(consultaService, times(1)).listarTodos();
    }

    @Test
    void buscarId_cuandoExiste_deberiaRetornarStatus200() {
        when(consultaService.existeId(1L)).thenReturn(true);
        when(consultaService.buscarDetallado(1L)).thenReturn(consultaResponse);

        ResponseEntity<?> respuesta = consultaController.buscarId(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        ConsultaResponseDTO body = (ConsultaResponseDTO) respuesta.getBody();
        assertEquals("Dolor de cabeza", body.getMotivo());
        verify(consultaService, times(1)).buscarDetallado(1L);
    }

    @Test
    void buscarId_cuandoNoExiste_deberiaRetornarStatus400() {
        when(consultaService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = consultaController.buscarId(99L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("no existe consulta con id: 99", respuesta.getBody());
        verify(consultaService, never()).buscarDetallado(anyLong());
    }

    @Test
    void buscarVariosId_deberiaRetornarLista() {
        when(consultaService.buscarVariosId("1,2")).thenReturn(Arrays.asList(consultaResponse));

        ResponseEntity<List<ConsultaResponseDTO>> respuesta = consultaController.buscarVariosId("1,2");

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(consultaService, times(1)).buscarVariosId("1,2");
    }

    @Test
    void crear_deberiaRetornarStatus201() {
        when(consultaService.crear(any(ConsultaRequestDTO.class))).thenReturn(consultaResponse);

        ResponseEntity<?> respuesta = consultaController.crear(consultaRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        ConsultaResponseDTO body = (ConsultaResponseDTO) respuesta.getBody();
        assertEquals("Dolor de cabeza", body.getMotivo());
        verify(consultaService, times(1)).crear(any(ConsultaRequestDTO.class));
    }

    @Test
    void actualizar_cuandoExiste_deberiaRetornarStatus200() {
        when(consultaService.existeId(1L)).thenReturn(true);
        when(consultaService.actualizar(eq(1L), any(ConsultaRequestDTO.class))).thenReturn(consultaResponse);

        ResponseEntity<?> respuesta = consultaController.actualizar(1L, consultaRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        verify(consultaService, times(1)).actualizar(eq(1L), any(ConsultaRequestDTO.class));
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaRetornarStatus400() {
        when(consultaService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = consultaController.actualizar(99L, consultaRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("consulta con id 99no existe", respuesta.getBody());
        verify(consultaService, never()).actualizar(anyLong(), any());
    }

    @Test
    void borrar_cuandoExiste_deberiaRetornarStatus204() {
        when(consultaService.existeId(1L)).thenReturn(true);
        doNothing().when(consultaService).borrar(1L);

        ResponseEntity<?> respuesta = consultaController.borrar(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        verify(consultaService, times(1)).borrar(1L);
    }

    @Test
    void borrar_cuandoNoExiste_deberiaRetornarStatus400() {
        when(consultaService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = consultaController.borrar(99L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("consulta con id 99no existe", respuesta.getBody());
        verify(consultaService, never()).borrar(anyLong());
    }
}