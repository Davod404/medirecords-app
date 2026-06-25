package medirecords_ms.especialidad.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import medirecords_ms.especialidad.dto.EspecialidadRequestDTO;
import medirecords_ms.especialidad.dto.EspecialidadResponseDTO;
import medirecords_ms.especialidad.model.Especialidad;
import medirecords_ms.especialidad.repository.EspecialidadRepository;

@ExtendWith(MockitoExtension.class)
public class EspecialidadServiceTest {

    @Mock
    private EspecialidadRepository especialidadRepository;

    @InjectMocks
    private EspecialidadService especialidadService;

    private Especialidad especialidad;
    private EspecialidadRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        especialidad = new Especialidad(1L, "Cardiología");
        requestDTO = new EspecialidadRequestDTO(null, "Cardiología");
    }

    @Test
    void listarTodos_deberiaRetornarLista() {
        when(especialidadRepository.findAll()).thenReturn(Arrays.asList(especialidad));

        List<EspecialidadResponseDTO> resultado = especialidadService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Cardiología", resultado.get(0).getEspecialidad());
        verify(especialidadRepository, times(1)).findAll();
    }

    @Test
    void buscarId_cuandoExiste_deberiaRetornarEspecialidad() {
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad));

        Especialidad resultado = especialidadService.buscarId(1L);

        assertNotNull(resultado);
        assertEquals("Cardiología", resultado.getEspecialidad());
    }

    @Test
    void buscarId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(especialidadRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            especialidadService.buscarId(99L);
        });
        assertEquals("Especialidad no encontrado", excepcion.getMessage());
    }

    @Test
    void existeId_deberiaRetornarBoolean() {
        when(especialidadRepository.existsById(1L)).thenReturn(true);
        when(especialidadRepository.existsById(99L)).thenReturn(false);

        assertTrue(especialidadService.existeId(1L));
        assertFalse(especialidadService.existeId(99L));
    }

    @Test
    void buscarDetallado_deberiaRetornarDTO() {
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad));

        EspecialidadResponseDTO resultado = especialidadService.buscarDetallado(1L);

        assertNotNull(resultado);
        assertEquals("Cardiología", resultado.getEspecialidad());
    }

    @Test
    void buscarVariosId_deberiaRetornarListaDTO() {
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad));

        List<EspecialidadResponseDTO> resultado = especialidadService.buscarVariosId("1");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Cardiología", resultado.get(0).getEspecialidad());
    }

    @Test
    void crear_deberiaGuardarYRetornarDTO() {
        when(especialidadRepository.save(any(Especialidad.class))).thenReturn(especialidad);

        EspecialidadResponseDTO resultado = especialidadService.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("Cardiología", resultado.getEspecialidad());
        verify(especialidadRepository, times(1)).save(any(Especialidad.class));
    }

    @Test
    void actualizar_deberiaRetornarDTOActualizado() {
        Especialidad actualizada = new Especialidad(1L, "Pediatría");
        EspecialidadRequestDTO requestActualizar = new EspecialidadRequestDTO(null, "Pediatría");

        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidad));
        when(especialidadRepository.save(any(Especialidad.class))).thenReturn(actualizada);

        EspecialidadResponseDTO resultado = especialidadService.actualizar(1L, requestActualizar);

        assertNotNull(resultado);
        assertEquals("Pediatría", resultado.getEspecialidad());
    }

    @Test
    void borrar_cuandoExiste_deberiaEjecutarDelete() {
        when(especialidadRepository.existsById(1L)).thenReturn(true);
        doNothing().when(especialidadRepository).deleteById(1L);

        assertDoesNotThrow(() -> especialidadService.borrar(1L));
        verify(especialidadRepository, times(1)).deleteById(1L);
    }

    @Test
    void borrar_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(especialidadRepository.existsById(99L)).thenReturn(false);

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            especialidadService.borrar(99L);
        });
        assertEquals("especialidad no existe", excepcion.getMessage());
        verify(especialidadRepository, never()).deleteById(anyLong());
    }
}