package medirecords_ms.hospital.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import medirecords_ms.hospital.dto.HospitalRequestDTO;
import medirecords_ms.hospital.dto.HospitalResponseDTO;
import medirecords_ms.hospital.model.Hospital;
import medirecords_ms.hospital.repository.HospitalRepository;

@Service
public class HospitalService {
    @Autowired private HospitalRepository hospitalRepository;

    public List<HospitalResponseDTO> listarTodos(){
        List<Hospital> lista = hospitalRepository.findAll();
        List<HospitalResponseDTO> resultado = new ArrayList<>();

        for(Hospital hospital : lista){
            HospitalResponseDTO respuesta = new HospitalResponseDTO(
                hospital.getId(),
                hospital.getNombre(),
                hospital.getDireccion(),
                hospital.getTelefono()
            );
            resultado.add(respuesta);
        }
        return resultado;
    }

    public Hospital buscarId(Long id){
        return hospitalRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("hospital no encontrado"));
    }

    public Boolean existeId(Long id){
        return hospitalRepository.existsById(id);
    }
    
    public HospitalResponseDTO buscarDetallado(Long id) {
        Hospital hospital = buscarId(id);

        return new HospitalResponseDTO(
            hospital.getId(),
            hospital.getNombre(),
            hospital.getDireccion(),
            hospital.getTelefono()
        );
    }

    public HospitalResponseDTO crear(HospitalRequestDTO request){
        Hospital nuevo = new Hospital();
        nuevo.setNombre(request.getNombre());
        nuevo.setDireccion(request.getDireccion());
        nuevo.setTelefono(request.getTelefono());
        Hospital guardado = hospitalRepository.save(nuevo);

        return new HospitalResponseDTO(
            guardado.getId(),
            guardado.getNombre(),
            guardado.getDireccion(),
            guardado.getTelefono()
        );
    }

    public HospitalResponseDTO actualizar(Long id, HospitalRequestDTO request){
        Hospital encontrado = buscarId(id);
        encontrado.setNombre(request.getNombre());
        encontrado.setDireccion(request.getDireccion());
        encontrado.setTelefono(request.getTelefono());
        Hospital guardado = hospitalRepository.save(encontrado);

        return new HospitalResponseDTO(
            guardado.getId(),
            guardado.getNombre(),
            guardado.getDireccion(),
            guardado.getTelefono()
        );
    }

    public void borrar(Long id){
        if (!existeId(id)){
            throw new RuntimeException("hospital no existe");
        }
        hospitalRepository.deleteById(id);
    }
}
