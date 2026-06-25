package medirecords_ms.medicamento.service;

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

import medirecords_ms.medicamento.dto.MedicamentoRequestDTO;
import medirecords_ms.medicamento.dto.MedicamentoResponseDTO;
import medirecords_ms.medicamento.model.Medicamento;
import medirecords_ms.medicamento.repository.MedicamentoRepository;

@ExtendWith(MockitoExtension.class)
public class MedicamentoServiceTest {

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @InjectMocks
    private MedicamentoService medicamentoService;

    private Medicamento medicamento;
    private MedicamentoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        medicamento = new Medicamento(1L, "Paracetamol", "Genfar", "Tableta", 3500, 100);
        requestDTO = new MedicamentoRequestDTO(null, "Paracetamol", "Genfar", "Tableta", 3500, 100);
    }

    @Test
    void listarTodos_deberiaRetornarListaDeMedicamentos() {
        when(medicamentoRepository.findAll()).thenReturn(Arrays.asList(medicamento));

        List<MedicamentoResponseDTO> resultado = medicamentoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Paracetamol", resultado.get(0).getNombre());
        verify(medicamentoRepository, times(1)).findAll();

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el repositorio retorna lista vacía, el test fallaría.
        */
    }

    @Test
    void buscarId_cuandoExiste_deberiaRetornarMedicamento() {
        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(medicamento));

        Medicamento resultado = medicamentoService.buscarId(1L);

        assertNotNull(resultado);
        assertEquals("Paracetamol", resultado.getNombre());
        verify(medicamentoRepository, times(1)).findById(1L);
    }

    @Test
    void buscarId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(medicamentoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            medicamentoService.buscarId(99L);
        });
        assertEquals("Medicamento no encontrado", excepcion.getMessage());
    }

    @Test
    void existeId_deberiaRetornarBoolean() {
        when(medicamentoRepository.existsById(1L)).thenReturn(true);
        when(medicamentoRepository.existsById(99L)).thenReturn(false);

        assertTrue(medicamentoService.existeId(1L));
        assertFalse(medicamentoService.existeId(99L));
    }

    @Test
    void buscarDetallado_cuandoExiste_deberiaRetornarDTO() {
        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(medicamento));

        MedicamentoResponseDTO resultado = medicamentoService.buscarDetallado(1L);

        assertNotNull(resultado);
        assertEquals("Paracetamol", resultado.getNombre());
        assertEquals(3500, resultado.getPrecio());
        assertEquals(100, resultado.getStock());
    }

    @Test
    void buscarVariosId_deberiaRetornarListaDTO() {
        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(medicamento));

        List<MedicamentoResponseDTO> resultado = medicamentoService.buscarVariosId("1");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Paracetamol", resultado.get(0).getNombre());
    }

    @Test
    void crear_deberiaGuardarYRetornarDTO() {
        when(medicamentoRepository.save(any(Medicamento.class))).thenReturn(medicamento);

        MedicamentoResponseDTO resultado = medicamentoService.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("Paracetamol", resultado.getNombre());
        verify(medicamentoRepository, times(1)).save(any(Medicamento.class));

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el DTO tiene campos nulos, la validación debería fallar antes del Service.
        */
    }

    @Test
    void actualizar_cuandoExiste_deberiaRetornarDTOActualizado() {
        Medicamento actualizado = new Medicamento(1L, "Ibuprofeno", "Bayer", "Cápsula", 5200, 80);
        MedicamentoRequestDTO requestActualizar = new MedicamentoRequestDTO(null, "Ibuprofeno", "Bayer", "Cápsula", 5200, 80);

        when(medicamentoRepository.findById(1L)).thenReturn(Optional.of(medicamento));
        when(medicamentoRepository.save(any(Medicamento.class))).thenReturn(actualizado);

        MedicamentoResponseDTO resultado = medicamentoService.actualizar(1L, requestActualizar);

        assertNotNull(resultado);
        assertEquals("Ibuprofeno", resultado.getNombre());
        assertEquals(5200, resultado.getPrecio());
    }

    @Test
    void borrar_cuandoExiste_deberiaEjecutarDelete() {
        when(medicamentoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(medicamentoRepository).deleteById(1L);

        assertDoesNotThrow(() -> medicamentoService.borrar(1L));
        verify(medicamentoRepository, times(1)).deleteById(1L);
    }

    @Test
    void borrar_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(medicamentoRepository.existsById(99L)).thenReturn(false);

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            medicamentoService.borrar(99L);
        });
        assertEquals("medicamento no existe", excepcion.getMessage());
        verify(medicamentoRepository, never()).deleteById(anyLong());
    }
}