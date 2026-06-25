package medirecords_ms.cargo.controller;

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

import medirecords_ms.cargo.dto.CargoRequestDTO;
import medirecords_ms.cargo.dto.CargoResponseDTO;
import medirecords_ms.cargo.service.CargoService;

@ExtendWith(MockitoExtension.class)
public class CargoControllerTest {

    @Mock
    private CargoService cargoService;

    @InjectMocks
    private CargoController cargoController;

    private CargoResponseDTO cargoResponse;
    private CargoRequestDTO cargoRequest;

    @BeforeEach
    void setUp() {
        cargoResponse = new CargoResponseDTO(1L, "Médico General");
        cargoRequest = new CargoRequestDTO(null, "Médico General");
    }

    @Test
    void listarTodos_deberiaRetornarStatus200YLista() {
        when(cargoService.listarTodos()).thenReturn(Arrays.asList(cargoResponse));

        ResponseEntity<List<CargoResponseDTO>> respuesta = cargoController.listarTodos();

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        assertEquals("Médico General", respuesta.getBody().get(0).getCargo());
        verify(cargoService, times(1)).listarTodos();
    }

    @Test
    void buscarId_cuandoExiste_deberiaRetornarStatus200() {
        when(cargoService.existeId(1L)).thenReturn(true);
        when(cargoService.buscarDetallado(1L)).thenReturn(cargoResponse);

        ResponseEntity<?> respuesta = cargoController.buscarId(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        CargoResponseDTO body = (CargoResponseDTO) respuesta.getBody();
        assertEquals("Médico General", body.getCargo());
        verify(cargoService, times(1)).buscarDetallado(1L);
    }

    @Test
    void buscarId_cuandoNoExiste_deberiaRetornarStatus400() {
        when(cargoService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = cargoController.buscarId(99L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("no existe cargo con id: 99", respuesta.getBody());
        verify(cargoService, never()).buscarDetallado(anyLong());
    }

    @Test
    void crear_deberiaRetornarStatus201() {
        when(cargoService.crear(any(CargoRequestDTO.class))).thenReturn(cargoResponse);

        ResponseEntity<?> respuesta = cargoController.crear(cargoRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        CargoResponseDTO body = (CargoResponseDTO) respuesta.getBody();
        assertEquals("Médico General", body.getCargo());
        verify(cargoService, times(1)).crear(any(CargoRequestDTO.class));
    }

    @Test
    void actualizar_cuandoExiste_deberiaRetornarStatus200() {
        when(cargoService.existeId(1L)).thenReturn(true);
        when(cargoService.actualizar(eq(1L), any(CargoRequestDTO.class))).thenReturn(cargoResponse);

        ResponseEntity<?> respuesta = cargoController.actualizar(1L, cargoRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        verify(cargoService, times(1)).actualizar(eq(1L), any(CargoRequestDTO.class));
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaRetornarStatus400() {
        when(cargoService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = cargoController.actualizar(99L, cargoRequest);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("cargo con id 99no existe", respuesta.getBody());
        verify(cargoService, never()).actualizar(anyLong(), any());
    }

    @Test
    void borrar_cuandoExiste_deberiaRetornarStatus204() {
        when(cargoService.existeId(1L)).thenReturn(true);
        doNothing().when(cargoService).borrar(1L);

        ResponseEntity<?> respuesta = cargoController.borrar(1L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        verify(cargoService, times(1)).borrar(1L);
    }

    @Test
    void borrar_cuandoNoExiste_deberiaRetornarStatus400() {
        when(cargoService.existeId(99L)).thenReturn(false);

        ResponseEntity<?> respuesta = cargoController.borrar(99L);

        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("cargo con id 99no existe", respuesta.getBody());
        verify(cargoService, never()).borrar(anyLong());
    }
}