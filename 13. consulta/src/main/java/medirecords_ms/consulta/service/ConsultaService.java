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
        
        for (Consulta consulta : lista) {
            PacienteDTO paciente = pacienteCliente.buscarDetallado(consulta.getPacienteId());
            PersonalDTO personal = personalCliente.buscarDetallado(consulta.getPersonalId());
            HospitalDTO hospital = hospitalCliente.buscarDetallado(consulta.getHospitalId());

            ConsultaResponseDTO response = new ConsultaResponseDTO(
                consulta.getId(),
                consulta.getFechaConsulta(),
                consulta.getMotivo(),
                consulta.getDiagnostico(),
                paciente,
                personal,
                hospital
            );
            resultado.add(response);
        }
        return resultado;
    }

    private Consulta buscarId(Long id) {
        return consultaRepository.findById(id)    
            .orElseThrow(() -> new RuntimeException("Consulta no encontrada con id: " + id));
    }

    public Boolean existeId(Long id){
        return consultaRepository.existsById(id);
    }

    public ConsultaResponseDTO buscarDetallado(Long id) {
        Consulta encontrado = buscarId(id);
        
        PacienteDTO paciente = pacienteCliente.buscarDetallado(encontrado.getPacienteId());
        PersonalDTO personal = personalCliente.buscarDetallado(encontrado.getPersonalId());
        HospitalDTO hospital = hospitalCliente.buscarDetallado(encontrado.getHospitalId());

        return new ConsultaResponseDTO(
            encontrado.getId(),
            encontrado.getFechaConsulta(),
            encontrado.getMotivo(),
            encontrado.getDiagnostico(),
            paciente,
            personal,
            hospital
        );
    }

    public List<ConsultaResponseDTO> buscarVariosId(List<Long> consultas){
        List<ConsultaResponseDTO> resultado = new ArrayList<>();

        for(Long id : consultas){
            Consulta encontrado = buscarId(id);

            PacienteDTO paciente = pacienteCliente.buscarDetallado(encontrado.getPacienteId());
            PersonalDTO personal = personalCliente.buscarDetallado(encontrado.getPersonalId());
            HospitalDTO hospital = hospitalCliente.buscarDetallado(encontrado.getHospitalId());
            
            ConsultaResponseDTO response = new ConsultaResponseDTO(
            encontrado.getId(),
            encontrado.getFechaConsulta(),
            encontrado.getMotivo(),
            encontrado.getDiagnostico(),
            paciente,
            personal,
            hospital
            );
            resultado.add(response);
        }
        return resultado;
    }

    public ConsultaResponseDTO crear(ConsultaRequestDTO request) {
        Consulta encontrada = buscarId(request.getId());
        
        PacienteDTO paciente = pacienteCliente.buscarDetallado(encontrada.getPacienteId());
        PersonalDTO personal = personalCliente.buscarDetallado(encontrada.getPersonalId());
        HospitalDTO hospital = hospitalCliente.buscarDetallado(encontrada.getHospitalId());

        Consulta consulta = new Consulta();
        consulta.setFechaConsulta(request.getFechaConsulta());
        consulta.setDiagnostico(request.getDiagnostico());
        consulta.setMotivo(request.getMotivo());
        consulta.setPacienteId(request.getPacienteId());
        consulta.setPersonalId(request.getPacienteId());
        consulta.setHospitalId(request.getPacienteId());
        Consulta guardado = consultaRepository.save(consulta);
        
        return new ConsultaResponseDTO(
            guardado.getId(),
            guardado.getFechaConsulta(),
            guardado.getMotivo(),
            guardado.getDiagnostico(),
            paciente,
            personal,
            hospital
        );
    }

    public ConsultaResponseDTO actualizar(Long id, ConsultaRequestDTO request) {
        Consulta encontrada = buscarId(id);
        
        PacienteDTO paciente = pacienteCliente.buscarDetallado(encontrada.getPacienteId());
        PersonalDTO personal = personalCliente.buscarDetallado(encontrada.getPersonalId());
        HospitalDTO hospital = hospitalCliente.buscarDetallado(encontrada.getHospitalId());

        Consulta actualizado = new Consulta();
        actualizado.setFechaConsulta(request.getFechaConsulta());
        actualizado.setDiagnostico(request.getDiagnostico());
        actualizado.setMotivo(request.getMotivo());
        actualizado.setPacienteId(request.getPacienteId());
        actualizado.setPersonalId(request.getPacienteId());
        actualizado.setHospitalId(request.getPacienteId()); 
        Consulta guardado = consultaRepository.save(actualizado);
        
        return new ConsultaResponseDTO(
            guardado.getId(),
            guardado.getFechaConsulta(),
            guardado.getMotivo(),
            guardado.getDiagnostico(),
            paciente,
            personal,
            hospital
        );
    }

    public void borrar(Long id){
        if (!existeId(id)){
            throw new RuntimeException("consulta no existe");
        }
        consultaRepository.deleteById(id);
    }
}
