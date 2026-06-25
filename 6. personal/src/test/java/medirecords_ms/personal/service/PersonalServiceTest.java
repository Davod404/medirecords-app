package medirecords_ms.personal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

import medirecords_ms.personal.client.CargoCliente;
import medirecords_ms.personal.client.EspecialidadCliente;
import medirecords_ms.personal.dto.CargoDTO;
import medirecords_ms.personal.dto.EspecialidadDTO;
import medirecords_ms.personal.dto.PersonalRequestDTO;
import medirecords_ms.personal.dto.PersonalResponseDTO;
import medirecords_ms.personal.model.Personal;
import medirecords_ms.personal.repository.PersonalRepository;

@ExtendWith(MockitoExtension.class)
public class PersonalServiceTest {

    @Mock
    private PersonalRepository personalRepository;

    @Mock
    private CargoCliente cargoCliente;

    @Mock
    private EspecialidadCliente especialidadCliente;

    @InjectMocks
    private PersonalService personalService;

    private Personal personal;
    private PersonalRequestDTO requestDTO;
    private CargoDTO cargoDTO;
    private EspecialidadDTO especialidadDTO;

    @BeforeEach
    void setUp() {
        cargoDTO = new CargoDTO(1L, "Médico General");
        especialidadDTO = new EspecialidadDTO(1L, "Cardiología");
        personal = new Personal(1L, "87654321", '1', "Ana María", "González López",
                "987654321", "ana@email.com", 1L, "1");
        requestDTO = new PersonalRequestDTO(null, "87654321", '1', "Ana María", "González López",
                "987654321", "ana@email.com", 1L, "1");
    }

    @Test
    void listarTodos_deberiaRetornarLista() {
        when(personalRepository.findAll()).thenReturn(Arrays.asList(personal));
        when(cargoCliente.buscarId(1L)).thenReturn(cargoDTO);
        when(especialidadCliente.buscarVariosId("1")).thenReturn(Arrays.asList(especialidadDTO));

        List<PersonalResponseDTO> resultado = personalService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Ana María", resultado.get(0).getNombresPersonal());
        assertEquals("Médico General", resultado.get(0).getCargo().getCargo());
        assertEquals(1, resultado.get(0).getEspecialidades().size());
        verify(personalRepository, times(1)).findAll();
        verify(cargoCliente, times(1)).buscarId(1L);
        verify(especialidadCliente, times(1)).buscarVariosId("1");

        /* CASO HIPOTÉTICO DE FALLA PARA QA:
           Si las especialidades están vacías, el Service lanza RuntimeException.
           Verificar que los datos de prueba siempre tengan al menos una especialidad.
        */
    }

    @Test
    void buscarId_cuandoExiste_deberiaRetornarPersonal() {
        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));

        Personal resultado = personalService.buscarId(1L);

        assertNotNull(resultado);
        assertEquals("Ana María", resultado.getNombresPersonal());
    }

    @Test
    void buscarId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(personalRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            personalService.buscarId(99L);
        });
        assertEquals("personal no encontrado", excepcion.getMessage());
    }

    @Test
    void existeId_deberiaRetornarBoolean() {
        when(personalRepository.existsById(1L)).thenReturn(true);
        when(personalRepository.existsById(99L)).thenReturn(false);

        assertTrue(personalService.existeId(1L));
        assertFalse(personalService.existeId(99L));
    }

    @Test
    void buscarDetallado_deberiaRetornarDTO() {
        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(cargoCliente.buscarId(1L)).thenReturn(cargoDTO);
        when(especialidadCliente.buscarVariosId("1")).thenReturn(Arrays.asList(especialidadDTO));

        PersonalResponseDTO resultado = personalService.buscarDetallado(1L);

        assertNotNull(resultado);
        assertEquals("Ana María", resultado.getNombresPersonal());
        assertEquals("Médico General", resultado.getCargo().getCargo());
        verify(especialidadCliente, times(1)).buscarVariosId("1");
    }

    @Test
    void crear_deberiaGuardarYRetornarDTO() {
        when(cargoCliente.buscarId(1L)).thenReturn(cargoDTO);
        when(especialidadCliente.buscarVariosId("1")).thenReturn(Arrays.asList(especialidadDTO));
        when(personalRepository.save(any(Personal.class))).thenReturn(personal);

        PersonalResponseDTO resultado = personalService.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("Ana María", resultado.getNombresPersonal());
        assertEquals("Médico General", resultado.getCargo().getCargo());
        verify(personalRepository, times(1)).save(any(Personal.class));
    }

    @Test
    void actualizar_deberiaRetornarDTOActualizado() {
        Personal actualizado = new Personal(1L, "11111111", '2', "Luis", "Contreras",
                "111111111", "luis@email.com", 1L, "1");
        PersonalRequestDTO requestActualizar = new PersonalRequestDTO(null, "11111111", '2', "Luis", "Contreras",
                "111111111", "luis@email.com", 1L, "1");

        when(personalRepository.findById(1L)).thenReturn(Optional.of(personal));
        when(cargoCliente.buscarId(1L)).thenReturn(cargoDTO);
        when(especialidadCliente.buscarVariosId("1")).thenReturn(Arrays.asList(especialidadDTO));
        when(personalRepository.save(any(Personal.class))).thenReturn(actualizado);

        PersonalResponseDTO resultado = personalService.actualizar(1L, requestActualizar);

        assertNotNull(resultado);
        assertEquals("Luis", resultado.getNombresPersonal());
        verify(personalRepository, times(1)).save(any(Personal.class));
    }

    @Test
    void borrar_cuandoExiste_deberiaEjecutarDelete() {
        when(personalRepository.existsById(1L)).thenReturn(true);
        doNothing().when(personalRepository).deleteById(1L);

        assertDoesNotThrow(() -> personalService.borrar(1L));
        verify(personalRepository, times(1)).deleteById(1L);
    }

    @Test
    void borrar_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(personalRepository.existsById(99L)).thenReturn(false);

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            personalService.borrar(99L);
        });
        assertEquals("personal no existe", excepcion.getMessage());
        verify(personalRepository, never()).deleteById(anyLong());
    }
}