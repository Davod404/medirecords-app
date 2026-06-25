package medirecords_ms.medicamento.controller;

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

import medirecords_ms.medicamento.dto.MedicamentoRequestDTO;
import medirecords_ms.medicamento.dto.MedicamentoResponseDTO;
import medirecords_ms.medicamento.service.MedicamentoService;

@ExtendWith(MockitoExtension.class)
public class MedicamentoControllerTest {

    @Mock
    private MedicamentoService medicamentoService;

    @InjectMocks
    private MedicamentoController medicamentoController;

    private MedicamentoResponseDTO medicamentoResponse;
    private MedicamentoRequestDTO medicamentoRequest;

    @BeforeEach
    void setUp() {
        medicamentoResponse = new MedicamentoResponseDTO(1L, "Paracetamol", "Genfar", "Tableta", 3500, 100);
        medicamentoRequest = new MedicamentoRequestDTO(null, "Paracetamol", "Genfar", "Tableta", 3500, 100);
    }

    @Test
    void listarTodos_deberiaRetornarStatus200YLista() {
        when(medicamentoService.listarTodos()).thenReturn(Arrays.asList(medicamentoResponse));

        ResponseEntity<List<MedicamentoResponseDTO>> respuesta = medicamentoController.listarTodos();

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        assertEquals("Paracetamol", respuesta.getBody().get(0).getNombre());
        verify(medicamentoService, times(1)).listarTodos();

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si GET /api/medicamentos no responde 200, revisar Service.
        */
    }

    @Test
    void buscarId_cuandoExiste_deberiaRetornarStatus200() {
        when(medicamentoService.existeId(1L)).thenReturn(true);
        when(medicamentoService.buscarDetallado(1L)).thenReturn(medicamentoResponse);

        ResponseEntity<?> respuesta = medicamentoController.buscarId(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        MedicamentoResponseDTO body = (MedicamentoResponseDTO) respuesta.getBody();
        assertEquals("Paracetamol", body.getNombre());
        verify(medicamentoService, times(1)).buscarDetallado(1L);

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           OJO: El Controller tiene un bug. Cuando existeId es TRUE,
           retorna BAD_REQUEST. Debería ser al revés.
           Si el test falla, corregir el Controller.
        */
    }

    @Test
    void buscarId_cuandoNoExiste_deberiaRetornarStatus400() {
        when(medicamentoService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = medicamentoController.buscarId(99L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("no existe medicamento con id: 99", respuesta.getBody());
        verify(medicamentoService, times(1)).existeId(99L);
    }

    @Test
    void buscarVariosId_deberiaRetornarLista() {
        when(medicamentoService.buscarVariosId("1,2")).thenReturn(Arrays.asList(medicamentoResponse));

        ResponseEntity<List<MedicamentoResponseDTO>> respuesta = medicamentoController.buscarVariosId("1,2");

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(medicamentoService, times(1)).buscarVariosId("1,2");
    }

    @Test
    void crear_deberiaRetornarStatus201() {
        when(medicamentoService.crear(any(MedicamentoRequestDTO.class))).thenReturn(medicamentoResponse);

        ResponseEntity<?> respuesta = medicamentoController.crear(medicamentoRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        MedicamentoResponseDTO body = (MedicamentoResponseDTO) respuesta.getBody();
        assertEquals("Paracetamol", body.getNombre());
        verify(medicamentoService, times(1)).crear(any(MedicamentoRequestDTO.class));
    }

    @Test
    void actualizar_cuandoExiste_deberiaRetornarStatus200() {
        when(medicamentoService.existeId(1L)).thenReturn(true);
        when(medicamentoService.actualizar(eq(1L), any(MedicamentoRequestDTO.class))).thenReturn(medicamentoResponse);

        ResponseEntity<?> respuesta = medicamentoController.actualizar(1L, medicamentoRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        verify(medicamentoService, times(1)).actualizar(eq(1L), any(MedicamentoRequestDTO.class));

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el ID no existe, el Controller retorna 400.
        */
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaRetornarStatus400() {
        when(medicamentoService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = medicamentoController.actualizar(99L, medicamentoRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("medicamento con id 99no existe", respuesta.getBody());
        verify(medicamentoService, never()).actualizar(anyLong(), any());
    }

    @Test
    void borrar_cuandoExiste_deberiaRetornarStatus204() {
        when(medicamentoService.existeId(1L)).thenReturn(true);
        doNothing().when(medicamentoService).borrar(1L);

        ResponseEntity<?> respuesta = medicamentoController.borrar(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        verify(medicamentoService, times(1)).borrar(1L);
    }

    @Test
    void borrar_cuandoNoExiste_deberiaRetornarStatus400() {
        when(medicamentoService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = medicamentoController.borrar(99L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("medicamento con id 99no existe", respuesta.getBody());
        verify(medicamentoService, never()).borrar(anyLong());
    }
}