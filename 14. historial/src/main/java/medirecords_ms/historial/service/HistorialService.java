package medirecords_ms.historial.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import medirecords_ms.historial.client.ConsultaCliente;
import medirecords_ms.historial.client.PacienteCliente;
import medirecords_ms.historial.dto.ConsultaDTO;
import medirecords_ms.historial.dto.HistorialRequestDTO;
import medirecords_ms.historial.dto.HistorialResponseDTO;
import medirecords_ms.historial.dto.PacienteDTO;
import medirecords_ms.historial.model.Historial;
import medirecords_ms.historial.repository.HistorialRepository;

@Service
public class HistorialService {
    @Autowired private HistorialRepository historialRepository;
    @Autowired private PacienteCliente pacienteCliente;
    @Autowired private ConsultaCliente consultaCliente;

    private String convertirListaToString(List<Long> consultas) {
        if (consultas == null || consultas.isEmpty()) {
            return "";
        }
        String resultado = "";
        for (int i = 0; i < consultas.size(); i++) {
            resultado = resultado + consultas.get(i);
            if (i < consultas.size() - 1) {
                resultado = resultado + ",";
            }
        }
        return resultado;
    }

     public List<HistorialResponseDTO> listarTodos() {
        List<Historial> lista = historialRepository.findAll();
        List<HistorialResponseDTO> resultado = new ArrayList<>();
        
        for (Historial historial : lista) {
            
            PacienteDTO paciente = pacienteCliente.buscarDetallado(historial.getPacienteId());
            
            String consultasId = historial.getConsultasId();
            List<ConsultaDTO> consultas = consultaCliente.buscarVariosId(consultasId);
            if(consultas.isEmpty()){
                throw new RuntimeException("consultas no puede estar vacio");
            }

            HistorialResponseDTO response = new HistorialResponseDTO(
                historial.getId(),
                historial.getNotas(),
                historial.getFechaActualizacion(),
                paciente,
                consultas
            );
            resultado.add(response);
        }
        return resultado;
    }
    
    private Historial buscarId(Long id) {
        return historialRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Historial no encontrado con id: " + id));
    }

    public Boolean existeId(Long id){
        return historialRepository.existsById(id);
    }

    public HistorialResponseDTO buscarDetallado(Long id) {
        Historial historial = buscarId(id);
        
        PacienteDTO paciente = pacienteCliente.buscarDetallado(historial.getPacienteId());
        
        String consultasId = historial.getConsultasId();
        List<ConsultaDTO> consultas = consultaCliente.buscarVariosId(consultasId);
        if(consultas.isEmpty()){
            throw new RuntimeException("consultas no puede estar vacio");
        }

        return new HistorialResponseDTO(
            historial.getId(),
            historial.getNotas(),
            historial.getFechaActualizacion(),
            paciente,
            consultas
        );
    }

    public List<HistorialResponseDTO> buscarVariosId(List<Long> ids) {
        List<Historial> historiales = historialRepository.findAllById(ids);
        List<HistorialResponseDTO> resultado = new ArrayList<>();
        
        for (Historial historial : historiales) {
            PacienteDTO paciente = pacienteCliente.buscarDetallado(historial.getPacienteId());
            
            String consultasId = historial.getConsultasId();
            List<ConsultaDTO> consultas = consultaCliente.buscarVariosId(consultasId);
            if(consultas.isEmpty()){
                throw new RuntimeException("consultas no puede estar vacio");
            }

            HistorialResponseDTO response = new HistorialResponseDTO(
                historial.getId(),
                historial.getNotas(),
                historial.getFechaActualizacion(),
                paciente,
                consultas
            );
            resultado.add(response);
        }
        return resultado;
    }

    public HistorialResponseDTO crear(HistorialRequestDTO request) {
        PacienteDTO paciente = pacienteCliente.buscarDetallado(request.getPacienteId());
        
        String idsString = convertirListaToString(request.getConsultasId());
        List<ConsultaDTO> consultas = consultaCliente.buscarVariosId(idsString);
        if(consultas.isEmpty()){
            throw new RuntimeException("consultas no puede estar vacio");
        }

        Historial historial = new Historial();
        historial.setNotas(request.getNotas());
        historial.setFechaActualizacion(request.getFechaActualizacion());
        historial.setPacienteId(request.getPacienteId());
        historial.setConsultasId(idsString);
        
        Historial guardado = historialRepository.save(historial);
        
        return new HistorialResponseDTO(
            guardado.getId(),
            guardado.getNotas(),
            guardado.getFechaActualizacion(),
            paciente,
            consultas
        );
    }

   public HistorialResponseDTO actualizar(Long id, HistorialRequestDTO request) {
        Historial existente = buscarId(id);
        
        PacienteDTO paciente = pacienteCliente.buscarDetallado(request.getPacienteId());
        
        String consultasId = convertirListaToString(request.getConsultasId());
        List<ConsultaDTO> consultas = consultaCliente.buscarVariosId(consultasId);
        if(consultas.isEmpty()){
            throw new RuntimeException("consultas no puede estar vacio");
        }   
        
        existente.setNotas(request.getNotas());
        existente.setFechaActualizacion(request.getFechaActualizacion());
        existente.setPacienteId(request.getPacienteId());
        existente.setConsultasId(consultasId);
        
        Historial actualizado = historialRepository.save(existente);
        
        return new HistorialResponseDTO(
            actualizado.getId(),
            actualizado.getNotas(),
            actualizado.getFechaActualizacion(),
            paciente,
            consultas
        );
    }

    public void borrar(Long id){
        if (!existeId(id)){
            throw new RuntimeException("historial no existe");
        }
        historialRepository.deleteById(id);
    }
}
