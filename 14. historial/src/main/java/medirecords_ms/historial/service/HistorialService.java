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

    private List<Long> convertirStringToLista(String stringsId) {
        if (stringsId == null || stringsId.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> lista = new ArrayList<>();
        String[] partes = stringsId.split(",");
        for (String parte : partes) {
            lista.add(Long.parseLong(parte));
        }
        return lista;
    }

    private String convertirListaToString(List<Long> listaIds) {
        if (listaIds == null || listaIds.isEmpty()) {
            return "";
        }
        String resultado = "";
        for (int i = 0; i < listaIds.size(); i++) {
            resultado = resultado + listaIds.get(i);
            if (i < listaIds.size() - 1) {
                resultado = resultado + ",";
            }
        }
        return resultado;
    }

    public List<HistorialResponseDTO> listarTodos() {
        List<Historial> lista = historialRepository.findAll();
        List<HistorialResponseDTO> resultado = new ArrayList<>();
        
        for (Historial historial : lista) {
            PacienteDTO paciente = pacienteCliente.buscarDetalle(historial.getPacienteId());
            
            List<Long> consultasId = convertirStringToLista(historial.getConsultasId());
            List<ConsultaDTO> consultas = consultaCliente.buscarVariosId(consultasId);
            
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

    public HistorialResponseDTO buscarDetalle(Long id) {
        Historial historial = buscarId(id);
        
        PacienteDTO paciente = pacienteCliente.buscarDetalle(historial.getPacienteId());
        
        List<Long> consultasId = convertirStringToLista(historial.getConsultasId());
        List<ConsultaDTO> consultas = consultaCliente.buscarVariosId(consultasId);
        
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
            PacienteDTO paciente = pacienteCliente.buscarDetalle(historial.getPacienteId());
            
            List<Long> consultasId = convertirStringToLista(historial.getConsultasId());
            List<ConsultaDTO> consultas = consultaCliente.buscarVariosId(consultasId);
            
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

    public HistorialResponseDTO crearHistorial(HistorialRequestDTO request) {
        PacienteDTO paciente = pacienteCliente.buscarDetalle(request.getPacienteId());
        
        String idsString = convertirListaToString(request.getConsultasId());
        List<ConsultaDTO> consultas = consultaCliente.buscarVariosId(request.getConsultasId());
        
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

   public HistorialResponseDTO actualizarHistorial(Long id, HistorialRequestDTO request) {
        Historial existente = buscarId(id);
        
        PacienteDTO paciente = pacienteCliente.buscarDetalle(request.getPacienteId());
        
        String stringsId = convertirListaToString(request.getConsultasId());
        List<ConsultaDTO> consultas = consultaCliente.buscarVariosId(request.getConsultasId());
        
        existente.setNotas(request.getNotas());
        existente.setFechaActualizacion(request.getFechaActualizacion());
        existente.setPacienteId(request.getPacienteId());
        existente.setConsultasId(stringsId);
        
        Historial actualizado = historialRepository.save(existente);
        
        return new HistorialResponseDTO(
            actualizado.getId(),
            actualizado.getNotas(),
            actualizado.getFechaActualizacion(),
            paciente,
            consultas
        );
    }

    public void borrarHistorial(Long id){
        historialRepository.deleteById(id);
    }
}
