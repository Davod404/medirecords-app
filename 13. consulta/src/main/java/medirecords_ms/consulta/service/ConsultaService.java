package medirecords_ms.consulta.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class ConsultaService {
    @Autowired private ConsultaRepository consultaRepository;
    @Autowired private HospitalCliente hospitalCliente;
    @Autowired private PersonalCliente personalCliente;
    @Autowired private PacienteCliente pacienteCliente;

    public List<ConsultaResponseDTO> listarTodos() {
        List<Consulta> lista = consultaRepository.findAll();
        List<ConsultaResponseDTO> resultado = new ArrayList<>();
        
        for (Consulta request : lista) {
            ConsultaResponseDTO response = new ConsultaResponseDTO(
                request.getId(),
                request.getFechaConsulta(),
                request.getMotivo(),
                request.getDiagnostico(),
                request.getPacienteId(),
                request.getPersonalId(),
                request.getHospitalId()
            );
            resultado.add(response);
        }
        
        return resultado;
    }

    public Consulta buscarId(Long id) {
        return consultaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Consulta no encontrada"));
    }

    public ConsultaResponseDTO buscarDetalle(Long id) {
        Consulta request = buscarId(id);
        
        HospitalDTO hospital = hospitalCliente.buscarHospitalPorId(request.getHospitalId());
        PersonalDTO personal = personalCliente.buscarDetalle(request.getPersonalId());
        PacienteDTO paciente = pacienteCliente.buscarDetalle(request.getPacienteId());  
        
        return new ConsultaResponseDTO(
            request.getId(),
            request.getFechaConsulta(),
            request.getMotivo(),
            request.getDiagnostico(),
            paciente.getId(),
            personal.getId(),
            hospital.getId()
        );
    }

    public List<ConsultaResponseDTO> buscarVariosId(List<Long> ids) {
        List<Consulta> consultas = consultaRepository.findAllById(ids);
        List<ConsultaResponseDTO> resultado = new ArrayList<>();

        for (Consulta consulta : consultas) {
            resultado.add(new ConsultaResponseDTO(
                consulta.getId(),
                consulta.getFechaConsulta(), 
                consulta.getMotivo(), 
                consulta.getDiagnostico(),
                consulta.getPacienteId(),
                consulta.getPersonalId(),
                consulta.getHospitalId()
            ));
        }
        return resultado;
    }


    public ConsultaResponseDTO crearConsulta(ConsultaRequestDTO request){
        HospitalDTO hospital = hospitalCliente.buscarHospitalPorId(request.getHospitalId());
        PersonalDTO personal = personalCliente.buscarDetalle(request.getPersonalId());
        PacienteDTO paciente = pacienteCliente.buscarDetalle(request.getPacienteId());

        Consulta nueva = new Consulta();
        nueva.setFechaConsulta(request.getFechaConsulta());
        nueva.setMotivo(request.getMotivo());
        nueva.setDiagnostico(request.getDiagnostico());
        nueva.setHospitalId(hospital.getId());
        nueva.setPersonalId(personal.getId());
        nueva.setPacienteId(paciente.getId());

        Consulta guardada = consultaRepository.save(nueva);

        return new ConsultaResponseDTO(
            guardada.getId(),
            guardada.getFechaConsulta(),
            guardada.getMotivo(),
            guardada.getDiagnostico(),
            guardada.getPacienteId(),
            guardada.getPersonalId(),
            guardada.getHospitalId()
        );
    }

    public ConsultaResponseDTO actualizarConsulta(Long id, ConsultaRequestDTO request){
        Consulta encontrada = buscarId(id);

        HospitalDTO hospital = hospitalCliente.buscarHospitalPorId(request.getHospitalId());
        PersonalDTO personal = personalCliente.buscarDetalle(request.getPersonalId());
        PacienteDTO paciente = pacienteCliente.buscarDetalle(request.getPacienteId());        
    
        encontrada.setFechaConsulta(request.getFechaConsulta());
        encontrada.setMotivo(request.getMotivo());
        encontrada.setDiagnostico(request.getDiagnostico());
        encontrada.setPacienteId(paciente.getId());
        encontrada.setPersonalId(personal.getId());
        encontrada.setHospitalId(hospital.getId());
        
        Consulta actualizada = consultaRepository.save(encontrada);

        return new ConsultaResponseDTO(
            actualizada.getId(),
            actualizada.getFechaConsulta(),
            actualizada.getMotivo(),
            actualizada.getDiagnostico(),
            actualizada.getPacienteId(),
            actualizada.getPersonalId(),
            actualizada.getHospitalId()
        );
    }

    public void borrarConsulta(Long id){
        consultaRepository.deleteById(id);
    }
}
