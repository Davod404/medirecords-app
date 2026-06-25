package medirecords_ms.paciente.service;

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

import medirecords_ms.paciente.client.HospitalCliente;
import medirecords_ms.paciente.dto.HospitalDTO;
import medirecords_ms.paciente.dto.PacienteRequestDTO;
import medirecords_ms.paciente.dto.PacienteResponseDTO;
import medirecords_ms.paciente.model.Paciente;
import medirecords_ms.paciente.repository.PacienteRepository;

@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private HospitalCliente hospitalCliente;

    @InjectMocks
    private PacienteService pacienteService;

    private Paciente paciente;
    private PacienteRequestDTO requestDTO;
    private HospitalDTO hospitalDTO;

    @BeforeEach
    void setUp() {
        hospitalDTO = new HospitalDTO(1L, "Hospital Central", "Av. Principal 123", "271234567");
        paciente = new Paciente(1L, "12345678", '9', "Juan Carlos", "Pérez González",
                "912345678", "juan@email.com", LocalDate.of(1990, 5, 15), 1L);
        requestDTO = new PacienteRequestDTO(null, "12345678", '9', "Juan Carlos", "Pérez González",
                "912345678", "juan@email.com", LocalDate.of(1990, 5, 15), 1L);
    }

    /*
     * TEST 1: listarTodos()
     */
    @Test
    void listarTodos_deberiaRetornarListaDePacientes() {
        when(pacienteRepository.findAll()).thenReturn(Arrays.asList(paciente));
        when(hospitalCliente.buscarId(1L)).thenReturn(hospitalDTO);

        List<PacienteResponseDTO> resultado = pacienteService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan Carlos", resultado.get(0).getNombresPaciente());
        assertEquals("Hospital Central", resultado.get(0).getHospital().getNombre());

        verify(pacienteRepository, times(1)).findAll();
        verify(hospitalCliente, times(1)).buscarId(1L);

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si el Feign Client falla al obtener el hospital, el servicio
           debería manejar el error sin lanzar excepción no controlada.
        */
    }

    /*
     * TEST 2: buscarId() - Existe
     */
    @Test
    void buscarId_cuandoExiste_deberiaRetornarPaciente() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        Paciente resultado = pacienteService.buscarId(1L);

        assertNotNull(resultado);
        assertEquals("Juan Carlos", resultado.getNombresPaciente());
        verify(pacienteRepository, times(1)).findById(1L);
    }

    /*
     * TEST 3: buscarId() - No existe
     */
    @Test
    void buscarId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            pacienteService.buscarId(99L);
        });
        assertEquals("Paciente no encontrado", excepcion.getMessage());
        verify(pacienteRepository, times(1)).findById(99L);
    }

    /*
     * TEST 4: existeId()
     */
    @Test
    void existeId_deberiaRetornarBoolean() {
        when(pacienteRepository.existsById(1L)).thenReturn(true);
        when(pacienteRepository.existsById(99L)).thenReturn(false);

        assertTrue(pacienteService.existeId(1L));
        assertFalse(pacienteService.existeId(99L));
        verify(pacienteRepository, times(2)).existsById(anyLong());
    }

    /*
     * TEST 5: buscarDetallado()
     */
    @Test
    void buscarDetallado_cuandoExiste_deberiaRetornarDTO() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(hospitalCliente.buscarId(1L)).thenReturn(hospitalDTO);

        PacienteResponseDTO resultado = pacienteService.buscarDetallado(1L);

        assertNotNull(resultado);
        assertEquals("Juan Carlos", resultado.getNombresPaciente());
        assertEquals("Hospital Central", resultado.getHospital().getNombre());
        verify(hospitalCliente, times(1)).buscarId(1L);
    }

    /*
     * TEST 6: crear()
     */
    @Test
    void crear_deberiaGuardarYRetornarDTO() {
        when(hospitalCliente.buscarId(1L)).thenReturn(hospitalDTO);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        PacienteResponseDTO resultado = pacienteService.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("Juan Carlos", resultado.getNombresPaciente());
        assertEquals("Hospital Central", resultado.getHospital().getNombre());
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    /*
     * TEST 7: actualizar()
     */
    @Test
    void actualizar_cuandoExiste_deberiaRetornarDTOActualizado() {
        Paciente pacienteActualizado = new Paciente(1L, "87654321", 'K', "Ana María",
                "González", "987654321", "ana@email.com", LocalDate.of(1985, 8, 22), 1L);
        PacienteRequestDTO requestActualizar = new PacienteRequestDTO(null, "87654321", 'K', "Ana María",
                "González", "987654321", "ana@email.com", LocalDate.of(1985, 8, 22), 1L);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(hospitalCliente.buscarId(1L)).thenReturn(hospitalDTO);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteActualizado);

        PacienteResponseDTO resultado = pacienteService.actualizar(1L, requestActualizar);

        assertNotNull(resultado);
        assertEquals("Ana María", resultado.getNombresPaciente());
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    /*
     * TEST 8: borrar() - Existe
     */
    @Test
    void borrar_cuandoExiste_deberiaEjecutarDelete() {
        when(pacienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(pacienteRepository).deleteById(1L);

        assertDoesNotThrow(() -> pacienteService.borrar(1L));
        verify(pacienteRepository, times(1)).deleteById(1L);
    }

    /*
     * TEST 9: borrar() - No existe
     */
    @Test
    void borrar_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(pacienteRepository.existsById(99L)).thenReturn(false);

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            pacienteService.borrar(99L);
        });
        assertEquals("hospital no existe", excepcion.getMessage());
        verify(pacienteRepository, never()).deleteById(anyLong());
    }
}