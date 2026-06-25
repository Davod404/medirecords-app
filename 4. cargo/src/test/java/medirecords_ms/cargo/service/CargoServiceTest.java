package medirecords_ms.cargo.service;

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

import medirecords_ms.cargo.dto.CargoRequestDTO;
import medirecords_ms.cargo.dto.CargoResponseDTO;
import medirecords_ms.cargo.model.Cargo;
import medirecords_ms.cargo.repository.CargoRepository;

@ExtendWith(MockitoExtension.class)
public class CargoServiceTest {

    @Mock
    private CargoRepository cargoRepository;

    @InjectMocks
    private CargoService cargoService;

    private Cargo cargo;
    private CargoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        cargo = new Cargo(1L, "Médico General");
        requestDTO = new CargoRequestDTO(null, "Médico General");
    }

    @Test
    void listarTodos_deberiaRetornarListaDeCargos() {
        when(cargoRepository.findAll()).thenReturn(Arrays.asList(cargo));

        List<CargoResponseDTO> resultado = cargoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Médico General", resultado.get(0).getCargo());
        verify(cargoRepository, times(1)).findAll();
    }

    @Test
    void buscarId_cuandoExiste_deberiaRetornarCargo() {
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));

        Cargo resultado = cargoService.buscarId(1L);

        assertNotNull(resultado);
        assertEquals("Médico General", resultado.getCargo());
    }

    @Test
    void buscarId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(cargoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            cargoService.buscarId(99L);
        });
        assertEquals("Cargo no encontrado", excepcion.getMessage());
    }

    @Test
    void existeId_deberiaRetornarBoolean() {
        when(cargoRepository.existsById(1L)).thenReturn(true);
        when(cargoRepository.existsById(99L)).thenReturn(false);

        assertTrue(cargoService.existeId(1L));
        assertFalse(cargoService.existeId(99L));
    }

    @Test
    void buscarDetallado_deberiaRetornarDTO() {
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));

        CargoResponseDTO resultado = cargoService.buscarDetallado(1L);

        assertNotNull(resultado);
        assertEquals("Médico General", resultado.getCargo());
    }

    @Test
    void crear_deberiaGuardarYRetornarDTO() {
        when(cargoRepository.save(any(Cargo.class))).thenReturn(cargo);

        CargoResponseDTO resultado = cargoService.crear(requestDTO);

        assertNotNull(resultado);
        assertEquals("Médico General", resultado.getCargo());
        verify(cargoRepository, times(1)).save(any(Cargo.class));
    }

    @Test
    void actualizar_deberiaRetornarDTOActualizado() {
        Cargo actualizado = new Cargo(1L, "Enfermero");
        CargoRequestDTO requestActualizar = new CargoRequestDTO(null, "Enfermero");

        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));
        when(cargoRepository.save(any(Cargo.class))).thenReturn(actualizado);

        CargoResponseDTO resultado = cargoService.actualizar(1L, requestActualizar);

        assertNotNull(resultado);
        assertEquals("Enfermero", resultado.getCargo());
    }

    @Test
    void borrar_cuandoExiste_deberiaEjecutarDelete() {
        when(cargoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(cargoRepository).deleteById(1L);

        assertDoesNotThrow(() -> cargoService.borrar(1L));
        verify(cargoRepository, times(1)).deleteById(1L);
    }

    @Test
    void borrar_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(cargoRepository.existsById(99L)).thenReturn(false);

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            cargoService.borrar(99L);
        });
        assertEquals("cargo no existe", excepcion.getMessage());
        verify(cargoRepository, never()).deleteById(anyLong());
    }
}