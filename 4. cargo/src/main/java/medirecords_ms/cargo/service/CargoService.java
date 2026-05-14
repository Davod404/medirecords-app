package medirecords_ms.cargo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import medirecords_ms.cargo.dto.CargoRequestDTO;
import medirecords_ms.cargo.dto.CargoResponseDTO;
import medirecords_ms.cargo.model.Cargo;
import medirecords_ms.cargo.repository.CargoRepository;

@Service
public class CargoService {
    @Autowired private CargoRepository cargoRepository;

        public List<CargoResponseDTO> listarTodos(){
        List<Cargo> lista = cargoRepository.findAll();
        List<CargoResponseDTO> resultado = new ArrayList<>();
        
        for (Cargo cargo : lista){
            CargoResponseDTO respuesta = new CargoResponseDTO(
                cargo.getId(),
                cargo.getCargo()
            );
            resultado.add(respuesta);
        }
        return resultado;
    }

    public Cargo buscarId(Long id){
        return cargoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));
    }

    public Boolean existeId(Long id){
        return cargoRepository.existsById(id);
    }

    public CargoResponseDTO buscarDetallado(Long id) {
        Cargo cargo = buscarId(id);
        return new CargoResponseDTO(
            cargo.getId(),
            cargo.getCargo()
        );
    }
    
    public CargoResponseDTO crear(CargoRequestDTO request){
        Cargo nuevo = new Cargo();
        nuevo.setCargo(request.getCargo());
        Cargo guardado = cargoRepository.save(nuevo);
        
        return new CargoResponseDTO(
            guardado.getId(),
            guardado.getCargo()
        );
    }

    public CargoResponseDTO actualizar(Long id, CargoRequestDTO request){
        Cargo encontrado = buscarId(id);
        encontrado.setCargo(request.getCargo());
        Cargo actualizado = cargoRepository.save(encontrado);

        return new CargoResponseDTO(
            actualizado.getId(),
            actualizado.getCargo()
        );
    }

    public void borrar(Long id){
        if (!existeId(id)){
            throw new RuntimeException("cargo no existe");
        }
        cargoRepository.deleteById(id);
    }
}