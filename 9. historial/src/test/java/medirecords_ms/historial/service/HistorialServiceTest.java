package medirecords_ms.historial.service;

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

import medirecords_ms.historial.client.ConsultaCliente;
import medirecords_ms.historial.client.PacienteCliente;
import medirecords_ms.historial.dto.ConsultaDTO;
import medirecords_ms.historial.dto.HistorialRequestDTO;
import medirecords_ms.historial.dto.HistorialResponseDTO;
import medirecords_ms.historial.dto.PacienteDTO;
import medirecords_ms.historial.model.Historial;
import medirecords_ms.historial.repository.HistorialRepository;

@ExtendWith(MockitoExtension.class)
public class HistorialServiceTest {

    @Mock
    private HistorialRepository historialRepository;

    @Mock
    private PacienteCliente pacienteCliente;

    @Mock
    private ConsultaCliente consultaCliente;

    @InjectMocks
    private HistorialService historialService;

    private Historial historial;
    private HistorialRequestDTO requestDTO;
    private PacienteDTO pacienteDTO;
    private ConsultaDTO consultaDTO;

    @BeforeEach
    void setUp() {
        pacienteDTO = new PacienteDTO();
        consultaDTO = new ConsultaDTO();
        historial = new Historial(1L, "Paciente con migraña crónica", LocalDate.now(), 1L, "1");
        requestDTO = new HistorialRequestDTO(null, "Paciente con migraña crónica", LocalDate.now(), 1L, "1");
    }

    @Test
    void listarTodos_deberiaRetornarLista() {
        when(historialRepository.findAll()).thenReturn(Arrays.asList(historial));
        when(pacienteCliente.buscarId(1L)).thenReturn(pacienteDTO);
        when(consultaCliente.buscarVariosId("1")).thenReturn(Arrays.asList(consultaDTO));

        List<HistorialResponseDTO> resultado = historialService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Paciente con migraña crónica", resultado.get(0).getNotas());
        assertNotNull(resultado.get(0).getPacienteId());
        assertEquals(1, resultado.get(0).getConsultasId().size());
        verify(historialRepository, times(1)).findAll();
    }

    @Test
    void buscarId_cuandoExiste_deberiaRetornarHistorial() {
        when(historialRepository.findById(1L)).thenReturn(Optional.of(historial));

        Historial resultado = historialService.buscarId(1L);

        assertNotNull(resultado);
        assertEquals("Paciente con migraña crónica", resultado.getNotas());
    }

    @Test
    void buscarId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(historialRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            historialService.buscarId(99L);
        });
        assertEquals("historial no encontrado", excepcion.getMessage());
    }

    @Test
    void existeId_deberiaRetornarBoolean() {
        when(historialRepository.existsById(1L)).thenReturn(true);
        when(historialRepository.existsById(99L)).thenReturn(false);

        assertTrue(historialService.existeId(1L));
        assertFalse(historialService.existeId(99L));
    }

    @Test
    void buscarDetallado_deberiaRetornarDTO() {
        when(historialRepository.findById(1L)).thenReturn(Optional.of(historial));
        when(pacienteCliente.buscarId(1L)).thenReturn(pacienteDTO);
        when(consultaCliente.buscarVariosId("1")).thenReturn(Arrays.asList(consultaDTO));

        HistorialResponseDTO resultado = historialService.buscarDetallado(1L);

        assertNotNull(resultado);
        assertEquals("Paciente con migraña crónica", resultado.getNotas());
    }

    @Test
    void crear_deberiaGuardarYRetornarDTO() {
        when(pacienteCliente.buscarId(1L)).thenReturn(pacienteDTO);
        when(consultaCliente.buscarVariosId("1")).thenReturn(Arrays.asList(consultaDTO));
        when(historialRepository.save(any(Historial.class))).thenReturn(historial);

        HistorialResponseDTO resultado = historialService.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("Paciente con migraña crónica", resultado.getNotas());
        verify(historialRepository, times(1)).save(any(Historial.class));
    }

    @Test
    void actualizar_deberiaRetornarDTOActualizado() {
        Historial actualizado = new Historial(1L, "Notas actualizadas", LocalDate.now(), 1L, "1,2");
        HistorialRequestDTO requestActualizar = new HistorialRequestDTO(null, "Notas actualizadas", LocalDate.now(), 1L, "1,2");

        when(historialRepository.findById(1L)).thenReturn(Optional.of(historial));
        when(pacienteCliente.buscarId(1L)).thenReturn(pacienteDTO);
        when(consultaCliente.buscarVariosId("1")).thenReturn(Arrays.asList(consultaDTO));
        when(historialRepository.save(any(Historial.class))).thenReturn(actualizado);

        HistorialResponseDTO resultado = historialService.actualizar(1L, requestActualizar);

        assertNotNull(resultado);
        assertEquals("Notas actualizadas", resultado.getNotas());
        verify(historialRepository, times(1)).save(any(Historial.class));
    }

    @Test
    void borrar_cuandoExiste_deberiaEjecutarDelete() {
        when(historialRepository.existsById(1L)).thenReturn(true);
        doNothing().when(historialRepository).deleteById(1L);

        assertDoesNotThrow(() -> historialService.borrar(1L));
        verify(historialRepository, times(1)).deleteById(1L);
    }

    @Test
    void borrar_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(historialRepository.existsById(99L)).thenReturn(false);

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            historialService.borrar(99L);
        });
        assertEquals("historial no existe", excepcion.getMessage());
        verify(historialRepository, never()).deleteById(anyLong());
    }
}