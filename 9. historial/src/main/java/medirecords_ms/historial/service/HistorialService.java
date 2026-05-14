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

    public List<HistorialResponseDTO> listarTodos() {
        List<Historial> lista = historialRepository.findAll();
        List<HistorialResponseDTO> resultado = new ArrayList<>();
        
        for (Historial historial : lista) {
            
            PacienteDTO paciente = pacienteCliente.buscarId(historial.getPacienteId());
            List<ConsultaDTO> consultas = consultaCliente.buscarVariosId(historial.getConsultasId());
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

    public Historial buscarId(Long id){
        return historialRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("historial no encontrado"));
    }

    public Boolean existeId(Long id){
        return historialRepository.existsById(id);
    }

    public HistorialResponseDTO buscarDetallado(Long id) {
        Historial encontrado = buscarId(id);

        PacienteDTO paciente = pacienteCliente.buscarId(encontrado.getPacienteId());
        
        List<ConsultaDTO> consultas = consultaCliente.buscarVariosId(encontrado.getConsultasId());
        if(consultas.isEmpty()){
            throw new RuntimeException("consultas no puede estar vacio");
        }

        return new HistorialResponseDTO(
            encontrado.getId(),
            encontrado.getNotas(),
            encontrado.getFechaActualizacion(),
            paciente,
            consultas
        );
    }
    
    public HistorialResponseDTO crear(HistorialRequestDTO request) {
        PacienteDTO paciente = pacienteCliente.buscarId(request.getPacienteId());
        
        List<ConsultaDTO> consultas = consultaCliente.buscarVariosId(request.getConsultasId());
        if(consultas.isEmpty()){
            throw new RuntimeException("consultas no puede estar vacio");
        }

        Historial historial = new Historial();
        historial.setNotas(request.getNotas());
        historial.setFechaActualizacion(request.getFechaActualizacion());
        historial.setPacienteId(request.getPacienteId());
        historial.setConsultasId(request.getConsultasId());
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
        Historial encontrado = buscarId(id);
        
        PacienteDTO paciente = pacienteCliente.buscarId(request.getPacienteId());
        
        List<ConsultaDTO> consultas = consultaCliente.buscarVariosId(encontrado.getConsultasId());
        if(consultas.isEmpty()){
            throw new RuntimeException("consultas no puede estar vacio");
        }
        
        encontrado.setNotas(request.getNotas());
        encontrado.setFechaActualizacion(request.getFechaActualizacion());
        encontrado.setPacienteId(request.getPacienteId());
        encontrado.setConsultasId(request.getConsultasId());
        Historial actualizado = historialRepository.save(encontrado);
        
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

