package medirecords_ms.especialidad.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import medirecords_ms.especialidad.dto.EspecialidadRequestDTO;
import medirecords_ms.especialidad.dto.EspecialidadResponseDTO;
import medirecords_ms.especialidad.model.Especialidad;
import medirecords_ms.especialidad.repository.EspecialidadRepository;

@Service
public class EspecialidadService {
    @Autowired private EspecialidadRepository especialidadRepository;

    public List<EspecialidadResponseDTO> listarTodos(){
        List<Especialidad> lista = especialidadRepository.findAll();
        List<EspecialidadResponseDTO> resultado = new ArrayList<>();
        
        for (Especialidad especialidad : lista){
            EspecialidadResponseDTO respuesta = new EspecialidadResponseDTO(
                especialidad.getId(),
                especialidad.getEspecialidad()
            );
            resultado.add(respuesta);
        }
        return resultado;
    }

    public Especialidad buscarId(Long id){
        return especialidadRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Especialidad no encontrado"));
    }

    public Boolean existeId(Long id){
        return especialidadRepository.existsById(id);
    }

    public EspecialidadResponseDTO buscarDetallado(Long id){
        Especialidad especialidad = buscarId(id);
        return new EspecialidadResponseDTO(
            especialidad.getId(),
            especialidad.getEspecialidad()
        );
    }
    
    public EspecialidadResponseDTO crear(EspecialidadRequestDTO request){
        if (existeId(request.getId())){
            throw new RuntimeException("especialidad ya existe");
        }
        
        Especialidad nuevo = new Especialidad();
        nuevo.setEspecialidad(request.getEspecialidad());
        nuevo.setEspecialidad(request.getEspecialidad());
        Especialidad guardado = especialidadRepository.save(nuevo);
        
        return new EspecialidadResponseDTO(
            guardado.getId(),
            guardado.getEspecialidad()
        );
    }

    public EspecialidadResponseDTO actualizar(Long id, EspecialidadRequestDTO request){
        Especialidad encontrado = buscarId(id);
        
        encontrado.setEspecialidad(request.getEspecialidad());
        encontrado.setEspecialidad(request.getEspecialidad());
        Especialidad actualizado = especialidadRepository.save(encontrado);

        return new EspecialidadResponseDTO(
            actualizado.getId(),
            actualizado.getEspecialidad()
        );
    }

    public void borrar(Long id){
        if (!existeId(id)){
            throw new RuntimeException("especialidad no existe");
        }
        especialidadRepository.deleteById(id);
    }
}