package medirecords_ms.receta.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import medirecords_ms.receta.client.ConsultaCliente;
import medirecords_ms.receta.client.MedicamentoCliente;
import medirecords_ms.receta.dto.ConsultaDTO;
import medirecords_ms.receta.dto.MedicamentoDTO;
import medirecords_ms.receta.dto.RecetaRequestDTO;
import medirecords_ms.receta.dto.RecetaResponseDTO;
import medirecords_ms.receta.model.Receta;
import medirecords_ms.receta.repository.RecetaRepository;

@Service
public class RecetaService {
    @Autowired private RecetaRepository recetaRepository;
    @Autowired private ConsultaCliente consultaCliente;
    @Autowired private MedicamentoCliente medicamentoCliente;

    private String convertirListaToString(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return "";
        }
        String resultado = "";
        for (int i = 0; i < ids.size(); i++) {
            resultado = resultado + ids.get(i);
            if (i < ids.size() - 1) {
                resultado = resultado + ",";
            }
        }
        return resultado;
    }

    private List<Long> convertirStringToLista(String idsString) {
        if (idsString == null || idsString.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> lista = new ArrayList<>();
        String[] partes = idsString.split(",");
        for (String parte : partes) {
            lista.add(Long.parseLong(parte));
        }
        return lista;
    }

    public List<RecetaResponseDTO> listarTodos() {
        List<Receta> lista = recetaRepository.findAll();
        List<RecetaResponseDTO> resultado = new ArrayList<>();
        
        for (Receta receta : lista) {
            ConsultaDTO consulta = consultaCliente.buscarId(receta.getConsultaId());
            
            List<Long> ids = convertirStringToLista(receta.getMedicamentosId());
            List<MedicamentoDTO> medicamentos = medicamentoCliente.buscarVariosId(
                ids.stream().map(String::valueOf).collect(Collectors.joining(","))
            );
            
            resultado.add(new RecetaResponseDTO(
                receta.getId(),
                receta.getFechaReceta(),
                receta.getInstrucciones(),
                consulta,
                medicamentos
            ));
        }
        return resultado;
    }

    private Receta buscarId(Long id) {
        return recetaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Receta no encontrada con id: " + id));
    }

    public RecetaResponseDTO buscarDetallada(Long id) {
        Receta receta = buscarId(id);
        
        ConsultaDTO consulta = consultaCliente.buscarId(receta.getConsultaId());
        
        List<Long> ids = convertirStringToLista(receta.getMedicamentosId());
        List<MedicamentoDTO> medicamentos = medicamentoCliente.buscarVariosId(
            ids.stream().map(String::valueOf).collect(Collectors.joining(","))
        );
        
        return new RecetaResponseDTO(
            receta.getId(),
            receta.getFechaReceta(),
            receta.getInstrucciones(),
            consulta,
            medicamentos
        );
    }


    
    public RecetaResponseDTO crearReceta(RecetaRequestDTO dto) {
        ConsultaDTO consulta = consultaCliente.buscarId(dto.getConsultaId());
        
        List<Long> ids = dto.getMedicamentosId();
        if (ids == null) {
            ids = new ArrayList<>();
        }
        
        String idsString = convertirListaToString(ids);
        
        List<MedicamentoDTO> medicamentos = medicamentoCliente.buscarVariosId(
            ids.stream().map(String::valueOf).collect(Collectors.joining(","))
        );
        
        Receta receta = new Receta();
        receta.setFechaReceta(dto.getFechaReceta());
        receta.setInstrucciones(dto.getInstrucciones());
        receta.setConsultaId(dto.getConsultaId());
        receta.setMedicamentosId(idsString);
        
        Receta guardado = recetaRepository.save(receta);
        
        return new RecetaResponseDTO(
            guardado.getId(),
            guardado.getFechaReceta(),
            guardado.getInstrucciones(),
            consulta,
            medicamentos
        );
    }

    public RecetaResponseDTO actualizarReceta(Long id, RecetaRequestDTO dto) {
        Receta existente = buscarId(id);
        
        ConsultaDTO consulta = consultaCliente.buscarId(dto.getConsultaId());
        
        List<Long> ids = dto.getMedicamentosId();
        if (ids == null) {
            ids = new ArrayList<>();
        }
        
        String idsString = convertirListaToString(ids);
        
        List<MedicamentoDTO> medicamentos = medicamentoCliente.buscarVariosId(
            ids.stream().map(String::valueOf).collect(Collectors.joining(","))
        );
        
        existente.setFechaReceta(dto.getFechaReceta());
        existente.setInstrucciones(dto.getInstrucciones());
        existente.setConsultaId(dto.getConsultaId());
        existente.setMedicamentosId(idsString);
        
        Receta actualizado = recetaRepository.save(existente);
        
        return new RecetaResponseDTO(
            actualizado.getId(),
            actualizado.getFechaReceta(),
            actualizado.getInstrucciones(),
            consulta,
            medicamentos
        );
    }

    public void borrarReceta(Long id){
        recetaRepository.deleteById(id);
    }
}
