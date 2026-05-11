package medirecords_ms.medicamento.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import medirecords_ms.medicamento.dto.MedicamentoRequestDTO;
import medirecords_ms.medicamento.dto.MedicamentoResponseDTO;
import medirecords_ms.medicamento.model.Medicamento;
import medirecords_ms.medicamento.repository.MedicamentoRepository;

@Service
public class MedicamentoService {
    @Autowired private MedicamentoRepository medicamentoRepository;

    public List<MedicamentoResponseDTO> listarTodos(){
        List<Medicamento> lista = medicamentoRepository.findAll();
        List<MedicamentoResponseDTO> resultado = new ArrayList<>();
        
        for (Medicamento medicamento : lista){
            MedicamentoResponseDTO respuesta = new MedicamentoResponseDTO(
                medicamento.getId(),
                medicamento.getNombre(),
                medicamento.getMarca(),
                medicamento.getTipo(),
                medicamento.getPrecio(),
                medicamento.getStock()
            );
            resultado.add(respuesta);
        }
        return resultado;
    }

    public Medicamento buscarId(Long id){
        return medicamentoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Medicamento no encontrado"));
    }

    public Boolean existeId(Long id){
        return medicamentoRepository.existsById(id);
    }

    public MedicamentoResponseDTO buscarDetallado(Long id) {
        Medicamento medicamento = buscarId(id);
        return new MedicamentoResponseDTO(
            medicamento.getId(),
            medicamento.getNombre(),
            medicamento.getMarca(),
            medicamento.getTipo(),
            medicamento.getPrecio(),
            medicamento.getStock()
        );
    }
    
    public MedicamentoResponseDTO crear(MedicamentoRequestDTO request) {
        if (existeId(request.getId())){
            throw new RuntimeException("medicamento ya existe");
        }

        Medicamento nuevo = new Medicamento();
        nuevo.setNombre(request.getNombre());
        nuevo.setMarca(request.getMarca());
        nuevo.setTipo(request.getTipo());
        nuevo.setPrecio(request.getPrecio());
        nuevo.setStock(request.getStock());
        Medicamento guardado = medicamentoRepository.save(nuevo);
        
        return new MedicamentoResponseDTO(
            guardado.getId(),
            guardado.getNombre(),
            guardado.getMarca(),
            guardado.getTipo(),
            guardado.getPrecio(),
            guardado.getStock()
        );
    }

    public MedicamentoResponseDTO actualizar(Long id, MedicamentoRequestDTO request){
        Medicamento encontrado = buscarId(id);

        encontrado.setNombre(request.getNombre());
        encontrado.setMarca(request.getMarca());
        encontrado.setTipo(request.getTipo());
        encontrado.setPrecio(request.getPrecio());
        encontrado.setStock(request.getStock());

        Medicamento actualizado = medicamentoRepository.save(encontrado);

        return new MedicamentoResponseDTO(
            actualizado.getId(),
            actualizado.getNombre(),
            actualizado.getMarca(),
            actualizado.getTipo(),
            actualizado.getPrecio(),
            actualizado.getStock()
        );
    }

    public void borrar(Long id){
        if (!existeId(id)){
            throw new RuntimeException("medicamento no existe");
        }
        medicamentoRepository.deleteById(id);
    }
}