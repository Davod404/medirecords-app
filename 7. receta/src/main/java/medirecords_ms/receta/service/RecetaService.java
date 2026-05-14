package medirecords_ms.receta.service;

import java.util.ArrayList;
import java.util.List;

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
    
    public List<RecetaResponseDTO> listarTodos() {
        List<Receta> lista = recetaRepository.findAll();
        List<RecetaResponseDTO> resultado = new ArrayList<>();
        
        for (Receta receta : lista) {
            ConsultaDTO consulta = consultaCliente.buscarId(receta.getConsultaId());
            
            List<MedicamentoDTO> medicamentos = medicamentoCliente.buscarVariosId(receta.getMedicamentosId());
            if(medicamentos.isEmpty()){
                throw new RuntimeException("medicamentos no puede estar vacio");
            }

            RecetaResponseDTO response = new RecetaResponseDTO(
                receta.getId(),
                receta.getFechaReceta(),
                receta.getInstrucciones(),
                consulta,
                medicamentos
            );
            resultado.add(response);
        }
        return resultado;
    }

    private Receta buscarId(Long id) {
        return recetaRepository.findById(id)    
            .orElseThrow(() -> new RuntimeException("Receta no encontrada con id: " + id));
    }

    public Boolean existeId(Long id){
        return recetaRepository.existsById(id);
    }

    public RecetaResponseDTO buscarDetallado(Long id) {
        Receta encontrada = buscarId(id);
        
        ConsultaDTO consulta = consultaCliente.buscarId(encontrada.getConsultaId());
        
        List<MedicamentoDTO> medicamentos = medicamentoCliente.buscarVariosId(encontrada.getMedicamentosId());
        if(medicamentos.isEmpty()){
            throw new RuntimeException("medicamentos no puede estar vacio");
        }

        return new RecetaResponseDTO(
            encontrada.getId(),
            encontrada.getFechaReceta(),
            encontrada.getInstrucciones(),
            consulta,
            medicamentos
        );
    }
    
    public RecetaResponseDTO crear(RecetaRequestDTO request) {
        ConsultaDTO consulta = consultaCliente.buscarId(request.getConsultaId());
        
        String medicamentosId = request.getMedicamentosId();
        List<MedicamentoDTO> medicamentos = medicamentoCliente.buscarVariosId(request.getMedicamentosId());
        if(medicamentos.isEmpty()){
            throw new RuntimeException("medicamentos no puede estar vacio");
        }

        Receta receta = new Receta();
        receta.setFechaReceta(request.getFechaReceta());
        receta.setInstrucciones(request.getInstrucciones());
        receta.setConsultaId(request.getConsultaId());
        receta.setMedicamentosId(medicamentosId);
        Receta guardado = recetaRepository.save(receta);
        
        return new RecetaResponseDTO(
            guardado.getId(),
            guardado.getFechaReceta(),
            guardado.getInstrucciones(),
            consulta,
            medicamentos
        );
    }

    public RecetaResponseDTO actualizar(Long id, RecetaRequestDTO request) {
        Receta encontrado = buscarId(id);
        
        ConsultaDTO consulta = consultaCliente.buscarId(request.getConsultaId());
        
        String medicamentosId = request.getMedicamentosId();
        List<MedicamentoDTO> medicamentos = medicamentoCliente.buscarVariosId(request.getMedicamentosId());
        if(medicamentos.isEmpty()){
            throw new RuntimeException("medicamentos no puede estar vacio");
        }

        encontrado.setFechaReceta(request.getFechaReceta());
        encontrado.setInstrucciones(request.getInstrucciones());
        encontrado.setConsultaId(request.getConsultaId());
        encontrado.setMedicamentosId(medicamentosId);
        Receta actualizado = recetaRepository.save(encontrado);
        
        return new RecetaResponseDTO(
            actualizado.getId(),
            actualizado.getFechaReceta(),
            actualizado.getInstrucciones(),
            consulta,
            medicamentos
        );
    }

    public void borrar(Long id){
        if (!existeId(id)){
            throw new RuntimeException("receta no existe");
        }
        recetaRepository.deleteById(id);
    }
}
