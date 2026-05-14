package medirecords_ms.personal.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import medirecords_ms.personal.client.CargoCliente;
import medirecords_ms.personal.client.EspecialidadCliente;
import medirecords_ms.personal.dto.CargoDTO;
import medirecords_ms.personal.dto.EspecialidadDTO;
import medirecords_ms.personal.dto.PersonalRequestDTO;
import medirecords_ms.personal.dto.PersonalResponseDTO;
import medirecords_ms.personal.model.Personal;
import medirecords_ms.personal.repository.PersonalRepository;

@Service
public class PersonalService {
    @Autowired private PersonalRepository personalRepository;
    @Autowired private CargoCliente cargoCliente;
    @Autowired private EspecialidadCliente especialidadCliente;

    public List<PersonalResponseDTO> listarTodos() {
        List<Personal> lista = personalRepository.findAll();
        List<PersonalResponseDTO> resultado = new ArrayList<>();
        
        for (Personal personal : lista) {
            CargoDTO cargo = cargoCliente.buscarId(personal.getCargoId());
            
            List<EspecialidadDTO> especialidades = especialidadCliente.buscarVariosId(personal.getEspecialidadesId());
            if(especialidades.isEmpty()){
                throw new RuntimeException("especialidades no puede estar vacio");
            }

            PersonalResponseDTO response = new PersonalResponseDTO(
                personal.getId(),
                personal.getRut(),
                personal.getDvRut(),
                personal.getNombresPersonal(),
                personal.getApellidosPersonal(),
                personal.getTelefono(),
                personal.getEmail(),
                cargo,
                especialidades
            );
            resultado.add(response);
        }
        return resultado;
    }

    public Personal buscarId(Long id){
        return personalRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("personal no encontrado"));
    }

    public Boolean existeId(Long id){
        return personalRepository.existsById(id);
    }

    public PersonalResponseDTO buscarDetallado(Long id) {
        Personal encontrado = buscarId(id);

        CargoDTO cargo = cargoCliente.buscarId(encontrado.getCargoId());
        
        List<EspecialidadDTO> especialidades = especialidadCliente.buscarVariosId(encontrado.getEspecialidadesId());
        if(especialidades.isEmpty()){
            throw new RuntimeException("especialidades no puede estar vacio");
        }

        return new PersonalResponseDTO(
            encontrado.getId(),
            encontrado.getRut(),
            encontrado.getDvRut(),
            encontrado.getNombresPersonal(),
            encontrado.getApellidosPersonal(),
            encontrado.getTelefono(),
            encontrado.getEmail(),
            cargo,
            especialidades
        );
    }
    
    public PersonalResponseDTO crear(PersonalRequestDTO request) {
        CargoDTO cargo = cargoCliente.buscarId(request.getCargoId());
        
        String especialidadesId = request.getEspecialidadesId();
        List<EspecialidadDTO> especialidades = especialidadCliente.buscarVariosId(request.getEspecialidadesId());
        if(especialidades.isEmpty()){
            throw new RuntimeException("especialidades no puede estar vacio");
        }

        Personal personal = new Personal();
        personal.setRut(request.getRut());
        personal.setDvRut(request.getDvRut());
        personal.setNombresPersonal(request.getNombresPersonal());
        personal.setApellidosPersonal(request.getApellidosPersonal());
        personal.setTelefono(request.getTelefono());
        personal.setEmail(request.getEmail());
        personal.setCargoId(request.getCargoId());
        personal.setEspecialidadesId(especialidadesId);
        Personal guardado = personalRepository.save(personal);
        
        return new PersonalResponseDTO(
            guardado.getId(),
            guardado.getRut(),
            guardado.getDvRut(),
            guardado.getNombresPersonal(),
            guardado.getApellidosPersonal(),
            guardado.getTelefono(),
            guardado.getEmail(),
            cargo,
            especialidades
        );
    }

    public PersonalResponseDTO actualizar(Long id, PersonalRequestDTO request) {
        Personal encontrado = buscarId(id);
        
        CargoDTO cargo = cargoCliente.buscarId(request.getCargoId());
        
        String especialidadesId = request.getEspecialidadesId();
        List<EspecialidadDTO> especialidades = especialidadCliente.buscarVariosId(encontrado.getEspecialidadesId());
        if(especialidades.isEmpty()){
            throw new RuntimeException("especialidades no puede estar vacio");
        }
        
        encontrado.setRut(request.getRut());
        encontrado.setDvRut(request.getDvRut());
        encontrado.setNombresPersonal(request.getNombresPersonal());
        encontrado.setApellidosPersonal(request.getApellidosPersonal());
        encontrado.setTelefono(request.getTelefono());
        encontrado.setEmail(request.getEmail());
        encontrado.setCargoId(request.getCargoId());
        encontrado.setEspecialidadesId(especialidadesId);
        Personal actualizado = personalRepository.save(encontrado);
        
        return new PersonalResponseDTO(
            actualizado.getId(),
            actualizado.getRut(),
            actualizado.getDvRut(),
            actualizado.getNombresPersonal(),
            actualizado.getApellidosPersonal(),
            actualizado.getTelefono(),
            actualizado.getEmail(),
            cargo,
            especialidades
        );
    }

    public void borrar(Long id){
        if (!existeId(id)){
            throw new RuntimeException("personal no existe");
        }
        personalRepository.deleteById(id);
    }
}