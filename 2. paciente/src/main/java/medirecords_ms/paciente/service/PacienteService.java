package medirecords_ms.paciente.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import medirecords_ms.paciente.client.HospitalCliente;
import medirecords_ms.paciente.dto.HospitalDTO;
import medirecords_ms.paciente.dto.PacienteRequestDTO;
import medirecords_ms.paciente.dto.PacienteResponseDTO;
import medirecords_ms.paciente.model.Paciente;
import medirecords_ms.paciente.repository.PacienteRepository;

@Service
public class PacienteService {
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private HospitalCliente hospitalCliente;

    public List<PacienteResponseDTO> listarTodos(){
        List<Paciente> lista = pacienteRepository.findAll();
        List<PacienteResponseDTO> resultado = new ArrayList<>();
        
        for (Paciente paciente : lista){
            HospitalDTO hospital = hospitalCliente.buscarId(paciente.getHospitalId());
            
            PacienteResponseDTO respuesta = new PacienteResponseDTO(
                paciente.getId(),
                paciente.getRut(),
                paciente.getDvRut(),
                paciente.getNombresPaciente(),
                paciente.getApellidosPaciente(),
                paciente.getTelefono(),
                paciente.getEmail(),
                paciente.getFechaNacimiento(),
                hospital
            );
            resultado.add(respuesta);
        }
        return resultado;
    }

    public Paciente buscarId(Long id){
        return pacienteRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }

    public Boolean existeId(Long id){
        return pacienteRepository.existsById(id);
    }

    public PacienteResponseDTO buscarDetallado(Long id) {
        Paciente paciente = buscarId(id);

        HospitalDTO hospital = hospitalCliente.buscarId(paciente.getHospitalId());

        return new PacienteResponseDTO(
            paciente.getId(),
            paciente.getRut(),
            paciente.getDvRut(),
            paciente.getNombresPaciente(),
            paciente.getApellidosPaciente(),
            paciente.getTelefono(),
            paciente.getEmail(),
            paciente.getFechaNacimiento(),
            hospital
        );
    }
    
    public PacienteResponseDTO crear(PacienteRequestDTO request) {
        if (existeId(request.getId())){
            throw new RuntimeException("paciente ya existe");
        }
        
        HospitalDTO hospital = hospitalCliente.buscarId(request.getHospitalId());
        
        Paciente nuevo = new Paciente();
        nuevo.setRut(request.getRut());
        nuevo.setDvRut(request.getDvRut());
        nuevo.setNombresPaciente(request.getNombresPaciente());
        nuevo.setApellidosPaciente(request.getApellidosPaciente());
        nuevo.setTelefono(request.getTelefono());
        nuevo.setEmail(request.getEmail());
        nuevo.setFechaNacimiento(request.getFechaNacimiento());
        nuevo.setHospitalId(request.getHospitalId());
        Paciente guardado = pacienteRepository.save(nuevo);
        
        return new PacienteResponseDTO(
            guardado.getId(),
            guardado.getRut(),
            guardado.getDvRut(),
            guardado.getNombresPaciente(),
            guardado.getApellidosPaciente(),
            guardado.getTelefono(),
            guardado.getEmail(),
            guardado.getFechaNacimiento(),
            hospital
        );
    }

    public PacienteResponseDTO actualizar(Long id, PacienteRequestDTO request){
        HospitalDTO hospital = hospitalCliente.buscarId(request.getHospitalId());
        
        Paciente encontrado = buscarId(id);
        encontrado.setRut(request.getRut());
        encontrado.setDvRut(request.getDvRut());
        encontrado.setNombresPaciente(request.getNombresPaciente());
        encontrado.setApellidosPaciente(request.getApellidosPaciente());
        encontrado.setTelefono(request.getTelefono());
        encontrado.setEmail(request.getEmail());
        encontrado.setFechaNacimiento(request.getFechaNacimiento());
        encontrado.setHospitalId(request.getHospitalId());
        Paciente actualizado = pacienteRepository.save(encontrado);

        return new PacienteResponseDTO(
            actualizado.getId(),
            actualizado.getRut(),
            actualizado.getDvRut(),
            actualizado.getNombresPaciente(),
            actualizado.getApellidosPaciente(),
            actualizado.getTelefono(),
            actualizado.getEmail(),
            actualizado.getFechaNacimiento(),
            hospital
        );
    }

    public void borrar(Long id){
        if (!existeId(id)){
            throw new RuntimeException("hospital no existe");
        }
        pacienteRepository.deleteById(id);
    }
}
