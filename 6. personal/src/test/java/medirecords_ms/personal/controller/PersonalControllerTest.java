package medirecords_ms.personal.controller;

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

import medirecords_ms.personal.dto.PersonalRequestDTO;
import medirecords_ms.personal.dto.PersonalResponseDTO;
import medirecords_ms.personal.service.PersonalService;

@ExtendWith(MockitoExtension.class)
public class PersonalControllerTest {

    @Mock
    private PersonalService personalService;

    @InjectMocks
    private PersonalController personalController;

    private PersonalResponseDTO personalResponse;
    private PersonalRequestDTO personalRequest;

    @BeforeEach
    void setUp() {
        personalResponse = new PersonalResponseDTO(1L, "87654321", '1', "Ana María", "González López",
                "987654321", "ana@email.com", null, Arrays.asList());
        personalRequest = new PersonalRequestDTO(null, "87654321", '1', "Ana María", "González López",
                "987654321", "ana@email.com", 1L, "1");
    }

    @Test
    void listarTodos_deberiaRetornarStatus200YLista() {
        when(personalService.listarTodos()).thenReturn(Arrays.asList(personalResponse));

        ResponseEntity<List<PersonalResponseDTO>> respuesta = personalController.listarTodos();

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        assertEquals("Ana María", respuesta.getBody().get(0).getNombresPersonal());
        verify(personalService, times(1)).listarTodos();
    }

    @Test
    void buscarId_cuandoExiste_deberiaRetornarStatus200() {
        when(personalService.existeId(1L)).thenReturn(true);
        when(personalService.buscarDetallado(1L)).thenReturn(personalResponse);

        ResponseEntity<?> respuesta = personalController.buscarId(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        PersonalResponseDTO body = (PersonalResponseDTO) respuesta.getBody();
        assertEquals("Ana María", body.getNombresPersonal());
        verify(personalService, times(1)).buscarDetallado(1L);
    }

    @Test
    void buscarId_cuandoNoExiste_deberiaRetornarStatus400() {
        when(personalService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = personalController.buscarId(99L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("no existe personal con id: 99", respuesta.getBody());
        verify(personalService, never()).buscarDetallado(anyLong());
    }

    @Test
    void crear_deberiaRetornarStatus201() {
        when(personalService.crear(any(PersonalRequestDTO.class))).thenReturn(personalResponse);

        ResponseEntity<?> respuesta = personalController.crear(personalRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        PersonalResponseDTO body = (PersonalResponseDTO) respuesta.getBody();
        assertEquals("Ana María", body.getNombresPersonal());
        verify(personalService, times(1)).crear(any(PersonalRequestDTO.class));
    }

    @Test
    void actualizar_cuandoExiste_deberiaRetornarStatus200() {
        when(personalService.existeId(1L)).thenReturn(true);
        when(personalService.actualizar(eq(1L), any(PersonalRequestDTO.class))).thenReturn(personalResponse);

        ResponseEntity<?> respuesta = personalController.actualizar(1L, personalRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        verify(personalService, times(1)).actualizar(eq(1L), any(PersonalRequestDTO.class));
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaRetornarStatus400() {
        when(personalService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = personalController.actualizar(99L, personalRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("personal con id 99no existe", respuesta.getBody());
        verify(personalService, never()).actualizar(anyLong(), any());
    }

    @Test
    void borrar_cuandoExiste_deberiaRetornarStatus204() {
        when(personalService.existeId(1L)).thenReturn(true);
        doNothing().when(personalService).borrar(1L);

        ResponseEntity<?> respuesta = personalController.borrar(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        verify(personalService, times(1)).borrar(1L);
    }

    @Test
    void borrar_cuandoNoExiste_deberiaRetornarStatus400() {
        when(personalService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = personalController.borrar(99L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("personal con id 99no existe", respuesta.getBody());
        verify(personalService, never()).borrar(anyLong());
    }
}