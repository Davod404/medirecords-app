package medirecords_ms.consulta.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import medirecords_ms.consulta.client.HospitalCliente;
import medirecords_ms.consulta.client.PacienteCliente;
import medirecords_ms.consulta.client.PersonalCliente;
import medirecords_ms.consulta.dto.ConsultaRequestDTO;
import medirecords_ms.consulta.dto.ConsultaResponseDTO;
import medirecords_ms.consulta.dto.HospitalDTO;
import medirecords_ms.consulta.dto.PacienteDTO;
import medirecords_ms.consulta.dto.PersonalDTO;
import medirecords_ms.consulta.model.Consulta;
import medirecords_ms.consulta.repository.ConsultaRepository;

@ExtendWith(MockitoExtension.class)
public class ConsultaServiceTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private HospitalCliente hospitalCliente;

    @Mock
    private PersonalCliente personalCliente;

    @Mock
    private PacienteCliente pacienteCliente;

    @InjectMocks
    private ConsultaService consultaService;

    private Consulta consulta;
    private ConsultaRequestDTO requestDTO;
    private PacienteDTO pacienteDTO;
    private PersonalDTO personalDTO;
    private HospitalDTO hospitalDTO;

    @BeforeEach
    void setUp() {
        pacienteDTO = new PacienteDTO();
        personalDTO = new PersonalDTO();
        hospitalDTO = new HospitalDTO();
        consulta = new Consulta(1L, LocalDate.now(), "Dolor de cabeza", "Migraña", 1L, 1L, 1L);
        requestDTO = new ConsultaRequestDTO(null, LocalDate.now(), "Dolor de cabeza", "Migraña", 1L, 1L, 1L);
    }

    @Test
    void listarTodos_deberiaRetornarLista() {
        when(consultaRepository.findAll()).thenReturn(Arrays.asList(consulta));
        when(pacienteCliente.buscarDetallado(1L)).thenReturn(pacienteDTO);
        when(personalCliente.buscarDetallado(1L)).thenReturn(personalDTO);
        when(hospitalCliente.buscarDetallado(1L)).thenReturn(hospitalDTO);

        List<ConsultaResponseDTO> resultado = consultaService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Dolor de cabeza", resultado.get(0).getMotivo());
        assertEquals("Migraña", resultado.get(0).getDiagnostico());
        assertNotNull(resultado.get(0).getPaciente());
        assertNotNull(resultado.get(0).getPersonal());
        assertNotNull(resultado.get(0).getHospital());
        verify(consultaRepository, times(1)).findAll();
    }

    @Test
    void existeId_deberiaRetornarBoolean() {
        when(consultaRepository.existsById(1L)).thenReturn(true);
        when(consultaRepository.existsById(99L)).thenReturn(false);

        assertTrue(consultaService.existeId(1L));
        assertFalse(consultaService.existeId(99L));
    }

    @Test
    void buscarDetallado_deberiaRetornarDTO() {
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(pacienteCliente.buscarDetallado(1L)).thenReturn(pacienteDTO);
        when(personalCliente.buscarDetallado(1L)).thenReturn(personalDTO);
        when(hospitalCliente.buscarDetallado(1L)).thenReturn(hospitalDTO);

        ConsultaResponseDTO resultado = consultaService.buscarDetallado(1L);

        assertNotNull(resultado);
        assertEquals("Dolor de cabeza", resultado.getMotivo());
    }

    @Test
    void crear_deberiaGuardarYRetornarDTO() {
        when(pacienteCliente.buscarDetallado(1L)).thenReturn(pacienteDTO);
        when(personalCliente.buscarDetallado(1L)).thenReturn(personalDTO);
        when(hospitalCliente.buscarDetallado(1L)).thenReturn(hospitalDTO);
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consulta);

        ConsultaResponseDTO resultado = consultaService.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("Dolor de cabeza", resultado.getMotivo());
        verify(consultaRepository, times(1)).save(any(Consulta.class));
    }

    @Test
    void actualizar_deberiaRetornarDTOActualizado() {
        Consulta actualizada = new Consulta(1L, LocalDate.now(), "Dolor de espalda", "Lumbalgia", 1L, 1L, 1L);
        ConsultaRequestDTO requestActualizar = new ConsultaRequestDTO(null, LocalDate.now(), "Dolor de espalda", "Lumbalgia", 1L, 1L, 1L);

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(pacienteCliente.buscarDetallado(1L)).thenReturn(pacienteDTO);
        when(personalCliente.buscarDetallado(1L)).thenReturn(personalDTO);
        when(hospitalCliente.buscarDetallado(1L)).thenReturn(hospitalDTO);
        when(consultaRepository.save(any(Consulta.class))).thenReturn(actualizada);

        ConsultaResponseDTO resultado = consultaService.actualizar(1L, requestActualizar);

        assertNotNull(resultado);
        assertEquals("Dolor de espalda", resultado.getMotivo());
        verify(consultaRepository, times(1)).save(any(Consulta.class));
    }

    @Test
    void borrar_cuandoExiste_deberiaEjecutarDelete() {
        when(consultaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(consultaRepository).deleteById(1L);

        assertDoesNotThrow(() -> consultaService.borrar(1L));
        verify(consultaRepository, times(1)).deleteById(1L);
    }

    @Test
    void borrar_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(consultaRepository.existsById(99L)).thenReturn(false);

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            consultaService.borrar(99L);
        });
        assertEquals("consulta no existe", excepcion.getMessage());
        verify(consultaRepository, never()).deleteById(anyLong());
    }
}