package medirecords_ms.hospital.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import medirecords_ms.hospital.dto.HospitalRequestDTO;
import medirecords_ms.hospital.dto.HospitalResponseDTO;
import medirecords_ms.hospital.model.Hospital;
import medirecords_ms.hospital.repository.HospitalRepository;

@ExtendWith(MockitoExtension.class)
public class HospitalServiceTest {

    @Mock
    private HospitalRepository hospitalRepository;

    @InjectMocks
    private HospitalService hospitalService;

    private Hospital hospital;
    private HospitalRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        hospital = new Hospital(1L, "Hospital Central", "Av. Principal 123", "271234567");
        requestDTO = new HospitalRequestDTO(1L, "Hospital Central", "Av. Principal 123", "271234567");
    }

    /*
     * TEST 1: listarTodos()
     * Valida que retorne la lista de hospitales desde el repositorio.
     */
    @Test
    void listarTodos_deberiaRetornarListaDeHospitales() {
        // ARRANGE
        when(hospitalRepository.findAll()).thenReturn(Arrays.asList(hospital));

        // ACT
        List<HospitalResponseDTO> resultado = hospitalService.listarTodos();

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Hospital Central", resultado.get(0).getNombre());
        assertEquals("Av. Principal 123", resultado.get(0).getDireccion());
        assertEquals("271234567", resultado.get(0).getTelefono());

        // VERIFY
        verify(hospitalRepository, times(1)).findAll();

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el repositorio devuelve lista vacía, el test fallaría.
           Desarrollo debe verificar el manejo de listas vacías desde la BD.
        */
    }

    /*
     * TEST 2: buscarId() - Hospital existe
     */
    @Test
    void buscarId_cuandoExiste_deberiaRetornarHospital() {
        // ARRANGE
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));

        // ACT
        Hospital resultado = hospitalService.buscarId(1L);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Hospital Central", resultado.getNombre());

        // VERIFY
        verify(hospitalRepository, times(1)).findById(1L);

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el ID no existe, findById retorna Optional.empty() y lanza RuntimeException.
           Desarrollo debe asegurar que el controller maneje esta excepción.
        */
    }

    /*
     * TEST 3: buscarId() - Hospital NO existe
     */
    @Test
    void buscarId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // ARRANGE
        when(hospitalRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            hospitalService.buscarId(99L);
        });
        assertEquals("hospital no encontrado", excepcion.getMessage());

        // VERIFY
        verify(hospitalRepository, times(1)).findById(99L);

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el mensaje de excepción cambia, el test falla.
           Verificar que el Service mantenga mensajes consistentes.
        */
    }

    /*
     * TEST 4: existeId() - Verifica existencia
     */
    @Test
    void existeId_deberiaRetornarBoolean() {
        // ARRANGE
        when(hospitalRepository.existsById(1L)).thenReturn(true);
        when(hospitalRepository.existsById(99L)).thenReturn(false);

        // ACT + ASSERT
        assertTrue(hospitalService.existeId(1L));
        assertFalse(hospitalService.existeId(99L));

        // VERIFY
        verify(hospitalRepository, times(1)).existsById(1L);
        verify(hospitalRepository, times(1)).existsById(99L);

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si existeId() no retorna el valor esperado, el controller
           tomará decisiones incorrectas (ej: permitir actualizar un ID inexistente).
        */
    }

    /*
     * TEST 5: buscarDetallado() - Retorna DTO
     */
    @Test
    void buscarDetallado_cuandoExiste_deberiaRetornarDTO() {
        // ARRANGE
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));

        // ACT
        HospitalResponseDTO resultado = hospitalService.buscarDetallado(1L);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Hospital Central", resultado.getNombre());
        assertEquals("Av. Principal 123", resultado.getDireccion());
        assertEquals("271234567", resultado.getTelefono());

        // VERIFY
        verify(hospitalRepository, times(1)).findById(1L);

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el mapeo de Hospital a HospitalResponseDTO es incorrecto,
           los datos retornados no coincidirán con los esperados.
        */
    }

    /*
     * TEST 6: crear() - Crea un hospital desde DTO
     */
    @Test
    void crear_deberiaGuardarYRetornarDTO() {
        // ARRANGE
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(hospital);

        // ACT
        HospitalResponseDTO resultado = hospitalService.crear(requestDTO);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Hospital Central", resultado.getNombre());
        assertEquals("Av. Principal 123", resultado.getDireccion());
        assertEquals("271234567", resultado.getTelefono());

        // VERIFY
        verify(hospitalRepository, times(1)).save(any(Hospital.class));

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el DTO de entrada no tiene los campos requeridos (@NotBlank),
           la validación debería fallar antes de llegar al Service.
        */
    }

    /*
     * TEST 7: actualizar() - Actualiza hospital existente
     */
    @Test
    void actualizar_cuandoExiste_deberiaRetornarDTOActualizado() {
        // ARRANGE
        HospitalRequestDTO actualizado = new HospitalRequestDTO(null, "Hospital Actualizado", "Nueva Dir", "999");
        Hospital hospitalGuardado = new Hospital(1L, "Hospital Actualizado", "Nueva Dir", "999");

        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(hospitalGuardado);

        // ACT
        HospitalResponseDTO resultado = hospitalService.actualizar(1L, actualizado);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Hospital Actualizado", resultado.getNombre());
        assertEquals("Nueva Dir", resultado.getDireccion());
        assertEquals("999", resultado.getTelefono());

        // VERIFY
        verify(hospitalRepository, times(1)).findById(1L);
        verify(hospitalRepository, times(1)).save(any(Hospital.class));

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el ID no existe, findById lanza excepción y el controller
           debe responder con 400. Verificar en HospitalControllerTest.
        */
    }

    /*
     * TEST 8: borrar() - Elimina hospital existente
     */
    @Test
    void borrar_cuandoExiste_deberiaEjecutarDelete() {
        // ARRANGE
        when(hospitalRepository.existsById(1L)).thenReturn(true);
        doNothing().when(hospitalRepository).deleteById(1L);

        // ACT
        assertDoesNotThrow(() -> hospitalService.borrar(1L));

        // VERIFY
        verify(hospitalRepository, times(1)).existsById(1L);
        verify(hospitalRepository, times(1)).deleteById(1L);

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si existeId retorna false, el Service lanza RuntimeException.
           El controller debe capturarla y retornar 400.
        */
    }

    /*
     * TEST 9: borrar() - Intenta eliminar hospital inexistente
     */
    @Test
    void borrar_cuandoNoExiste_deberiaLanzarExcepcion() {
        // ARRANGE
        when(hospitalRepository.existsById(99L)).thenReturn(false);

        // ACT + ASSERT
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            hospitalService.borrar(99L);
        });
        assertEquals("hospital no existe", excepcion.getMessage());

        // VERIFY
        verify(hospitalRepository, times(1)).existsById(99L);
        verify(hospitalRepository, never()).deleteById(anyLong());

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el mensaje de excepción no es "hospital no existe",
           el controller podría mostrar un mensaje incorrecto al usuario.
        */
    }

    @Test
    void testBuscarId_Exitoso() {
        // ARRANGE
        Hospital hospital = new Hospital(1L, "Hospital Central", "Dir 1", "999");
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));

        // ACT
        Hospital resultado = hospitalService.buscarId(1L);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Hospital Central", resultado.getNombre());
        
        /* CASO HIPOTÉTICO DE FALLA PARA QA: 
           Si el repositorio retorna null en lugar de un Optional vacío, el método lanzará NullPointerException.
           Desarrollo debe asegurar que el Repository siempre devuelva Optional.
        */
    }

    @Test
    void testBuscarId_NoEncontrado() {
        // ARRANGE
        when(hospitalRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(RuntimeException.class, () -> hospitalService.buscarId(99L));
    }

    @Test
    void testExisteId() {
        // ARRANGE
        when(hospitalRepository.existsById(1L)).thenReturn(true);

        // ACT
        Boolean existe = hospitalService.existeId(1L);

        // ASSERT
        assertTrue(existe);
        verify(hospitalRepository, times(1)).existsById(1L);
    }

    @Test
    void testCrearHospital() {
        // ARRANGE
        HospitalRequestDTO request = new HospitalRequestDTO(null, "Nuevo Hospital", "Dir Nueva", "555");
        Hospital hospitalGuardado = new Hospital(1L, "Nuevo Hospital", "Dir Nueva", "555");
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(hospitalGuardado);

        // ACT
        HospitalResponseDTO resultado = hospitalService.crear(request);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Nuevo Hospital", resultado.getNombre());
        verify(hospitalRepository, times(1)).save(any(Hospital.class));

        /* CASO HIPOTÉTICO DE FALLA: 
           Si el DTO no se mapea correctamente, el nombre podría llegar como null.
           QA debe verificar que el DTO sea correctamente convertido a la entidad en el Service.
        */
    }

    @Test
    void testActualizarHospital() {
        // ARRANGE
        Long id = 1L;
        HospitalRequestDTO request = new HospitalRequestDTO(id, "Hospital Editado", "Dir Ed", "999");
        Hospital hospitalExistente = new Hospital(id, "Viejo", "Dir", "111");
        when(hospitalRepository.findById(id)).thenReturn(Optional.of(hospitalExistente));
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(hospitalExistente);

        // ACT
        HospitalResponseDTO resultado = hospitalService.actualizar(id, request);

        // ASSERT
        assertEquals("Hospital Editado", resultado.getNombre());
        verify(hospitalRepository).save(any(Hospital.class));
    }

    @Test
    void testBorrarHospital() {
        // ARRANGE
        Long id = 1L;
        when(hospitalRepository.existsById(id)).thenReturn(true);
        doNothing().when(hospitalRepository).deleteById(id);

        // ACT
        hospitalService.borrar(id);

        // ASSERT & VERIFY
        verify(hospitalRepository, times(1)).deleteById(id);
        
        /* CASO HIPOTÉTICO DE FALLA: 
           Si el método borra un id que no existe y el Service no lanza la excepción esperada, 
           habría un error de consistencia.
        */
    }
}