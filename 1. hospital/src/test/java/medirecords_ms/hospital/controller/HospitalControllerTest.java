package medirecords_ms.hospital.controller;

import static org.junit.jupiter.api.Assertions.*;
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

import medirecords_ms.hospital.dto.HospitalRequestDTO;
import medirecords_ms.hospital.dto.HospitalResponseDTO;
import medirecords_ms.hospital.service.HospitalService;

@ExtendWith(MockitoExtension.class)
public class HospitalControllerTest {

    @Mock
    private HospitalService hospitalService;

    @InjectMocks
    private HospitalController hospitalController;

    private HospitalResponseDTO hospitalResponse;
    private HospitalRequestDTO hospitalRequest;

    @BeforeEach
    void setUp() {
        hospitalResponse = new HospitalResponseDTO(1L, "Hospital Central", "Av. Principal 123", "271234567");
        hospitalRequest = new HospitalRequestDTO(1L, "Hospital Central", "Av. Principal 123", "271234567");
    }

    /*
     * TEST 1: GET /api/hospitales
     * Valida que retorne 200 OK y lista de hospitales.
     */
    @Test
    void listarTodos_deberiaRetornarStatus200YListaDeHospitales() {
        // ARRANGE
        when(hospitalService.listarTodos()).thenReturn(Arrays.asList(hospitalResponse));

        // ACT
        ResponseEntity<List<HospitalResponseDTO>> respuesta = hospitalController.listarTodos();

        // ASSERT
        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(200, respuesta.getStatusCode().value());
        assertNotNull(respuesta.getBody());
        assertEquals(1, respuesta.getBody().size());
        assertEquals("Hospital Central", respuesta.getBody().get(0).getNombre());
        assertEquals("Av. Principal 123", respuesta.getBody().get(0).getDireccion());
        assertEquals("271234567", respuesta.getBody().get(0).getTelefono());

        // VERIFY
        verify(hospitalService, times(1)).listarTodos();

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el endpoint GET /api/hospitales no responde 200 OK,
           verificar que el Service esté retornando datos correctamente.
        */
    }

    /*
     * TEST 2: GET /api/hospitales/{id} - Hospital existe
     */
    @Test
    void buscarId_cuandoExiste_deberiaRetornarStatus200YHospital() {
        // ARRANGE
        when(hospitalService.existeId(1L)).thenReturn(true);
        when(hospitalService.buscarDetallado(1L)).thenReturn(hospitalResponse);

        // ACT
        ResponseEntity<?> respuesta = hospitalController.buscarId(1L);

        // ASSERT
        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(200, respuesta.getStatusCode().value());
        HospitalResponseDTO body = (HospitalResponseDTO) respuesta.getBody();
        assertNotNull(body);
        assertEquals("Hospital Central", body.getNombre());
        assertEquals("Av. Principal 123", body.getDireccion());
        assertEquals("271234567", body.getTelefono());

        // VERIFY
        verify(hospitalService, times(1)).existeId(1L);
        verify(hospitalService, times(1)).buscarDetallado(1L);

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el ID existe pero el DTO retornado es incorrecto,
           verificar el mapeo en buscarDetallado() del Service.
        */
    }

    /*
     * TEST 3: GET /api/hospitales/{id} - Hospital NO existe
     */
    @Test
    void buscarId_cuandoNoExiste_deberiaRetornarStatus400() {
        // ARRANGE
        when(hospitalService.existeId(99L)).thenReturn(false);

        // ACT
        ResponseEntity<?> respuesta = hospitalController.buscarId(99L);

        // ASSERT
        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(400, respuesta.getStatusCode().value());
        assertEquals("no existe hospital con id: 99", respuesta.getBody());

        // VERIFY
        verify(hospitalService, times(1)).existeId(99L);
        verify(hospitalService, never()).buscarDetallado(anyLong());

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el mensaje de error cambia, los tests de integración fallarán.
           Mantener consistencia en los mensajes de error del Controller.
        */
    }

    /*
     * TEST 4: POST /api/hospitales
     */
    @Test
    void crear_deberiaRetornarStatus201YHospitalCreado() {
        // ARRANGE
        when(hospitalService.crear(any(HospitalRequestDTO.class))).thenReturn(hospitalResponse);

        // ACT
        ResponseEntity<?> respuesta = hospitalController.crear(hospitalRequest);

        // ASSERT
        assertNotNull(respuesta);
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(201, respuesta.getStatusCode().value());
        HospitalResponseDTO body = (HospitalResponseDTO) respuesta.getBody();
        assertNotNull(body);
        assertEquals("Hospital Central", body.getNombre());

        // VERIFY
        verify(hospitalService, times(1)).crear(any(HospitalRequestDTO.class));

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el @Valid falla (campos nulos), el Controller debería retornar 400.
           Este test asume datos válidos.
        */
    }

    /*
     * TEST 5: PUT /api/hospitales/{id} - Hospital existe
     */
    @Test
    void actualizar_cuandoExiste_deberiaRetornarStatus200() {
        // ARRANGE
        when(hospitalService.existeId(1L)).thenReturn(true);
        when(hospitalService.actualizar(eq(1L), any(HospitalRequestDTO.class))).thenReturn(hospitalResponse);

        // ACT
        ResponseEntity<?> respuesta = hospitalController.actualizar(1L, hospitalRequest);

        // ASSERT
        assertNotNull(respuesta);
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(200, respuesta.getStatusCode().value());
        HospitalResponseDTO body = (HospitalResponseDTO) respuesta.getBody();
        assertNotNull(body);
        assertEquals("Hospital Central", body.getNombre());

        // VERIFY
        verify(hospitalService, times(1)).existeId(1L);
        verify(hospitalService, times(1)).actualizar(eq(1L), any(HospitalRequestDTO.class));

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el ID no existe, el Controller retorna 400.
           Este test cubre solo el caso exitoso.
        */
    }

    /*
     * TEST 6: PUT /api/hospitales/{id} - Hospital NO existe
     */
    @Test
    void actualizar_cuandoNoExiste_deberiaRetornarStatus400() {
        // ARRANGE
        when(hospitalService.existeId(99L)).thenReturn(false);

        // ACT
        ResponseEntity<?> respuesta = hospitalController.actualizar(99L, hospitalRequest);

        // ASSERT
        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(400, respuesta.getStatusCode().value());
        assertEquals("hospital con id 99no existe", respuesta.getBody());

        // VERIFY
        verify(hospitalService, times(1)).existeId(99L);
        verify(hospitalService, never()).actualizar(anyLong(), any());

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           El mensaje tiene un error tipográfico: "99no" en vez de "99 no".
           Si se corrige en el Controller, actualizar este test.
        */
    }

    /*
     * TEST 7: DELETE /api/hospitales/{id} - Hospital existe
     */
    @Test
    void borrar_cuandoExiste_deberiaRetornarStatus204() {
        // ARRANGE
        when(hospitalService.existeId(1L)).thenReturn(true);
        doNothing().when(hospitalService).borrar(1L);

        // ACT
        ResponseEntity<?> respuesta = hospitalController.borrar(1L);

        // ASSERT
        assertNotNull(respuesta);
        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
        assertEquals(204, respuesta.getStatusCode().value());

        // VERIFY
        verify(hospitalService, times(1)).existeId(1L);
        verify(hospitalService, times(1)).borrar(1L);

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el Service lanza excepción inesperada, el Controller
           debería manejarla y no retornar 500.
        */
    }

    /*
     * TEST 8: DELETE /api/hospitales/{id} - Hospital NO existe
     */
    @Test
    void borrar_cuandoNoExiste_deberiaRetornarStatus400() {
        // ARRANGE
        when(hospitalService.existeId(99L)).thenReturn(false);

        // ACT
        ResponseEntity<?> respuesta = hospitalController.borrar(99L);

        // ASSERT
        assertNotNull(respuesta);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals(400, respuesta.getStatusCode().value());
        assertEquals("hospital con id 99no existe", respuesta.getBody());

        // VERIFY
        verify(hospitalService, times(1)).existeId(99L);
        verify(hospitalService, never()).borrar(anyLong());

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Consistencia con el test de actualizar: el mensaje para ID 99
           debe ser el mismo en PUT y DELETE.
        */
    }
}