package medirecords_ms.receta.service;

import java.time.LocalDate;
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

import medirecords_ms.receta.client.ConsultaCliente;
import medirecords_ms.receta.client.MedicamentoCliente;
import medirecords_ms.receta.dto.ConsultaDTO;
import medirecords_ms.receta.dto.MedicamentoDTO;
import medirecords_ms.receta.dto.RecetaRequestDTO;
import medirecords_ms.receta.dto.RecetaResponseDTO;
import medirecords_ms.receta.model.Receta;
import medirecords_ms.receta.repository.RecetaRepository;

@ExtendWith(MockitoExtension.class)
public class RecetaServiceTest {

    @Mock
    private RecetaRepository recetaRepository;

    @Mock
    private ConsultaCliente consultaCliente;

    @Mock
    private MedicamentoCliente medicamentoCliente;

    @InjectMocks
    private RecetaService recetaService;

    private Receta receta;
    private RecetaRequestDTO requestDTO;
    private ConsultaDTO consultaDTO;
    private MedicamentoDTO medicamentoDTO;

    @BeforeEach
    void setUp() {
        consultaDTO = new ConsultaDTO(1L, LocalDate.now(), "Dolor de cabeza", "Migraña");
        medicamentoDTO = new MedicamentoDTO(1L, "Paracetamol", "Genfar", "Tableta", 3500, 100);
        receta = new Receta(1L, LocalDate.now(), "Tomar cada 8 horas", 1L, "1");
        requestDTO = new RecetaRequestDTO(null, LocalDate.now(), "Tomar cada 8 horas", 1L, "1");
    }

    @Test
    void listarTodos_deberiaRetornarLista() {
        when(recetaRepository.findAll()).thenReturn(Arrays.asList(receta));
        when(consultaCliente.buscarId(1L)).thenReturn(consultaDTO);
        when(medicamentoCliente.buscarVariosId("1")).thenReturn(Arrays.asList(medicamentoDTO));

        List<RecetaResponseDTO> resultado = recetaService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Tomar cada 8 horas", resultado.get(0).getInstrucciones());
        assertNotNull(resultado.get(0).getConsulta());
        assertEquals(1, resultado.get(0).getMedicamento().size());
        verify(recetaRepository, times(1)).findAll();
        verify(consultaCliente, times(1)).buscarId(1L);
        verify(medicamentoCliente, times(1)).buscarVariosId("1");
    }

    @Test
    void existeId_deberiaRetornarBoolean() {
        when(recetaRepository.existsById(1L)).thenReturn(true);
        when(recetaRepository.existsById(99L)).thenReturn(false);

        assertTrue(recetaService.existeId(1L));
        assertFalse(recetaService.existeId(99L));
    }

    @Test
    void buscarDetallado_deberiaRetornarDTO() {
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));
        when(consultaCliente.buscarId(1L)).thenReturn(consultaDTO);
        when(medicamentoCliente.buscarVariosId("1")).thenReturn(Arrays.asList(medicamentoDTO));

        RecetaResponseDTO resultado = recetaService.buscarDetallado(1L);

        assertNotNull(resultado);
        assertEquals("Tomar cada 8 horas", resultado.getInstrucciones());
        assertEquals("Paracetamol", resultado.getMedicamento().get(0).getNombre());
    }

    @Test
    void crear_deberiaGuardarYRetornarDTO() {
        when(consultaCliente.buscarId(1L)).thenReturn(consultaDTO);
        when(medicamentoCliente.buscarVariosId("1")).thenReturn(Arrays.asList(medicamentoDTO));
        when(recetaRepository.save(any(Receta.class))).thenReturn(receta);

        RecetaResponseDTO resultado = recetaService.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("Tomar cada 8 horas", resultado.getInstrucciones());
        verify(recetaRepository, times(1)).save(any(Receta.class));
    }

    @Test
    void actualizar_deberiaRetornarDTOActualizado() {
        Receta actualizada = new Receta(1L, LocalDate.now(), "Tomar cada 12 horas", 1L, "1,2");
        RecetaRequestDTO requestActualizar = new RecetaRequestDTO(null, LocalDate.now(), "Tomar cada 12 horas", 1L, "1,2");

        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));
        when(consultaCliente.buscarId(1L)).thenReturn(consultaDTO);
        when(medicamentoCliente.buscarVariosId("1,2")).thenReturn(Arrays.asList(medicamentoDTO));
        when(recetaRepository.save(any(Receta.class))).thenReturn(actualizada);

        RecetaResponseDTO resultado = recetaService.actualizar(1L, requestActualizar);

        assertNotNull(resultado);
        assertEquals("Tomar cada 12 horas", resultado.getInstrucciones());
        verify(recetaRepository, times(1)).save(any(Receta.class));
    }

    @Test
    void borrar_cuandoExiste_deberiaEjecutarDelete() {
        when(recetaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(recetaRepository).deleteById(1L);

        assertDoesNotThrow(() -> recetaService.borrar(1L));
        verify(recetaRepository, times(1)).deleteById(1L);
    }

    @Test
    void borrar_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(recetaRepository.existsById(99L)).thenReturn(false);

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            recetaService.borrar(99L);
        });
        assertEquals("receta no existe", excepcion.getMessage());
        verify(recetaRepository, never()).deleteById(anyLong());
    }
}