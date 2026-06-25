package medirecords_ms.receta.controller;

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

import medirecords_ms.receta.dto.RecetaRequestDTO;
import medirecords_ms.receta.dto.RecetaResponseDTO;
import medirecords_ms.receta.service.RecetaService;

@ExtendWith(MockitoExtension.class)
public class RecetaControllerTest {

    @Mock
    private RecetaService recetaService;

    @InjectMocks
    private RecetaController recetaController;

    private RecetaResponseDTO recetaResponse;
    private RecetaRequestDTO recetaRequest;

    @BeforeEach
    void setUp() {
        recetaResponse = new RecetaResponseDTO(1L, LocalDate.now(), "Tomar cada 8 horas", null, Arrays.asList());
        recetaRequest = new RecetaRequestDTO(null, LocalDate.now(), "Tomar cada 8 horas", 1L, "1");
    }

    @Test
    void listarTodos_deberiaRetornarStatus200YLista() {
        when(recetaService.listarTodos()).thenReturn(Arrays.asList(recetaResponse));

        ResponseEntity<List<RecetaResponseDTO>> respuesta = recetaController.listarTodos();

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        assertEquals("Tomar cada 8 horas", respuesta.getBody().get(0).getInstrucciones());
        verify(recetaService, times(1)).listarTodos();
    }

    @Test
    void buscarId_cuandoExiste_deberiaRetornarStatus200() {
        when(recetaService.existeId(1L)).thenReturn(true);
        when(recetaService.buscarDetallado(1L)).thenReturn(recetaResponse);

        ResponseEntity<?> respuesta = recetaController.buscarId(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        RecetaResponseDTO body = (RecetaResponseDTO) respuesta.getBody();
        assertEquals("Tomar cada 8 horas", body.getInstrucciones());
        verify(recetaService, times(1)).buscarDetallado(1L);
    }

    @Test
    void buscarId_cuandoNoExiste_deberiaRetornarStatus400() {
        when(recetaService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = recetaController.buscarId(99L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("no existe receta con id: 99", respuesta.getBody());
        verify(recetaService, never()).buscarDetallado(anyLong());
    }

    @Test
    void crear_deberiaRetornarStatus201() {
        when(recetaService.crear(any(RecetaRequestDTO.class))).thenReturn(recetaResponse);

        ResponseEntity<?> respuesta = recetaController.crear(recetaRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        RecetaResponseDTO body = (RecetaResponseDTO) respuesta.getBody();
        assertEquals("Tomar cada 8 horas", body.getInstrucciones());
        verify(recetaService, times(1)).crear(any(RecetaRequestDTO.class));
    }

    @Test
    void actualizar_cuandoExiste_deberiaRetornarStatus200() {
        when(recetaService.existeId(1L)).thenReturn(true);
        when(recetaService.actualizar(eq(1L), any(RecetaRequestDTO.class))).thenReturn(recetaResponse);

        ResponseEntity<?> respuesta = recetaController.actualizar(1L, recetaRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        verify(recetaService, times(1)).actualizar(eq(1L), any(RecetaRequestDTO.class));
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaRetornarStatus400() {
        when(recetaService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = recetaController.actualizar(99L, recetaRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("receta con id 99no existe", respuesta.getBody());
        verify(recetaService, never()).actualizar(anyLong(), any());
    }

    @Test
    void borrar_cuandoExiste_deberiaRetornarStatus204() {
        when(recetaService.existeId(1L)).thenReturn(true);
        doNothing().when(recetaService).borrar(1L);

        ResponseEntity<?> respuesta = recetaController.borrar(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        verify(recetaService, times(1)).borrar(1L);
    }

    @Test
    void borrar_cuandoNoExiste_deberiaRetornarStatus400() {
        when(recetaService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = recetaController.borrar(99L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("receta con id 99no existe", respuesta.getBody());
        verify(recetaService, never()).borrar(anyLong());
    }
}